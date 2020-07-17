一、原理简介

 

1.简介：

         canal为阿里巴巴产品，它主要模拟了mysql的Slave向Master发送请求，当mysql有增删改查时则会出发请求将数据发送到canal服务中，canal将数据存放到内存，直到客户端程序(canal服务端和客户端程序都是由java编写,且客户端逻辑由我们借助com.alibaba.otter.canal工具包下的类完成开发)通过发布-订阅这种模式消费canal服务中的数据。

 

2.原理：

         1.canal模拟mysql slave的交互协议，伪装自己为mysql slave，向mysql master发送dump协议 

         2.mysql master收到dump请求，开始推送binary log给slave(也就是canal) 

         3.canal解析binary log对象(原始为byte流)

3.为什么使用canal：

        通过大量的资料收集和调查，查到从mysql同步到redis有如下几种方法：

        ①先从Redis读取数据，如果没有查询到；便从mysql查询数据，将查询到的内容放到Redis中。

        ②使用mysql的udf去做【菜鸟玩Linux开发】通过MySQL自动同步刷新Redis

        ③通过Gearman去同步【GearmanMySQL到Redis数据复制方案】

        ④使用open-replicator解析binlog 【采用OpenReplicator解析MySQL binlog】

        ⑤使用canal进行同步（个人觉得是最简便,只需安装好canal，充当中间键，连接mysql和redis，java程序需要改动的地方也不多，文章会把java程序给出，需要改动的地方也已标出）

二、开始干活

大致流程：

在canal中配置上mysql的数据库信息，在canal客户端中配置连接canal的信息，在redisUtil的工具类中配置上redis的信息，即可实现对mysql增删改，实时同步到redis中。
 

具体实现：

 

1.前提

安装好mysql【linux系统安装mysql】。

2.开启mysql的binlog模块

切换到mysql的安装路径（一般在cd /etc目录下），找到my.cnf(Linux)/my.ini (windows)，在【mysqld】中加入如下内容：    

log-bin=mysql-bin #添加这一行就ok
binlog-format=ROW #选择row模式
server_id=1 #配置mysql replaction需要定义，不能和canal的slaveId重复
3.配置完成后，需要重启数据库


4.创建canal用户（两种方法），用来管理canal的访问权限。我们可以通过对canal用户访问权限的控制，进而控制canal能够获取的内容。

法一：进入到mysql程序中，输入下列命令，创建用户，(用户名和密码都是：canal)

CREATE USER canal IDENTIFIED BY 'canal';    
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON 数据库名.表名 TO 'canal'@'%';  
-- GRANT ALL PRIVILEGES ON 数据库名.表名 TO 'canal'@'%' ;  
FLUSH PRIVILEGES;


5.canal配置与部署

 ①下载，解压，

下载：

本人使用的是当下最新版（canal.deployer-1.0.26-SNAPSHOT.tar.gz）

官网地址：https://github.com/alibaba/canal/releases/

百度云地址（1.0.26版）：链接：https://pan.baidu.com/s/1xsXUO-iq9DMkgbNb_0TXUg 密码：rhu7

解压：

tar -zxvf canal.deployer-1.0.26-SNAPSHOT.tar.gz         //进入到canal目录下，进行解压，解压后会出现下图文件夹


② 配置canal

   主要配置的文件有两处，canal/conf/example/instance.properties 和 canal/conf/canal.properties .下面对这两处做修改：

 cd canal/conf/example/ 目录下对 instance.properties进行修改：

## mysql serverId
canal.instance.mysql.slaveId = 1234 (和二大步中的2小步的 server_id不一致即可，故此用1234)
 
# position info
canal.instance.master.address = ***.***.***.***:3306 #改成自己的mysql数据库地址
canal.instance.master.journal.name = 
canal.instance.master.position = 
canal.instance.master.timestamp = 
 
#canal.instance.standby.address = 
#canal.instance.standby.journal.name =
#canal.instance.standby.position = 
#canal.instance.standby.timestamp = 
 
# username/password
canal.instance.dbUsername = canal #改成自己的数据库信息 （此处为第4步mysql中创建的用户名canal）
canal.instance.dbPassword = canal #改成自己的数据库信息  （此处为第4步创建的密码）
canal.instance.defaultDatabaseName =  #改成自己的数据库信息  （可不指定）
canal.instance.connectionCharset = UTF-8 #改成自己的数据库信息  （一般不用改）
 
# table regex
canal.instance.filter.regex = .*\\..*
# table black regex
canal.instance.filter.black.regex = 
 cd canal/conf/ 目录下对 canal.properties 进行修改： 如果需要对canal进行复杂的配置，可以参考【详细配置】

canal.id= 1
canal.ip= 10.185.162.101          #修改成你的Linux可以识别的IP，本人用的是我的Linux的IP（切记不可使用127.0.0.1）
canal.port= 11111                 #端口号默认11111
canal.zkServers=
# flush data to zk
canal.zookeeper.flush.period = 1000
# flush meta cursor/parse position to file
canal.file.data.dir = ${canal.conf.dir}
canal.file.flush.period = 1000
## memory store RingBuffer size, should be Math.pow(2,n)
canal.instance.memory.buffer.size = 16384
## memory store RingBuffer used memory unit size , default 1kb
canal.instance.memory.buffer.memunit = 1024
## meory store gets mode used MEMSIZE or ITEMSIZE
canal.instance.memory.batch.mode = MEMSIZE
 
## detecing config
canal.instance.detecting.enable = false
#canal.instance.detecting.sql = insert into retl.xdual values(1,now()) on duplicate key update x=now()
canal.instance.detecting.sql = select 1
"canal.properties" 72L, 2782C
 

③启动canal

 

cd /usr/canal/bin                   //进入到canal下的bin目录下
./startup.sh                        //启动canal
canal/logs/canal/canal.log         进入到canal的日志下，若有canal server is running now.... 离成功更近一步
 

2018-4-27 14 [main] INFO  com.alibaba.otter.canal.deployer.CanalLauncher - ## start the canal server.
2018-4-27 14 [main] INFO  com.alibaba.otter.canal.deployer.CanalController - ## start the canal server[192.168.1.99:11111]
2018-4-27 14 [main] INFO  com.alibaba.otter.canal.deployer.CanalLauncher - ## the canal server is running now ......
canal/logs/example/example.log       进入到example的路径下，若有successful...字样，恭喜，启动成功
2018-4-27 14 [main] INFO  c.a.o.c.i.spring.support.PropertyPlaceholderConfigurer - Loading properties file from class path resource [canal.properties]
2018-4-27 14 [main] INFO  c.a.o.c.i.spring.support.PropertyPlaceholderConfigurer - Loading properties file from class path resource [example/instance.properties]
2018-4-27 14 [main] INFO  c.a.otter.canal.instance.spring.CanalInstanceWithSpring - start CannalInstance for 1-example 
2018-4-27 14 [main] INFO  c.a.otter.canal.instance.core.AbstractCanalInstance - start successful....
④关闭canal

 

cd /usr/canal/bin                   //进入到canal下的bin目录下
stop.sh                             //关闭canal
6.Java连接canal执行同步操作
在maven项目中中加载canal和redis依赖包.

<dependency>    
    <groupId>redis.clients</groupId>    
    <artifactId>jedis</artifactId>    
    <version>2.4.2</version>    
</dependency> 
<dependency>    
    <groupId>com.alibaba.otter</groupId>    
    <artifactId>canal.client</artifactId>    
    <version>1.0.22</version>    
</dependency>  
 

建立canal客户端，从canal中获取数据，并将数据更新至Redis.

 

package com.hyxt;
 
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
 
import java.net.InetSocketAddress;
import java.util.List;
 
/**
 * Copyright © 2018 zxl
 * <p>
 * DESCRIPTION        这里主要做两个工作，一个是循环从Canal上取数据，一个是将数据更新至Redis
 *
 * @create 2018-04-16 16:04
 * @author: zxl
 */
@Component
public class ClientSimple {
 
    //日志
    private  final Logger logger = LoggerFactory.getLogger(ClientSimple.class);
 
    public  void syn() {
 
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("10.185.162.101",
                11111), "example", "", "");//IP和端口为上文中 canal.properties中的IP和端口，“example”为默认，用户名密码不填
 
        logger.info("正在连接...");
        System.out.println("正在连接...");
        System.out.println(connector);
        int batchSize = 1000;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            logger.info("连接成功");
            System.out.println("连接成功");
            connector.rollback();
            while (true) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    printEntry(message.getEntries());
                }
 
                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }
 
        } finally {
            connector.disconnect();
            logger.info("连接释放成功");
            System.out.println("连接释放成功");
        }
    }
 
    private  void printEntry( List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
 
            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
 
            EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));
 
            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    redisDelete(rowData.getBeforeColumnsList());
                    logger.info("删除redis数据成功");
                } else if (eventType == EventType.INSERT) {
                    redisInsert(rowData.getAfterColumnsList());
                    logger.info("成功新增数据到redis");
                } else {
                    System.out.println("------->修改之前的数据为：");
//                    printColumn(rowData.getBeforeColumnsList());
                    logger.info("修改之前的数据为："+printColumn(rowData.getBeforeColumnsList()));
                    System.out.println("------->修改之后的数据为：");
                    redisUpdate(rowData.getAfterColumnsList());
//                    printColumn(rowData.getBeforeColumnsList());
                    logger.info("修改之后的数据为："+printColumn(rowData.getAfterColumnsList()));
                }
            }
        }
    }
 
 
 
    private  String printColumn( List<Column> columns) {
        String s = null;
        for (Column column : columns) {
            s = column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated();
            System.out.println(s);
            logger.info("字段名为："+column.getName() + " 值为: " + column.getValue() + "  是否更新为：" +Integer.toString(column.getUpdated()==true?1:0));
            s=s+"    ";
        }
        return s;
    }
//下面是往redis里操作数据
    private static void redisInsert( List<Column> columns){
        JSONObject json=new JSONObject();
        for (Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        if(columns.size()>0){
            RedisUtil.stringSet(columns.get(0).getValue(),json.toJSONString());
        }
    }
 
    private   void redisUpdate( List<Column> columns){
        JSONObject json=new JSONObject();
        for (Column column : columns) {
            json.put(column.getName(), column.getValue());
//            logger.info("更新到redis后的字段为："+column.getName()+"  值为："+column.getValue());
        }
        if(columns.size()>0){
//            RedisUtil.stringSet("ceshi:"+ columns.get(0).getValue(),json.toJSONString());//加上“ceshi：”的话，是redis的结构有“ceshi”的文件夹，数据会在这个文件夹下
            RedisUtil.stringSet(columns.get(0).getValue(),json.toJSONString());
        }
    }
 
    private static  void redisDelete( List<Column> columns){
        JSONObject json=new JSONObject();
        for (Column column : columns) {
            json.put(column.getName(), column.getValue());
        }
        if(columns.size()>0){
//            RedisUtil.delKey("ceshi:"+ columns.get(0).getValue());
            RedisUtil.delKey(columns.get(0).getValue());
        }
    }
}
RedisUtil工具类，用来连接redis

package com.hyxt;
 
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
/**
 * Copyright © 2018 zxl
 * <p>
 * DESCRIPTION
 *
 * @create 2018-04-16 16:19
 * @author: zxl
 */
public class RedisUtil {
    // Redis服务器IP
    private static String ADDR = "10.185.162.113";
 
    // Redis的端口号
    private static int PORT = 6355;
 
    // 访问密码
    private static String AUTH = "asdf!@#$!@#$";
 
    // 可用连接实例的最大数目，默认值为8；
    // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;
 
    // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
 
    // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
 
    // 过期时间
    protected static int  expireTime = 660 * 660 *24;
 
    // 连接池
    protected static JedisPool pool;
 
    /**
     * 静态代码，只在初次调用一次
     */
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxTotal(MAX_ACTIVE);
        //最多空闲实例
        config.setMaxIdle(MAX_IDLE);
        //超时时间
        config.setMaxWaitMillis(MAX_WAIT);
        //
        config.setTestOnBorrow(false);
        pool = new JedisPool(config, ADDR, PORT, 1000);
    }
 
    /**
     * 获取jedis实例
     */
    protected static synchronized Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
        }
        return jedis;
    }
 
    /**
     * 释放jedis资源
     *
     * @param jedis
     * @param isBroken
     */
    protected static void closeResource(Jedis jedis, boolean isBroken) {
        try {
            if (isBroken) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }
        } catch (Exception e) {
 
        }
    }
 
    /**
     *  是否存在key
     *
     * @param key
     */
    public static boolean existKey(String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(0);
            return jedis.exists(key);
        } catch (Exception e) {
            isBroken = true;
        } finally {
            closeResource(jedis, isBroken);
        }
        return false;
    }
 
    /**
     *  删除key
     *
     * @param key
     */
    public static void delKey(String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(0);
            jedis.del(key);
        } catch (Exception e) {
            isBroken = true;
        } finally {
            closeResource(jedis, isBroken);
        }
    }
 
    /**
     *  取得key的值
     *
     * @param key
     */
    public static String stringGet(String key) {
        Jedis jedis = null;
        boolean isBroken = false;
        String lastVal = null;
        try {
            jedis = getJedis();
            jedis.select(0);
            lastVal = jedis.get(key);
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
        } finally {
            closeResource(jedis, isBroken);
        }
        return lastVal;
    }
 
    /**
     *  添加string数据
     *
     * @param key
     * @param value
     */
    public static String stringSet(String key, String value) {
        Jedis jedis = null;
        boolean isBroken = false;
        String lastVal = null;
        try {
            jedis = getJedis();
            jedis.select(0);
            lastVal = jedis.set(key, value);
            jedis.expire(key, expireTime);
        } catch (Exception e) {
            e.printStackTrace();
            isBroken = true;
        } finally {
            closeResource(jedis, isBroken);
        }
        return lastVal;
    }
 
    /**
     *  添加hash数据
     *
     * @param key
     * @param field
     * @param value
     */
    public static void hashSet(String key, String field, String value) {
        boolean isBroken = false;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis != null) {
                jedis.select(0);
                jedis.hset(key, field, value);
                jedis.expire(key, expireTime);
            }
        } catch (Exception e) {
            isBroken = true;
        } finally {
            closeResource(jedis, isBroken);
        }
    }
}
 

日志的工具类（logback-spring.xml）

 

<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <property name="APP_NAME" value="SysMySqledis"/>   <!--项目名为SysMySqledis-->
 
    <contextName>${APP_NAME}</contextName>
    <jmxConfigurator/>
 
    <logger name="org.springframework.web" level="INFO"/>
    <logger name="com.springboot.in.action" level="TRACE"/>
    <logger name="org.apache.velocity.runtime.log" level="INFO"/>
 
    appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{35} - %msg %n"
    charset = Charset.forName("utf8")
    }
    }
 
    <property name="LOG_PATTERN" value="%d{HH:mm:ss.SSS} [%thread] %-5level %logger{35} - %msg %n"></property>
    <property name="LOG_FILE_PATTERN" value="${APP_NAME}.%d{yyyy-MM-dd}.log"></property>
 
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>
 
    <appender name="dailyRollingFileAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
 
        <!--<File>../DataSynchronize/logs/info/GpsDataFilter_INFO</File>-->
        <!--<File>logFile.log</File>-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>./logs/info/SysMySqledis%d{yyyy-MM-dd}_INFO</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
 
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>INFO</level>
        </filter>
        <encoder charset="UTF-8">
            <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] %level [%thread] %file:%line - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
 
 
    <appender name="SYS_ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--<File>../DataSynchronize/logs/error/GpsDataFilter_ERROR</File>-->
        <!--过滤器,只打ERROR级别的日志-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>./logs/error/SysMySqledis%d{yyyy-MM-dd}_ERROR</fileNamePattern>            <maxHistory>12</maxHistory>
        </rollingPolicy>
 
        <encoder charset="UTF-8">
            <pattern>[%d{yyyy-MM-dd HH:mm:ss.SSS}] %level [%thread] %file:%line - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>
    <root level="DEBUG">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="dailyRollingFileAppender"/>
        <appender-ref ref="SYS_ERROR"/>
    </root>
 
 
</configuration>
 

 

 

7.更新数据：
 

UPDATE hyxt SET id='1', name='使用 Alibaba Canal 增量订阅&消费组件,同步MySQL数据到 Redis' WHERE id='1');
8.查看响应
***************************************************** Batch Id: [27] ,count : [3] , memsize : [325] , Time : 2018-04-27 13:57:33* Start : [mysql-bin.000005:13948:1503986259000(2017-08-29 13:57:39)] * End : [mysql-bin.000005:14295:1503986259000(2018-04-27 13:57:39)] ****************************************************
------->修改之前的数据为：
id : 1    type=int(11)
name : 使用 阿里巴巴 Canal 增量订阅&消费 binlog 同步 MySQL 数据到 Redis    type=varchar(1000）
------->修改之后的数据为：
id : 1    type=int(11)
name : 使用 Alibaba Canal 增量订阅&消费组件,同步MySQL数据到 Redis    type=varchar(1000)    update=true
 END ----> transaction id: 307
================> binlog[mysql-bin.000005:14295] , executeTime : 1503986259000 , delay : -5056ms
使用 Alibaba Canal 增量订阅&消费组件,同步MySQL数据到 Redis    type=varchar(1000)    update=true
 END ----> transaction id: 307
================> binlog[mysql-bin.000005:14295] , executeTime : 1503986259000 , delay : -5056ms
 

 

 

 

9.查看Redis
 

查看Redis 是否已经同步

{
    "name": "使用 Alibaba Canal 增量订阅&消费组件,同步MySQL数据到 Redis",
    "id": "1"
}
 

10.OK!
————————————————
版权声明：本文为CSDN博主「zxl技术博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/zxljsbk/article/details/80055544
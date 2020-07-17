Linux下查看mysql、apache是否安装,并卸载。

指令 ps -ef|grep mysql 得出结果

root     17659     1  0  2011 ?        00:00:00 /bin/sh /usr/bin/mysqld_safe --datadir=/var/lib/mysql --socket=/var/lib/mysql/mysql.sock --log-error=/var/log/mysqld.log --pid-file=/var/run/mysqld/mysqld.pid   
mysql    17719 17659  0  2011 ?        03:14:57 /usr/libexec/mysqld --basedir=/usr --datadir=/var/lib/mysql --user=mysql --pid-file=/var/run/mysqld/mysqld.pid --skip-external-locking --socket=/var/lib/mysql/mysql.sock  
usr/bin/mysql 是指：mysql的运行路径 
var/lib/mysql 是指：mysql数据库文件的存放路径 
usr/lib/mysql 是指：mysql的安装路径
ps -ef|grep mysql

Centos7 查看Mysql配置文件
my.cnf是mysql启动时加载的配置文件，一般会放在mysql的安装目录中，用户也可以放在其他目录加载。

安装mysql后，系统中会有多个my.cnf文件，有些是用于测试的。

使用locate my.cnf命令可以列出所有的my.cnf文件

命令

locate my.cnf








1、通过rpm包安装的MySQL




 
service mysqld restart
/etc/inint.d/mysqld start
 




2、从源码包安装的MySQL








 
// Linux关闭MySQL的命令
$mysql_dir/bin/mysqladmin -uroot -p shutdown
// linux启动MySQL的命令
$mysql_dir/bin/mysqld_safe &
 




其中mysql_dir为MySQL的安装目录，mysqladmin和mysqld_safe位于MySQL安装目录的bin目录下。



cat /usr/local/canal/logs/canal/canal.log


validate_password=off

vi /usr/local/canal/conf/example/instance.properties

/usr/local/canal/bin/startup.sh
192.144.226.54
172.21.0.2

vi /usr/local/canal/conf/canal.properties
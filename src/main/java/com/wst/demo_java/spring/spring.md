1.
@Autowried注解进行默认方式装配则是按类型进行装配,那么在此情况一个接口有两个实现类按照类型装配则会有两个实例,则会抛出org.springframework.beans.factory.BeanCreationException bean异常,无法做出选择,那么我们可以使用以下方式

 

@Controller
public class UserController {
    @Resource(name="UserServiceImpl1")

    private PersonService service;

    @Autowired
    @Qualifier("UserServiceImpl2")
    private PersonService service2;

    @RequestMapping("listPerson")
    @ResponseBody
    public void listPerson(){
        List<Person> list = service.listPerson();
        System.out.println(list);
    }

    @RequestMapping("listPerson2")
    @ResponseBody
    public void listPerson2(){
        List<Person> list = service2.listPerson();
        System.out.println(list);
    }
}

（2）@Resource

@Resource默认按照ByName自动注入，由J2EE提供，需要导入包javax.annotation.Resource。@Resource有两个重要的属性：name和type，而Spring将@Resource注解的name属性解析为bean的名字，而type属性则解析为bean的类型。所以，如果使用name属性，则使用byName的自动注入策略，而使用type属性时则使用byType自动注入策略。如果既不制定name也不制定type属性，这时将通过反射机制使用byName自动注入策略。

复制代码
复制代码
public class TestServiceImpl {
    // 下面两种@Resource只要使用一种即可
    @Resource(name="userDao")
    private UserDao userDao; // 用于字段上
    
    @Resource(name="userDao")
    public void setUserDao(UserDao userDao) { // 用于属性的setter方法上
        this.userDao = userDao;
    }
}

注：最好是将@Resource放在setter方法上，因为这样更符合面向对象的思想，通过set、get去操作属性，而不是直接去操作属性。


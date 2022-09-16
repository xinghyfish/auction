# 项目开发笔记

# Bug记录

## 1 静态资源404问题

通过浏览器访问前端H5页面时，样式渲染和JS均没有成功加载。在控制台中可以看到，请求的资源都出现了404错误。

经过检索发现，一种可能的解释是路径问题，访问静态资源时，由于在`application.yaml`和`WebConfig.java`中已经配置好了静态资源路径，因此在访问资源时不需要添加`/static`前缀也可以进行访问。

既然访问的路径没有问题，那唯一的可能就是这个路径遭到了拦截。因此，通过修改拦截器代码或许可以改变这个问题。

拦截器中，我们添加一系列的`EXCLUDE_PATH`，当用户访问这些路径时默认不会被拦截。我们将相关的静态资源文件路径添加到其中：

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATH = {
            "/customer/login", "/login", "/customer/register", "/register",
            "/admin/login",
            "/auctioneer/login",
            "/", "/css/**", "/js/**", "/images/**", "/fonts/**", "/vendor/**", "/images/icons/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
```

注意到，这个类**一定不要添加`@EnableWebMvc`注解**，否则exclude的路径将全部失效。

添加上述配置后，访问H5页面，样式和JS都正常加载和渲染。

> 参考文献：
> - [Springboot无法加载css js报错404](https://blog.csdn.net/weixin_42360600/article/details/107580037)
> - [SpringBoot——Interceptor如何不拦截静态资源](https://blog.csdn.net/qq_41773240/article/details/93321854)

## 2 `@SpringBootTest`注释运行错误

项目最后进行打包时出现问题，根据`target`给出的报错信息，可以定位到是测试类出现了问题。Console给出的错误信息为：`javax.websocket.server.ServerContainer not available`，之前的项目中运行测试类并没有发现这个问题。

之前的项目中并没有出现这样的问题，经过检索发现是引入`websocket`引发的问题。经查阅资料，得知`@SpringBootTest`注解标注的测试类在启动的时候不会启动服务器，所以添加WebSocket后会报错。

解决这个问题的方案就是手动添加选项`webEnvironment`，以便将Tomcat提供一个测试的web环境。如下：

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuctionApplicationTests {

    @Test
    void contextLoads() {
    }

}
```

添加完上述的注解后，单独启动测试类或者使用maven进行打包时就不会报错。

通过阅读源码可以发现，`@SpringBootTest`注解默认的`webEnvironment = SpringBootTest.WebEnvironment.MOCK`，该注解提供一个默认的servlet环境而并没有服务器，而`webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT`则会添加一个web服务器，这样WebSocket连接就可以被建立。

相关源代码如下：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith(SpringExtension.class)
public @interface SpringBootTest {

	@AliasFor("properties")
	String[] value() default {};

	@AliasFor("value")
	String[] properties() default {};

	String[] args() default {};

	Class<?>[] classes() default {};

	WebEnvironment webEnvironment() default WebEnvironment.MOCK;

	enum WebEnvironment {

		/**
		 * Creates a {@link WebApplicationContext} with a mock servlet environment if
		 * servlet APIs are on the classpath, a {@link ReactiveWebApplicationContext} if
		 * Spring WebFlux is on the classpath or a regular {@link ApplicationContext}
		 * otherwise.
		 */
		MOCK(false),

		/**
		 * Creates a web application context (reactive or servlet based) and sets a
		 * {@code server.port=0} {@link Environment} property (which usually triggers
		 * listening on a random port). Often used in conjunction with a
		 * {@link LocalServerPort @LocalServerPort} injected field on the test.
		 */
		RANDOM_PORT(true);

		private final boolean embedded;

		WebEnvironment(boolean embedded) {
			this.embedded = embedded;
		}

		/**
		 * Return if the environment uses an {@link ServletWebServerApplicationContext}.
		 * @return if an {@link ServletWebServerApplicationContext} is used.
		 */
		public boolean isEmbedded() {
			return this.embedded;
		}
	}
}
```

> 参考资料：
> - [SpringBoot启动测试时报错（javax.websocket.server.ServerContainer not available](https://blog.csdn.net/dc282614966/article/details/102462509)

## 3 iframe跳转

为了复用代码，在搭建客户端时，使用iframe进行不同页面的展示，而导航栏不发生跳转。但是，在实际开发时，页面跳转出现了"嵌套"的情况，即iframe内嵌入的网页中包含导航栏，这和我们的期望和设计时是不相符的，因此有必要探究iframe进行前端跳转时的的机制。

### 3.1 背景
经过查找资料可以发现，不同的字段可以进行不同方式的跳转。假设现在有jsp页面，其嵌套关系如下：`A->B->C->D`，其中A为最外层，D为最内层iframe，在D中进行跳转的方法如下：

### 3.2 JavaScript跳转

- `window.location.href`、`location.href`：本页面跳转，D页面跳转 
- `parent.location.href`：上一层页面跳转，C页面跳转 
- `top.location.href`：最外层页面跳转，A页面跳转

### 3.3 HTML跳转

HTML跳转的主要方式为标签`<a>`跳转或者通过form表单提交，通过属性可以修改，下面以form为例介绍：

- `<form>`: form提交后D页面跳转
- `<form target="_blank">`: form提交后弹出新页面
- `<form target="_parent">`: form提交后C页面跳转
- `<form target="_top">`: form提交后A页面跳转

### 3.4 刷新

- `parent.location.reload()`：C页面刷新 
- `window.opener.document.location.reload()`：C页面刷新(使用子窗口的opener对象来获得父窗口对象)
- `top.location.reload()`：A页面刷新

## 4 前端使用ajax提交表单失败

在登陆或者注册界面中，由于原则上不信任前端上传的任何数据，因此前端的数据需要传入后端进行检查，例如用户输入的邮箱和密码，需要核验邮箱是否存在、密码是否正确等问题。

虽然需要后端检查，但是对于用户来说，客户端并不需要进行刷新和跳转，因此不直接使用submit+form进行提交，而是使用ajax进行提交局部刷新。

在本项目的需求中，我们不需要异步提交，因此要将`async`字段的值改为`false`，但是这样提交后，虽然能够将数据正确地存储到后端的数据库中，但是却无法正确跳转到目标界面。

深一步进行探究后发现，`<button>`默认`type="submit"`，而如果ajax使用同步，所以提交表单动作被挂起直到ajax完毕后（此时执行请求过一次服务器），表单会提交，这样就会执行页面指定的action的地址, 而ajax回调success href的链接赋值不成功。

因此，只需要将`<button>`的`type`属性改为`"button"`即可，这样form表单中的连接即不会被挂起，不会发生意料之外的跳转。

> 参考资料：
> - https://cloud.tencent.com/developer/article/1553854



# 项目开发笔记

# Bug记录

## 1. 静态资源404问题

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


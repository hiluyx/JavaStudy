package proxy;

import proxy.CGLIB_proxy.CglibProxyExample;
import proxy.CGLIB_proxy.proxy.ReflectServiceImpl;
import proxy.jdk_proxy.JdkProxyExample;
import proxy.jdk_proxy.proxy.HelloWorld;
import proxy.jdk_proxy.proxy.HelloWorldImpl;
import org.junit.Test;

public class TestProxy {
    @Test
    public void testJdkProxy() {
        JdkProxyExample jdk = new JdkProxyExample();
        HelloWorld proxy = (HelloWorld)jdk.bind(new HelloWorldImpl());
        proxy.sayHello("john");
    }
    @Test
    public void testCglibProxy() {
        CglibProxyExample cpe = new CglibProxyExample();
        ReflectServiceImpl obj = (ReflectServiceImpl) cpe.getProxy(ReflectServiceImpl.class);
        obj.sayHello("john");
    }
}

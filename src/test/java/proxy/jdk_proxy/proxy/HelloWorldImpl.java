package proxy.jdk_proxy.proxy;

public class HelloWorldImpl implements HelloWorld{
    @Override
    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }
}

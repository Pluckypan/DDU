package engineer.echo.easyapi.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class EasyHandler<T> implements InvocationHandler {

    private T client;

    EasyHandler(T client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return method.invoke(client, objects);
    }

    @SuppressWarnings("unchecked")
    public T getProxy() {
        Class<?> clz = client.getClass();
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), this);
    }
}

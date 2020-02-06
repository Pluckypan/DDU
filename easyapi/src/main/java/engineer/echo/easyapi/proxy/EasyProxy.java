package engineer.echo.easyapi.proxy;

import java.util.concurrent.ConcurrentHashMap;

import engineer.echo.easyapi.annotation.JobApi;

public final class EasyProxy {

    private final static ConcurrentHashMap<Class<?>, Object> ownerMap = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Class<?>, EasyHandler> handlerMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> owner){
        if (ownerMap.containsKey(owner)){
            return (T)ownerMap.get(owner);
        }else {
             T object= getHandlerByAnno(owner).getProxy();
             ownerMap.put(owner,object);
             return object;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> EasyHandler<T> getHandlerByAnno(Class<T> owner){
        if (handlerMap.containsKey(owner)){
            return (EasyHandler<T>)handlerMap.get(owner);
        }else {
            JobApi easyServer = owner.getAnnotation(JobApi.class);
            String uniqueId= easyServer.uniqueId();
            EasyHandler handler = new EasyHandler(uniqueId);
            handlerMap.put(owner,handler);
            return handler;
        }
    }
}

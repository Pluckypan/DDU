package engineer.echo.easyapi.annotation;

import java.lang.reflect.Method;

public final class EasyJobHelper {
    public static final String PACKAGE = "engineer.echo.easyapi.compiler";
    public static final String CLASS = "MetaInfo";

    public static String getClassById(String id) {
        try {
            Class<?> clz = Class.forName(PACKAGE + "." + CLASS);
            Method method = clz.getMethod("getClassById", String.class);
            // 静态方法对象传 null
            Object result = method.invoke(null, id);
            if (result instanceof String) {
                return (String) result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getObjectById(String id) {
        String className = getClassById(id);
        if (className == null || className.length() == 0) return null;
        try {
            Class<?> clz = Class.forName(className);
            return clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

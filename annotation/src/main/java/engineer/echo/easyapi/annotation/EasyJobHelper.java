package engineer.echo.easyapi.annotation;

import java.lang.reflect.Field;

public final class EasyJobHelper {
    private static final String PACKAGE = "engineer.echo.easyapi.compiler";
    public static final String CLASS = "MetaInfo";
    private static final String JOB_API_URL = "EasyApi/EasyProxy/?api=";

    public static boolean isEasyJobRequest(String url) {
        return url != null && url.length() > 0 && url.contains(JOB_API_URL);
    }

    public static String generateRetrofitPath(String api, String method) {
        return JOB_API_URL + api + "&method=" + method;
    }

    public static String generatePackage(String id) {
        return PACKAGE + ".meta" + MD5Tool.getMD5(id);
    }

    private static String getClassById(String id) {
        try {
            Class<?> clz = Class.forName(generatePackage(id) + "." + CLASS);
            Field field = clz.getField("metaInfo");
            // 静态方法对象传 null
            Object result = field.get(null);
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

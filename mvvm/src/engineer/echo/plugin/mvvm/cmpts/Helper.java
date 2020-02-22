package engineer.echo.plugin.mvvm.cmpts;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.Messages;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Helper {

    public static void showTips(String title, String msg) {
        Messages.showMessageDialog(msg, title, Messages.getInformationIcon());
    }

    public static String readSource(AnAction action, String filename) {
        return new String(readStream(action.getClass().getResourceAsStream("/" + filename)));
    }

    public static void generateTarget(String content, String filepath, String filename) {
        try {
            File folder = new File(filepath);
            if (!folder.exists()) {
                boolean mkdirs = folder.mkdirs();
            }
            File file = new File(filepath + "/" + filename);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underlineToCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camelToUnderline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static String handleAndroidManifest(File androidManifest, String activityClassPath) {
        Document document;
        Element newActivity;
        OutputStream outputStream = null;
        String packageName = null;
        try {
            document = new SAXBuilder().build(androidManifest);
            Element manifest = document.getRootElement();
            Namespace namespace = manifest.getNamespace("android");
            packageName = manifest.getAttribute("package").getValue();
            if (packageName != null && packageName.length() > 0) {
                String shortClassPath = activityClassPath.replace(packageName, "");
                Element application = manifest.getChild("application");
                List<Element> activityList = application.getChildren("activity");
                if (activityList != null && activityList.size() > 1) {
                    newActivity = activityList.get(activityList.size() - 1).clone();
                    newActivity.removeAttribute("launchMode");
                    newActivity.getAttribute("name", namespace).setValue(shortClassPath);
                } else {
                    newActivity = new Element("activity");
                    newActivity.setAttribute("name", shortClassPath, namespace);
                }
                newActivity.setAttribute("screenOrientation", "portrait", namespace);
                newActivity.setAttribute("configChanges", "orientation|keyboardHidden|screenSize|screenLayout", namespace);

                application.addContent(newActivity);
                outputStream = new FileOutputStream(androidManifest);

                Format format = Format.getPrettyFormat();
                format.setEncoding("utf-8");
                XMLOutputter output = new XMLOutputter(format);
                output.output(document, outputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return packageName;
    }

    private static byte[] readStream(InputStream inStream) {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[2048];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outSteam.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outSteam.toByteArray();
    }
}

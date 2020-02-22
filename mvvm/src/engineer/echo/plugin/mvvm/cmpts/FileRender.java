package engineer.echo.plugin.mvvm.cmpts;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

public final class FileRender {

    /**
     * 待渲染的 Kotlin 模板
     */
    private static final String[] KOTLIN_TPL = new String[]{"KotlinActivity.tpl", "KotlinContract.tpl", "KotlinModel.tpl", "KotlinViewModel.tpl"};

    /**
     * 待渲染布局 模板
     */
    private static final String[] XML_LAYOUT_TPL = new String[]{"activity_kotlin.tpl"};

    /**
     * $rootPackage$           包名,如 engineer.echo.yi
     * $currentPackage$        当前包路径 engineer.echo.yi.ui.user
     * $moduleName$            功能模块类名 UserMain
     * $activityAffix$         activity_xxx.xml 如 user_main
     * $baseActivityPackage$   Activity 基类路径,如 androidx.appcompat.app.AppCompatActivity
     * $baseActivity$ Activity 类名,如 AppCompatActivity
     *
     * @param action          plugin 上下文
     * @param currentPick     当前选中的文件或目录
     * @param androidManifest AndroidManifest Android 配置清单
     * @param javaDir         代码路径文件夹
     */
    public static void start(final AnAction action, final VirtualFile currentPick, final File androidManifest, final File javaDir, String module, boolean useSample) {
        String pickPath = currentPick.isDirectory() ? currentPick.getPath() : currentPick.getParent().getPath();
        module = module.trim().replace(" ", "").replace("　", "").replace("_", "");
        String moduleName = Helper.underlineToCamel(module, false);
        String firstLetter = moduleName.substring(0, 1).toUpperCase();
        // 类名首字母大写
        moduleName = moduleName.length() > 1 ? (firstLetter + module.substring(1)) : firstLetter;
        String moduleLower = moduleName.toLowerCase();
        String activityAffix = Helper.camelToUnderline(moduleName).toLowerCase();
        String baseActivityPackage = "androidx.appcompat.app.AppCompatActivity";
        String baseActivity = baseActivityPackage.substring(baseActivityPackage.lastIndexOf(".") + 1);
        String currentPackagePath = new File(pickPath, moduleLower).getPath();
        String currentPackage = currentPackagePath.replace(javaDir.getPath(), "").replace(File.separator, ".").substring(1);
        String currentActivityClassPath = new File(currentPackagePath, moduleName).getPath();
        currentActivityClassPath = currentActivityClassPath.replace(javaDir.getPath(), "").replace(File.separator, ".");
        String rootPackage = Helper.handleAndroidManifest(androidManifest, currentActivityClassPath);
        if (rootPackage != null && rootPackage.length() > 0) {
            renderKotlin(action, currentPackagePath, rootPackage, currentPackage, moduleName, activityAffix, baseActivityPackage, baseActivity);
            String layoutPath = new File(javaDir.getParent(), "res" + File.separator + "layout").getPath();
            renderLayout(action, layoutPath, rootPackage, currentPackage, moduleName, activityAffix, baseActivityPackage, baseActivity);
        } else {
            Helper.showTips("EasyMVVM", "invalid AndroidManifest.xml");
        }
    }

    private static String getRenderFile(String path, String file) {
        return "template" + File.separator + path + File.separator + file;
    }


    private static String replaceTpl(final String source,
                                     final String rootPackage,
                                     final String currentPackage,
                                     final String moduleName,
                                     final String activityAffix,
                                     final String baseActivityPackage,
                                     final String baseActivity) {
        return source.replace("$rootPackage$", rootPackage)
                .replace("$currentPackage$", currentPackage)
                .replace("$moduleName$", moduleName)
                .replace("$activityAffix$", activityAffix)
                .replace("$baseActivityPackage$", baseActivityPackage)
                .replace("$baseActivity$", baseActivity);
    }

    /**
     * 渲染 Kotlin 模板
     */
    private static void renderKotlin(final AnAction action,
                                     final String currentPackagePath,
                                     final String rootPackage,
                                     final String currentPackage,
                                     final String moduleName,
                                     final String activityAffix,
                                     final String baseActivityPackage,
                                     final String baseActivity) {
        for (String tplName : KOTLIN_TPL) {
            String source = Helper.readSource(action, getRenderFile("kotlin", tplName));
            String target = replaceTpl(source, rootPackage, currentPackage, moduleName, activityAffix, baseActivityPackage, baseActivity);
            Helper.generateTarget(target, currentPackagePath, tplName.replace("Kotlin", moduleName).replace("tpl", "kt"));
        }
    }

    /**
     * 渲染布局模板
     */
    private static void renderLayout(AnAction action,
                                     final String layoutPath,
                                     final String rootPackage,
                                     final String currentPackage,
                                     final String moduleName,
                                     final String activityAffix,
                                     final String baseActivityPackage,
                                     final String baseActivity) {
        for (String tplName : XML_LAYOUT_TPL) {
            String source = Helper.readSource(action, getRenderFile("layout", tplName));
            String target = replaceTpl(source, rootPackage, currentPackage, moduleName, activityAffix, baseActivityPackage, baseActivity);
            Helper.generateTarget(target, layoutPath, tplName.replace("kotlin", activityAffix).replace("tpl", "xml"));
        }
    }
}

package engineer.echo.plugin.mvvm;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import engineer.echo.plugin.mvvm.cmpts.FileRender;
import engineer.echo.plugin.mvvm.cmpts.Helper;

import java.io.File;

public class EasyMVVM extends AnAction {

    public EasyMVVM() {
        super("EasyMVVM");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        VirtualFile currentPick = e.getData(CommonDataKeys.VIRTUAL_FILE);
        CreateDialog dialog = new CreateDialog((module, sample) -> {

            File androidManifest = null;
            if (currentPick != null) {
                int mainIndex = currentPick.getPath().indexOf("main");
                if (mainIndex > 1) {
                    String mainPath = currentPick.getPath().substring(0, mainIndex);
                    androidManifest = new File(new File(mainPath, "main"), "AndroidManifest.xml");
                }
            }
            if (project != null && currentPick != null && androidManifest != null && androidManifest.exists()) {
                File javaDir = new File(androidManifest.getParent(), "java");
                if (currentPick.getPath().contains(javaDir.getPath())) {
                    FileRender.start(this, currentPick, androidManifest, javaDir, module, sample);
                } else {
                    Helper.showTips("EasyMVVM", "invalid path.");
                }
            } else {
                Helper.showTips("EasyMVVM", "Android project required,missing AndroidManifest.xml.");
            }
        });
        dialog.setVisible(true);
        project.getBaseDir().refresh(false, true);
    }
}

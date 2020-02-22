package engineer.echo.plugin.mvvm;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

public class EasyMVVM extends AnAction {

    private Project project;
    private String packageName;
    private String moduleName;
    private boolean useSample = false;

    public EasyMVVM() {
        super("EasyMVVM");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        CreateDialog dialog = new CreateDialog((module, sample) -> {
            this.moduleName = module;
            this.useSample = sample;
        });
        dialog.setVisible(true);
        project.getBaseDir().refresh(false, true);
    }

}

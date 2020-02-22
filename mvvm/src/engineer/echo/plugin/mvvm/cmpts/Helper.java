package engineer.echo.plugin.mvvm.cmpts;

import com.intellij.openapi.ui.Messages;

public class Helper {

    public static void showTips(String title, String msg) {
        Messages.showInfoMessage(msg, title);
    }

}

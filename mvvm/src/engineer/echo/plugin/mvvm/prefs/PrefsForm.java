package engineer.echo.plugin.mvvm.prefs;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@State(name = "EasyMVVMPrefs", storages = {@Storage("EasyMVVMPrefs.xml")})
public class PrefsForm implements Configurable {

    private JTextPane versionField;
    private JTextPane projectField;
    private JTextPane appField;
    private JTextPane commonField;
    private JTextField baseActivityField;
    private JPanel prefsPanel;
    private JTextPane moduleField;
    private JTree treeTemplate;
    private JTextArea textTemplate;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;

    private String baseActivity = PrefsUtil.getBaseActivity();


    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "EasyMVVM";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        baseActivityField.setText(baseActivity);
        return prefsPanel;
    }

    @Override
    public void disposeUIResources() {

    }

    @Override
    public boolean isModified() {
        String inputVal = baseActivityField.getText();
        return !inputVal.equals(baseActivity);
    }

    @Override
    public void apply() throws ConfigurationException {
        PrefsUtil.setBaseActivity(baseActivityField.getText());
    }
}

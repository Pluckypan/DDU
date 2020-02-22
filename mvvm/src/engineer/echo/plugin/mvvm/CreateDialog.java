package engineer.echo.plugin.mvvm;

import engineer.echo.plugin.mvvm.cmpts.Helper;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CreateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField moduleName;
    private JRadioButton sampleRadioButton;

    public CreateDialog(CreateDialogListener listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(null);
        setSize(300, 240);

        buttonOK.addActionListener(e -> {
            String text = moduleName.getText().trim().replace(" ", "").replace("　", "").replace("_", "");
            if (text.isEmpty()) {
                Helper.showTips("EasyMVVM", "Module required.");
                return;
            }
            if (listener != null) {
                listener.onConfirm(moduleName.getText(), sampleRadioButton.isSelected());
            }
            dispose();
        });

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        CreateDialog dialog = new CreateDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public interface CreateDialogListener {
        void onConfirm(String module, boolean sample);
    }
}

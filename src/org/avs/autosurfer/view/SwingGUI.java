package org.avs.autosurfer.view;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 
 * Creates a GUI (using Java Swing, multithreading), declares its behavior (using AWT Listeners), and links it to a
 * controller class (using Events). Passes entered credentials to the controller and store them in LoginFormEvent class.
 * 
 * @author Andrey Skvortsov
 * 
 */

public class SwingGUI extends JFrame implements ActionListener
{

    private static final long serialVersionUID = 1L;

    private JLabel driverPathLabel, loginLabel, passwordLabel;
    
    private JTextField driverPathTextField;
    private String driverPath;
    
    private JTextField loginTextField;
    private String username;
    
    private JPasswordField passwordTextField;
    private char[] password;

    private JButton startButton, stopButton, exitButton, loginButton;

    private JCheckBox watchAllAdsCheck, watchAllAdsPrizesCheck;
    private boolean isWatchAllAdsChecked, iswatchAllAdsPrizesChecked;

    private LoginListener loginListener;
    private StartListener startListener;
    private StopListener stopListener;
    private ExitListener exitListener;
    
    private SwingGUILengthyStart swingGUILengthyStart;

    // Creates a label for GUI-interaction tasks.

    private JLabel makeLabel(String caption)
    {

        JLabel label = new JLabel(caption);
        getContentPane().add(label);
        return label;

    }

    // Creates a text field for GUI-interaction tasks.

    private JTextField makeTextField()
    {

        JTextField textField = new JTextField(10);
        getContentPane().add(textField);
        return textField;

    }

    // Creates a password field for GUI-interaction tasks.

    private JPasswordField makePasswordField()
    {

        JPasswordField PasswordTextField = new JPasswordField(12);
        getContentPane().add(PasswordTextField);
        return PasswordTextField;

    }

    // Creates a button for GUI-interaction tasks.

    private JButton makeButton(String caption)
    {

        JButton button = new JButton(caption);
        button.setActionCommand(caption);
        button.addActionListener(this);
        getContentPane().add(button);
        return button;

    }

    // Creates a check-box for GUI-interaction tasks.

    private JCheckBox makeCheckBox(String caption)
    {

        JCheckBox checkBox = new JCheckBox(caption);
        checkBox.addActionListener(this);
        getContentPane().add(checkBox);
        return checkBox;

    }

    // Declares constructor for a View-part (SwingGUI).

    public SwingGUI()
    {

        super("Neobux Autosurfer 1.0.0     ");

        // Sets up the GUI parts.

        driverPathLabel = makeLabel("Driver path:");
        loginLabel = makeLabel("Login:");
        passwordLabel = makeLabel("Password:");

        driverPathTextField = makeTextField();
        loginTextField = makeTextField();
        passwordTextField = makePasswordField();

        loginButton = makeButton("Login");
        loginButton.setEnabled(false);
        exitButton = makeButton("Exit");

        watchAllAdsCheck = makeCheckBox("Watch all advertisements.");
        watchAllAdsPrizesCheck = makeCheckBox("Watch all AdsPrizes.");

        startButton = makeButton("Start");
        stopButton = makeButton("Stop");
        startButton.setEnabled(false);
        stopButton.setEnabled(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(driverPathLabel)
                                .addComponent(loginLabel)
                                .addComponent(passwordLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(driverPathTextField)
                                .addComponent(loginTextField)
                                .addComponent(passwordTextField)))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(loginButton)
                        .addComponent(exitButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(watchAllAdsCheck)
                        .addComponent(watchAllAdsPrizesCheck))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addComponent(stopButton))
                .addGroup(layout.createSequentialGroup()));

        layout.linkSize(SwingConstants.HORIZONTAL, loginButton, exitButton, startButton, stopButton);
        layout.linkSize(SwingConstants.HORIZONTAL, driverPathTextField, loginTextField, passwordTextField);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(driverPathLabel)
                        .addComponent(driverPathTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(loginLabel)
                        .addComponent(loginTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(passwordLabel)
                        .addComponent(passwordTextField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(loginButton)
                        .addComponent(exitButton))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(watchAllAdsCheck)
                        .addComponent(watchAllAdsPrizesCheck))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(startButton)
                        .addComponent(stopButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)));

        layout.linkSize(SwingConstants.VERTICAL, startButton, stopButton, exitButton, loginButton);
        layout.linkSize(SwingConstants.VERTICAL, driverPathTextField, loginTextField, passwordTextField);

        pack();

        setAlwaysOnTop(true);

        setSize(265, 230);

        setResizable(false);

        // Positions the window in the middle of a screen.

        this.setLocation(((Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2));

        setVisible(true);

        // Enables/disables the Login button on-the-fly as the user edits driver path/login/password fields, ensuring
        // that all are entered.

        TextFieldFilled textFieldFilled = new TextFieldFilled(loginButton);
        textFieldFilled.addTextField(driverPathTextField);
        textFieldFilled.addTextField(loginTextField);
        textFieldFilled.addTextField(passwordTextField);

        // Enables/disables the Start button on-the-fly as the user checks/unchecks check-boxes, ensuring that at least
        // one is selected.

        CheckboxesSelected checkboxesSelected = new CheckboxesSelected(startButton);
        checkboxesSelected.addCheckbox(watchAllAdsCheck);
        checkboxesSelected.addCheckbox(watchAllAdsPrizesCheck);

    }

    // Declares actions for ActionListener (buttons and check-boxes).

    public void actionPerformed(ActionEvent event)
    {

        driverPath = driverPathTextField.getText();
        username = loginTextField.getText();
        password = passwordTextField.getPassword();

        switch (event.getActionCommand())
        {

        case "Login":

            new SwingGUILengthyLogin(driverPath, username, password).execute(); // Starts a thread to perform lengthy
                                                                                // LoginEvent.

            loginButton.setEnabled(false);
            exitButton.setEnabled(false);

            break;

        case "Start":

            isWatchAllAdsChecked = watchAllAdsCheck.isSelected();
            iswatchAllAdsPrizesChecked = watchAllAdsPrizesCheck.isSelected();

            if (isWatchAllAdsChecked || iswatchAllAdsPrizesChecked)
            {

                // Starts a thread to perform lengthy StartEvent.

                swingGUILengthyStart = new SwingGUILengthyStart(isWatchAllAdsChecked, iswatchAllAdsPrizesChecked);
                swingGUILengthyStart.execute();

                startButton.setEnabled(false);
                stopButton.setEnabled(true);

            }

            break;

        case "Stop":

            new SwingGUILengthyStop().execute(); // Starts a thread to perform lengthy StopEvent.

            break;

        case "Exit":

            new SwingGUILengthyExit().execute(); // Starts a thread to perform lengthy ExitEvent.

            loginButton.setEnabled(true);
            exitButton.setEnabled(false);

            break;

        }

    }

    private void fireLoginEvent(LoginFormEvent event)
    {

        if (loginListener != null)
        {

            loginListener.loginPerformed(event);

        }

    }

    private void fireStartEvent(boolean isWatchAllAdsChecked, boolean isWatchAllAdsPrizesChecked)
    {

        if (startListener != null)
        {

            startListener.startPerformed(isWatchAllAdsChecked, isWatchAllAdsPrizesChecked);

        }

    }

    private void fireStopEvent()
    {

        if (stopListener != null)
        {

            stopListener.stopPerformed();

        }

    }

    private void fireExitEvent()
    {

        if (exitListener != null)
        {

            exitListener.exitPerformed();

        }

    }

    // Sets Listeners.

    public void setLoginListener(LoginListener loginListener)
    {

        this.loginListener = loginListener;

    }

    public void setStartListener(StartListener startListener)
    {

        this.startListener = startListener;

    }

    public void setStopListener(StopListener stopListener)
    {

        this.stopListener = stopListener;

    }

    public void setExitListener(ExitListener exitListener)
    {

        this.exitListener = exitListener;

    }

    // Enables/disables the Login button on-the-fly as the user edits driver path/login/password fields, ensuring that
    // all are entered. Uses a DocumentListener that is notified whenever the JTextField/JPasswordField are modified.

    private class TextFieldFilled implements DocumentListener
    {

        private List<JTextField> textFields = new ArrayList<JTextField>();
        JButton button;

        public TextFieldFilled(JButton button)
        {

            this.button = button;

        }

        public void addTextField(JTextField textField)
        {

            if (textField != null)
            {

                textFields.add(textField);
                textField.getDocument().addDocumentListener(this);

            }

        }

        @Override
        public void changedUpdate(DocumentEvent event)
        {

            checkUpdatedField(event);

        }

        @Override
        public void insertUpdate(DocumentEvent event)
        {

            checkUpdatedField(event);

        }

        @Override
        public void removeUpdate(DocumentEvent event)
        {

            checkUpdatedField(event);

        }

        public boolean isCredentialsEntered()
        {

            for (JTextField textField : textFields)
            {

                // Eliminates leading and trailing spaces by using trim().

                if (textField.getText().trim().length() == 0)
                {

                    return false;

                }

            }

            return true;

        }

        private void checkUpdatedField(DocumentEvent event)
        {

            button.setEnabled(isCredentialsEntered());

        }

    }

    // Enables/disables the Start button on-the-fly as the user checks/unchecks check-boxes, ensuring that at least one
    // is selected.

    private class CheckboxesSelected implements ItemListener
    {

        private List<JCheckBox> watchCheckboxFields = new ArrayList<JCheckBox>();
        private JButton button;

        public CheckboxesSelected(JButton button)
        {

            this.button = button;

        }

        @Override
        public void itemStateChanged(ItemEvent event)
        {

            checkChangedItem(event);

        }

        public void addCheckbox(JCheckBox checkbox)
        {

            if (checkbox != null)
            {

                watchCheckboxFields.add(checkbox);
                checkbox.addItemListener(this);

            }

        }

        public boolean isCheckboxesSelected()
        {

            for (JCheckBox watchCheckboxField : watchCheckboxFields)

                if (watchCheckboxField.isSelected())
                {

                    return true;

                }

            return false;

        }

        public void checkChangedItem(ItemEvent event)
        {

            button.setEnabled(isCheckboxesSelected());

        }

    }

    // Creates a thread to perform lengthy Login task, using SwingWorker.

    private class SwingGUILengthyLogin extends SwingWorker<Void, Void>
    {

        private String driverPath;
        private String username;
        private char[] password;

        public SwingGUILengthyLogin(String driverPath, String username, char[] password)
        {

            this.driverPath = driverPath;
            this.username = username;
            this.password = password;

        }

        @Override
        protected Void doInBackground() throws Exception
        {

            fireLoginEvent(new LoginFormEvent(driverPath, username, password));

            return null;
        }

        @Override
        protected void done()
        {

            if (isDone())
            {

                exitButton.setEnabled(true);

            }

        }

    }

    // Create a thread to perform lengthy Start task, using SwingWorker.

    private class SwingGUILengthyStart extends SwingWorker<Void, Void>
    {

        boolean isWatchAllAdsChecked;
        boolean isWatchAllAdsPrizesChecked;

        public SwingGUILengthyStart(boolean isWatchAllAdsChecked, boolean iswatchAllAdsPrizesChecked)
        {

            this.isWatchAllAdsChecked = isWatchAllAdsChecked;
            this.isWatchAllAdsPrizesChecked = iswatchAllAdsPrizesChecked;

        }

        @Override
        protected Void doInBackground() throws Exception
        {

            fireStartEvent(isWatchAllAdsChecked, isWatchAllAdsPrizesChecked);

            return null;
        }

        @Override
        protected void done()
        {

            if (isDone())
            {

                startButton.setEnabled(true);
                stopButton.setEnabled(false);

                watchAllAdsCheck.setSelected(false);
                watchAllAdsPrizesCheck.setSelected(false);

            }

        }

    }

    // Create a thread to perform lengthy Stop task, using SwingWorker.

    private class SwingGUILengthyStop extends SwingWorker<Void, Void>
    {

        @Override
        protected Void doInBackground() throws Exception
        {

            if (swingGUILengthyStart != null)
            {

                swingGUILengthyStart.cancel(true);

            }

            fireStopEvent();

            return null;
        }

        @Override
        protected void done()
        {

            if (isDone())
            {
                
                startButton.setEnabled(true);
                stopButton.setEnabled(false);

                watchAllAdsCheck.setSelected(false);
                watchAllAdsPrizesCheck.setSelected(false);

            }

        }

    }

    // Create a thread to perform lengthy Exit task, using SwingWorker.

    private class SwingGUILengthyExit extends SwingWorker<Void, Void>
    {

        @Override
        protected Void doInBackground() throws Exception
        {

            fireExitEvent();

            return null;

        }

        @Override
        protected void done()
        {

            if (isDone())
            {

                dispose();

            }

        }

    }

}
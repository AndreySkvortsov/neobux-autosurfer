package org.avs.autosurfer.application;

import javax.swing.SwingUtilities;

import org.avs.autosurfer.view.SwingGUI;
import org.avs.autosurfer.controller.Controller;

/**
 * 
 * Creates and starts the application.
 * 
 * @author Andrey Skvortsov
 * 
 */
public class Autosurfer
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                runAutosurfer();
            }
        });
    }

    public static void runAutosurfer()
    {
        SwingGUI swingGUI = new SwingGUI();
        Controller controller = new Controller();

        // Links View-part (SwingGUI) with Controller-part (Controller).
        swingGUI.setLoginListener(controller);
        swingGUI.setStartListener(controller);
        swingGUI.setStopListener(controller);
        swingGUI.setExitListener(controller);
    }
}
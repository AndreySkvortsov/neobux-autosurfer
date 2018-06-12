package org.avs.autosurfer.view;

/**
 * 
 * Manages credentials and a path to ChromeDriver file in a safe manner by utilizing getters and setters.
 * 
 * @author Andrey Skvortsov
 * 
 */
public class LoginFormEvent
{
    private String driverPath;
    private String username;
    private char[] password;

    public LoginFormEvent(String driverPath, String username, char[] password)
    {
        this.driverPath = driverPath;
        this.username = username;
        this.password = password;
    }

    public String getDriverPath()
    {
        return driverPath;
    }

    public void setDriverPath(String driverPath)
    {
        this.driverPath = driverPath;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public char[] getPassword()
    {
        return password;
    }

    public void setPassword(char[] password)
    {
        this.password = password;
    };
}
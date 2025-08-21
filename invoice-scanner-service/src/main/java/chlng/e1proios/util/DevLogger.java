package chlng.e1proios.util;

import jakarta.inject.Singleton;

@Singleton
public class DevLogger {

    private boolean showTestLogs = true;

    public void log(String message) {
        this.log(message, false);
    }
    public void log(String message, boolean error) {
        if (this.showTestLogs) {
            if (error) {
                System.err.println(message);
            } else {
                System.out.println(message);
            }

        }
    }

    public void setTestLogging(boolean showTestLogs) {
        this.showTestLogs = showTestLogs;
    }
}

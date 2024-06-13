package vsdk.source;

import java.io.BufferedWriter;

import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

/**
 * VSDK Logger class.
 */
public class VLogger {
    private static boolean LOG_TO_FILES = false;

    private static final ArrayList<String> logs = new ArrayList<>();

    private static String log(String message, String type) {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];

        return String.format("[%s] %s->%s: %s [%s];", caller.getFileName(), caller.getClassName(), caller.getMethodName(), message, type);
    }

    private static void printAndRemember(String log) {
        if(LOG_TO_FILES) logs.add(log);

        System.out.println(log);
    }

    /**
     * Information log.
     *
     * @param message Message.
     */
    public static void info(String message) {
        printAndRemember(log(message, "INFO"));
    }

    /**
     * Warning log.
     *
     * @param message Message.
     */
    public static void warning(String message) {
        printAndRemember(log(message, "WARNING"));
    }

    /**
     * Error log.
     *
     * @param message Message.
     */
    public static void error(String message) {
        printAndRemember(log(message, "ERROR"));
    }

    /**
     * Critical log.
     *
     * @param message Message.
     */
    public static void critical(String message) {
        printAndRemember(log(message, "CRITICAL"));
    }

    /**
     * Write all messages logged earlier to file.
     *
     * @param file Path to file.
     */
    public static void saveLogs(String file) {
        BufferedWriter buffWriter = null;

        try {
            buffWriter = new BufferedWriter(new FileWriter(file));
        } catch(IOException ioException) {
            error("Can't save logs: FileWriter caused an IOException.");
        }

        if(buffWriter != null) {
            for(String log : logs) {
                try {
                    buffWriter.write(log + "\n");
                } catch(IOException ioException) {
                    error("Can't save logs: Writing to buffer caused an IOException.");
                }
            }
        }

        try {
            if(buffWriter != null) buffWriter.close();
        } catch(IOException ioException) {
            error("Can't save logs: Closing buffer caused an IOException.");
        }
    }

    /**
     * Should logger remember logs so later you can use <code>saveLogs</code>?
     *
     * @param log Enable logging to files?
     */
    public static void setLogToFiles(boolean log) {
        LOG_TO_FILES = log;
    }
}

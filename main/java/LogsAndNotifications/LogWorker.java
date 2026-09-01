package LogsAndNotifications;


import java.io.File;
import java.nio.file.*;


public class LogWorker {
    private static File logFile;

    public static void out(Log log) {
        System.out.println(log.out(true, false, true));
    }

    public static void out(LogLevel level, LogType type, Class anyClass, String body) {
        Log log = new Log(level, type, anyClass, body);
        out(log);
    }

    public static void out(LogLevel level, LogType type, Class anyClass, String body, Exception error) {
        Log log = new Log(level, type, anyClass, body, error);
        out(log);
        error.printStackTrace();
    }


    public static void initLogFile() {
        if (logFile != null) return;
        int i = 0;
        try {
            do {
                logFile = new File("log" + i + ".txt");
                i++;
            } while (!logFile.createNewFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

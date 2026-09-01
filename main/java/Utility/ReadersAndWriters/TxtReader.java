package Utility.ReadersAndWriters;

import LogsAndNotifications.LogLevel;
import LogsAndNotifications.LogType;
import LogsAndNotifications.LogWorker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public abstract class TxtReader {
    public File file;

    public TxtReader(File file) {
        this.file = file;
    }

    public TxtReader(String pathToFile, String fileName) {
        file = new File(pathToFile + "\\" + fileName);
    }

    public final ArrayList<String> readAll() {
        try {
            return (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LogWorker.out(LogLevel.CRITICAL, LogType.FILE_IO, getClass(), "Reading error " + file.getAbsolutePath(), e);
        }
        return null;
    }
    public final ArrayList<String> read() {
        try {
            ArrayList<String> list = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            ArrayList<String> output = new ArrayList<>();
            list.forEach(s -> {
                String notEmpty = notEmpty(s);
                if (notEmpty != null)
                    output.add(notEmpty.trim());
            });
            return output;
        } catch (IOException e) {
            LogWorker.out(LogLevel.CRITICAL, LogType.FILE_IO, getClass(), "Reading error " + file.getAbsolutePath(), e);
        }
        return null;
    }

    public String findText(String toFind) {
        ArrayList<String> input = read();
        for (String s : input)
            if (s.contains(toFind))
                return s;
        return null;
    }

    public final String readerAsString() {
        ArrayList<String> list = read();
        if (list == null) return null;
        StringBuilder output = new StringBuilder();
        list.forEach(s -> {
            output.append(s).append("\n");
        });
        return output.toString();
    }

    public abstract String notEmpty(String s);// return input String to read all, null isn't added

    public String getPath() {
        return file.getPath();
    }
}

package Utility.ReadersAndWriters;

import LogsAndNotifications.LogLevel;
import LogsAndNotifications.LogType;
import LogsAndNotifications.LogWorker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TxtWriter {
    private File file;
    private String filename;
    private boolean isTemp = false;

    public TxtWriter(String pathToFile, String fileName) {
        this.filename = fileName;
        file = new File(pathToFile + "\\" + fileName);
    }

    public TxtWriter(File file) {
        this.file = file;
    }

    public void writingToFile(ArrayList<String> input) throws IOException {
        if (input != null && input.size() > 0) {
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            StringBuilder toWrite = new StringBuilder();
            for (String s : input) {
                toWrite.append(s).append("\n");
            }
            if ((int) toWrite.charAt(toWrite.length() - 1) == 10)
                toWrite.deleteCharAt(toWrite.length() - 1);
            if (toWrite.toString().isEmpty()) throw new IOException();
            writer.println(toWrite);
            writer.flush();
            writer.close();
        }
    }

    public void writingToFile(String input) {
        if (!(input != null && input.length() > 0)) return;
        try {
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            writer.println(input);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replaceText(String toFind, String toReplace) throws IOException {
        TxtReader reader = new TxtReader(file) {
            @Override
            public String notEmpty(String s) {
                return s;
            }
        };
        if (reader.read() == null) throw new IOException();
        ArrayList<String> output = new ArrayList<>();
        for (String s : reader.read()) {
            output.add(s.replace(toFind, toReplace));
        }
        writingToFile(output);
    }

    public void tempFile() {
        try {
            File tmpFile = File.createTempFile(filename, ".txt");
            this.file = tmpFile;
            isTemp = true;
            file.deleteOnExit();
        } catch (IOException e) {
            LogWorker.out(LogLevel.CRITICAL, LogType.FILE_IO, getClass(), "Can't create file", e);
        }
    }

    public boolean isTemp() {
        return isTemp;
    }

    public File getFile() {
        return file;
    }
}

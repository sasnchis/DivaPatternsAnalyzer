package Utility;


import LogsAndNotifications.LogLevel;
import LogsAndNotifications.LogType;
import LogsAndNotifications.LogWorker;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.Scanner;

public class MyUtilities {
    public static void setClipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static int toInteger(String s) {
        if (s.startsWith("#hide#")) s = s.substring(6);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static String strGetAfterEquals(String str) {
        if (str != null && str.split("=").length >= 2)
            return str.substring(str.indexOf("=")+1).trim();
        else
            return null;
    }

    public static boolean yes_no() throws YesNoException {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        if (s.equalsIgnoreCase("y")) {
            return true;
        }
        if (s.equalsIgnoreCase("n")) {
            return false;
        }
        throw new YesNoException();
    }

    public static double toDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static Comparator<String> stringComparator() {
        return (first, second) -> {
            first = first.replace("#hide#","").split("=")[0]; // delete #hide# and take before =
            second = second.replace("#hide#","").split("=")[0];
            return MyUtilities.isLineEquals(first, second);
        };
    }

    public static int isLineEquals(String first, String second) {
        if (first.equals(second)) return 0; // return duplicate
        for (int j = 0; (j < first.length()) && (j < second.length()); j++) {
            if (first.charAt(j) == second.charAt(j)) {
            } else if (first.charAt(j) > second.charAt(j)) {
                return 1; // INCREMENT (RIGHT SORTING FOR LogicAndObjects.Objects.PVDB)
            } else return -1; // DECREMENT (INCORRECT FOR LogicAndObjects.Objects.PVDB)
        }
        if (first.length() > second.length()) return 1;
        else return -1;
    }

    public static String getRuntimePath() {
        String path = null;
        try {
            path = new File(".").getCanonicalPath();
        } catch (IOException e) {
            LogWorker.out(LogLevel.CRASH, LogType.INIT, MyUtilities.class, "Can't load runtime path", e);
        }
        return path;
    }

    public static String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class YesNoException extends Exception {
    }
}

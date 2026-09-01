package LogsAndNotifications;

public enum LogLevel {
    INFO(false),
    WARNING(true),
    CRITICAL(true),
    CRASH(true);

    private boolean showing;

    LogLevel(boolean showing) {
        this.showing = showing;
    }

    static {
        // TODO Log Level in Settings
    }


    public boolean isOn() {
        return showing;
    }
}

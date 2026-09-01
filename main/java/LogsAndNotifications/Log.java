package LogsAndNotifications;

public class Log {
    private final LogLevel level;
    private final LogType type;
    private final String source;
    private final String body;
    private final Exception error;

    public Log(LogLevel level, LogType type, String source, String body, Exception error) {
        this.level = level;
        this.type = type;
        this.source = source;
        this.body = body;
        this.error = error;
    }

    public Log(LogLevel level, LogType type, Class anyClass, String body, Exception error) {
        this(level, type, anyClass.toString(), body, error);
    }

    public Log(LogLevel level, LogType type, Class anyClass, String body) {
        this(level, type, anyClass.toString(), body, null);
    }


    public LogLevel getLevel() {
        return level;
    }

    public LogType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getBody() {
        return body;
    }

    public Exception getError() {
        return error;
    }

    public String out(boolean needType, boolean needSource, boolean needError) {
        StringBuilder out = new StringBuilder();
        // LEVEL
        out.append("[").append(level.toString()).append("] ");
        // TYPE
        if (needType)
            out.append("(").append(type.toString()).append(") ");
        // SOURCE
        if (needSource)
            out.append("(source:").append(source).append("): ");
        // BODY
        out.append(body);
        // ERROR
        if (needError && error != null) {
            out.append("\n\t [ERROR] ").append(error.getMessage());
            error.printStackTrace();
        }
        // OUTPUT
        return out.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Log)) return false;
        Log objGet = (Log) obj;
        return objGet.type == type &&
                objGet.level == level &&
                objGet.body.equals(body);
    }
}

package Exceptions;

public class ScriptNotParsed extends RuntimeException {
    public ScriptNotParsed(String message) {
        super(message);
    }

  public ScriptNotParsed(String message, Throwable cause) {
    super(message, cause);
  }

  public ScriptNotParsed(Throwable cause) {
    super(cause);
  }

  public ScriptNotParsed(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ScriptNotParsed() {
  }
}

package ScriptDecryptor.PVScriptDecriptor;

import java.util.Arrays;

public class PVCommand {
    public final static int MaxPVCommandParamCount = 24;
    public PVCommandType Type;
    public long[] param = new long[MaxPVCommandParamCount];

    public PVCommand() {
    }

    public PVCommand(PVCommandType type) {
        Type = type;
    }

    public long ParamCount() {
        return PVScript.GetPVCommandParamCount(Type);
    }
    public String Name() {
        return PVScript.GetPVCommandName(Type);
    }

    @Override
    public String toString() {
        return "PVCommand{" +
                "Type=" + Type +
                ", param=" + Arrays.toString(param) +
                '}';
    }
}

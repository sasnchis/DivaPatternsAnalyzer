package ScriptDecryptor;

import ScriptDecryptor.PVScriptDecriptor.PVCommand;
import ScriptDecryptor.PVScriptDecriptor.PVCommandType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PVScriptTimings {
    private long time;
    private final ArrayList<PVCommand> pvCommands = new ArrayList<>();

    public PVScriptTimings(long time, PVCommand pvCommand) {
        this.time = time;
        this.pvCommands.add(pvCommand);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<PVCommand> getPvCommands() {
        return pvCommands;
    }

    public boolean add(long time, PVCommand command) {
        if (this.time == time) {
            pvCommands.add(command);
            return true;
        } else
            return false;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("{");
        pvCommands.forEach(command -> out.append(command.Type).append(" | "));
        out.delete(out.length()-3, out.length());
        out.append("}");
        return "PVScriptTime " + time + " " + out.toString();
    }

    public boolean contains(PVCommandType type) {
        return pvCommands.parallelStream().anyMatch(command -> command.Type == type);
    }

    public ArrayList<PVCommand> getByType(PVCommandType type) {
        if (contains(type))
            return pvCommands.parallelStream().filter(command -> command.Type==type).collect(Collectors.toCollection(ArrayList::new));
        else return null;
    }
}

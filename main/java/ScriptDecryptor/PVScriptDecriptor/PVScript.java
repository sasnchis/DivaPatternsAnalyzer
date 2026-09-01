package ScriptDecryptor.PVScriptDecriptor;

import Exceptions.ScriptNotParsed;
import LogsAndNotifications.LogLevel;
import LogsAndNotifications.LogType;
import LogsAndNotifications.LogWorker;
import ScriptDecryptor.PVScriptTimings;
import ScriptDecryptor.Targets.*;
import Utility.ReadersAndWriters.ReadDSC.StreamReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import static ScriptDecryptor.PVScriptDecriptor.PVCommandType.*;

public class PVScript extends StreamReader {
    private Long scriptVersion;
    private final Vector<PVCommand> pvCommands = new Vector<>();

    public PVScript(File file) throws IOException {
        super(file);
    }

    public PVScript(byte[] bytes) {
        super(bytes);
    }

    public static int GetPVCommandParamCount(PVCommandType type) {
        return type.id >= PVCommandType.values().length ? 0 : values()[type.id].paramCount;
    }

    protected static String GetPVCommandName(PVCommandType type) {
        return type.id >= PVCommandType.values().length ? "UNKNOWN" : values()[type.id].displayName;
    }

    public Long getScriptVersion() {
        return scriptVersion;
    }

    public static String chooseDSC() {
        return "";
    }

    public void addCommand(PVCommand command) {
        pvCommands.add(command);
    }

    public void addCommand(PVCommandType type, long[] params) {
        PVCommand command = new PVCommand(type);
        command.param = params;
        addCommand(command);
    }

    /**
     * <h1>Util to convert PVCommands to targets with time and fly-time.</h1>
     * <h2>First part (Getting command):</h2>
     * <u1>
     *     <li>Get PVCommand</li>
     *     <li>Check it for Time, Target and TargetFlyingTime</li>
     *     <li>Set time when finds Time PVCommand, set TargetFlyingTime when finds it</li>
     *     <li> When finds Target -> go to Second part</li>
     * </u1>
     * <h2>Second Part (Ends check):</h2>
     * <u1>
     *     <li>Checking target for exists</li>
     *     <li>Changes first to SlideL of SlideR if its first in a row for ChainSlide</li>
     *     <li>If new Time or FlyingTime add as new TargetInfo, if not its adds to last TargetInfo</li>
     * </u1>
     *
     * <h1>Required for display script</h1>
     * @return Time, FlyTime, Targets to display as list.
     */
    public TargetList toTargets() {
        TargetList output = new TargetList();
        long lastTime = -1;
        long lastFlyingTime = 0;
        // Getting command
        for (PVCommand curCommand : pvCommands) {
            if (curCommand.Type == Time) {
                lastTime = curCommand.param[0];
                continue;
            }
            if (curCommand.Type == TargetFlyingTime) {
                lastFlyingTime = curCommand.param[0];
                continue;
            }
            if (curCommand.Type == Target) {
                if (!output.isEmpty() && output.getLast().getTime() == lastTime) {
                    output.getLast().addNote(TargetHelper.toTarget(curCommand));
                } else {
                    output.add(new TargetInfo(lastTime, lastFlyingTime, TargetHelper.toTarget(curCommand)));
                }

            }
        }
        return output;
    }

    public ArrayList<PVScriptTimings> getPVTimingsList() throws ScriptNotParsed {
        long lastTime = 0;
        ArrayList<PVScriptTimings> timings = new ArrayList<>();
        if (pvCommands == null || pvCommands.size() == 0) throw new ScriptNotParsed();
        for (PVCommand command : pvCommands) {
            if (command.Type == Time) {
                lastTime = command.param[0];
            }
            if (timings.size() > 0) {
                if (!timings.get(timings.size()-1).add(lastTime, command)) {
                    timings.add(new PVScriptTimings(lastTime, command));
                }
            } timings.add(new PVScriptTimings(lastTime, command));
        }
        return timings;
    }

    public Vector<PVCommand> getPvCommands() {
        return pvCommands;
    }

    /**
     * Converts LogicAndObjects.Objects.PVScript into line
     * same with DSCEditor <a href="https://nastys.github.io/">...</a>
     * and with Comfy Studio <a href="https://github.com/samyuu/Comfy">...</a>
     *
     * @return String with all commands, 1 command = 1 line
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (PVCommand command : pvCommands) {
            builder.append(command.Type.displayName).append("(");
            for (int i = 0; i < command.Type.paramCount; i++) {
                builder.append(command.param[i]);
                if (i < command.Type.paramCount - 1) builder.append(", ");
            }
            builder.append(");").append("\n");
        }
        return builder.toString();
    }



    /**
     * Parsing script file, necessary for getting PVCommands
     */
    public void parse() {
        try {
            scriptVersion = readUnsignedInt();
            while (stream.available() > 1) {
                try {
                    long readHead = readUnsignedInt();
                    PVCommandType commandType = PVCommandType.values()[(int) readHead];
                    int paramCount = GetPVCommandParamCount(commandType);
                    long[] params = new long[paramCount];
                    for (int i = 0; i < paramCount; i++) {
                        params[i] = readInt();
                    }
                    addCommand(commandType, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            stream.close();
        } catch (Exception e) {
            LogWorker.out(LogLevel.CRITICAL, LogType.PV_SCRIPT, PVScript.class, "Something happening when decrypt dsc.", e);
        }
    }

    /**
     * @return last TargetType.Time from LogicAndObjects.Objects.PVScript (.dsc)
     */
    public long getLastTime() {
        for (int i = pvCommands.size() - 1; i >= 0; i--) {
            if (pvCommands.get(i).Type == Time) {
                return pvCommands.get(i).param[0];
            }
        }
        return -1;
    }

    /**
     * @return correct end time of script
     * true = correct
     * false = incorrect
     */
    public boolean checkingEndingScript() {
        long lastTime = -1;
        for (int i = pvCommands.size() - 1; i >= 0; i--) {
            if (pvCommands.get(i).Type == Time) {
                if (lastTime == -1)
                    lastTime = pvCommands.get(i).param[0];
                else
                    return lastTime - pvCommands.get(i).param[0] < 100000;
            }
        }
        return false;
    }

}

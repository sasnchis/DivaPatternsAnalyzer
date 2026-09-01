package ScriptDecryptor.Targets;

import ScriptDecryptor.PVScriptDecriptor.PVCommand;
import ScriptDecryptor.PVScriptDecriptor.PVCommandType;

public class TargetHelper {
    public static Target toTarget(PVCommand command) {
        if (command.Type != PVCommandType.Target) return null;
        TargetType type = TargetType.get((int) command.param[0]);
        double posX = (double) command.param[1]; // 500
        double posY = (double) command.param[2]; // 500
        double angle = (double) command.param[3]; // 1000
        double distance = (double) command.param[4]; // 1000
        double amplitude = (double) command.param[5];
        double frequency = (double) command.param[6];
        Target target = new Target(posX, posY, angle, distance, amplitude, frequency);
        target.setType(type);
        return target;
    }
}

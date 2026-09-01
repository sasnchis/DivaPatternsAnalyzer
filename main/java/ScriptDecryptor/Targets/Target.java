package ScriptDecryptor.Targets;

import ScriptDecryptor.PVScriptDecriptor.PVCommand;

public class Target {
    protected TargetType type;
    protected double posX;
    protected double posY;
    protected double angle;
    protected double distance;
    protected double amplitude;
    protected double frequency;
    protected boolean sync;

    public Target(double posX, double posY, double angle, double distance, double amplitude, double frequency) {
        this(posX, posY, angle, distance, amplitude, frequency, false);
    }
    public Target(double posX, double posY, double angle, double distance, double amplitude, double frequency, boolean sync) {
        this.sync = sync;
        this.posX = posX;
        this.posY = posY;
        this.angle = angle;
        this.distance = distance;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    public void setType(TargetType type) {
        this.type = type;
    }

    public final TargetType getType() {
        return type;
    }

    public final double getPosX() {
        return posX;
    }

    public final double getPosY() {
        return posY;
    }

    public final double getAngle() {
        return angle;
    }

    public final double getDistance() {
        return distance;
    }

    public final double getAmplitude() {
        return amplitude;
    }

    public final double getFrequency() {
        return frequency;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}

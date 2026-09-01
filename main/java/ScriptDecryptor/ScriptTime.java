package ScriptDecryptor;

import java.time.LocalTime;

public class ScriptTime {
    public int h;
    public int min;
    public int sec;
    public int nanosec;

    public ScriptTime(int h, int min, int sec, int nanosec) {
        this.h = h;
        this.min = min;
        this.sec = sec;
        this.nanosec = nanosec;
    }

    public ScriptTime(LocalTime time) {
        h = time.getHour();
        min = time.getMinute();
        sec = time.getSecond();
        nanosec = time.getNano();
    }

    /**
     * @param time transforms to HH:MM:SS.nano
     */
    public ScriptTime(long time) {
        calc(time);
    }

    private void calc(long time) {
        nanosec = (int) (time % 100000);
        time -= nanosec;
        time /= 100000;
        h = (int) (time / 3600);
        time -= h* 3600L;
        min = (int) (time/60);
        time -= min* 60L;
        sec = (int) time;
    }

    public long getNano() {
        return (h* 3600L +min* 60L + sec)*100000L+nanosec;
    }
    /**
     * @return as HH:MM:SS.nanos
     */
    @Override
    public String toString() {
        String hours = h < 10 ? "0" + h : ""+h;
        String minutes = min < 10 ? "0" + min : ""+min;
        String seconds = sec < 10 ? "0" + sec : ""+sec;
        return hours + ":" + minutes + ":" + seconds + "." + String.format("%5d", nanosec);
    }
    /**
     * @return seconds without nanos
     */
    public int asSeconds() {
        return h*3600+min*60+sec;
    }
    /**
     * @return MM:SS style
     */
    public String getMinSec() {
        String seconds = sec < 10 ? "0" + sec : ""+sec;
        String minutes = (h*60+min) < 10 ? "0" + (h*60+min) : ""+(h*60+min);
        return minutes+":"+seconds;
    }

    public ScriptTime addNanosec (long nano) {
        calc(getNano()+nano);
        return this;
    }

    public static ScriptTime parseStringToNano(String s) {
        String[] getNano = s.split("\\.");
        int nanos = 0;
        try {
        if (getNano.length == 2) {
            if (getNano[1].length() < 5) getNano[1] = getNano[1] + "0".repeat(5-getNano[1].length());
            nanos = Integer.parseInt(getNano[1]);
        }
        } catch (Exception ignored) { }

        String[] getTime = getNano[0].split(":");
        int hours = 0; // why I do this? for what?
        try {
            if (getTime.length == 3) hours = Integer.parseInt(getTime[0]);
        } catch (Exception ignored) { }

        int min = 0;
        try {
            if (getTime.length == 3) min = Integer.parseInt(getTime[1]);
            else if (getTime.length == 2) min = Integer.parseInt(getTime[0]);
        } catch (Exception ignored) { }

        int sec = 0;
        try {
            if (getTime.length == 3) sec = Integer.parseInt(getTime[2]);
            else if (getTime.length == 2) sec = Integer.parseInt(getTime[1]);
            else if (getTime.length == 1) sec = Integer.parseInt(getTime[0]);
        } catch (Exception ignored) { }

        return new ScriptTime(hours, min, sec, nanos);
    }
}

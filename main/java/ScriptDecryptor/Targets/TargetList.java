package ScriptDecryptor.Targets;

import java.util.ArrayList;

public class TargetList extends ArrayList<TargetInfo> {

    public TargetList() {
    }

    public TargetList(TargetInfo testNote) {
        add(testNote);
    }

    public TargetInfo getLast() {
        if (size() == 0) return null;
        return get(size()-1);
    }

    public void check() {
        this.parallelStream().forEach(TargetInfo::checkCorrect);
    }
    public void calc() {
        parallelStream().forEach(TargetInfo::setSync);
        parallelStream().forEach(TargetInfo::calcBPM);

        for (int i = 0; i < size()-1; i++) {
            get(i).calcBPMPatternToNextAsFlyTime(get(i+1));
            get(i).calcBPMAsNoteTimings(get(i+1));
            get(i).calcBPMPatternToNextAndMovement(get(i+1));
        }
        getLast().calcBPMPatternToNextAsFlyTime(null);
        getLast().calcBPMPatternToNextAndMovement(null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (TargetInfo note : this) {
            builder.append(note).append("\n");
        }
        return builder.toString();
    }
}

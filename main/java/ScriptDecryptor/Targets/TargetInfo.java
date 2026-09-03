package ScriptDecryptor.Targets;

import Exceptions.NoTargets;

import java.util.ArrayList;

public class TargetInfo {
    private final ArrayList<Target> notes;
    private double beatsPerMinuteAsFlyTime = 0;
    private double beatsPerMinuteAsNoteTimings = 0;
    private double bpmPatternToNextAsFlyTime;
    private double bpmPatternToNextAsPlacement;
    private double angleMovementToNextNote;

    private final long flyTime; // flying time
    private final long time; // pressed time
    private boolean isMultiNote = false;
    private boolean isGimmick = false;

    public TargetInfo(long time, long flyTime, Target note) {
        this.flyTime = flyTime;
        this.notes = new ArrayList<>();
        this.time = time;
        addNote(note);
    }

    public double getBPMAsFlyTime() {
        return beatsPerMinuteAsFlyTime;
    }

    public double getBPMAsNoteTimings() {
        return beatsPerMinuteAsNoteTimings;
    }

    public double getAngleMovementToNextNote() {
        return angleMovementToNextNote;
    }

    public String getBpmPatternToNextAsFlyTimeAsString() {
        if (bpmPatternToNextAsFlyTime == -1) return "Change Fly Time";
        if (bpmPatternToNextAsFlyTime == 0) return "Last Note";
        if (bpmPatternToNextAsFlyTime < 1) return "1/" + String.format("%.3f", bpmPatternToNextAsFlyTime);
        else return "1/" + String.format("%.0f", bpmPatternToNextAsFlyTime);
    }

    public double getBpmPatternToNextAsFlyTime() {
        return bpmPatternToNextAsFlyTime;
    }

    public String getBpmPatternToNextAsPlacementString() {
        if (bpmPatternToNextAsPlacement == -1) return "Change Fly Time";
        if (bpmPatternToNextAsPlacement == 0) return "Last Note";
        if (bpmPatternToNextAsPlacement < 1) return "1/" + String.format("%.3f", bpmPatternToNextAsPlacement);
        else return "1/" + String.format("%.0f", bpmPatternToNextAsPlacement);
    }

    public double getBpmPatternToNextAsPlacement() {
        return bpmPatternToNextAsPlacement;
    }

    public long getFlyTime() {
        return flyTime;
    }

    public ArrayList<Target> getNotes() {
        return notes;
    }

    public long getTime() {
        return time;
    }

    public boolean isMultiNote() {
        return isMultiNote;
    }

    public boolean isGimmick() {
        return isGimmick;
    }

    public void addNote(Target note) {
        notes.add(note);
    }

    public void setSync() {
        if (notes.size() >= 2) {
            isMultiNote = true;
        }
    }

    public void checkCorrect() throws NoTargets {
        if (notes.isEmpty()) throw new NoTargets("No targets in list");
    }

    public void calcBPM() {
        beatsPerMinuteAsFlyTime = 60.0 / flyTime * 4000;
    }

    public void calcBPMPatternToNextAsFlyTime(TargetInfo target2) {
        if (target2 == null) {
            bpmPatternToNextAsFlyTime = 0;
            return;
        }
        if (flyTime == target2.flyTime) {
            long diff = target2.time - time;
            double exact = flyTime * 100.0 / diff;
            bpmPatternToNextAsFlyTime = exact > 1 ? Math.round(exact) : exact;
        } else {
            bpmPatternToNextAsFlyTime = -1;
        }
    }

    public void calcBPMPatternToNextAndMovement(TargetInfo target2) {
        if (target2 == null) {
            bpmPatternToNextAsFlyTime = 0;
            return;
        }
        if (flyTime == target2.flyTime) {
            ArrayList<Double> placementDiff = new ArrayList<>();
            ArrayList<Double> tanDiff = new ArrayList<>();
            for (Target noteFirst : notes) {
                for (Target noteSecond : target2.notes) {
                    double diffX = noteFirst.posX - noteSecond.posX;
                    double diffY = noteFirst.posY - noteSecond.posY;
                    double diffPif = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
                    placementDiff.add(diffPif); // /1000/3 -> to placement
                    double tan = Math.toDegrees(Math.atan2(diffY, diffX)) - 90;
                    if (tan < 0) tan += 360;
                    tanDiff.add(tan);
                }
            }
            int check = 0;
            for (int i = 0; i < placementDiff.size() - 1; i++) {
                if (placementDiff.get(i) < placementDiff.get(i + 1))
                    check = i;
            }
            bpmPatternToNextAsPlacement = 192000 / placementDiff.get(check);
            angleMovementToNextNote = tanDiff.get(check);

        } else {
            bpmPatternToNextAsFlyTime = -1;
        }
    }

    public void calcBPMAsNoteTimings(TargetInfo target2) {
        if (target2 == null) {
            beatsPerMinuteAsNoteTimings = 0;
            return;
        }
        if (flyTime == target2.flyTime) {
            long diff = target2.time - time;
            double exact = 1500000.0 / diff;
            beatsPerMinuteAsNoteTimings = (double) Math.round(exact * 1000) / 1000;
        } else {
            beatsPerMinuteAsNoteTimings = -1;
        }
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();
        if (isMultiNote) {
            builder.append("MULTI(");
            for (Target note : notes) {
                builder.append(note).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
            builder.append(")");
        } else builder.append(notes.getFirst());
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (isMultiNote) {
            builder.append("MULTI(");
            for (Target note : notes) {
                builder.append(note).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
            builder.append(")");
        } else builder.append(notes.getFirst());
        builder.append(String.format(" %s %.5f, %s %s",
                "AngleToNext =", angleMovementToNextNote, "BPM as Timing =", getBPMAsNoteTimings()));

        return builder.toString();
    }
}

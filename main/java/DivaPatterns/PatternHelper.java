package DivaPatterns;

import ScriptDecryptor.Targets.Target;
import ScriptDecryptor.Targets.TargetInfo;

import static DivaPatterns.PatternsSettings.CORRECT_TIMING_DIFFERENCE;
import static ScriptDecryptor.Targets.TargetType.*;
import static ScriptDecryptor.Targets.TargetType.SlideRChance;
import static ScriptDecryptor.Targets.TargetType.Star;
import static ScriptDecryptor.Targets.TargetType.StarDouble;

public class PatternHelper {

    public static long[] getDiffTimings(TargetInfo... notes) {
        long[] output = new long[notes.length-1];
        for (int i = 0; i < output.length; i++) {
            output[i] = notes[i+1].getTime()-notes[i].getTime();
        }
        return output;
    }

    public static boolean is360Pattern(TargetInfo note1, TargetInfo note2, TargetInfo note3, TargetInfo note4) {
        if (note1.isMultiNote() || note2.isMultiNote() || note3.isMultiNote() || note4.isMultiNote()) return false;
        int[] types = {
                        note1.getNotes().getFirst().getType().id,
                        note2.getNotes().getFirst().getType().id,
                        note3.getNotes().getFirst().getType().id,
                        note4.getNotes().getFirst().getType().id
        };

        for (int k = 0; k < 4; k++) { // To 1-4 notes, chance hold and double (NC) notes
            types[k] = convertTypeNoteToBase(types[k]);
        }
        for (int k = 0; k < types.length-1; k++) { // Checking for same note
            if (types[k] == types[k+1]) return false;
        }
        /* 1 - C, 2 - X, 3 - S, 4 - T
         Clockwise
         4-3-2-1 -> 4-4 = 0 -> +0 -> 4 3 2 1 -> %4 -> 0 3 2 1
         3-2-1-4- > 4-3 = 1 -> +1 -> 4 3 2 5 -> %4 -> 0 3 2 1
         2-1-4-3 -> 4-2 = 2 -> +2 -> 4 3 6 5 -> %4 -> 0 3 2 1
         1-4-3-2 -> 4-1 = 3 -> +3 -> 4 7 6 5 -> %4 -> 0 3 2 1
         Counterclockwise
         1-2-3-4 -> 4-1 = 3 -> +3 -> 4 5 6 7 -> %4 -> 0 1 2 3
         2-3-4-1 -> 4-2 = 2 -> +2 -> 4 5 6 3 -> %4 -> 0 1 2 3
         3-4-1-2 -> 4-3 = 1 -> +1 -> 4 5 2 3 -> %4 -> 0 1 2 3
          4-1-2-3 -> 4-4 = 0 -> +0 -> 4 1 2 3 -> %4 -> 0 1 2 3
      */
        int toAdd = 4 - types[0];
        for (int k = 0; k < 4; k++) {
            types[k] = (types[k] + toAdd) % 4;
        }
        // Clockwise
        if (types[0] == 0 && types[1] == 3 && types[2] == 2 && types[3] == 1) return true;
        // Counterclockwise
        if (types[0] == 0 && types[1] == 1 && types[2] == 2 && types[3] == 3) return true;
        return false;
    }

    public static int convertTypeNoteToBase(int type) {
        if (type >= 4 && type <= 7) type -= 4; // Hold
        if (type >= 18 && type <= 21) type -= 18; // Chance
        if (type >= 29 && type <= 32) type -= 29; // Double (NC)
        if (type == 0) type = 4; // Triangle to 4
        return type;
    }

    public static boolean isSlideNote(TargetInfo note) {
        return note
                .getNotes()
                .stream()
                .map(Target::getType)
                .anyMatch(type -> (type.id >= SlideBoth.id &&  type.id <= SlideChainR.id) ||
                        (type.id >= SlideBothChance.id && type.id <= SlideRChance.id) ||
                        type == Star || type == StarDouble);
    }

    public static boolean isChainSlideNote(TargetInfo note) {
        return note.getNotes()
                .stream()
                .map(Target::getType)
                .anyMatch(type -> type == SlideChainR || type == SlideChainL);
    }

    public static boolean isSameTiming(double multiplier, TargetInfo... notes) { // x1 for 1/8, x1.5 for 1/12,
        long[] diffTimes = getDiffTimings(notes);
        boolean output = true;
        boolean[] check = new boolean[diffTimes.length-1];
        for (int i = 0; i < diffTimes.length-1; i++) {
            if (isChainSlideNote(notes[i+1]) && isChainSlideNote(notes[i])) check[i] = false;
            if ((Math.abs(diffTimes[i+1] - diffTimes[i]*multiplier) < CORRECT_TIMING_DIFFERENCE)) check[i] = true;
            if ((Math.abs(diffTimes[i+1]*multiplier - diffTimes[i]) < CORRECT_TIMING_DIFFERENCE)) check[i] = true;
        }
        for (boolean b : check) {
            if (!b) {
                output = false;
                break;
            }
        }
        return output;
    }
}

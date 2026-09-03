package DivaPatterns;

import ScriptDecryptor.Targets.Target;
import ScriptDecryptor.Targets.TargetInfo;

import java.util.Comparator;

import static DivaPatterns.PatternsSettings.CORRECT_TIMING_DIFFERENCE;
import static ScriptDecryptor.Targets.TargetType.*;
import static ScriptDecryptor.Targets.TargetType.SlideRChance;
import static ScriptDecryptor.Targets.TargetType.Star;
import static ScriptDecryptor.Targets.TargetType.StarDouble;

public class PatternHelper {

    public static long[] getDiffTimings(TargetInfo... notes) {
        long[] output = new long[notes.length - 1];
        for (int i = 0; i < output.length; i++) {
            output[i] = notes[i + 1].getTime() - notes[i].getTime();
        }
        return output;
    }

    public static boolean is360Pattern(TargetInfo note1, TargetInfo note2, TargetInfo note3, TargetInfo note4) {
        OrderNaming order = isClockwiseCounterClockwisePattern(note1, note2, note3, note4);
        return order != null;
    }

    public static boolean is270Pattern(TargetInfo note1, TargetInfo note2, TargetInfo note3) {
        OrderNaming order = isClockwiseCounterClockwisePattern(note1, note2, note3);
        return order != null;
    }

    public static OrderNaming isClockwiseCounterClockwisePattern(TargetInfo... notes) {
        for (TargetInfo note : notes) if (note.isMultiNote()) return null;
        boolean isCounterClockwise = true;
        boolean isClockwise = true;
        for (int i = 0; i < notes.length-1; i++) {
            OrderNaming order = getOrderOfNextNote(notes[i], notes[i+1]);
            if (order != OrderNaming.CLOCKWISE) {
                isClockwise = false;
            }
            if (order != OrderNaming.COUNTER_CLOCKWISE) {
                isCounterClockwise = false;
            }
            if (!isClockwise && !isCounterClockwise) return null;
        }
        if (isClockwise) return OrderNaming.CLOCKWISE;
        if (isCounterClockwise) return OrderNaming.COUNTER_CLOCKWISE;
        return null;
    }

    /**
     * @return INCORRECT if can't get order to next
     *         SAME if same notes
     *         OPPOSITE if opposite notes (T-X, S-C)
     *         TRIPLES_CHANGING if triples and not same triples
     *         CLOCKWISE if its clockwise (T-C-X-S)
     *         COUNTER_CLOCKWISE if its counter clockwise (T-S-X-C)
     */
    public static OrderNaming getOrderOfNextNote(TargetInfo notes1, TargetInfo notes2) {
        if (notes1.isGimmick() || notes2.isGimmick()) return OrderNaming.INCORRECT;
        if (isSlideNote(notes1) || isSlideNote(notes2)) return OrderNaming.INCORRECT;
        if (notes1.getNotes().size() != notes2.getNotes().size()) return OrderNaming.INCORRECT; // not same num of notes
        if (isSameNote(notes1, notes2)) return OrderNaming.SAME;
        if (isOppositeNote(notes1, notes2)) return OrderNaming.OPPOSITE;
        if (isTriplesChangingNote(notes1, notes2)) return OrderNaming.TRIPLES_CHANGING;
        int directional = getDirectionalOfNextNote(notes1, notes2);
        int checkException = checkExceptionDirectionInRulesForDoubles(notes1, notes2);
        if (checkException != 0) directional = checkException;
        if (directional > 0) return OrderNaming.CLOCKWISE;
        if (directional < 0) return OrderNaming.COUNTER_CLOCKWISE;
        return OrderNaming.INCORRECT;
    }

    /**
     * @return <0 if counter clockwise
     *         >0 if clockwise
     *         0 if incorrect
     */
    public static int getDirectionalOfNextNote(TargetInfo notes1, TargetInfo notes2) {
        int directional = 0;
        for (int i = 0; i < notes1.getNotes().size(); i++) {
            int type1 = notes1.getNotes().get(i).getType().id;
            int type2 = notes2.getNotes().get(i).getType().id;
            type1 = convertTypeNoteToBase(type1);
            type2 = convertTypeNoteToBase(type2);
            switch (type1) {
                case 1: // Circle
                    if (type2 == 4) directional += -1; // COUNTER CLOCKWISE
                    else if (type2 == 2) directional += 1; // CLOCKWISE
                    else directional = 0;
                    break;
                case 2: // CROSS
                    if (type2 == 1) directional += -1; // COUNTER CLOCKWISE
                    else if (type2 == 3) directional += 1; // CLOCKWISE
                    else directional = 0;
                    break;
                case 3: // SQUARE
                    if (type2 == 2) directional += -1; // COUNTER CLOCKWISE
                    else if (type2 == 4) directional += 1; // CLOCKWISE
                    else directional = 0;
                    break;
                case 4: // TRIANGLE
                    if (type2 == 3) directional += -1; // COUNTER CLOCKWISE
                    else if (type2 == 1) directional += 1; // CLOCKWISE
                    else directional = 0;
                    break;
                default:
                    break;
            }
            if (directional == 0) return directional;
        }
        return directional;
    }

    /**
     * @return true if same note, false if note same (ignores HOLD, CHANCE, DOUBLE(NC)->ONE NOTE)
     */
    public static boolean isSameNote(TargetInfo notes1, TargetInfo notes2) {
        // Checking same
        boolean[] sameNote = new boolean[notes1.getNotes().size()];
        for (int i = 0; i < notes1.getNotes().size(); i++) {
            int type1 = convertTypeNoteToBase(notes1.getNotes().get(i).getType().id);
            boolean contains = false;
            for (Target note2 : notes2.getNotes()) {
                int type2 = convertTypeNoteToBase(note2.getType().id);
                if (type1 == type2) {
                    contains = true;
                    break;
                }
            }
            if (contains) sameNote[i] = true;
        }
        boolean allSame = true;
        for (boolean b : sameNote)
            if (!b) {
                allSame = false;
                break;
            }
        return allSame;
    }

    /**
     * @return true if triangle-cross or square-circle combination
     */
    public static boolean isOppositeNote(TargetInfo notes1, TargetInfo notes2) {
        boolean isOpposite = true;
        for (Target note1 : notes1.getNotes()) {
            int type1 = convertTypeNoteToBase(note1.getType().id);
            boolean isOppositeThis = false;
            for (Target note2 : notes2.getNotes()) {
                int type2 = convertTypeNoteToBase(note2.getType().id);
                if (Math.abs(type1 - type2) == 2) {
                    isOppositeThis = true;
                    break;
                }
            }
            if (!isOppositeThis) {
                isOpposite = false;
                break;
            }
        }
        return isOpposite;
    }

    /**
     * @return true if triples with 1 different notes (always if its not same)
     */
    public static boolean isTriplesChangingNote(TargetInfo notes1, TargetInfo notes2) {
        if (notes1.getNotes().size() != 3 || notes2.getNotes().size() != 3) return false;
        byte sameNotes = 0;
        for (Target note1 : notes1.getNotes()) {
            for (Target note2 : notes2.getNotes())
                if (note1.getType() == note2.getType()) {
                    sameNotes++;
                    break;
                }
        }
        return sameNotes == 2;
    }

    /**
     * @return checking cross-circle to triangle circle and square-triangle to triangle-circle
     */
    public static int checkExceptionDirectionInRulesForDoubles(TargetInfo notes1, TargetInfo notes2) {
        if (notes1.getNotes().size() != 2 && notes2.getNotes().size() != 2) return 0;
        int type1_1 = notes1.getNotes().get(0).getType().id;
        int type1_2 = notes1.getNotes().get(1).getType().id;
        int type2_1 = notes2.getNotes().get(0).getType().id;
        int type2_2 = notes2.getNotes().get(1).getType().id;
        if (type1_1 == 2 && type1_2 == 1 && type2_1 == 0 && type2_2 == 1) return -1;
        if (type1_1 == 0 && type1_2 == 3 && type2_1 == 0 && type2_2 == 1) return 1;
        return 0;
    }

    /**
     * @param type - type id of note
     * @return typeId where:
     *         1 - Circle
     *         2 - Square
     *         3 - Cross
     *         4 - Triangle
     *         Ignores Hold notes, Double (NC) notes, Chance Time notes
     */
    public static int convertTypeNoteToBase(int type) {
        if (type >= 4 && type <= 7) type -= 4; // Hold
        if (type >= 18 && type <= 21) type -= 18; // Chance
        if (type >= 29 && type <= 32) type -= 29; // Double (NC)
        if (type == 0) type = 4; // Triangle to 4
        return type;
    }

    /**
     * @return any slide note (chain, chain slide and star notes)
     */
    public static boolean isSlideNote(TargetInfo note) {
        return note
                .getNotes()
                .stream()
                .map(Target::getType)
                .anyMatch(type -> (type.id >= SlideBoth.id && type.id <= SlideChainR.id) ||
                        (type.id >= SlideBothChance.id && type.id <= SlideRChance.id) ||
                        type == Star || type == StarDouble);
    }

    /**
     * @return only if chain slide
     */
    public static boolean isChainSlideNote(TargetInfo note) {
        return note.getNotes()
                .stream()
                .map(Target::getType)
                .anyMatch(type -> type == SlideChainR || type == SlideChainL);
    }

    /**
     * @param multiplier set 1 to ignore time difference grid, 1.5 for 1/8 to 1/12 grid, 2 for 1/8 to 1/16 grid and etc.
     * @param notes how much notes to check
     * @return true if same timing with this multiplayer
     */
    public static boolean isSameTiming(double multiplier, TargetInfo... notes) { // x1 for 1/8, x1.5 for 1/12,
        long[] diffTimes = getDiffTimings(notes);
        boolean output = true;
        boolean[] check = new boolean[diffTimes.length - 1];
        for (int i = 0; i < diffTimes.length - 1; i++) {
            if (isChainSlideNote(notes[i + 1]) && isChainSlideNote(notes[i])) check[i] = false;
            if ((Math.abs(diffTimes[i + 1] - diffTimes[i] * multiplier) < CORRECT_TIMING_DIFFERENCE)) check[i] = true;
            if ((Math.abs(diffTimes[i + 1] * multiplier - diffTimes[i]) < CORRECT_TIMING_DIFFERENCE)) check[i] = true;
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

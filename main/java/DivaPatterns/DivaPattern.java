package DivaPatterns;

import ScriptDecryptor.ScriptTime;
import ScriptDecryptor.Targets.TargetInfo;
import ScriptDecryptor.Targets.TargetList;

import java.util.ArrayList;
import java.util.function.Function;

import static DivaPatterns.PatternsSettings.*;


public enum DivaPattern implements PatternAnalyzer {
    STREAM((notes) -> {
        ArrayList<Integer> output = new ArrayList<>();
        for (int i = 0; i < notes.size() - 1; i++) {
            int start = i;
            int end = i;
            for (int j = 0; i + j < notes.size() - 2; j++) {
                if (start == end && PatternHelper.isChainSlideNote(notes.get(i+j))) break;
                if (notes.get(i + j).getBpmPatternToNextAsFlyTime() < (STREAM_MIN_GRID - 0.5)) {
                    if (end - start >= STREAM_MIN_NOTES) { // 6 or more notes!
                        output.add(start);
                        output.add(end);
                        i = end;
                    }
                    break;
                } // Checking grid
                if (PatternHelper.isSameTiming(1.0, notes.get(i + j), notes.get(i + j + 1), notes.get(i + j + 2))) {
                    end = i + j + 2;
                } else {
                    int slideNotesInStream = 0;
                    for (int k = start; k < end; k++) {
                        if (PatternHelper.isSlideNote(notes.get(k)))
                            slideNotesInStream++;
                    }
                    if ((double) slideNotesInStream / (end - start) > 0.8)
                        break; // no more than 80% of sliders in stream
                    if (end - start >= STREAM_MIN_NOTES) { // 6 or more notes!
                        output.add(start);
                        output.add(end);
                        i = end;
                    }
                    break;
                }
            }
        }
        if (output.isEmpty()) return null;
        return output;
    }), // stream
    STREAM_WITH_DIFF_BPM_PATTERNS(notes -> { // Stream (1/8) contains triples (or some other) in 1/16 (or other)
        ArrayList<Integer> output = new ArrayList<>();
        for (int i = 0; i < notes.size() - 1; i++) {
            int start = i;
            int end = i;
            for (int j = 0; i + j < notes.size() - 2; j++) {
                if (start == end && PatternHelper.isChainSlideNote(notes.get(i+j))) break;
                if (notes.get(i + j).getBpmPatternToNextAsFlyTime() < (STREAM_MIN_GRID - 0.5)) {
                    if (end - start >= STREAM_MIN_NOTES) { // 6 or more notes!
                        output.add(start);
                        output.add(end-1);
                        i = end-1;
                    }
                    break;
                } // Checking grid
                if (PatternHelper.isSameTiming(1.0, notes.get(i + j), notes.get(i + j + 1), notes.get(i + j + 2)) || // if 1/8 in 1/8
                        PatternHelper.isSameTiming(1.5, notes.get(i + j), notes.get(i + j + 1), notes.get(i + j + 2)) || // if 1/8 in 1/12
                        PatternHelper.isSameTiming(2.0, notes.get(i + j), notes.get(i + j + 1), notes.get(i + j + 2)) // if 1/8 in 1/16
                ){
                    end = i + j + 2;
                } else{
                    int slideNotesInStream = 0;
                    for (int k = start; k < end; k++) {
                        if (PatternHelper.isSlideNote(notes.get(k)))
                            slideNotesInStream++;
                    }
                    if ((double) slideNotesInStream / (end - start) > 0.8)
                        break; // no more than 80% of sliders in stream
                    if (end - start >= STREAM_MIN_NOTES) { // 6 or more notes!
                        output.add(start);
                        output.add(end);
                        i = end;
                    }
                    break;
                }
            }
        }
        if (output.isEmpty()) return null;
        return output;
    }),
    STREAM_WITH_SLIDERS(notes -> { // Start or end with sliders include!
        ArrayList<Integer> output = new ArrayList<>();
        ArrayList<Integer> streamPatterns = STREAM.analyzePattern(notes);
        next:
        for (int i = 0; i < streamPatterns.size() - 1; i += 2) {
            int start = streamPatterns.get(i);
            int end = streamPatterns.get(i + 1);
            for (int k = start; k <= end; k++) {
                if (PatternHelper.isSlideNote(notes.get(k))) {
                    System.out.println("Find in " + notes.get(k) + " time " + new ScriptTime(notes.get(k).getTime()));
                    output.add(start);
                    output.add(end);
                    continue next;
                }
            }
        }
        if (output.isEmpty()) return null;
        return output;
    }),
    WAVES_270((notes) -> {
        return null;
    }),
    WAVES_360((notes) -> {
        return null;
    }),
    _270((notes) -> {
        return null;
    }),
    _360(notes -> {
        ArrayList<Integer> output = new ArrayList<>();
        for (int i = 0; i < notes.size() - 4; i++) {
            int start = i;
            int end = start;
            next:
            for (int k = 0; k + i < notes.size() - 4; k++) {
                // Check same timing
                long[] diffTimings = PatternHelper.getDiffTimings(notes.get(i + k), notes.get(i + k + 1), notes.get(i + k + 2), notes.get(i + k + 3));
                for (int j = 0; j < diffTimings.length - 1; j++) {
                    if (Math.abs(diffTimings[j] - diffTimings[j + 1]) > 15) {
                        if (end != start) {
                            output.add(start);
                            output.add(end);
                        }
                        i = end;
                        break next;
                    }
                }
                // Check 360 pattern (start from any note)
                boolean circlePattern = PatternHelper.is360Pattern(notes.get(i + k), notes.get(i + k + 1), notes.get(i + k + 2), notes.get(i + k + 3));
                if (circlePattern)
                    end = k + i + 3;
                else {
                    if (end != start) {
                        output.add(start);
                        output.add(end);
                    }
                    i = end;
                    break;
                }
            }
        }
        if (output.isEmpty()) return null;
        else return output;
    }),
    _360_WITH_CHANGING_DIRECTIONAL(notes -> {
        ArrayList<Integer> output = new ArrayList<>();
        ArrayList<Integer> _360Patterns = _360.analyzePattern(notes);
        if (_360Patterns == null) return null;
        for (int i = 0; i < _360Patterns.size() - 3; i += 2) {
            if (_360Patterns.get(i + 2) - _360Patterns.get(i + 1) == 1) { // If first 360 end and second 360 starts
                TargetInfo note1 = notes.get(_360Patterns.get(i + 2));
                TargetInfo note2 = notes.get(_360Patterns.get(i + 1));
                if (note1.getBPMAsNoteTimings() - note2.getBPMAsNoteTimings() > 1) {
                    continue;
                }
                int note1_Type = PatternHelper.convertTypeNoteToBase(note1.getNotes().getFirst().getType().id);
                int note2_Type = PatternHelper.convertTypeNoteToBase(note2.getNotes().getFirst().getType().id);
                if (note1_Type == note2_Type) { // if LAST note of first pattern same with FIRST note of second pattern
                    // ADD SAME BPM
                    output.add(_360Patterns.get(i)); // Add start of first pattern
                    output.add(_360Patterns.get(i + 3)); // Add end of second pattern
                }
            }
        }
        if (output.isEmpty()) return null;
        else return output;
    }),
    RIGHT_SWITCHING((notes) -> { // default stream with switches
        return null;
    }),
    LEFT_SWITCHING((notes) -> { // 3-3-3-3 or 3-5-3-5 and etc
        return null;
    }),
    GIMMICK((notes) -> {  // FLYING TIME, DISTANCE FREQUENCY WITH AMPLITUDE
        return null;
    }),
    SINGLE_DOUBLE((notes) -> { // 1-2-1-2-1-2
        return null;
    }),
    MULTINOTES((notes) -> {
        return null;
    }),
    WALLS((notes) -> {
        return null;
    }),
    SINGLE_WITH_MULTI_END((notes) -> {
        return null;
    }),
    SINGLE_WITH_MULTI_CENTER((notes) -> {
        return null;
    }),
    SINGLE_WITH_MULTI_START((notes) -> {
        return null;
    }),

    ;

    private final Function<TargetList, ArrayList<Integer>> analyzedList;

    DivaPattern(Function<TargetList, ArrayList<Integer>> analyzedList) {
        this.analyzedList = analyzedList;
    }

    @Override
    public ArrayList<Integer> analyzePattern(TargetList notes) {
        return analyzedList.apply(notes);
    }
}

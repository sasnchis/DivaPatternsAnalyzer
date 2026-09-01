package DivaPatterns;

import ScriptDecryptor.Targets.TargetList;
import ScriptDecryptor.Targets.TargetType;

import java.util.ArrayList;
import java.util.function.Function;

public enum DirectionalPattern implements PatternAnalyzer {
    CIRCLE((targets) -> {
        return null;
    }),
    LINE((targets) -> {
        ArrayList<Integer> patternsList = new ArrayList<>();
        boolean start = false;
        int startNote = -1;
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i).getNotes().stream().anyMatch(note ->
                    note.getType() == TargetType.SlideChainR || note.getType() == TargetType.SlideChainL) ) {
                start = false;
                continue;
            }
            if (targets.get(i).getAngleMovementToNextNote() % 90 == 0) {
                if (!start) {
                    startNote = i;
                }
                start = true;

            } else {
                if (start) {
                    start = false;
                    if ((i - startNote) > 3) {
                        patternsList.add(startNote);
                        patternsList.add(i);
                    }
                }
            }
        }
        if (patternsList.isEmpty()) return null;
        return patternsList;
    }),
    _30_DEGREES((targets) -> {
        return null;
    }),
    _45_DEGREES((targets) -> {
        return null;
    }),
    _60_DEGREES((targets) -> {
        return null;
    }),
    BROKEN_ARCADE_ORDER((notes) -> {
        return null;
    }),
    BROKEN_MULTIES((notes) -> {
        return null;
    }),
    ;


    private final Function<TargetList, ArrayList<Integer>> analyzedList;

    DirectionalPattern(Function<TargetList, ArrayList<Integer>> analyzedList) {
        this.analyzedList = analyzedList;
    }

    @Override
    public ArrayList<Integer> analyzePattern(TargetList notes) {
        return analyzedList.apply(notes);
    }
}

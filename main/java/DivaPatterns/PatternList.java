package DivaPatterns;

import ScriptDecryptor.Targets.TargetInfo;
import ScriptDecryptor.Targets.TargetList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static ScriptDecryptor.Targets.TargetType.SlideChainL;
import static ScriptDecryptor.Targets.TargetType.SlideChainR;

public class PatternList {
    private final HashMap<PatternAnalyzer, ArrayList<Integer>> patterns = new HashMap<>();

    public HashMap<PatternAnalyzer, ArrayList<Integer>> getPatterns() {
        return patterns;
    }

    public void analyze(TargetList notes) {
        for (DirectionalPattern directionalPattern : DirectionalPattern.values()) {
            ArrayList<Integer> values = directionalPattern.analyzePattern(notes);
            patterns.put(directionalPattern, values);
        }

        for (DivaPattern divaPattern : DivaPattern.values()) {
            ArrayList<Integer> values = divaPattern.analyzePattern(notes);
            patterns.put(divaPattern, values);
        }
    }

    protected TargetList getWithoutChainSliders(TargetList notes) {
        return notes.parallelStream().filter(note -> note
                .getNotes()
                .parallelStream()
                .anyMatch(note2 -> note2.getType() != SlideChainR && note2.getType() != SlideChainL))
                .collect(Collectors.toCollection(TargetList::new));
    }

    protected TargetList getOnlyMulti(TargetList notes) {
        return notes.parallelStream().filter(TargetInfo::isMultiNote)
                .collect(Collectors.toCollection(TargetList::new));
    }

    protected TargetList getOnlySingle(TargetList notes) {
        return notes.parallelStream().filter(note -> !note.isMultiNote())
                .collect(Collectors.toCollection(TargetList::new));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Patterns:\n");
        patterns.forEach((patternType, values) -> {
            if (values == null) {
//                builder.append(patternType).append(": ");
//                builder.append("null");
//                builder.append("\n");
            } else {
                builder.append(patternType).append(": ");
                for (int i = 0; i < values.size(); i += 2) {
                    if (i+1 >= values.size()) {
                        builder.append(values.get(i)).append("-END");
                        builder.append(", ");
                    } else {
                        builder.append(values.get(i)).append("-").append(values.get(i + 1));
                        builder.append(", ");
                    }
                }
                builder.delete(builder.length() - 2, builder.length());
                builder.append("\n");
            }
        });
        return builder.toString();
    }
}

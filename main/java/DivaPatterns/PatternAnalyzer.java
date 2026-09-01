package DivaPatterns;

import ScriptDecryptor.Targets.TargetList;

import java.util.ArrayList;

public interface PatternAnalyzer {
    ArrayList<Integer> analyzePattern(TargetList targets);
}

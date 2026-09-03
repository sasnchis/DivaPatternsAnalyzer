package DivaPatterns;

import ScriptDecryptor.PVScriptDecriptor.PVScript;
import ScriptDecryptor.ScriptTime;
import ScriptDecryptor.Targets.TargetList;

import java.util.ArrayList;

import static DivaPatterns.DirectionalPattern.*;

public class PVScriptAnalyzer {
    private final PVScript script;
    private final TargetList notes;
    private final PatternList patterns;

    public PVScriptAnalyzer(PVScript script) {
        this.script = script;
        script.parse();
        notes = script.toTargets();
        notes.check();
        notes.calc();
        patterns = new PatternList();
        analyze();
        try {
            ArrayList<Integer> test = patterns.getPatterns().get(LINE);
            int start = 187;
            int end = 190;
            for (int i = start; i <= end; i++) {
//                System.out.printf("Order for %s to %s: %s\n",
//                        notes.get(i).getName(),
//                        notes.get(i+1).getName(),
//                        PatternHelper.getOrderOfNextNote(notes.get(i), notes.get(i+1))
//                );
                System.out.print(notes.get(i) + " in ");
                System.out.println(new ScriptTime(notes.get(i).getTime()));
            }
        } catch (Exception e) {
            System.out.println("Ignored output");
        }
    }

    public PVScript getScript() {
        return script;
    }

    public TargetList getNotes() {
        return notes;
    }

    public PatternList getPatterns() {
        return patterns;
    }

    public void analyze() {
        patterns.analyze(notes);
        System.out.println(patterns);
    }
}

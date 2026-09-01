package Main;

import DivaPatterns.PVScriptAnalyzer;
import ScriptDecryptor.PVScriptDecriptor.PVScript;

import java.io.File;
import java.io.IOException;

public class Starter {
    public static void main(String[] args) {
        String targetFile = "C:\\Users\\sasnc\\IdeaProjects\\DivaPatternsAnalyzer\\src\\main\\resources\\dscfiles\\"; // pv_200000_extreme_1 Gorgon -> _1 changed fly time
//        String targetFile = "G:\\Games\\DivaMods\\CustomSongs\\Hatsune Creation Myth Holiday\\"; // pv_347_extreme.dsc Hatsune Creation Myth
//        String targetFile = "G:\\Games\\DivaMods\\Train\\Train Intense\\"; // pv_042_extreme.dsc Intense Extreme
        File dscFile = new File(targetFile+"pv_200000_extreme.dsc");
        System.out.println(dscFile.getAbsolutePath());
        try {
            PVScriptAnalyzer pvScriptAnalyzer = new PVScriptAnalyzer(new PVScript(dscFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

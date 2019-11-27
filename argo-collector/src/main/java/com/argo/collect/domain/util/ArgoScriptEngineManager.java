package com.argo.collect.domain.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

@Component
public class ArgoScriptEngineManager {

    @Value("${collect.auth-script-location}")
    private String scriptLocation;

    private ScriptEngine se;

    @PostConstruct
    public void init() {
        ScriptEngineManager sem = new ScriptEngineManager();
        se = sem.getEngineByName("JavaScript");
        File jsDir = new File(scriptLocation);

        try {
            File[] jsFiles = jsDir.listFiles();
            if (jsFiles == null) {
                return;
            }

            Arrays.sort(jsFiles);
            for (File js : jsFiles) {
                FileReader reader = new FileReader(js);
                se.eval(reader);
            }
        } catch (FileNotFoundException | ScriptException e) {
            e.printStackTrace();
        }
    }

    public ScriptEngine getScriptEngine() {
        return se;
    }
}

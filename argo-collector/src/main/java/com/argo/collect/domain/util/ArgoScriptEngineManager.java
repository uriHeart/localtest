package com.argo.collect.domain.util;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Component
public class ArgoScriptEngineManager {

    private ScriptEngine se;

    @PostConstruct
    public void init() {
        ScriptEngineManager sem = new ScriptEngineManager();
        se = sem.getEngineByName("JavaScript");
        File jsDir = new File("/Users/ags0688/git/argo/argo-collector/src/main/resources/js");

        try {
            if (jsDir.listFiles() == null) {
                return;
            }

            for (File js : jsDir.listFiles()) {
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

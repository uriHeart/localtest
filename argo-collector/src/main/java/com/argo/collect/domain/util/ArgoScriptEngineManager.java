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
        File js = new File("/Users/ags0688/git/argo/cryptDes.js");
        try {
            FileReader reader = new FileReader(js);
            se.eval(reader);
        } catch (FileNotFoundException | ScriptException e) {
            e.printStackTrace();
        }
    }

    public ScriptEngine getScriptEngine() {
        return se;
    }
}

package com.argo.collect.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class ArgoScriptEngineManagerTest {

    public static void main(String[] args) throws Exception {
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("JavaScript");
        File js = new File("/Users/ags0688/git/argo/cryptDes.js");
        try {
            FileReader reader = new FileReader(js);
            se.eval(reader);
            System.out.println(
                    se.eval("var temp = cryptDes.des('5d58d0fa', 'test', 1, 0); cryptDes.stringToHex(temp);"));

            Map<String, String> test = Maps.newHashMap();
            test.put("S_SDATE", "S_SDATE");
            test.put("S_EDATE", "S_EDATE");
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.print(objectMapper.writeValueAsString(test));
        } catch (FileNotFoundException | ScriptException e) {
            e.printStackTrace();
        }
    }

}

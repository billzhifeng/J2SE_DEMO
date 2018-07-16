package com.jdk.jdk8;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Java8JavaScript {
    public static void main(String[] args) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        System.out.println(engine.getClass().getName());
        try {
            System.out.println("output: " + engine.eval("function show() {return 10;}; show();"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        // jdk.nashorn.api.scripting.NashornScriptEngine
        // output: 10
    }
}

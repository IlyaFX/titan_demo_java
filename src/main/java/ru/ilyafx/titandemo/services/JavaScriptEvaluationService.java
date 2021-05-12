package ru.ilyafx.titandemo.services;

import org.springframework.stereotype.Service;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Service
public class JavaScriptEvaluationService implements IJavaScriptEvaluationService {

    private ScriptEngineManager scriptManager = new ScriptEngineManager();

    @Override
    public int eval(String script, int value) throws ScriptException, NoSuchMethodException {
        ScriptEngine javascriptEngine = scriptManager.getEngineByName("JavaScript");
        javascriptEngine.eval(script);
        return ((Double) ((Invocable) javascriptEngine).invokeFunction("eval", value)).intValue();
    }
}

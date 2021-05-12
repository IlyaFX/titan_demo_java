package ru.ilyafx.titandemo.services;

import javax.script.ScriptException;

public interface IJavaScriptEvaluationService {

    int eval(String script, int value) throws ScriptException, NoSuchMethodException;

}

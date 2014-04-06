package com.ivanparraga.nashornembeddedexample;

import java.util.Arrays;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;


public class NashornEcmaEvaluator {
	private static final String NASHORN_ENGINE_NAME = "nashorn";
	private static final String REFERENCE_ERROR = "ReferenceError";

	public static EcmaValue evaluate(String expression, SymbolTable table) {
		List<EcmaVariable> variables = table.getVariables();
		return evaluateExpression(expression, variables);
	}

	private static EcmaValue evaluateExpression(String expression,
			List<EcmaVariable> variables) {
		ScriptEngineManager scriptManager = new ScriptEngineManager();
		ScriptEngine nashornEngine = scriptManager.getEngineByName(NASHORN_ENGINE_NAME);
		
		try {
			putJavaVariablesIntoEngine(nashornEngine, variables);
			Object javaValue = nashornEngine.eval(expression);
			EcmaValue value = EcmaValue.create(javaValue);
			return value;
		} catch (ScriptException e) {
			handleException(e, variables);
			return null;
		}
	}
		
	private static void putJavaVariablesIntoEngine(ScriptEngine engine,
			List<EcmaVariable> variables) {
		
		Bindings bindings = new SimpleBindings();

		for (EcmaVariable variable : variables) {
			putJavaVariableIntoEcmaScope(bindings, variable);
		}
		
		engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
	}

	private static void putJavaVariableIntoEcmaScope(Bindings bindings,
			EcmaVariable variable) {

		String variableName = variable.getName();
		EcmaValue ecmaValue = variable.getValue();
		Object javaValue = ecmaValue.getValue();

		bindings.put(variableName, javaValue);
	}

	private static EcmaValue handleException(ScriptException exception,
			List<EcmaVariable> variables) {
		if (isReferenceError(exception)) {
			throw new IllegalArgumentException("I couldn't resolve some "
				+ "variable on expression with vars "
				+ Arrays.toString(variables.toArray()), exception);
		}
		throw new RuntimeException(exception);
	}
	
	private static boolean isReferenceError(ScriptException exception) {
		String message = exception.getMessage();
		return message.startsWith(REFERENCE_ERROR);
	}
}

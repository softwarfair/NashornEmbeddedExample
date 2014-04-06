package com.ivanparraga.nashornembeddedexample;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ivanparraga.nashornembeddedexample.EcmaValue;
import com.ivanparraga.nashornembeddedexample.NashornEcmaEvaluator;
import com.ivanparraga.nashornembeddedexample.SymbolTable;

public class NashornEcmaEvaluatorTest {
	@Test
	public void testEvaluateSimpleIntExpressionNoVariable() {
		int intValue = 3;
		String expression = "" + intValue;
		SymbolTable table = new SymbolTable();

		EcmaValue actualValue = NashornEcmaEvaluator.evaluate(expression, table);

		assertEquals(intValue, actualValue.getValue());
	}

	@Test
	public void testEvaluateIntExpressionNoVariable() {
		String expression = "Math.pow(2,3)";
		SymbolTable table = new SymbolTable();

		EcmaValue actualValue = NashornEcmaEvaluator.evaluate(expression, table);

		double expectedValue = Math.pow(2, 3);
		assertEquals(expectedValue, actualValue.getValue());
	}

	@Test
	public void testEvaluateSimpleStringExpressionNoVariable() {
		String strValue = "Hello world";
		String expression = "\"" + strValue + "\"";
		SymbolTable table = new SymbolTable();

		EcmaValue actualValue = NashornEcmaEvaluator.evaluate(expression, table);

		assertEquals(strValue, actualValue.getValue());
	}

	@Test
	public void testEvaluateStringExpressionNoVariable() {
		String expression = "\"how many? \" + 10";
		SymbolTable table = new SymbolTable();

		EcmaValue actualValue = NashornEcmaEvaluator.evaluate(expression, table);

		String expectedValue = "how many? 10";
		assertEquals(expectedValue, actualValue.getValue());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEvaluateVariableNotInSymbolTable() {
		String expression = "i";
		SymbolTable table = new SymbolTable();

		NashornEcmaEvaluator.evaluate(expression, table);
	}

	@Test
	public void testEvaluateStringExpressionVariable() {
		String expression = "\"how many? \" + i";
		SymbolTable table = new SymbolTable();
		table.putSymbol("i", EcmaValue.create(10));

		EcmaValue actualValue = NashornEcmaEvaluator.evaluate(expression, table);

		String expectedValue = "how many? 10";
		assertEquals(expectedValue, actualValue.getValue());
	}

	@Test
	public void testEvaluateStringExpressionTwoVariables() {
		String expression = "Math.pow(i,j)";
		SymbolTable table = new SymbolTable();
		table.putSymbol("i", EcmaValue.create(2));
		table.putSymbol("j", EcmaValue.create(3));

		EcmaValue actualValue = NashornEcmaEvaluator.evaluate(expression, table);

		double expectedValue = Math.pow(2, 3);
		assertEquals(expectedValue, actualValue.getValue());
	}
}

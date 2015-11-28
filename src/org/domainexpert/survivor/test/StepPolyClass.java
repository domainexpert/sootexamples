package org.domainexpert.survivor.test;

/**
 * This is the example Java code in Figure 2.12 of the thesis:
 * Raja Vallee-Rai. "Soot: A Java Bytecode Optimization Framework"
 * 
 * Although the Jimple output in the thesis seems to suggest that
 * Java variable names are preserved in the Jimple output, translating
 * the same example to Jimple shows otherwise. Java variable name
 * information will not be included in the source without the javac
 * -g:vars option but even so, Soot does not take advantage of it.
 */
public class StepPolyClass {
	int a;
	int b;
	
	public int stepPoly(int x) {
		int[] array = new int[10];
		
		if (x > 10) {
			array[0] = a;
			System.out.println("foo");
		} else if (x > 5) {
			x = x + 10 * b;
		}
		
		x = array[0];
		return x;
	}
}

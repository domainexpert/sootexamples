package org.domainexpert.survivor.test;

/**
 * This is the code example from Page 10
 * of SootSurvivor's Guide.
 * 
 * @author andrew
 *
 */
public class Foo {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Foo f = new Foo();
		int a = 7;
		int b = 14;
		int x = (f.bar(21) + a) * b;
	}
	
	public int bar(int n) {
		return n + 42;
	}
}

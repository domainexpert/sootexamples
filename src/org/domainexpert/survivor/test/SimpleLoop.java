package org.domainexpert.survivor.test;

/**
 * A program containing simple loop to test
 * the example of instrumentation in Chapter 5 of
 * Vallee-Rai's thesis.
 * 
 * The loop iterates n-times, where n is the given
 * argument. It therefore executes a goto statement
 * n times as well. This should be displayed at the end
 * by the instrumented program.
 * 
 * @author andrew
 *
 */
public class SimpleLoop {

	public static void main(String[] args) {
		int x = Integer.valueOf(args[0]).intValue();
		
		for (int i = 0; i < x; i++) {
			System.out.println("Index is: " + i);
		}
	}

}

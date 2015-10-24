package com.domainexpert.survivor.test;

/**
 * This class is for demonstrating the difference of
 * Jimple and Shimple representations. It is from
 * Page 11 of the Soot Survivor's Guide.
 * 
 * @author andrew
 *
 */
public class ShimpleExample {

	boolean as_long_as_it_takes;
	
	public int test() {
		int x = 100;
		while (as_long_as_it_takes) {
			if (x < 200) {
				x = 100;
			} else {
				x = 200;
			}
		}
		return x;
	}
}

package com.domainexpert.survivor.test;

/**
 * This is the example of Figure 14 of the
 * Soot Survivor's Guide.
 * 
 * @author andrew
 *
 */
public class Foo {
	@SuppressWarnings("unused")
	private int i;

	public void foo(int j) {
		this.i = j;
	}
}

package org.domainexpert.survivor.test.foo;

/**
 * This is the example of Figure 14 of the
 * Soot Survivor's Guide. It is part of the test
 * subject for the intermediate representation
 * example of the Survivor's Guide.
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

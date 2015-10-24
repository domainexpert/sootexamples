package com.domainexpert.survivor.test.foo;

/**
 * This class is part of the test subject of the intermediate
 * representation example from the Soot Survivor's Guide.
 * 
 * @author andrew
 *
 */
public class FooUser {

	private static Foo someMethod() {
		return new Foo();
	}
	
	public static boolean useFoo(int n) {
		Foo f1 = new Foo();
		f1.foo(n);
		
		Foo f2 = someMethod();

		Foo f3 = f1;
		
		if (f3 == f2) {
			return false;
		}
		return true;
	}
}

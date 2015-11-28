package org.domainexpert.survivor.test;

public class NullPointerTest {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String foo = null;
		String bar = new String("hello");
		String baz = "world";
		if (foo != null)
			System.out.println(foo);
		if (bar != null)
			System.out.println(bar);
		if (baz != null)
			System.out.println(baz);
	}

}

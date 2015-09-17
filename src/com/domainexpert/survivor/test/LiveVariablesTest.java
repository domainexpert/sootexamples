package com.domainexpert.survivor.test;

public class LiveVariablesTest {

	public static void main(String[] args) {
		int x,y,z;
		x = Integer.getInteger(args[1]).intValue();
		while (x>1) {
			y = x/2;
			if (y>3)
				x = x-y;
			z = x-4;
			if (z>0)
				x = x/2;
			z = z-1;
		}
		System.out.println(x + "");
	}

}

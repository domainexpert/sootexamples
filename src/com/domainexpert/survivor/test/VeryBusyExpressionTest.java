package com.domainexpert.survivor.test;

public class VeryBusyExpressionTest {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		int x, u;
		int i, j;
		int v = 2;

		i = 0;
		do {
			int w = 5;

			if (args.length > 2 && args[1].equals("0")) {
				if (args[2].equals("1")) {
					v = 3;
				} else {
					// v + w here is busy
					x = v + w;
				}
			}

			j = 0;
			do {
				// v + w here is busy
				u = v + w;
				if (args[j].length() > 1) {
					System.out.println(args[j]);
				}
			} while (j++ < args.length);

		} while (i++ < 10);
	}
}

package com.domainexpert.survivor.test;

public class ArrayBoundsTest {
	public static void main(String[] args) {
		int j;
		
		for (int i = 0; i <= args.length; i++) {
			// This is an error
			System.out.println(args[i]);
		}
		
		for (j = 0; j < args.length; j++) {
			System.out.println(args[j]);
		}
		// This is an error
		System.out.println(args[j]);
	}
}

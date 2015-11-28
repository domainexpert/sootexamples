package org.domainexpert.soot.thesis.scene;

import soot.PackManager;
import soot.Transform;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Syntax: java Main -app <main_classfile> [soot options]");
			System.exit(0);
		}

		PackManager.v().getPack("wjtp").add(new Transform("wjtp.profiler", Evaluator.v()));

		// For the transformer to be executed, as it was added as one of
		// whole-program analysis, we need to set whole-program analysis to true.
		soot.options.Options.v().set_whole_program(true);
		
		// This is to skip analyzing the bodies of excluded classes (e.g., JDK).
		soot.options.Options.v().set_no_bodies_for_excluded(true);
		
		soot.Main.main(args);
	}

}

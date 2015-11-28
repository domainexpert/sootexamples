package org.domainexpert.soot.thesis.instrumentation;

import soot.PackManager;
import soot.Transform;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Syntax: java Main --app <main_classfile> [soot " + "options]");
			System.exit(0);
		}
		
		PackManager.v().getPack("jtp").add(new Transform("jtp.instrumenter",
				GotoInstrumenter.v()));
		soot.Main.main(args);
	}

}

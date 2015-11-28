package org.domainexpert.soot.thesis.scene;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;

import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.JimpleBody;

/**
 * The Scene Evaluator from Vallee-Rai's thesis. This transformer
 * simply counts the number of classes, methods, and statements
 * (units) in a scene.
 * 
 * @author andrew
 *
 */
public class Evaluator extends SceneTransformer {
	
	private static Evaluator instance = new Evaluator();
	private Evaluator() {}
	static String oldPath;
	
	public static Evaluator v() { return instance; }
	
	@Override
	protected void internalTransform(String phaseName,
			Map<String, String> options) {
		long classCount = 0;
		long stmtCount = 0;
		long methodCount = 0;
		
		G.v().out.println("Evaluating ...");
		
		// Pre-process each class, constructing the invokeToNumberMap
		Iterator<SootClass> classIt = Scene.v().getApplicationClasses().iterator();
		while (classIt.hasNext()) {
			SootClass sClass = classIt.next();
			classCount++;
			
			Iterator<SootMethod> methodIt = sClass.getMethods().iterator();
			
			while (methodIt.hasNext()) {
				SootMethod m = (SootMethod) methodIt.next();
				methodCount++;
				
				if (!m.isConcrete())
					continue;
				
				JimpleBody body = (JimpleBody) m.retrieveActiveBody();
				stmtCount += body.getUnits().size();			
			}
		}
		
		DecimalFormat format = new DecimalFormat("0.0");
		
		G.v().out.println("Classes: \t" + classCount);
		G.v().out.println("Methods: \t" + methodCount + " (" +
				format.format((double) methodCount / classCount) + " methods/class)");
		G.v().out.println("Stmts:   \t" + stmtCount + " (" +
				format.format((double) stmtCount / methodCount) + " units/methods)");
		System.exit(0);
	}

}

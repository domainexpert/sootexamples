package com.domainexpert.soot.thesis.instrumentation;

import java.util.Iterator;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticInvokeExpr;
import soot.util.Chain;

/**
 * This is the class GotoInstrumenter, adapted from Vallee-Rai's
 * thesis to insert counting for executed goto statements, and
 * displaying of the total count before program exit.
 * 
 * The example demonstrates how Soot can be used to instrument code.
 * 
 * @author andrew
 *
 */
public class GotoInstrumenter extends BodyTransformer {

	private static GotoInstrumenter instance = new GotoInstrumenter();
	
	private GotoInstrumenter() {}
	
	public static GotoInstrumenter v() { return instance; }
		
	private boolean addedFieldToMainClassAndLoadedPrintStream = false;
	private SootClass javaIoPrintStream;
	
	private Local addTmpRef(Body body) {
		Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
		body.getLocals().add(tmpRef);
		return tmpRef;
	}
	
	private Local addTmpLong(Body body) {
		Local tmpLong = Jimple.v().newLocal("tmpLong", LongType.v());
		body.getLocals().add(tmpLong); 
		return tmpLong; 
	}
	
	private void addStmtsToBefore(Chain<Unit> units, Unit s, SootField gotoCounter,
			Local tmpRef, Local tmpLong) {
		// insert "tmpRef = java.lang.System.out;"
		units.insertBefore(Jimple.v().newAssignStmt(tmpRef,
				Jimple.v().newStaticFieldRef(
						Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())),
						s);
		
		// insert "tmpLong = gotoCounter;"
		units.insertBefore(Jimple.v().newAssignStmt(tmpLong,
				Jimple.v().newStaticFieldRef(gotoCounter.makeRef())), s);
		
		// insert "tmpRef.println(tmpLong);"
		SootMethod toCall = javaIoPrintStream.getMethod("void println(long)");
		units.insertBefore(Jimple.v().newInvokeStmt(
				Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), tmpLong)), s);
	}
	
	@Override
	protected void internalTransform(Body body, String phaseName,
			Map<String, String> options) {
		SootClass sClass = body.getMethod().getDeclaringClass();
		SootField gotoCounter = null;
		boolean addedLocals = false;
		Local tmpRef = null, tmpLong = null;
		Chain<Unit> units = body.getUnits();
	
		if (!Scene.v().getMainClass().declaresMethod("void main(java.lang.String[])")) {
			throw new RuntimeException("couldn't find main() in " + sClass.getName());
		}
		
		if (addedFieldToMainClassAndLoadedPrintStream)
			gotoCounter = Scene.v().getMainClass().getFieldByName("gotoCount");
		else {
			// Add gotoCounter field
			gotoCounter = new SootField("gotoCount", LongType.v(), Modifier.STATIC);
			Scene.v().getMainClass().addField(gotoCounter);
			
			javaIoPrintStream = Scene.v().getSootClass("java.io.PrintStream");
			addedFieldToMainClassAndLoadedPrintStream = true;
		}
		
		// Code to increase goto counter each time a goto is encountered
		boolean isMainMethod = body.getMethod().getSubSignature().equals("void " +
				"main(java.lang.String[])");
		
		Local tmpLocal = Jimple.v().newLocal("tmp", LongType.v());
		body.getLocals().add(tmpLocal);
		
		// Using snapshopIterator avoids ConcurrentModificationException
		// form being thrown.
		Iterator<Unit> stmtIt = units.snapshotIterator();
		
		while (stmtIt.hasNext()) {
			Unit s = stmtIt.next();
			
			if (s instanceof GotoStmt) {
				// In case this was a goto statement, increment the counter
				
				AssignStmt toAdd1 = Jimple.v().newAssignStmt(tmpLocal,
						Jimple.v().newStaticFieldRef(gotoCounter.makeRef()));
				AssignStmt toAdd2 = Jimple.v().newAssignStmt(tmpLocal,
						Jimple.v().newAddExpr(tmpLocal, LongConstant.v(1L)));
				AssignStmt toAdd3 = Jimple.v().newAssignStmt(Jimple.v().
						newStaticFieldRef(gotoCounter.makeRef()), tmpLocal);
				
				// insert "tmpLocal = gotoCounter;"
				units.insertBefore(toAdd1, s);
				
				// insert "tmpLocal = tmpLocal + 1L;"
				units.insertBefore(toAdd2, s);
				
				// insert "gotoCounter = tmpLocal;"
				units.insertBefore(toAdd3, s);

			} else if (s instanceof InvokeStmt) {
				// In case we reached the end of the program via
				// System.exit(int), print the counter.
				InvokeExpr iexpr = (InvokeExpr) ((InvokeStmt) s).getInvokeExpr();
				
				if (iexpr instanceof StaticInvokeExpr) {
					SootMethod target = ((StaticInvokeExpr) iexpr).getMethod();
					
					if (target.getSignature().equals("<java.lang.System: void " +
							"exit(int)>")) {
						if (!addedLocals) {
							tmpRef = addTmpRef(body);
							tmpLong = addTmpLong(body);
							addedLocals = true;
						}
						addStmtsToBefore(units, s, gotoCounter, tmpRef, tmpLong);
					}
				}
			} else if (isMainMethod &&
					(s instanceof ReturnStmt || s instanceof ReturnVoidStmt)) {
				// In case this was a return statement from main() method,
				// display the counter value
				if (!addedLocals) {
					tmpRef = addTmpRef(body); tmpLong = addTmpLong(body);
					addedLocals = true;
				}
				addStmtsToBefore(units, s, gotoCounter, tmpRef, tmpLong);
			}
		}
	}

}

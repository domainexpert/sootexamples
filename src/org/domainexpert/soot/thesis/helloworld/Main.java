package org.domainexpert.soot.thesis.helloworld;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import soot.ArrayType;
import soot.G;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Type;
import soot.Unit;
import soot.VoidType;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.util.Chain;
import soot.util.JasminOutputStream;

/**
 * This is the Hello world! printer generator, adapted from Vallee-Rai's thesis.
 * There is another version: GenHelloWorld.java included in the sources
 * distribution of Soot Survivor's Guide.
 * 
 * @author andrew
 *
 */
public class Main {

	public static void main(String[] args) {
		SootClass sClass;
		SootMethod method;

		soot.options.Options.v().set_output_dir(args[0]);
		
		// Create the class
		Scene.v().loadClassAndSupport("java.lang.Object");
		Scene.v().loadClassAndSupport("java.lang.System");

		// Declare 'public class HelloWorld'
		sClass = new SootClass("HelloWorld", Modifier.PUBLIC);
		
		// 'extends Object'
		sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		Scene.v().addClass(sClass);
		
		// Create the method 'public static void main(String[])'
		method = new SootMethod("main",
				Arrays.asList(new Type[] { ArrayType.v(RefType.v("java.lang.String"), 1) }),
				VoidType.v(),
				Modifier.PUBLIC | Modifier.STATIC);

		sClass.addMethod(method);
		
		createMethodBody(method);
		
		write(sClass);
	}

	private static void write(SootClass sClass) {
		OutputStream streamOut = null;
		
		String filename = SourceLocator.v().getFileNameFor(sClass, Options.output_format_class);
		G.v().out.println("Print file name: " + filename);
		try {
			streamOut = new JasminOutputStream(new FileOutputStream(filename));
		} catch (FileNotFoundException e) {
			G.v().out.println("File not found: " + filename);
		}
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
		JasminClass jasClass = new JasminClass(sClass);
		jasClass.print(writerOut);
		writerOut.flush();
		writerOut.close();
	}
	
	private static void createMethodBody(SootMethod method) {
		// Create empty body
		JimpleBody body = Jimple.v().newBody(method);
		
		method.setActiveBody(body);
		Chain<Unit> units = body.getUnits();
		Local arg, tmpRef;
		
		// Add some locals, java.lang.String[] l0
		arg = Jimple.v().newLocal("l0", ArrayType.v(RefType.v("java.lang.String"), 1));
		body.getLocals().add(arg);

		// Add locals, java.io.PrintStream tmpRef
		tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
		body.getLocals().add(tmpRef);

		// Add 'l0 = @parameter0'
		units.add(Jimple.v().newIdentityStmt(arg,
				Jimple.v().newParameterRef(ArrayType.v(RefType.v("java.lang.String"), 1),
						0)));
		
		// Add 'tmpRef = java.lang.System.out'
		units.add(Jimple.v().newAssignStmt(tmpRef,
				Jimple.v().newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));

	
		// Insert 'tmpRef.println("Hello world!")'
		SootMethod toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
		units.add(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef,
				toCall.makeRef(), StringConstant.v("Hello world!"))));

		// Insert "return"
		units.add(Jimple.v().newReturnVoidStmt());
	}
}

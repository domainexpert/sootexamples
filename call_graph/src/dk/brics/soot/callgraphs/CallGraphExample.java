package dk.brics.soot.callgraphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.G;
import soot.MethodOrMethodContext;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Targets;

/**
 * This example has been heavily modified to collect statistics
 * about call graph generation.
 */
public class CallGraphExample
{	
	public static void main(String[] args) {
	   List<String> argsList = new ArrayList<String>(Arrays.asList(args));
	   argsList.addAll(Arrays.asList(new String[]{
			   "-w",
			   "-main-class",
			   "testers.CallGraphs",//main-class
			   "testers.CallGraphs",//argument classes
			   "testers.A"			//
	   }));
	

	   PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {

		@Override
		protected void internalTransform(String phaseName, Map<String, String> options) {
			CallGraph cgOld = Scene.v().getCallGraph();
			
			/*
			 * The following call is in the survivot's guide, however, it is
			 * actually unnecessary as -w option already generates the call graph.
			 * Not only unnecessary, it can also be harmful as it enlarges the
			 * call graph by adding duplicated edges.
			 * 
			 * Within the context of the Survivor's Guide, perhaps it has some
			 * instructional value.
			 */
			CHATransformer.v().transform();
			@SuppressWarnings("unused")
			SootClass a = Scene.v().getSootClass("testers.A");

			SootMethod src = Scene.v().getMainClass().getMethodByName("doStuff");
			CallGraph cg = Scene.v().getCallGraph();

			Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(src));
			while (targets.hasNext()) {
				SootMethod tgt = (SootMethod)targets.next();
				System.out.println(src + " may call " + tgt);
			}

			G.v().out.println("Number of edges before transform: " + cgOld.size());
			G.v().out.println("Number of edges: " + cg.size());
			
			G.v().out.println("Edges in before not in new: " +
					compareDifferentEdges(cgOld, cg));
			G.v().out.println("Edges in new not in before: " +
					compareDifferentEdges(cg, cgOld));
			G.v().out.println("Duplication of edges in old graph: " +
					detectDuplicateEdges(cgOld));
			G.v().out.println("Duplication of edges in new graph: " +
					detectDuplicateEdges(cg));
			
			G.v().out.println("Source methods in before not in new: " +
					compareDifferentSourceMethods(cgOld, cg));
			G.v().out.println("Source methods in new not in before: " +
					compareDifferentSourceMethods(cg, cgOld));
		}

		class StringPair {
			private String left;
			private String right;
			
			StringPair(String str1, String str2) {
				this.left = str1;
				this.right = str2;
			}


			@Override
			public boolean equals(Object pair) {
				if (!(pair instanceof StringPair))
					return false;

				StringPair localPair = (StringPair) pair;

				return this.left.equals(localPair.left) &&
						this.right.equals(localPair.right);
			}
		}
		
		
		private int detectDuplicateEdges(CallGraph cg) {
			Map<String, List<String>> signatureEdges = new HashMap<String, List<String>>();
			Map<StringPair, Integer> duplicatedEdges = new HashMap<StringPair, Integer>();
			int duplicationNo = 0;
			
			Iterator<Edge> ei = cg.iterator();
			while (ei.hasNext()) {
				Edge e = ei.next();
				String sourceMethod = e.getSrc().method().getSignature();
				List<String> targetList = signatureEdges.get(sourceMethod);
				String targetMethod = e.getTgt().method().getSignature();
				
				if (targetList != null) {
					for (String target: targetList) {
						if (target.equals(targetMethod)) {
							// Duplicate found
							duplicationNo++;
							
							StringPair tmpPair = new StringPair(sourceMethod, targetMethod);
							Integer count = duplicatedEdges.get(tmpPair);
							
							if (count != null) {
								duplicatedEdges.remove(tmpPair);
								duplicatedEdges.put(tmpPair, new Integer(count.intValue() + 1));							
							} else {
								duplicatedEdges.put(new StringPair(sourceMethod, targetMethod), new Integer(1));
							}						
							break;
						}
					}
				} else {
					// Empty target list, so we have a new edge
					targetList = new ArrayList<String>();
					targetList.add(targetMethod);
					signatureEdges.put(sourceMethod, targetList);			
				}
			}
			return duplicationNo;
		}
		
		
	   }));

	   args = argsList.toArray(new String[0]);

	   soot.Main.main(args);
	}

	private static int compareDifferentEdges(CallGraph cg1, CallGraph cg2) {
		Iterator<Edge> ei1 = cg1.iterator();
		List<Edge> unmatchedEdgeInCg1 = new ArrayList<Edge>();
		
		while (ei1.hasNext()) {
			Edge e1 = ei1.next();
			String sourceMethod = e1.getSrc().method().getSignature();
			String targetMethod = e1.getTgt().method().getSignature();

			Iterator<Edge> ei2 = cg2.iterator();
			boolean match = false;
			while (ei2.hasNext()) {
				Edge e2 = ei2.next();
				String sourceMethod2 = e2.getSrc().method().getSignature();
				if (sourceMethod2.equals(sourceMethod)) {
					String targetMethod2 = e2.getTgt().method().getSignature();
					if (targetMethod2.equals(targetMethod)) {
						match = true;
						break;
					}
				}
			}

			if (!match) {
				unmatchedEdgeInCg1.add(e1);
			}
		}

		return unmatchedEdgeInCg1.size();
	}
	
	private static int compareDifferentSourceMethods(CallGraph cg1, CallGraph cg2) {
		int differentSourceMethodsNo = 0;
		Iterator<MethodOrMethodContext> methodIterator = cg1.sourceMethods();
		while (methodIterator.hasNext()) {
			MethodOrMethodContext m = methodIterator.next();
			String MSignature = m.method().getSignature();
			
			Iterator<MethodOrMethodContext> methodIteratorOld =
					cg2.sourceMethods();
			boolean match = false;
			while (methodIteratorOld.hasNext()) {
				MethodOrMethodContext n = methodIteratorOld.next();
				if (n.method().getSignature().equals(MSignature)) {
					match = true;
					break;
				}
			}
			
			if (!match)
				differentSourceMethodsNo++;
		}
		return differentSourceMethodsNo;
	}

}

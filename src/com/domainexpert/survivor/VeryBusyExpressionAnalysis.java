package com.domainexpert.survivor;

import soot.EquivTo;
import soot.G;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

public class VeryBusyExpressionAnalysis extends BackwardFlowAnalysis<Unit, FlowSet> {

	public VeryBusyExpressionAnalysis(UnitGraph graph) {
		super(graph);
		doAnalysis();
	}

	@Override
	protected void flowThrough(FlowSet in, Unit node, FlowSet out) {
		Unit u = node;
		kill(in, u, out);
		gen(out, u);		
	}

	private void gen(FlowSet outSet, Unit u) {
		G.v().out.println("Executing gen on unit " + u.toString());
		G.v().out.flush();
	}

	private void kill(FlowSet inSet, Unit u, FlowSet outSet) {
		G.v().out.println("Executing kill on unit " + u.toString());
		G.v().out.flush();
	}

	@Override
	protected void copy(FlowSet source, FlowSet dest) {
		source.copy(dest);
	}

	@Override
	protected FlowSet entryInitialFlow() {
		return new ValueArraySparseSet();
	}


	@Override
	protected void merge(FlowSet in1, FlowSet in2, FlowSet out) {
		in1.intersection(in2, out);
	}

	@Override
	protected FlowSet newInitialFlow() {
		return new ValueArraySparseSet();
	}

	private class ValueArraySparseSet extends ArraySparseSet {
		@Override
		public boolean contains(Object obj) {
			for (int i = 0; i < numElements; i++) {
				if (elements[i] instanceof EquivTo
						&& ((EquivTo) elements[i]).equivTo(obj))
					return true;
				else if (elements[i].equals(obj))
					return true;
			}
			return false;
		}
	}

}

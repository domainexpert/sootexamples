package com.domainexpert.survivor;

import java.util.List;

import soot.Unit;
import soot.toolkits.graph.UnitGraph;

public class SimpleVeryBusyExpressions implements VeryBusyExpressions {
	private final VeryBusyExpressionAnalysis analysis;

	public SimpleVeryBusyExpressions(UnitGraph graph) {
		analysis = new VeryBusyExpressionAnalysis(graph);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getBusyExpressionsBefore(Unit s) {
		return analysis.getFlowBefore(s).toList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getBusyExpressionsAfter(Unit s) {
		return analysis.getFlowAfter(s).toList();
	}

}

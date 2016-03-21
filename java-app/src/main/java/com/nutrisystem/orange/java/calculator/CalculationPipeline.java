/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wei Gao
 * 
 */
public class CalculationPipeline {
	private List<ICalculationHandler> calculationHandlerList = new ArrayList<>();

	public void add(ICalculationHandler handler) {
		calculationHandlerList.add(handler);
	}

	public void process(UserData data) {
		for (ICalculationHandler calculationHandler : calculationHandlerList)
			calculationHandler.process(data);
	}
}

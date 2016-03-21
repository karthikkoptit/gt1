/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Wei Gao
 * 
 */
public class WeightHistoryOutput extends AbstractOutput {
    private List<DailyWeight> weightHistory;

    @XmlElement(name = "weight_history")
    public List<DailyWeight> getWeightHistory() {
        return weightHistory;
    }

    public void setWeightHistory(List<DailyWeight> weightHistory) {
        this.weightHistory = weightHistory;
    }
}

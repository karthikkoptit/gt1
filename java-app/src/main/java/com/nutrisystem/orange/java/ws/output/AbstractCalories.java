/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

/**
 * @author Wei Gao
 *
 */
public abstract class AbstractCalories {
    private Integer goal;
    
    private Integer net;
    
    private Integer food;
    
    private Integer activity;

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getNet() {
        return net;
    }

    public void setNet(Integer net) {
        this.net = net;
    }

    public Integer getFood() {
        return food;
    }

    public void setFood(Integer food) {
        this.food = food;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }
}

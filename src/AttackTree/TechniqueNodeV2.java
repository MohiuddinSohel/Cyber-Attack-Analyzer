/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AttackTree;

/**
 *
 * @author mahmed27
 */
public class TechniqueNodeV2 {
    private String techniqueId;
    private String techniqueName;
    private String tactic;
    private Double priorProbability;
    private boolean isVisited;
    public TechniqueNodeV2(String techniqueId, String tactic){
        this.techniqueId = techniqueId;
        this.tactic = tactic;
        isVisited = false;
    }

    public void setTactic(String tactic) {
        this.tactic = tactic;
    }

    public String getTactic() {
        return tactic;
    }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public boolean isIsVisited() {
        return isVisited;
    }

    public String getTechniqueId() {
        return techniqueId;
    }

    public String getTechniqueName() {
        return techniqueName;
    }

    public void setTechniqueId(String techniqueId) {
        this.techniqueId = techniqueId;
    }

    public void setTechniqueName(String techniqueName) {
        this.techniqueName = techniqueName;
    }

    public Double getPriorProbability() {
        return priorProbability;
    }

    public void setPriorProbability(Double priorProbability) {
        this.priorProbability = priorProbability;
    }
    
}

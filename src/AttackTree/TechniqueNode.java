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
public class TechniqueNode {
    private String techniqueId;
    private String techniqueName;
    private Double priorProbability;
    private boolean isVisited;
    public TechniqueNode(String techniqueId){
        this.techniqueId = techniqueId;
        isVisited = false;
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

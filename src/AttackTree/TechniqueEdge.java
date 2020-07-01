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
public class TechniqueEdge {
    private TechniqueNode startingNode;
    private TechniqueNode endingNode;
    private Double posteriorProbability;
    public TechniqueEdge(TechniqueNode startingNode, TechniqueNode endingNode, Double posteriorProbability) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.posteriorProbability = posteriorProbability;
    }

    public TechniqueNode getStartingNode() {
        return startingNode;
    }

    public TechniqueNode getEndingNode() {
        return endingNode;
    }

    public void setStartingNode(TechniqueNode startingNode) {
        this.startingNode = startingNode;
    }

    public void setEndingNode(TechniqueNode endingNode) {
        this.endingNode = endingNode;
    }

    public Double getPosteriorProbability() {
        return posteriorProbability;
    }

    public void setPosteriorProbability(Double posteriorProbability) {
        this.posteriorProbability = posteriorProbability;
    }
    
}

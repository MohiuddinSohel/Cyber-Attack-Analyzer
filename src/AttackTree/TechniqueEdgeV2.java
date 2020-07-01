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
public class TechniqueEdgeV2 {
    private TechniqueNodeV2 startingNode;
    private TechniqueNodeV2 endingNode;
    private Double posteriorProbability;
    public TechniqueEdgeV2(TechniqueNodeV2 startingNode, TechniqueNodeV2 endingNode, Double posteriorProbability) {
        this.startingNode = startingNode;
        this.endingNode = endingNode;
        this.posteriorProbability = posteriorProbability;
    }

    public TechniqueNodeV2 getStartingNode() {
        return startingNode;
    }

    public TechniqueNodeV2 getEndingNode() {
        return endingNode;
    }

    public void setStartingNode(TechniqueNodeV2 startingNode) {
        this.startingNode = startingNode;
    }

    public void setEndingNode(TechniqueNodeV2 endingNode) {
        this.endingNode = endingNode;
    }

    public Double getPosteriorProbability() {
        return posteriorProbability;
    }

    public void setPosteriorProbability(Double posteriorProbability) {
        this.posteriorProbability = posteriorProbability;
    }
    
}

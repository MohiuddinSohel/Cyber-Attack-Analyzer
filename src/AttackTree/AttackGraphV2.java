/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AttackTree;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author mahmed27
 */
public class AttackGraphV2 {
    ArrayList<TechniqueEdgeV2> edgeList;
    ArrayList<TechniqueNodeV2> nodeList;
    LinkedHashMap<String, ArrayList<TechniqueNodeV2>> adjacencyList;
    LinkedHashMap<String, ArrayList<TechniqueNodeV2>> reverseAdjacencyList;
    LinkedHashMap<String, Double> posteriorProbability;
    LinkedHashMap<String, Double> priorProbability;

    public ArrayList<TechniqueNodeV2> getNodeList() {
        return nodeList;
    }

    public LinkedHashMap<String, ArrayList<TechniqueNodeV2>> getReverseAdjacencyList() {
        return reverseAdjacencyList;
    }

    public AttackGraphV2(ArrayList<TechniqueNodeV2> nodeList, ArrayList<TechniqueEdgeV2> edgeList
            , LinkedHashMap<String, ArrayList<TechniqueNodeV2>> adjacencyList, LinkedHashMap<String, ArrayList<TechniqueNodeV2>> reverseAdjacencyList) {
        this.nodeList = nodeList;
        this.edgeList = edgeList;
        this.adjacencyList = adjacencyList;
        this.reverseAdjacencyList = reverseAdjacencyList;
        this.posteriorProbability = new LinkedHashMap();
        this.priorProbability = new LinkedHashMap();
    }

    public LinkedHashMap<String, Double> getPosteriorProbability() {
        return posteriorProbability;
    }

    public void setPosteriorProbability(LinkedHashMap<String, Double> posteriorProbability) {
        this.posteriorProbability = posteriorProbability;
    }

    public LinkedHashMap<String, Double> getPriorProbability() {
        return priorProbability;
    }

    public void setPriorProbability(LinkedHashMap<String, Double> priorProbability) {
        this.priorProbability = priorProbability;
    }

    public ArrayList<TechniqueEdgeV2> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(ArrayList<TechniqueEdgeV2> edgeList) {
        this.edgeList = edgeList;
    }

    public LinkedHashMap<String, ArrayList<TechniqueNodeV2>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(LinkedHashMap<String, ArrayList<TechniqueNodeV2>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public void setNodeList(ArrayList<TechniqueNodeV2> nodeList) {
        this.nodeList = nodeList;
    }

    public void setReverseAdjacencyList(LinkedHashMap<String, ArrayList<TechniqueNodeV2>> reverseAdjacencyList) {
        this.reverseAdjacencyList = reverseAdjacencyList;
    }
    
}

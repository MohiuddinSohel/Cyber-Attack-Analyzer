/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AttackTree;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mahmed27
 */
public class AttackGraph {
    ArrayList<TechniqueEdge> edgeList;
    ArrayList<TechniqueNode> nodeList;
    HashMap<String, ArrayList<TechniqueNode>> adjacencyList;
    HashMap<String, ArrayList<TechniqueNode>> reverseAdjacencyList;
    HashMap<String, Double> posteriorProbability;
    HashMap<String, Double> priorProbability;

    public ArrayList<TechniqueNode> getNodeList() {
        return nodeList;
    }

    public HashMap<String, ArrayList<TechniqueNode>> getReverseAdjacencyList() {
        return reverseAdjacencyList;
    }

    public AttackGraph(ArrayList<TechniqueNode> nodeList, ArrayList<TechniqueEdge> edgeList, HashMap<String, ArrayList<TechniqueNode>> adjacencyList, HashMap<String, ArrayList<TechniqueNode>> reverseAdjacencyList) {
        this.nodeList = nodeList;
        this.edgeList = edgeList;
        this.adjacencyList = adjacencyList;
        this.reverseAdjacencyList = reverseAdjacencyList;
        this.posteriorProbability = new HashMap();
        this.priorProbability = new HashMap();
    }

    public HashMap<String, Double> getPosteriorProbability() {
        return posteriorProbability;
    }

    public void setPosteriorProbability(HashMap<String, Double> posteriorProbability) {
        this.posteriorProbability = posteriorProbability;
    }

    public HashMap<String, Double> getPriorProbability() {
        return priorProbability;
    }

    public void setPriorProbability(HashMap<String, Double> priorProbability) {
        this.priorProbability = priorProbability;
    }

    public ArrayList<TechniqueEdge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(ArrayList<TechniqueEdge> edgeList) {
        this.edgeList = edgeList;
    }

    public HashMap<String, ArrayList<TechniqueNode>> getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(HashMap<String, ArrayList<TechniqueNode>> adjacencyList) {
        this.adjacencyList = adjacencyList;
    }

    public void setNodeList(ArrayList<TechniqueNode> nodeList) {
        this.nodeList = nodeList;
    }

    public void setReverseAdjacencyList(HashMap<String, ArrayList<TechniqueNode>> reverseAdjacencyList) {
        this.reverseAdjacencyList = reverseAdjacencyList;
    }
    
}

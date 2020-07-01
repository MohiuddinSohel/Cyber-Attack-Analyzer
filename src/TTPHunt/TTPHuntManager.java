/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTPHunt;

import AttackTree.AttackGraph;
import AttackTree.TechniqueNode;
import Helper.DataStorage;
import Helper.GraphGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author mahmed27
 */
public class TTPHuntManager {
    private ThreatReasoning threatReasoning;
    private ArrayList<String> candidateGoalList;
    private ArrayList<String> initialAccessPoint;
    AttackGraph graph;
    
    public TTPHuntManager() throws IOException {
        this.init();
    }
    
    private void init() throws IOException {
        LinkedHashMap<String, Boolean> eList = GraphGenerator.generateAllEdges(55);
        AttackGraph g = GraphGenerator.generateGraph(eList);
        ArrayList<String> iAList = GraphGenerator.initialAccessPoint(g);
        ArrayList<String> aGList = GraphGenerator.achieveableGoalList(g);
        HashMap<String, Double> posteriorP = GraphGenerator.setPosteriorProbability(g, aGList);
        HashMap<String, Double> priorP = GraphGenerator.setPriorProbability(g);
        g.setPriorProbability(priorP);
        g.setPosteriorProbability(posteriorP);
        
        this.graph = g;
        this.initialAccessPoint = iAList;
        this.candidateGoalList = aGList;
        this.threatReasoning = new ThreatReasoning(this.graph, this.candidateGoalList, this.initialAccessPoint);
    }
    
//    private void init1() throws IOException {
//        String csvPathC = "/My Things/Working Project/Evidential Reasoning/matrix1prob.csv";
//        String csvPathP = "/My Things/Working Project/Evidential Reasoning/PriorProbability.csv";
//        this.graph = Helper.CSVProcessor.readCSVCorrelationMatrix(csvPathC, csvPathP);
//        
//        this.generateInitialAccessPoint();
//        this.generateAchieveableGoalList();
//        this.threatReasoning = new ThreatReasoning(this.graph, this.candidateGoalList, this.initialAccessPoint);
//    }
    
    public void huntTTP() throws IOException {
        
        ArrayList<String> reportedTechnique = DataStorage.randomReportedTechniqueGenerator(0.1, 1);//this.generateReportedTechnique();
        HashMap<String, Double> candidateContributionMap = new HashMap();
        for(String goali: this.candidateGoalList) {
            candidateContributionMap.put(goali, this.threatReasoning.calculateContributionOfGoalForAllChain(goali, reportedTechnique));
        }
        
        //Hypothesis Generation
        ArrayList<ArrayList<String>> hypothesisSet = new ArrayList();
        ArrayList<String> hypothesis = new ArrayList();
        this.threatReasoning.generateHypothesis(hypothesisSet, hypothesis, this.candidateGoalList, candidateContributionMap, reportedTechnique);
        
        //Fidelity Score calculation
        ArrayList<String> finalH;
        Double currentScore = 0.0;
        for(ArrayList<String> h :  hypothesisSet) {
            Double tmp = this.threatReasoning.calculateFidelityScore(h, reportedTechnique);
            if(tmp > currentScore) {
                finalH = h;
                currentScore = tmp;
            }
        }
    }
    
    
    public ArrayList<String> generateReportedTechnique() {
        ArrayList<String> reportedTechnique = new ArrayList<>();
        reportedTechnique.add("t1059");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1002");
        reportedTechnique.add("t1005");
        reportedTechnique.add("t1036");
        return reportedTechnique;
    }
    
//    public void generateInitialAccessPoint() {
//        this.initialAccessPoint = new ArrayList();
//        this.initialAccessPoint.add("t1189");
//        this.initialAccessPoint.add("t1190");
//        this.initialAccessPoint.add("t1200");
//        this.initialAccessPoint.add("t1091");
//        this.initialAccessPoint.add("t1192");
//        this.initialAccessPoint.add("t1193");
//        this.initialAccessPoint.add("t1194");
//        this.initialAccessPoint.add("t1195");
//        this.initialAccessPoint.add("t1199");
//        this.initialAccessPoint.add("t1078");
//    }
//    
//    public void generateAchieveableGoalList() {
//        this.candidateGoalList = new ArrayList();
//        this.candidateGoalList.add("t1020");
//        this.candidateGoalList.add("t1002");
//        this.candidateGoalList.add("t1022");
//        this.candidateGoalList.add("t1030");
//        this.candidateGoalList.add("t1048");
//        this.candidateGoalList.add("t1041");
//        this.candidateGoalList.add("t1011");
//        this.candidateGoalList.add("t1052");
//        this.candidateGoalList.add("t1029");
//        
//        
//        this.candidateGoalList.add("t1043");
//        this.candidateGoalList.add("t1092");
//        this.candidateGoalList.add("t1094");
//        this.candidateGoalList.add("t1001");
//        
//        this.candidateGoalList.add("t1105");
//        this.candidateGoalList.add("t1021");
//        this.candidateGoalList.add("t1077");
//        this.candidateGoalList.add("t1028");
//        this.candidateGoalList.add("t1091");
//    }
    
    public static void main(String[] args) throws IOException {
//        TTPHuntManager manager = new TTPHuntManager();
//        manager.huntTTP();
        String line = "[1, 2, 3, 4, 5]"; 
        String[] chain = line.replaceAll("[\\[\\]\\s]", "").split(",");
        
        for(String a: chain) {
            System.out.println("dasd:"+a + ":dsad");
        }
    }
    
}

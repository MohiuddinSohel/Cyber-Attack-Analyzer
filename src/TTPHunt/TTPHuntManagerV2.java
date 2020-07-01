/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTPHunt;

import AttackTree.AttackGraphV2;
import Helper.DataStorage;
import Helper.GraphGeneratorV2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author mahmed27
 */
public class TTPHuntManagerV2 {
    private ThreatReasoningV2 threatReasoning;
    private ArrayList<String> candidateGoalList;
    private ArrayList<String> initialAccessPoint;
    AttackGraphV2 graph;
    
    public TTPHuntManagerV2() throws IOException {
        this.init();
    }
    
    public TTPHuntManagerV2(AttackGraphV2 g, ArrayList<String> iAList, ArrayList<String> aGList) throws IOException {
        this.graph = g;
        this.initialAccessPoint = iAList;
        this.candidateGoalList = aGList;
        this.threatReasoning = new ThreatReasoningV2(this.graph, this.candidateGoalList, this.initialAccessPoint);
    }
    
    private void init() throws IOException {
        LinkedHashMap<String, String> eList = GraphGeneratorV2.generateAllEdges(50);
        AttackGraphV2 g = GraphGeneratorV2.generateGraph(eList);
        ArrayList<String> iAList = GraphGeneratorV2.initialAccessPoint(g);
        ArrayList<String> aGList = GraphGeneratorV2.achieveableGoalList(g);
        LinkedHashMap<String, Double> posteriorP = GraphGeneratorV2.setPosteriorProbability(g, aGList);
        LinkedHashMap<String, Double> priorP = GraphGeneratorV2.setPriorProbability(g);
        g.setPriorProbability(priorP);
        g.setPosteriorProbability(posteriorP);
        Helper.CSVProcessorV2.createTacticLevel();
        
        this.graph = g;
        this.initialAccessPoint = iAList;
        this.candidateGoalList = aGList;
        this.threatReasoning = new ThreatReasoningV2(this.graph, this.candidateGoalList, this.initialAccessPoint);
    }
    
    public void huntTTP() throws IOException {
        
        ArrayList<String> reportedTechnique = /*DataStorage.randomReportedTechniqueGenerator(0.2, 1);*/this.generateReportedTechnique();
        
        if(reportedTechnique.size() == 0) {
            System.out.println("Number of reported technique is 0");
            return;
        }
        LinkedHashMap<String, Double> candidateContributionMap = new LinkedHashMap();
        Date dat = new Date();
        long tim1Mili = System.currentTimeMillis();
        for(String goali: this.candidateGoalList) {
            long tim1Mili11 = System.currentTimeMillis();
            candidateContributionMap.put(goali, this.threatReasoning.calculateContributionOfGoalForAllChain(goali, reportedTechnique));
            long tim1Mili22 = System.currentTimeMillis();
//            System.out.println("Time required to calculate one goal's contribution: " + (tim1Mili22 - tim1Mili11));
        }
        //Hypothesis Generation
        ArrayList<ArrayList<String>> hypothesisSet = new ArrayList();
        ArrayList<ArrayList<String>> hypothesisSet1 = new ArrayList();
        ArrayList<String> hypothesis = new ArrayList();
        
        LinkedHashMap<String, Double> sortedCandidateContributionMap = candidateContributionMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        
        this.threatReasoning.generateAllHypothesis(hypothesisSet, hypothesis, candidateGoalList, sortedCandidateContributionMap, reportedTechnique);
//        if(hypothesisSet.isEmpty()) {
            this.threatReasoning.generateHypothesis(hypothesisSet1, hypothesis, this.candidateGoalList, sortedCandidateContributionMap, reportedTechnique);
//        }
        System.out.println("hypothesisSet1: " + hypothesisSet1);
        
        //Fidelity Score calculation
        Double currentScore = this.threatReasoning.selectTheBestHypothesis(hypothesisSet, reportedTechnique);
        long tim1Mili2 = System.currentTimeMillis();
        System.out.println("Time required to calculate all goal's contribution: " + (tim1Mili2 - tim1Mili));
        System.out.print("Fidelity Score: " + currentScore);
    }
    
    
    public ArrayList<String> generateReportedTechnique() {
//        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1059");
//        reportedTechnique.add("t1003");
//        reportedTechnique.add("t1002");
//        reportedTechnique.add("t1005");
////        reportedTechnique.add("t1036");
////        reportedTechnique.add("t1077");
//        reportedTechnique.add("t1087");
//        return reportedTechnique;
//        return DataStorage.generateReportedTechniqueAttack01();
        return DataStorage.generateReportedTechniqueTestCast1();
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
        TTPHuntManagerV2 manager = new TTPHuntManagerV2();
        manager.huntTTP();
    }
    
}

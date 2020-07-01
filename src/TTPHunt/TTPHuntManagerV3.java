/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTPHunt;

import SyntheticAttackChain.SyntheticAttackChain;
import AttackTree.AttackGraphV2;
import Helper.DataStorage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author mahmed27
 */
public class TTPHuntManagerV3 {
    public ThreatReasoningV3 threatReasoning;
    private ArrayList<String> candidateGoalList;
    private ArrayList<String> initialAccessPoint;
    private ArrayList<ArrayList<String>> currentHypothesisSet;
    private ArrayList<ArrayList<String>> currentHypothesisSet1;
    private AttackGraphV2 graph;
 
    
    public TTPHuntManagerV3() throws IOException {
        this.init();
    }
    
    public TTPHuntManagerV3(AttackGraphV2 g, ArrayList<String> iAList, ArrayList<String> aGList) throws IOException {
        this.graph = g;
        this.initialAccessPoint = iAList;
        this.candidateGoalList = aGList;
        this.threatReasoning = new ThreatReasoningV3(this.graph, this.candidateGoalList, this.initialAccessPoint);
    }
    
    private void init() throws IOException {
        LinkedHashMap<String, LinkedHashMap<String, Double>> stats = new LinkedHashMap<>();
        SyntheticAttackChain synChain = new SyntheticAttackChain();
        LinkedHashMap<String, ArrayList<String>> initFinal = synChain.calculatePosteriorAndPriorProbability(stats);
        LinkedHashMap<String, String> eList = synChain.generateAllEdges();
        AttackGraphV2 g = SyntheticAttackChain.generateGraph(eList);
        ArrayList<String> iAList = initFinal.get("init");
        ArrayList<String> aGList = initFinal.get("goal");
        LinkedHashMap<String, Double> posteriorP = synChain.setProbabilityRandom(g, aGList, stats.get("posterior"), stats.get("prior")); //synChain.setProbability(g, aGList, stats.get("posterior"), stats.get("prior"));//
        LinkedHashMap<String, Double> priorP = stats.get("prior");
        g.setPriorProbability(priorP);
        g.setPosteriorProbability(posteriorP);
        
        
        this.graph = g;
        this.initialAccessPoint = iAList;
        this.candidateGoalList = aGList;
        this.threatReasoning = new ThreatReasoningV3(this.graph, this.candidateGoalList, this.initialAccessPoint);
        
        this.currentHypothesisSet = new ArrayList<ArrayList<String>>();
        this.currentHypothesisSet1 = new ArrayList<ArrayList<String>>();
    }
    
    public void huntTTP(ArrayList<String> reportedTechnique) throws IOException {
        this.currentHypothesisSet = new ArrayList<ArrayList<String>>();
        
//        ArrayList<String> reportedTechnique = /*DataStorage.randomReportedTechniqueGenerator(0.2, 1);*/this.generateReportedTechnique();
        
        if(reportedTechnique.isEmpty()) {
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
        ArrayList<String> hypothesis = new ArrayList();
        
        LinkedHashMap<String, Double> sortedCandidateContributionMap = candidateContributionMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        
        
        this.threatReasoning.generateHypothesis(this.currentHypothesisSet, hypothesis, this.candidateGoalList, sortedCandidateContributionMap, reportedTechnique);
//        this.threatReasoning.generateAllHypothesis(this.currentHypothesisSet1, hypothesis, candidateGoalList, sortedCandidateContributionMap, reportedTechnique);

        //Fidelity Score calculation
        //this.reEvaluateCurrentHypothesis(reportedTechnique);
        this.reEvaluateCurrentHypothesisLog(reportedTechnique, this.threatReasoning.cProbability);//this.threatReasoning.cProbability

        long tim1Mili2 = System.currentTimeMillis();
//        System.out.println("Time required to calculate all goal's contribution: " + (tim1Mili2 - tim1Mili));
//        System.out.print("Fidelity Score: " + currentScore);
    }
    
    
    public void reEvaluateCurrentHypothesis(ArrayList<String> reportedTechnique, HashMap<String, Double> cProbability) throws IOException {
        System.out.println("*********Fidelity of Selected Hypothesis************************");
        for(ArrayList<String> h: this.currentHypothesisSet) {
            ArrayList<String> c = new ArrayList<>();
            Double d = this.threatReasoning.calculateFidelityScore(h, reportedTechnique, c, cProbability);
            System.out.println("Hypothesis: " + h);
            System.out.println("Reported technique: " + reportedTechnique);
            System.out.println("General Fidelity Score: " + d);
            LinkedHashMap<ArrayList<String>, Double> fidelitylist = this.threatReasoning.calculateFidelityOfAllChainOfAHypothsis(h, reportedTechnique, cProbability);
            Double exponentialAverage = this.threatReasoning.calculateExponentialWeightedAverage(fidelitylist);
            Double weightedAverage = this.threatReasoning.calculateWeightedAverage(fidelitylist, reportedTechnique);
            System.out.println("Exponential weighted Fidelity Score: " + exponentialAverage);
            System.out.println("Weighted Fidelity Score: " + weightedAverage);
            
            SaveResultToFile(SyntheticAttackChain.dataSet, h, reportedTechnique, d, "", exponentialAverage, "", weightedAverage);
        }
        System.out.println("hypothesisSet: " + this.currentHypothesisSet);
        System.out.println("**********************************Fidelity of Selected Hypothesis************************");
//        Double currentScore = this.threatReasoning.selectTheBestHypothesis(this.currentHypothesisSet1, reportedTechnique);
    }
    
    
    public void reEvaluateCurrentHypothesisLog(ArrayList<String> reportedTechnique, HashMap<String, Double> cProbability) throws IOException {
        System.out.println("*********Fidelity of Selected Hypothesis************************");
        for(ArrayList<String> h: this.currentHypothesisSet) {
            ArrayList<String> c = new ArrayList<>();
            Double d = this.threatReasoning.calculateFidelityScore(h, reportedTechnique, c, cProbability);
            System.out.println("Hypothesis: " + h);
            System.out.println("Reported technique: " + reportedTechnique);
            System.out.println("General Fidelity Score: " + d);
            
            LinkedHashMap<String, LinkedHashMap<ArrayList<String>, Double>> fidelitylist = this.threatReasoning.calculateFidelityOfAllChainOfAHypothsisLog(h
                    , reportedTechnique, cProbability);
            LinkedHashMap<String, Double> exponentialAverage = this.threatReasoning.calculateExponentialWeightedAverageByGoal(fidelitylist);
            LinkedHashMap<String, Double> weightedAverage = this.threatReasoning.calculateWeightedAverageByGoal(fidelitylist, reportedTechnique);
            
            Map.Entry<String, Double> exponentialAverageGoalValue = exponentialAverage.entrySet().iterator().next();
            System.out.println("Exponential weighted Fidelity highest goal: " + exponentialAverageGoalValue.getKey());
            System.out.println("Exponential weighted Fidelity Score(Log): " + exponentialAverageGoalValue.getValue());
            
            Map.Entry<ArrayList<String>, Double> maxChain = null;//fidelitylist.get(exponentialAverageGoalValue.getKey()).entrySet().iterator().next();
            Iterator t = fidelitylist.get(exponentialAverageGoalValue.getKey()).entrySet().iterator();
            while(t.hasNext()) {
                maxChain =(Map.Entry<ArrayList<String>, Double>) t.next();
            }
            System.out.println("Exponential Max Chain(Log): " + maxChain.getKey());
            System.out.println("Exponential Max Chain Score(Log): " + maxChain.getValue() + "\n");
            
            
            Map.Entry<String, Double> weightedAverageGoalValue = weightedAverage.entrySet().iterator().next();
            System.out.println("Weighted Fidelity highest goal: " + weightedAverageGoalValue.getKey());
            System.out.println("Weighted Fidelity Score: " + weightedAverageGoalValue.getValue());
            
            Map.Entry<ArrayList<String>, Double> maxChain0  = null;//fidelitylist.get(exponentialAverageGoalValue.getKey()).entrySet().iterator().next();
            t = fidelitylist.get(weightedAverageGoalValue.getKey()).entrySet().iterator();
            while(t.hasNext()) {
                maxChain0 =(Map.Entry<ArrayList<String>, Double>) t.next();
            }
            System.out.println("Max Chain Weighted: " + maxChain0.getKey());
            System.out.println("Max Chain Score(Log) Weighted: " + maxChain0.getValue() + "\n");
            
            LinkedHashMap<ArrayList<String>, Double> fidelitylistNL = this.threatReasoning.calculateFidelityOfAllChainOfAHypothsis(h, reportedTechnique, cProbability);
            Double exponentialAverageNL = this.threatReasoning.calculateExponentialWeightedAverage(fidelitylistNL);
            Double weightedAverageNL = this.threatReasoning.calculateWeightedAverage(fidelitylistNL, reportedTechnique);
            System.out.println("Exponential weighted Fidelity Score not log: " + exponentialAverageNL);
            System.out.println("Weighted Fidelity Score not log: " + weightedAverageNL);
            
            SaveResultToFile(SyntheticAttackChain.dataSet, h, reportedTechnique, d, exponentialAverageGoalValue.getKey()
                    , exponentialAverageGoalValue.getValue(), maxChain0.getKey().toString(), exponentialAverageNL);
        }
        System.out.println("hypothesisSet: " + this.currentHypothesisSet);
        System.out.println("**********************************Fidelity of Selected Hypothesis************************\n\n");
    }
    
    public static void SaveResultToFile(String DataSet, ArrayList<String> hypothesis, ArrayList<String> reportedTechnique
            , Double generalFidelity, String exponentialAvgGoal, Double exponentialAverageFidelity
            , String weightedAvgGoal,Double weightedAverageFidelity) throws IOException {
        
        String stats = "DataSet: ";
        stats += DataSet+ "\n";
        
        stats += "Observed Techniques: " + reportedTechnique + "\n";
        stats += "Hypothesis: " + hypothesis + "\n";
        
        stats += "GF: " + generalFidelity + "\n";
        stats += "EAGoal: " + exponentialAvgGoal + "\n";
        stats += "EAF: " + exponentialAverageFidelity + "\n";
        stats += "WAGoal: " + weightedAvgGoal + "\n";
        stats += "WAF: " + weightedAverageFidelity + "\n\n\n";
        
        BufferedWriter w = new BufferedWriter(new FileWriter(new File(SyntheticAttackChain.resultStorePath), true));
        w.write(stats);
        w.close();
                
    }
    
    
   
    
    public static void main(String[] args) throws IOException {
        TTPHuntManagerV3 manager = new TTPHuntManagerV3();
//        manager.huntTTP();
        View view = new View(manager);
        view.selectGui();
    }
}

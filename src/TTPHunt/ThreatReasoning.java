/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTPHunt;

import AttackTree.AttackGraph;
import AttackTree.TechniqueNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mahmed27
 */
public class ThreatReasoning {
    private final AttackGraph graph;
    private final ArrayList<String> achievableGoalList;
    private final ArrayList<String> initialAccessPoint;
    private HashMap<String, ArrayList<String>> coveredTechniqueByAGoal;
    private HashMap<String, Double> cProbability;
    private HashMap<String, Double> pProbability;
    
    public ThreatReasoning(AttackGraph graph, ArrayList<String> gList
            , ArrayList<String> iAccessPoint) {
        this.initialAccessPoint = iAccessPoint;
        this.achievableGoalList = gList;
        this.graph = graph;
        this.coveredTechniqueByAGoal = new HashMap();
        this.cProbability = this.graph.getPosteriorProbability();
        this.pProbability = this.graph.getPriorProbability();
    }
    
    public Double caculateRelativeContributionOfGoal(String targetTechi, String goali) {
        Double denominator = 0.0;
        Double numerator = 0.0;
        if(this.cProbability.containsKey(targetTechi + "|" + goali)) {
            numerator = this.cProbability.get(targetTechi + "|" + goali);
        }
        if(numerator == 0.0) return 0.0;
        for(String gNode : this.achievableGoalList) {
            if(this.cProbability.containsKey( targetTechi + "|" + gNode)) {
                denominator += this.cProbability.get( targetTechi + "|" + gNode);
            }
        }
        if(denominator == 0.0) return 0.0;
        return (numerator / denominator);
    }
    
    public Double calculateNormalizedPosteriorProbability(String targetTechi, String goali) {
        Double denominator = 0.0;
        Double numerator = this.caculateRelativeContributionOfGoal(targetTechi, goali) 
                * (this.pProbability.containsKey(goali) ? this.pProbability.get(goali) : 0.11);
        if(numerator == 0.0) return 0.0;
        for(String gNode : this.achievableGoalList) {
            denominator += (this.caculateRelativeContributionOfGoal(targetTechi, gNode) 
                    * (this.pProbability.containsKey(gNode) ? this.pProbability.get(gNode) : 0.11));
        }
        if(denominator == 0.0) return 0.0;
        return (numerator / denominator);
    }
    
    public Double calculateContributionOfGoalInSingleChain(String goali, ArrayList<String> chainJ
            , ArrayList<String> reportedTechnique) {
        
        Double denominator = 0.0;
        Double numerator = 0.0;
        for(String techId : chainJ) {
            denominator += this.calculateNormalizedPosteriorProbability(techId, goali);
            if(!this.coveredTechniqueByAGoal.get(goali).contains(techId)) {
                this.coveredTechniqueByAGoal.get(goali).add(techId);
            }
            if(reportedTechnique.contains(techId)) {
                numerator += this.calculateNormalizedPosteriorProbability(techId, goali);
            }
        }
        if(denominator == 0.0) return 0.0;
        return (numerator / denominator);
    }
    
    public Double calculateContributionOfGoalForAllChain(String goali
            , ArrayList<String> reportedTechnique) throws IOException {
        
        if(!this.coveredTechniqueByAGoal.containsKey(goali)) {
            this.coveredTechniqueByAGoal.put(goali, new ArrayList<String>());
        }
        
        ArrayList<ArrayList<String>> chainList = new ArrayList<>();
        Helper.CSVProcessor.printAllPath(this.graph, chainList, goali, this.initialAccessPoint, reportedTechnique);
        if(chainList.size() == 0.0) return 0.0;
        Double totalScore = 0.0;
        for(ArrayList<String> chainJ : chainList) {
            totalScore += this.calculateContributionOfGoalInSingleChain(goali, chainJ, reportedTechnique);
        }
        return (totalScore / chainList.size());
    }
    
    public void generateHypothesis(ArrayList<ArrayList<String>> hypothesisSet
            , ArrayList<String> hypothesis, ArrayList<String> candidateList, HashMap<String, Double> candidateContributionMap
            , ArrayList<String> reportedTechnique) {
        Double cMax = 0.0;
        ArrayList<String> gQ = new ArrayList(); 
        
        for(String key : candidateList) {
            if(candidateContributionMap.get(key) > cMax) {
                cMax = candidateContributionMap.get(key);
                gQ.clear();
                gQ.add(key);
            } else {
                if( Math.abs(cMax - candidateContributionMap.get(key)) <= 0.0001) {
                    gQ.add(key);
                }
            }
        }
        
        HashMap<Integer, ArrayList<String>> hList = new HashMap<>();
        HashMap<Integer, ArrayList<String>> qkList = new HashMap<>();
        HashMap<Integer, ArrayList<String>> gcList = new HashMap<>();
        for(int i = 0; i < gQ.size(); i++) {
            ArrayList<String> tmpH = new ArrayList<> (hypothesis);
            tmpH.add(gQ.get(i));
            hList.put(i, tmpH);
            
            ArrayList<String> tmpQk = new ArrayList<> (reportedTechnique);
            for(String tmpTech : this.coveredTechniqueByAGoal.get(gQ.get(i))) {
//                tmpQk.removeIf((String emp)-> emp.contains(tmpTech));
                tmpQk.remove(tmpTech);
            }
            qkList.put(i, tmpQk);
            
            ArrayList<String> tmpGc = new ArrayList<> (candidateList);
            String s = gQ.get(i);
//            tmpGc.removeIf((String emp)-> emp.contains(s));
            tmpGc.remove(s);
            gcList.put(i, tmpGc);
        }
        
        for(Integer i : qkList.keySet()) {
            if(qkList.get(i).isEmpty()) {
                hypothesisSet.add(hList.get(i));
            }
        }
//Needs to handle all hypothesis under consideration . Currently the first genereated hypothesis is considered       
        if(hypothesisSet.size() > 0) { 
            return;
        } else {
            for(Integer i: hList.keySet()) {
                generateHypothesis(hypothesisSet, hList.get(i), gcList.get(i), candidateContributionMap, qkList.get(i));
            }
        } 
    }
    
    public Double calculateFidelityScore(ArrayList<String> hypothesis, ArrayList<String> reportedTechnique) {
        
        ArrayList<String> allCoveredTechnique = new ArrayList<>();
        for(String h: hypothesis) {
            allCoveredTechnique.removeAll(this.coveredTechniqueByAGoal.get(h));
            allCoveredTechnique.addAll(this.coveredTechniqueByAGoal.get(h));
        }
        
        Double numerator = 1.0;
        Double denominator = 1.0;
        
        for(String technique : allCoveredTechnique) {
            Double tmpNumerator = 1.0;
            Double tmpDenominator = 1.0;
            boolean mIHere = false;
            if(hypothesis.contains(technique)) continue;
            for(String h: hypothesis) {
                    tmpNumerator *= (1.0 - (this.cProbability.containsKey(technique + "|" + h) ? this.cProbability.get(technique + "|" + h) : 0));
                if(reportedTechnique.contains(technique)) {
                    tmpDenominator *= (1.0 - (this.cProbability.containsKey(technique + "|" + h) ? this.cProbability.get(technique + "|" + h) : 0));
                    mIHere = true;
                }
            }
            
            numerator *= (1 - tmpNumerator);
            if(mIHere) {
                denominator *= (1 - tmpDenominator);
            }
        }
        return numerator / denominator;
    }
}

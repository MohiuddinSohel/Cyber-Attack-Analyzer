/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTPHunt;

import AttackTree.AttackGraphV2;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toMap;

/**
 *
 * @author mahmed27
 */
public class ThreatReasoningV3 {
    private final AttackGraphV2 graph;
    private final ArrayList<String> achievableGoalList;
    private final ArrayList<String> initialAccessPoint;
    private HashMap<String, ArrayList<String>> coveredTechniqueByAGoal;
    private HashMap<String, ArrayList<ArrayList<String>>> techniqueChainByAGoal;
    public HashMap<String, Double> cProbability;
    public HashMap<String, Double> normalizedcProbability;
    private HashMap<String, Double> pProbability;
    private Double chainBelivability;
    
    public ThreatReasoningV3(AttackGraphV2 graph, ArrayList<String> gList
            , ArrayList<String> iAccessPoint) {
        this.initialAccessPoint = iAccessPoint;
        this.achievableGoalList = gList;
        this.graph = graph;
        this.coveredTechniqueByAGoal = new HashMap();
        this.techniqueChainByAGoal = new HashMap();
        this.cProbability = this.graph.getPosteriorProbability();
        this.normalizedcProbability = this.normalizedProbability(this.graph.getPosteriorProbability());
        this.pProbability = this.graph.getPriorProbability();
        this.chainBelivability = 0.00000001;
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
    
    public HashMap<String, Double> normalizedProbability(HashMap<String, Double> cProb) {
        HashMap<String, Double> posterior = new HashMap<>();
        for(String key : cProb.keySet()) {
            String[] tg = key.split("\\|");
            posterior.put(key, caculateRelativeContributionOfGoal(tg[0],tg[1]));
        }
        return posterior;
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
//            if(!this.coveredTechniqueByAGoal.get(goali).contains(techId)) {
//                this.coveredTechniqueByAGoal.get(goali).add(techId);
//            }
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
            this.coveredTechniqueByAGoal.put(goali, new ArrayList<>());
            this.techniqueChainByAGoal.put(goali, new ArrayList<>());
        }
        
        ArrayList<ArrayList<String>> chainList = new ArrayList<>();
        SyntheticAttackChain.SyntheticAttackChain.generateAllPath(this.graph, chainList, goali, this.initialAccessPoint, reportedTechnique);
//        Helper.CSVProcessorV2.generateAllPath(this.graph, chainList, goali, this.initialAccessPoint, reportedTechnique);
//        System.out.println(chainList);
        if(chainList.size() == 0.0) return 0.0;
        Double totalScore = 0.0;
        int numberOfChain = 0;
        for(ArrayList<String> chainJ : chainList) {
            
            Double tmpcScore = this.calculateContributionOfGoalInSingleChain(goali, chainJ, reportedTechnique);
            if(tmpcScore > this.chainBelivability) {
                numberOfChain++;
                this.techniqueChainByAGoal.get(goali).add(chainJ);
                this.coveredTechniqueByAGoal.get(goali).removeAll(chainJ);
                this.coveredTechniqueByAGoal.get(goali).addAll(chainJ);
                
                totalScore += tmpcScore;
            }
        }
//        return (totalScore / chainList.size());
        if(numberOfChain == 0) return 0.0;
        return (totalScore / numberOfChain);
    }
    
    private boolean hypothesisExists(ArrayList<ArrayList<String>> hypothesisSet
        , ArrayList<String> hypothesis) {
        ArrayList<String> a = new ArrayList<>(hypothesis);
        Collections.sort(a);
        for(ArrayList<String> hy : hypothesisSet) {
            ArrayList<String> b = new ArrayList<>(hy);
            Collections.sort(b);
            if(a.size() == b.size() && a.equals(b)) return true;
        }
        return false;
    }
    
    public void generateAllHypothesis(ArrayList<ArrayList<String>> hypothesisSet
        , ArrayList<String> hypothesis, ArrayList<String> candidateList, HashMap<String, Double> candidateContributionMap
        , ArrayList<String> reportedTechnique) {
        
        ArrayList<String>  tmpReportedTechnique = new ArrayList<> (reportedTechnique);
        ArrayList<String> tmpHypothesis = new ArrayList<> (hypothesis);
        
        for(String key : candidateContributionMap.keySet()) {
            if(candidateContributionMap.get(key) <= 0.0 || tmpHypothesis.contains(key)) continue;
            
            ArrayList<String> coveredTechnique = this.coveredTechniqueByAGoal.get(key);
            boolean res = tmpReportedTechnique.removeAll(coveredTechnique);
            
            if (res) {
                tmpHypothesis.add(key);
//            }
                if(tmpReportedTechnique.isEmpty() && !hypothesisExists(hypothesisSet, tmpHypothesis)) {
                    hypothesisSet.add(tmpHypothesis);

                } else {
                    generateAllHypothesis(hypothesisSet, tmpHypothesis, candidateList, candidateContributionMap, tmpReportedTechnique);
                }
                
                tmpReportedTechnique = new ArrayList<> (reportedTechnique);
                tmpHypothesis = new ArrayList<> (hypothesis);
            }
            
        }
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
                if( Math.abs(cMax - candidateContributionMap.get(key)) <= 0.000001 && cMax != 0.0) {
                    gQ.add(key);
                }
            }
        }
        
        if(gQ.isEmpty()) {
            System.out.println("candidate Size: " + candidateList.size());
            System.out.println("gQ Following techniques are not present in the graph:  ");
            System.out.println(reportedTechnique);
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
//        if(candidateList.isEmpty() && hypothesisSet.isEmpty()) {
//            System.out.println("Following techniques are not present in the graph:  ");
//            System.out.println(reportedTechnique);
//            return;
//        }
        if(hypothesisSet.size() > 0) { 
            return;
        } else {
            for(Integer i: hList.keySet()) {
                generateHypothesis(hypothesisSet, hList.get(i), gcList.get(i), candidateContributionMap, qkList.get(i));
            }
        } 
    }
    
    public Double selectTheBestHypothesis(ArrayList<ArrayList<String>> hypothesisSet, ArrayList<String> reportedTechnique
            , HashMap<String, Double> cProbability) {        
        ArrayList<String> allCoveredTechnique = new ArrayList<>();
        ArrayList<String> finalH = null;
        Double currentScore = 0.0;
        ArrayList<String> finalT = null;
        for(ArrayList<String> h :  hypothesisSet) {
            System.out.println("*********************************************************************");
            System.out.println("Hypothesis: " + h.toString());
            Double tmp = this.calculateFidelityScore(h, reportedTechnique, allCoveredTechnique, cProbability);
            LinkedHashMap<ArrayList<String>, Double> fidelitylist = this.calculateFidelityOfAllChainOfAHypothsis(h, reportedTechnique, cProbability);
            Double ewA = this.calculateExponentialWeightedAverage(fidelitylist);
            Double wA = this.calculateWeightedAverage(fidelitylist, reportedTechnique);
            System.out.println("General Fidelity: " + tmp);
            System.out.println("exponential weighted average Fidelity: " + ewA);
            System.out.println("weighted average Fidelity: " + wA);
            HashMap<ArrayList<String>, Double> fidelitylisttmp = fidelitylist.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey
                            , Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
            for(ArrayList<String> key : fidelitylisttmp.keySet()) {
                System.out.println("Chain with Highest Fidelity: " + key);
                System.out.println("Fidelity of Chain with Highest Fidelity: " + fidelitylisttmp.get(key));
                break;
            }
//            System.out.println("*********************************************************************");
            if(tmp > currentScore) {
                finalH = h;
                currentScore = tmp;
                finalT = new ArrayList<>(allCoveredTechnique);
                
            }
            
        }
        System.out.println("Reported Techniques: " + reportedTechnique);
        System.out.println("Final Hypothesis: " + finalH);
        System.out.println("Final Best score: " + currentScore);
//        System.out.println("All Covereded techniques: " + allCoveredTechnique);
        System.out.println("Number of techniques to Verify: " + (allCoveredTechnique.size() - reportedTechnique.size()));
        System.out.println("********************************************************************************");
//        System.out.println("TTP Chain covered by hypothesis:");
//        for(String h: finalH) {
//            System.out.println(this.techniqueChainByAGoal.get(h));
//        }
        return currentScore;

    }
    
    public Double calculateFidelityScore(ArrayList<String> hypothesis, ArrayList<String> reportedTechnique
            , ArrayList<String> allCoveredTechnique, HashMap<String, Double> cProbability) {
        
        allCoveredTechnique.clear();
        for(String h: hypothesis) {
//            System.out.println("Technique By goal:" + this.techniqueChainByAGoal.get(h));
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
                    tmpNumerator *= (1.0 - (cProbability.containsKey(technique + "|" + h) ? cProbability.get(technique + "|" + h) : 0.0));
                if(reportedTechnique.contains(technique)) {
                    tmpDenominator *= (1.0 - (cProbability.containsKey(technique + "|" + h) ? cProbability.get(technique + "|" + h) : 0.0));
                    mIHere = true;
                }
            }
                        
            Double asd = numerator;
            numerator *= (1 - tmpNumerator);
            if(numerator == 0.0) {
//                System.out.println("#########vvvv###############");
            }
            if(mIHere) {
                denominator *= (1 - tmpDenominator);
            }
        }
        return numerator / denominator;
    }
    
    public LinkedHashMap<ArrayList<String>, Double> calculateFidelityOfAllChainOfAHypothsis(ArrayList<String> hypothesis
            , ArrayList<String> reportedTechnique, HashMap<String, Double> cProbability) {
        LinkedHashMap<ArrayList<String>, Double> chainFidelityList = new LinkedHashMap<>();
                
        for(String g : hypothesis) {
            ArrayList<ArrayList<String>> chainList = this.techniqueChainByAGoal.get(g);
            for(ArrayList<String> tList : chainList) {
                chainFidelityList.put(tList, this.CalculateFidelityOfAChain(tList, hypothesis, reportedTechnique, cProbability));
            }
        }
        return chainFidelityList.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));//chainFidelityList;
    } 
    
    public Double CalculateFidelityOfAChain(ArrayList<String> chain, ArrayList<String> hypothesis
            , ArrayList<String> reportedTechnique, HashMap<String, Double> cProbability) {
        Double numerator = 1.0;
        Double denominator = 1.0;
        Double tmpNumerator = 1.0;
        Double tmpDenominator = 1.0;
        
        for(String technique : chain) {
            tmpNumerator = 1.0;
            tmpDenominator = 1.0;
            boolean mIHere = false;
            if(hypothesis.contains(technique)) continue;
            for(String h : hypothesis) {
                tmpNumerator *= (1.0 - (cProbability.containsKey(technique + "|" + h) ? cProbability.get(technique + "|" + h) : 0.0));
                if(reportedTechnique.contains(technique)) {
                    tmpDenominator *= (1.0 - (cProbability.containsKey(technique + "|" + h) ? cProbability.get(technique + "|" + h) : 0.0));
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
    
    
    
    public LinkedHashMap<String, LinkedHashMap<ArrayList<String>, Double>> calculateFidelityOfAllChainOfAHypothsisLog(ArrayList<String> hypothesis
            , ArrayList<String> reportedTechnique, HashMap<String, Double> cProbability) {
        LinkedHashMap<String, LinkedHashMap<ArrayList<String>, Double>> chainFidelityList = new LinkedHashMap<>();
                
        for(String g : hypothesis) {
            ArrayList<ArrayList<String>> chainList = this.techniqueChainByAGoal.get(g);
            LinkedHashMap<ArrayList<String>, Double> chainFidelityOfAGoal = new LinkedHashMap<>();
            
            for(ArrayList<String> tList : chainList) {
                chainFidelityOfAGoal.put(tList, this.CalculateFidelityOfAChainLog(tList, g, reportedTechnique, cProbability));
            }
            chainFidelityList.put(g, chainFidelityOfAGoal);
        }
        
        for(String g : chainFidelityList.keySet()) {
            LinkedHashMap<ArrayList<String>, Double> sortedMap = chainFidelityList.get(g).entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));//chainFidelityList;
            chainFidelityList.put(g, sortedMap);
        }
        return chainFidelityList;
    } 
    
    public Double CalculateFidelityOfAChainLog(ArrayList<String> chain, String goal, ArrayList<String> reportedTechnique
            , HashMap<String, Double> cProbability) {
        Double score = 0.0;
        Double numerator = 0.0;
        Double denominator = 0.0;
        
        
        for(String technique : chain) {
            if(technique.equals(goal)) continue;
            Double temp = cProbability.get(technique + "|" + goal);
            denominator += (cProbability.containsKey(technique + "|" + goal) ?  Math.log10(temp) : 0.0);
            if(reportedTechnique.contains(technique) && !technique.equals(goal)) {
                numerator += (cProbability.containsKey(technique + "|" + goal) ?  Math.log10(temp) : 0.0);
            } else {
                numerator += (cProbability.containsKey(technique + "|" + goal) ?  Math.log10(1 - temp) : 0.0);
            }

        }
        
        if(denominator == 0.0) return 0.0;
        score = numerator / denominator;
        return score;
    }
    
    public Double calculateExponentialWeightedAverage(LinkedHashMap<ArrayList<String>, Double> fidelitylist) {
        Double avg = 0.0;
        Double weightFactor = 0.05;//1.0 / fidelitylist.size();
        ArrayList<String> hChain = null;
        Double val = 0.0;
        for(ArrayList<String> key : fidelitylist.keySet()) {
            val = fidelitylist.get(key);
            avg = avg * weightFactor + (1 - weightFactor) * val;
            hChain = key;
        }
        System.out.println("\n Highst Chain: " + hChain + "Score: " + val);
        return avg;
    } 
    
    public LinkedHashMap<String, Double> calculateExponentialWeightedAverageByGoal(LinkedHashMap<String, LinkedHashMap<ArrayList<String>, Double>> fidelitylist) {
        LinkedHashMap<String, Double> goalFidelity = new LinkedHashMap<>();
        for(String goal : fidelitylist.keySet()) {
            
            Double avg = 0.0;
            Double weightFactor = 0.05;//1.0 / fidelitylist.size();
            for(ArrayList<String> key : fidelitylist.get(goal).keySet()) {
                avg = avg * weightFactor + (1 - weightFactor) * fidelitylist.get(goal).get(key);
            }
            goalFidelity.put(goal, avg);
        }
        
        return goalFidelity.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

    }
    
    public Double calculateWeightedAverage(LinkedHashMap<ArrayList<String>, Double> fidelitylist, ArrayList<String> reportedTechnique) {
        Double avg = 0.0;
        Double totalF = 0.0;
        for(ArrayList<String> key : fidelitylist.keySet()) {
            Double seen = 0.0;
            for(String rT : reportedTechnique) {
                if(key.contains(rT)) {
                    seen++;
                }
            }
            totalF += fidelitylist.get(key);
            avg = avg + (seen / (key.size() - 1)) * fidelitylist.get(key);
        }
        return avg / totalF;
    }
    
    public LinkedHashMap<String, Double> calculateWeightedAverageByGoal(LinkedHashMap<String, LinkedHashMap<ArrayList<String>, Double>> fidelitylist
            , ArrayList<String> reportedTechnique) {
        LinkedHashMap<String, Double> goalFidelity = new LinkedHashMap<>();
        for(String goal : fidelitylist.keySet()) {
        
            Double avg = 0.0;
            Double totalF = 0.0;
            for(ArrayList<String> key : fidelitylist.get(goal).keySet()) {
                Double seen = 0.0;
                for(String rT : reportedTechnique) {
                    if(key.contains(rT)) {
                        seen++;
                    }
                }
                totalF += fidelitylist.get(goal).get(key);
                avg = avg + (seen / (key.size() - 1)) * fidelitylist.get(goal).get
        (key);
            }
            goalFidelity.put(goal, avg / totalF);
        }
        
        return goalFidelity.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
        
    }
    
}

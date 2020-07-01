/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evidentialreasoning;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author mahmed27
 */
public class HypothesisGeneratorWithoutT {
    
    private HashMap<String, Double> posteriorObGTA;
    private HashMap<String, Double> posteriorTAGTTP;
    private HashMap<String, Double> posteriorTAGTT1 = new HashMap();
    
    
    private HashMap<String, Double> normalizedProbOfObservableGTTP = new HashMap();
    private HashMap<String, Double> normalizedProbabilityOfTTPGivenObservable = new HashMap();
    
    private HashMap<String, Double> priorProbOfTTP = new HashMap();
    
    private ArrayList<ArrayList<String>> hypothesisList = new ArrayList<> ();
    
    private HashMap<String, ArrayList<String>> tttpToObservableMapping = new HashMap<>();
    
    private ArrayList<String> notExplainedObservable = new ArrayList<>();
    
    private HashMap<String, Double> cOfTTP = new HashMap<>();
    
    public ArrayList<String> getnotExplainedObservable() {
        return notExplainedObservable;
    }
    
    public void setposteriorObGTA(HashMap<String, Double> p) {
        this.posteriorObGTA = p;
    }
    
    public void setposteriorTAGTTP(HashMap<String, Double> p) {
        this.posteriorTAGTTP = p;
    }
    
    public void settttpToObservableMapping(HashMap<String, ArrayList<String>> p) {
        this.tttpToObservableMapping = p;
    }
    
    public void setpriorProbOfTTP(HashMap<String, Double> p) {
        this.priorProbOfTTP = p;
    }
    
    private Double calculateProbabilityOfObservableGivenTTP(String observable, String ttp) {
        if(observable.trim().isEmpty()) return 0.0;
        Double prbObsGivenTtp = 0.0;
        for(String key: this.posteriorObGTA.keySet()) {
            
            if(key.contains(observable.trim().toLowerCase())) {
                String[] ota = key.split("[+]");
                Double tmp = 0.0;
                if(this.posteriorTAGTTP.containsKey(ota[1].trim() + "+" + ttp.trim().toLowerCase())) {
                    tmp = (posteriorObGTA.get(key) * posteriorTAGTTP.get(ota[1].trim().toLowerCase() + "+" + ttp.trim().toLowerCase()));
                }
                else {
                    //System.out.println("probability of TTP given threat does not contain all value");
                }
                prbObsGivenTtp += tmp;
            }
        }
        return prbObsGivenTtp;
    }
    
    private Double calculateNormalizedProbabilityOfObservableGivenTTTP(String observable, String ttp) {
                
        Double deneumerator = this.calculateProbabilityOfObservableGivenTTP(observable, ttp);
        
        Double total = 0.00;
        for(String key : this.tttpToObservableMapping.keySet()) {
            if(this.tttpToObservableMapping.get(key).contains(observable)) {
                total += this.calculateProbabilityOfObservableGivenTTP(observable, key);
            }
        } 
        
        return deneumerator / total;
    }
    
    private Double calculateNormalizedProbabilityOfTTPGivenObservable(String ttp, String observable) {
        Double deneumerator = this.calculateNormalizedProbabilityOfObservableGivenTTTP(observable, ttp)
                * this.priorProbOfTTP.get(ttp);
        
        Double total = 0.00;
        for(String key : this.tttpToObservableMapping.keySet()) {
            if(this.tttpToObservableMapping.get(key).contains(observable)) {
                total += (this.calculateNormalizedProbabilityOfObservableGivenTTTP(observable, key) 
                        * this.priorProbOfTTP.get(key));
            }
        }
        return deneumerator / total;
    }
      
    private Double calculateContributionOfEachTTP(String ttp, ArrayList<String> observableO) {
        Double deneumerator = 0.00;
        Double numerator = 0.00;
        for(String o : observableO) {
            if(this.tttpToObservableMapping.get(ttp).contains(o)) {
                deneumerator += this.calculateNormalizedProbabilityOfTTPGivenObservable(ttp, o);
            }
        }
        
        for(String unO : this.tttpToObservableMapping.get(ttp)) {
            numerator += this.calculateNormalizedProbabilityOfTTPGivenObservable(ttp, unO);
        }
        return deneumerator / numerator;
    }
    
    public void generateHypothesis(ArrayList<ArrayList<String>> hypothesisSet, ArrayList<String> hypothesis
            , ArrayList<String> uncObservable, ArrayList<String> ttpCandidateList, ArrayList<String> input) {
        
        System.out.print("... ");
        Double cMax = 0.0;
        ArrayList<String> fs = new ArrayList<>();
        for(String ttp : ttpCandidateList) {
            Double contribution;
            if(this.cOfTTP.containsKey(ttp)) {
                contribution = this.cOfTTP.get(ttp);
            } else {
                contribution = calculateContributionOfEachTTP(ttp, input);
                this.cOfTTP.put(ttp, contribution);
            }
            
            if(contribution > cMax) {
                cMax = contribution;
                fs = new ArrayList<>();
                fs.add(ttp);
            } else if(Math.abs(contribution - cMax) <= 0.00001) {
                fs.add(ttp);
            }
        }
        
        HashMap<Integer, ArrayList<String>> hList = new HashMap<>();
        HashMap<Integer, ArrayList<String>> skList = new HashMap<>();
        HashMap<Integer, ArrayList<String>> fcList = new HashMap<>();
        
        for(Integer i = 0; i < fs.size(); i++) {
            
            ArrayList<String> thy = new ArrayList<> (hypothesis);
            thy.add(fs.get(i));
            hList.put(i, thy);
            
            ArrayList<String> rmpSk = new ArrayList<>(uncObservable);
            for(String o : this.tttpToObservableMapping.get(fs.get(i))) {
                rmpSk.removeIf((String emp)-> emp.contains(o));
            }
            skList.put(i, rmpSk);
            
            ArrayList<String> tmpfc = new ArrayList<> (ttpCandidateList);
            String s = fs.get(i);
            tmpfc.removeIf((String emp)-> emp.contains(s));
            fcList.put(i, tmpfc);
        }
        
        for(Integer i : skList.keySet()) {
            if(skList.get(i).isEmpty()) {
                hypothesisSet.add(hList.get(i));
            }
        }
        
        if(hypothesisSet.size() > 0) {
//            System.out.print("END ");
            return;
        }
        else {
            for(Integer i : hList.keySet()) {
                generateHypothesis(hypothesisSet, hList.get(i), skList.get(i), fcList.get(i), input);
            }
        }
        
    }
    
    public Double calculateFidality(ArrayList<String> hypothesis, ArrayList<String> input) {
        Double deneumerator = 1.00;
        Double numerator = 1.00;
        ArrayList<String> observableall = new ArrayList();
        for(int i = 0 ; i < hypothesis.size(); i++) {
            for(String key : this.tttpToObservableMapping.keySet()) {
                if(key.contains(hypothesis.get(i))) {
                    for(String o : this.tttpToObservableMapping.get(key)) {
                        if(!observableall.contains(o)) {
                            observableall.add(o);
                        }
                    }
                }
            }
        }
        
        for(String o : observableall) {
            if(!input.contains(o)) {
                this.notExplainedObservable.add(o);
            }
        }
        
        for(int i = 0; i < observableall.size(); i++) {
            Double a = 1.00;
            for(int j = 0 ; j < hypothesis.size(); j++) {

                Double as = this.calculateProbabilityOfObservableGivenTTP(observableall.get(i), hypothesis.get(j));
                if (Math.abs(as - 1.00) == 0) {
                    as = 0.96;
                }
                if(j == 0) {
                    a = 1.00 - as;
                } else {
                    a *= (1.00 - as);
                }
            }
            deneumerator *= (1.00 - a);
        }
        
        
        for(int i = 0; i < input.size(); i++) {
            Double a = 1.00;
            for(int j = 0 ; j < hypothesis.size(); j++) {
                
                Double as = this.calculateProbabilityOfObservableGivenTTP(input.get(i), hypothesis.get(j));
                if (Math.abs(as - 1.00) == 0) {
                    as = 0.96;
                }
                if(j == 0) {
                    a = 1.00 - as;
                } else {
                    a *= (1.00 - as);
                }
            }
            numerator *= (1.00 - a);
        }
        System.out.println("d: " + deneumerator);
        System.out.println("n: " + numerator + " " + (deneumerator/ numerator));
        
        System.out.println("All Observable related to Hypothsis: " + observableall);
        System.out.println("Not Explained Observable by Hypothsis: " + this.notExplainedObservable);
        
        
        //test
//        for(int i = 0; i < this.notExplainedObservable.size(); i++) {
//            for(int j = 0; j < hypothesis.size(); j++) {
//                Double as = this.calculateProbabilityOfObservableGivenTTP(this.notExplainedObservable.get(i), hypothesis.get(j));
//                System.out.println(this.notExplainedObservable.get(i) + " Given " + hypothesis.get(j) + ": " + as);
//            }
//        }
        
        return deneumerator / numerator;
    }
    
    public ArrayList<String> getttpCandidateList(ArrayList<String> input) {
        ArrayList<String> output = new ArrayList<>();
        for(String key : this.tttpToObservableMapping.keySet()) {
            ArrayList<String> obList = this.tttpToObservableMapping.get(key);
            for(String o : input) {
                if(obList.contains(o)) {
                    output.add(key);
                    break;
                }
            }
        }
        return output;
    }
    
    private Double calculateNormalizedProbabilityOfObservableGivenTTTP1(String observable, String ttp, HashMap<String, Double> probOfObservableGTTP) {
                
        Double total = 0.00; 
        for(String obTKey : probOfObservableGTTP.keySet()) {
            if(obTKey.contains(observable.trim().toLowerCase())) {
                total += probOfObservableGTTP.get(obTKey);
            }
        }
        return probOfObservableGTTP.get(observable.trim().toLowerCase() + "+" + ttp.trim().toLowerCase()) / total;
    }
    
    private Double calculateNormalizedProbabilityOfTTPGivenObservable1(String ttp, String observable) {
        
        Double deneumerator = this.normalizedProbOfObservableGTTP.get(observable.trim().toLowerCase() + "+" + ttp.trim().toLowerCase()) 
                * this.priorProbOfTTP.get(ttp.trim().toLowerCase());
        
        Double total = 0.00;
        
        for(String key : this.normalizedProbOfObservableGTTP.keySet()) {
            if(key.contains(observable.trim().toLowerCase())) {
                String[] t = key.split("[+]");
                total += (this.normalizedProbOfObservableGTTP.get(key) * this.priorProbOfTTP.get(t[1]));
            }
            
        }
        return deneumerator / total;
    }
    
    private Double calculateContributionOfEachTTP1(String ttp) {
        Double deneumerator = 0.00;
        Double numerator = 0.00;
        for(String key : this.normalizedProbabilityOfTTPGivenObservable.keySet()) {
            if(key.contains(ttp.trim().toLowerCase())) {
                String[] a = key.split("[+]");
                numerator += this.normalizedProbabilityOfTTPGivenObservable.get(key);
                if(ttp.contains(a[1])) {
                    deneumerator += this.normalizedProbabilityOfTTPGivenObservable.get(key);
                }
            }
        }
        return deneumerator / numerator;
    }
    
}

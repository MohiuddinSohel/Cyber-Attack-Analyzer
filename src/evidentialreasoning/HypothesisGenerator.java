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
public class HypothesisGenerator {
    
    HashMap<String, Double> posteriorObGTA = new HashMap();
    HashMap<String, Double> posteriorTAGTTP = new HashMap();
    HashMap<String, Double> posteriorTTPGT = new HashMap();
    HashMap<String, Double> probOfObservableGThreat = new HashMap();
    HashMap<String, Double> normalizedProbOfObservableGThreat = new HashMap();
    HashMap<String, Double> normalizedProbabilityOfThreatGivenObservable = new HashMap();
    
    HashMap<String, Double> priorProbOfthreat = new HashMap();
    
    private Double calculateProbabilityOfObservableGivenThreat(String observable, String threat) {
        Double prbObsGivenThreat = 0.0;
        for(String obGTAKey: posteriorObGTA.keySet()) {
            
            if(obGTAKey.contains(observable)) {
                String[] ota = obGTAKey.split("+");
                Double tmPro = 0.0;
                for(String tAGtTPKey : posteriorTAGTTP.keySet()) {
                    if(tAGtTPKey.contains(ota[1].trim())) {
                        String[] taTtp = tAGtTPKey.split("+");
                        if(posteriorTTPGT.containsKey(taTtp[1].trim() + "+" + threat)) {
                            tmPro += (posteriorTAGTTP.get(tAGtTPKey) * posteriorTTPGT.get(taTtp[1].trim() + "+" + threat));
                        }
                        else {
                            System.out.println("probability of TTP given threat does not contain all value");
                        }
                    }
                }
                prbObsGivenThreat += (posteriorObGTA.get(obGTAKey) * tmPro);
            }
        }
        return prbObsGivenThreat;
    }
    
    private Double calculateNormalizedProbabilityOfObservableGivenThreat(String observable, String threat) {
        
        Double total = 0.00; 
        for(String obTKey : probOfObservableGThreat.keySet()) {
            if(obTKey.contains(observable.trim().toLowerCase())) {
                total += probOfObservableGThreat.get(obTKey);
            }
        }
        return this.probOfObservableGThreat.get(observable.trim().toLowerCase() + "+" + threat.trim().toLowerCase()) / total;
    }
    
    private Double calculateNormalizedProbabilityOfThreatGivenObservable(String observable, String threat) {
        
        Double deneumerator = this.normalizedProbOfObservableGThreat.get(observable.trim().toLowerCase() + "+" + threat.trim().toLowerCase()) 
                * this.priorProbOfthreat.get(threat.trim().toLowerCase());
        
        Double total = 0.00;
        
        for(String key : this.normalizedProbOfObservableGThreat.keySet()) {
            if(key.contains(observable.trim().toLowerCase())) {
                String[] t = key.split("+");
                total += (this.normalizedProbOfObservableGThreat.get(key) * this.priorProbOfthreat.get(t[1]));
            }
            
        }
        return deneumerator / total;
    }
    
    private Double calculateContributionOfEachThreat(String threat, ArrayList<String> obserbedObser) {
        Double deneumerator = 0.00;
        Double numerator = 0.00;
        for(String key : this.normalizedProbabilityOfThreatGivenObservable.keySet()) {
            if(key.contains(threat.trim().toLowerCase())) {
                String[] a = key.split("+");
                numerator += this.normalizedProbabilityOfThreatGivenObservable.get(key);
                if(obserbedObser.contains(a[1])) {
                    deneumerator += this.normalizedProbabilityOfThreatGivenObservable.get(key);
                }
            }
        }
        return deneumerator / numerator;
    }
    
    public ArrayList<String> generateHypothesis(ArrayList<String> observable) {
        return null;
    }
    
    public Double calculateFidality(ArrayList<String> hypothesis, ArrayList<String> obserbedObser) {
        Double deneumerator = 1.00;
        Double numerator = 1.00;
        ArrayList<String> observableall = new ArrayList();
        for(int i = 0 ; i < hypothesis.size(); i++) {
            for(String key : this.probOfObservableGThreat.keySet()) {
                if(key.contains(hypothesis.get(i))) {
                    String[] tmp = key.split("+");
                    if(!observableall.contains(tmp[0])) {
                        observableall.add(tmp[0]);
                    }
                }
            }
        }
        for(int i = 0; i < observableall.size(); i++) {
            Double a = 1.00;
            for(int j = 0 ; j < hypothesis.size(); j++) {
                String tmp = observableall.get(i) + "+" + hypothesis.get(j);
                if(this.probOfObservableGThreat.containsKey(tmp)) {
                   a *=  (1 - this.probOfObservableGThreat.get(tmp));
                }
            }
            deneumerator *= (1 - a);
        }
        
        
        for(int i = 0; i < obserbedObser.size(); i++) {
            Double a = 1.00;
            for(int j = 0 ; j < hypothesis.size(); j++) {
                String tmp = obserbedObser.get(i) + "+" + hypothesis.get(j);
                if(this.probOfObservableGThreat.containsKey(tmp)) {
                   a *=  (1 - this.probOfObservableGThreat.get(tmp));
                }
            }
            numerator *= (1 - a);
        }
        return deneumerator / numerator;
    }
}

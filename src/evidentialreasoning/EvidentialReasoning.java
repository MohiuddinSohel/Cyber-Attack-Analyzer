/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evidentialreasoning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author mahmed27
 */
public class EvidentialReasoning {

    public static String filePath = "/Users/mahmed27/Desktop/html of threat/Threat_Action_Ontology-Updatedv3.1-May19(reviewed).csv"; //Threat_Action_Ontology.txt
    public static String filePathobGTHO = "/Users/mahmed27/Desktop/html of threat/obGTHO.txt";
    public static String filePaththoGthe = "/Users/mahmed27/Desktop/html of threat/thOTe.txt";
    /**
     * @param args the command line arguments
     */
    
    private ArrayList<String[]> ontology = new ArrayList();
    private HashMap<String, Integer> observableGThOb = new HashMap<>();
    private HashMap<String, Integer> threatActionObject = new HashMap<>();
    
    private HashMap<String, Integer> technique = new HashMap<>();
    private HashMap<String, Integer> threatActionObjectGivenTech = new HashMap<>();
    
    private HashMap<String, ArrayList<String>> tttpToObservableMapping = new HashMap<>();
    private HashMap<String, Double> priorProbOfTTP = new HashMap();
    
    private HashMap<String, Double> posteriorObGTA = new HashMap();
    private HashMap<String, Double> posteriorTAGTTP = new HashMap();
    
    public HashMap<String, Double> getposteriorObGTA() {
        return this.posteriorObGTA;
    }
    
    public HashMap<String, Double> getposteriorTAGTTP() {
        return this.posteriorTAGTTP;
    }
    
    public HashMap<String, ArrayList<String>> gettttpToObservableMapping() {
        return this.tttpToObservableMapping;
    }
    
    public HashMap<String, Double> getpriorProbOfTTP() {
        return this.priorProbOfTTP;
    }
    
    public void copyOntologyInMemory(String csvPath) throws FileNotFoundException, IOException {

        BufferedReader in = new BufferedReader(new FileReader(csvPath));
        String factData = "";
        String line;
        while((line = in.readLine()) != null) {
            if(line.contains("Technique(Header)")) {
                continue;
            }
            String[] ontologyColumn = line.split(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            this.ontology.add(ontologyColumn);
        }

        in.close();
    }
    
    public void calculateProbabilityOfObservableGivenThreatActionObject() {
        System.out.println("Ontology Size:" + this.ontology.size());
    
        for(String[] a : this.ontology) {
            
            
            ArrayList<String> observable = new ArrayList();

            int ik = 0;
            String prefix = "";
            String[] observable1 = a[6].trim().toLowerCase().split(":");
            if(observable1.length  >= 2) {
                ik = 1;
                prefix = observable1[0].trim();
            }
            String[] mulObs = observable1[ik].split(";");
            for(String ob : mulObs) {
                if(ob.trim().isEmpty()) continue;
                if(prefix.isEmpty()) {
                    observable.add(ob.trim());
                } else {
                    observable.add(prefix + "-" + ob.trim());
                }
                
            }
            
            ArrayList<String> threatActionObject = new ArrayList();
            String[] object1 = a[5].trim().toLowerCase().split(";");
            String[] threatAction1 = a[4].trim().toLowerCase().split("/");
            
            for(String obj : object1) {
                for(String th : threatAction1) {
                    threatActionObject.add(th.trim() + "-" + obj.trim());
                }
            }
            
            for(int i = 0 ; i < observable.size(); i++) {
                for(int j = 0; j < threatActionObject.size(); j++) {
                    if(this.observableGThOb.containsKey(observable.get(i) + "+" + threatActionObject.get(j))) {
                        this.observableGThOb.put(observable.get(i) + "+" + threatActionObject.get(j)
                                , this.observableGThOb.get(observable.get(i) + "+" + threatActionObject.get(j)) + 1);
                        
                    } else {
                        this.observableGThOb.put(observable.get(i) + "+" + threatActionObject.get(j), 1);
                    }
                    
                    if(this.threatActionObject.containsKey(threatActionObject.get(j))) {
                        this.threatActionObject.put(threatActionObject.get(j), this.threatActionObject.get(threatActionObject.get(j)) + 1);
                    } else {
                        this.threatActionObject.put(threatActionObject.get(j), 1);
                    }
                }
            }
            
            //creating ttp to observable mapping
            String techniqueTTp = a[2].trim().toLowerCase();
            if(this.tttpToObservableMapping.containsKey(techniqueTTp)) {
                ArrayList<String> oList = this.tttpToObservableMapping.get(techniqueTTp);
                for(String o1 : observable) {
                    if(!oList.contains(o1) && !o1.trim().isEmpty()) {
                        oList.add(o1.trim());
                    }
                }
                this.tttpToObservableMapping.put(techniqueTTp, oList);
            } else {
                this.tttpToObservableMapping.put(techniqueTTp, observable);
            }
      
        }
        
    }
    
    public void calculateProbabilityOfThreatActionObjectGivenTechnique() {
        
        for(String[] a : this.ontology) {
            
            ArrayList<String> threatActionObject = new ArrayList();
            String[] object1 = a[5].trim().toLowerCase().split(";");
            String[] threatAction1 = a[4].trim().toLowerCase().split("/");
            
            for(String obj : object1) {
                for(String th : threatAction1) {
                    threatActionObject.add(th.trim() + "-" + obj.trim());
                }
            }
            
            String technique = a[2].trim().toLowerCase();
            for(int i = 0; i < threatActionObject.size(); i++) {
                if(this.threatActionObjectGivenTech.containsKey(threatActionObject.get(i) + "+" + technique)) {
                    this.threatActionObjectGivenTech.put(threatActionObject.get(i) + "+" + technique
                            , this.threatActionObjectGivenTech.get(threatActionObject.get(i) + "+" + technique) + 1);
                } else {
                    this.threatActionObjectGivenTech.put(threatActionObject.get(i) + "+" + technique, 1);
                }
                
                if(this.technique.containsKey(technique)) {
                    this.technique.put(technique, this.technique.get(technique) + 1);
                } else {
                    this.technique.put(technique, 1);
                }
            }
        }
    }
    
    public void writeTofile(String filePath, HashMap<String, Integer> datac, HashMap<String, Integer> data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for(String key : datac.keySet()) {

            String[] observThObject = key.split("[+]");

            Double probability = Double.valueOf(datac.get(key)) / Double.valueOf(data.get(observThObject[1]));
            writer.write("P( " + observThObject[0] + " Given " + observThObject[1] + " ), " + datac.get(key) + "/" + data.get(observThObject[1]) + ", " + probability);
            writer.newLine();
        }
        writer.close();
    }
    
    public void write() throws IOException {
        this.writeTofile(EvidentialReasoning.filePathobGTHO, this.observableGThOb, this.threatActionObject);
        this.writeTofile(EvidentialReasoning.filePaththoGthe, this.threatActionObjectGivenTech, this.technique);
    }
    
    void calculateCasualMatrix() {
        for(String key: this.observableGThOb.keySet()) {
            String[] observThObject = key.split("[+]");
            Double probability = Double.valueOf(this.observableGThOb.get(key)) / Double.valueOf(this.threatActionObject.get(observThObject[1]));
            this.posteriorObGTA.put(key, probability);
        }
        for(String key: this.threatActionObjectGivenTech.keySet()) {
            String[] observThObject = key.split("[+]");
            Double probability = Double.valueOf(this.threatActionObjectGivenTech.get(key)) / Double.valueOf(this.technique.get(observThObject[1]));
            this.posteriorTAGTTP.put(key, probability);
            this.priorProbOfTTP.put(observThObject[1], 0.0001);
        }
    }
    
  /*  public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        EvidentialReasoning er = new EvidentialReasoning();
        er.copyOntologyInMemory(filePath);
        er.calculateProbabilityOfObservableGivenThreatActionObject();
        er.calculateProbabilityOfThreatActionObjectGivenTechnique();
        //er.write();
        er.calculateCasualMatrix();
        
        HypothesisGeneratorWithoutT generator = new HypothesisGeneratorWithoutT();
        generator.setposteriorObGTA(er.getposteriorObGTA());
        generator.setposteriorTAGTTP(er.getposteriorTAGTTP());
        generator.setpriorProbOfTTP(er.getpriorProbOfTTP());
        generator.settttpToObservableMapping(er.gettttpToObservableMapping());
        
        ArrayList<ArrayList<String>> hypothesisSet = new ArrayList<>();
        ArrayList<String> hypothesis = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        input.add("Dport=SMB".toLowerCase().trim());
        input.add("Usage of Rundll32.exe".toLowerCase().trim());
        input.add("Download activity".toLowerCase().trim());
        input.add("C2 Command".toLowerCase().trim());
        input.add("Absence of Event logs".toLowerCase().trim());
        input.add("file search activity with specific string".toLowerCase().trim());
        
        //Add New observable
        input.add("dport=ftp".toLowerCase().trim());
        input.add("dport=scp".toLowerCase().trim());
        input.add("dport=sftp".toLowerCase().trim());
        input.add("dport=rsync".toLowerCase().trim());
        input.add("file access/modification activity".toLowerCase().trim());
        input.add("execution of application".toLowerCase().trim());        
//        input.add("command line arguments-netsh".toLowerCase().trim());
//        input.add("command line arguments-reg query".toLowerCase().trim());
//        input.add("command line arguments-dir".toLowerCase().trim());
//        input.add("searching for littlesnitch".toLowerCase().trim());
//        input.add("searching for KnockKnock".toLowerCase().trim());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
//        input.add("command line arguments-schtasks".toLowerCase().trim()); 
//        
//        
//        input.add("command line arguments-reg".toLowerCase().trim());
//        input.add("command line arguments-at".toLowerCase().trim());
//        input.add("command line arguments-schtasks /create /tn mysc /tr c".toLowerCase().trim());
//        input.add("file search activity".toLowerCase().trim());  
//        
//        
//        input.add("trusted file modification".toLowerCase().trim());
//        input.add("dport = smb".toLowerCase().trim());
//        input.add("dport = rpcs(135)".toLowerCase().trim());
//        input.add("binary file execution".toLowerCase().trim());
//        
//        input.add("process modification".toLowerCase().trim());
//        input.add("file modification in c-file modification in /bin directory".toLowerCase().trim());
//        
        ArrayList<String> ttpCandidateList = generator.getttpCandidateList(input);
        System.out.println("TTP Candidate List: " + ttpCandidateList);
        
        generator.generateHypothesis(hypothesisSet, hypothesis, input, ttpCandidateList, input);
        System.out.println("Generated Hypothesis: " + hypothesisSet);
        
        for(ArrayList<String> hList : hypothesisSet) {
            Double fd = generator.calculateFidality(hList, input);
            System.out.println("Hypothesis: " + hList + " Fidelity: " + fd);
        }
        
    }*/
    
}

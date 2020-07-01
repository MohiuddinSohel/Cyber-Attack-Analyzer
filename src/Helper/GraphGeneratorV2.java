/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import AttackTree.AttackGraphV2;
import AttackTree.TechniqueEdgeV2;
import AttackTree.TechniqueNodeV2;
import static Helper.CSVProcessor.writeToFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author mahmed27
 */
public class GraphGeneratorV2 {
    public static void main(String[] args) throws IOException {
//        GraphGeneratorV2.GraphGenerator1(56);
//        GraphGeneratorV2.calculatePosteriorProbability();
//        LinkedHashMap<String, Boolean> eList = GraphGenerator.generateAllEdges(100);
//        AttackGraphV2 g = GraphGenerator.generateGraph(eList);
//        ArrayList<String> iAList = GraphGenerator.initialAccessPoint(g);
//        ArrayList<String> aGList = GraphGenerator.achieveableGoalList(g);
//        HashMap<String, Double> posteriorP = GraphGenerator.setPosteriorProbability(g, aGList);
//        HashMap<String, Double> priorP = GraphGenerator.setPriorProbability(g);
//        g.setPriorProbability(priorP);
//        g.setPosteriorProbability(posteriorP);
        
    }
    
    public static void calculatePosteriorProbability() throws FileNotFoundException, IOException  {
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String lineToWrite = "";
        LinkedHashMap<String, Integer> postProbList = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> goalList = new LinkedHashMap<>();
        for(File file : files) {
            System.out.println("FilePath: " + file.getName());
            BufferedReader in = new BufferedReader(new FileReader(file));
            
            boolean skipFirstLine = false;
            String line = null;
            LinkedHashMap<String, Integer> techniqueList = new LinkedHashMap<>();
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().toLowerCase().split(",");
                if(mColumn[4].trim().contains("pre-")) continue;
                else if(!(mColumn[3].contains("exfiltration") || mColumn[3].contains("command-and-control"))) {
                    if(!techniqueList.containsKey(mColumn[4].trim())) {
                        techniqueList.put(mColumn[4].trim(), 1);
                    } else {
                        techniqueList.put(mColumn[4].trim(), techniqueList.get(mColumn[4].trim()) + 1);
                    }
                }
                else {
                    for(String key : techniqueList.keySet()) { 
                        if(key.contains(mColumn[4].trim())) continue;
                        postProbList.put(key + "/" + mColumn[4].trim(),  techniqueList.get(key));
                    }
                    if(!goalList.containsKey(mColumn[4].trim())) {
                        goalList.put(mColumn[4].trim(), 1);
                    } else {
                        goalList.put(mColumn[4].trim(), goalList.get(mColumn[4].trim()) + 1);
                    }
                }
            }
        }
        
        
        for(String key : postProbList.keySet()) {
            String[] tt = key.split("/");
            double d = (double)postProbList.get(key) / (double)goalList.get(tt[1]);
            d = (d > 1.0)? 1.0 : d;
            
            lineToWrite += key + " " + d + "\n";
            
            System.out.println("P(" + key + ") = " + d);
        }
        writeToFile("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/posteriorProbability.txt", lineToWrite);
    }
    
    public static LinkedHashMap<String, Double> collectPosteriorProbability() throws FileNotFoundException, IOException {
        LinkedHashMap<String, Double> p = new LinkedHashMap<>();
        BufferedReader in = new BufferedReader(new FileReader(new File("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/posteriorProbability.txt")));
        String line = null;
        while((line = in.readLine()) != null) {
            if(line.trim().isEmpty()) continue;
            String lines[] = line.split(" ");
            p.put(lines[0], Double.parseDouble(lines[1]));
        }
        return p;
    }
    
    public static void GraphGenerator1(int numberOfAttack) throws FileNotFoundException, IOException {
        String color = "\'255,1,255\'";
        

        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, String> eList = new LinkedHashMap<>();
        int attackNumber = 1;
        
        for(File file : files) {
            System.out.println("FilePath: " + file.getName());
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;

            ArrayList<String> techniqueListS = new ArrayList<>();
            ArrayList<String> techniqueListE = new ArrayList<>();

            String currentTactic = "";
            String nextTactic = "";
        
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(mColumn[4].trim().toLowerCase().contains("pre-")) continue;
                if(currentTactic.isEmpty() || currentTactic.contains(mColumn[3].toLowerCase().trim())) {
                    currentTactic = mColumn[3].toLowerCase().trim();
                    techniqueListS.add(mColumn[4].toLowerCase().trim());
                } else if(nextTactic.isEmpty() || nextTactic.contains(mColumn[3].toLowerCase().trim())) {
                    nextTactic = mColumn[3].toLowerCase().trim();
                    techniqueListE.add(mColumn[4].toLowerCase().trim());
                } else if(!nextTactic.contains(mColumn[3].toLowerCase().trim())) {


                    for(int i = 0; i < techniqueListS.size(); i++) {
                        int k = 0;
                        
                        if(outDegreeEdgeExceedsLimits(techniqueListS.get(i), 5, eList)) continue;
                        
                        for(int j = 0; j < techniqueListE.size(); j++) {
                            if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
                            if(k >= 1) break;
                            if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                    && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                                k++;
                                eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), currentTactic + "/" + nextTactic);
                            }

                        }
                    }

                    currentTactic = nextTactic;
                    nextTactic = mColumn[3].toLowerCase().trim();
                    techniqueListS.clear();
                    techniqueListS.addAll(techniqueListE);

                    techniqueListE.clear();
                    techniqueListE.add(mColumn[4].toLowerCase().trim());
                }
            }
            
            for(int i = 0; i < techniqueListS.size(); i++) {
                int k = 0;
                
                if(outDegreeEdgeExceedsLimits(techniqueListS.get(i), 5, eList)) continue;
                
                for(int j = 0; j < techniqueListE.size(); j++) {
                    if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
                    if(k == 2) break;
                    if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                            && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                        k++;
                        eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), currentTactic + "/" + nextTactic);
                    }

                }
            }
            techniqueListS.clear();
            techniqueListE.clear();
            
            in.close();
            attackNumber++;
            if(attackNumber > numberOfAttack) break;
        }
        
        for(String k : eList.keySet()) {
            lineToWrite += k.split("/")[0] + "," + k.split("/")[1] + ",true," + color + "\n";
        }
        writeToFile("/Users/mahmed27/Google Drive/Slide to merge/gephiEdgewithdegreelimit5.gdf", lineToWrite);
        eList.clear();
    }
    public static LinkedHashMap<String, String> generateAllEdgesOld(int numberOfAttack) throws IOException {
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, String> eList = new LinkedHashMap<>();
        int attackNumber = 1;
        
        for(File file : files) {
            System.out.println("FilePath: " + file.getName());
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;

            ArrayList<String> techniqueListS = new ArrayList<>();
            ArrayList<String> techniqueListE = new ArrayList<>();

            String currentTactic = "";
            String nextTactic = "";
        
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(mColumn[4].trim().toLowerCase().contains("pre-")) continue;
                if(currentTactic.isEmpty() || currentTactic.contains(mColumn[3].toLowerCase().trim())) {
                    currentTactic = mColumn[3].toLowerCase().trim();
                    techniqueListS.add(mColumn[4].toLowerCase().trim());
                } else if(nextTactic.isEmpty() || nextTactic.contains(mColumn[3].toLowerCase().trim())) {
                    nextTactic = mColumn[3].toLowerCase().trim();
                    techniqueListE.add(mColumn[4].toLowerCase().trim());
                } else if(!nextTactic.contains(mColumn[3].toLowerCase().trim())) {


                    for(int i = 0; i < techniqueListS.size(); i++) {
                        int k = 0;
                        
                        if(outDegreeEdgeExceedsLimits(techniqueListS.get(i), 5, eList)) continue;
                        
                        for(int j = 0; j < techniqueListE.size(); j++) {
                            if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
                            if(k >= 1) break;
                            if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                    && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                                k++;
                                eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), currentTactic + "/" + nextTactic);
                            }

                        }
                    }

                    currentTactic = nextTactic;
                    nextTactic = mColumn[3].toLowerCase().trim();
                    techniqueListS.clear();
                    techniqueListS.addAll(techniqueListE);

                    techniqueListE.clear();
                    techniqueListE.add(mColumn[4].toLowerCase().trim());
                }
            }
            
            for(int i = 0; i < techniqueListS.size(); i++) {
                int k = 0;
                
                if(outDegreeEdgeExceedsLimits(techniqueListS.get(i), 5, eList)) continue;
                
                for(int j = 0; j < techniqueListE.size(); j++) {
                    if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
                    if(k == 2) break;
                    if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                            && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                        k++;
                        eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), currentTactic + "/" + nextTactic);
                    }

                }
            }
            techniqueListS.clear();
            techniqueListE.clear();
            
            in.close();
            attackNumber++;
            if(attackNumber > numberOfAttack) break;
        }
        return eList;
    }
    
    public static LinkedHashMap<String, String> generateAllEdges(int numberOfAttack) throws IOException {
        Double corThreshold = 0.25;
        LinkedHashMap<String, Double> correlation = parseCorrelation("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/CorrelationamongFeaturesPearson.csv");
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, String> eList = new LinkedHashMap<>();
        int attackNumber = 1;
        
        for(File file : files) {
//            System.out.println("FilePath: " + file.getName());
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;

            ArrayList<String> techniqueListS = new ArrayList<>();
            ArrayList<String> techniqueListE = new ArrayList<>();

            String currentTactic = "";
            String nextTactic = "";
        
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(mColumn[4].trim().toLowerCase().contains("pre-")) continue;
                if(currentTactic.isEmpty() || currentTactic.contains(mColumn[3].toLowerCase().trim())) {
                    currentTactic = mColumn[3].toLowerCase().trim();
                    techniqueListS.add(mColumn[4].toLowerCase().trim());
                } else if(nextTactic.isEmpty() || nextTactic.contains(mColumn[3].toLowerCase().trim())) {
                    nextTactic = mColumn[3].toLowerCase().trim();
                    techniqueListE.add(mColumn[4].toLowerCase().trim());
                } else if(!nextTactic.contains(mColumn[3].toLowerCase().trim())) {


                    for(int i = 0; i < techniqueListS.size(); i++) {
                        int k = 0;
                        
//                        if(outDegreeEdgeExceedsLimits(techniqueListS.get(i), 5, eList)) continue;
                        
                        for(int j = 0; j < techniqueListE.size(); j++) {
                            if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
//                            if(k >= 1) break;
                            if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                    && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                                if(correlation.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j))
                                        && correlation.get(techniqueListS.get(i) + "/" + techniqueListE.get(j)) >= corThreshold ) {
                                    k++;
                                    eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), currentTactic + "/" + nextTactic);
                                }
                            }

                        }
                    }

                    currentTactic = nextTactic;
                    nextTactic = mColumn[3].toLowerCase().trim();
                    techniqueListS.clear();
                    techniqueListS.addAll(techniqueListE);

                    techniqueListE.clear();
                    techniqueListE.add(mColumn[4].toLowerCase().trim());
                }
            }
            
            for(int i = 0; i < techniqueListS.size(); i++) {
                int k = 0;
                
//                if(outDegreeEdgeExceedsLimits(techniqueListS.get(i), 5, eList)) continue;
                
                for(int j = 0; j < techniqueListE.size(); j++) {
                    if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
//                    if(k == 2) break;
                    if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                            && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                        if(correlation.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                && correlation.get(techniqueListS.get(i) + "/" + techniqueListE.get(j)) >= corThreshold ) {
                            k++;
                            eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), currentTactic + "/" + nextTactic);
                        }
                    }

                }
            }
            techniqueListS.clear();
            techniqueListE.clear();
            
            in.close();
            attackNumber++;
            if(attackNumber > numberOfAttack) break;
        }
        return eList;
    }
    
    public static Boolean outDegreeEdgeExceedsLimits(String keyWord, int limit, LinkedHashMap<String, String> eList) {
        int i = 0;
        for(String key: eList.keySet()) {
            if(key.contains(keyWord)) i++;
            if(i >= 5) return true;
        }
        return false;
    }
    
    public static LinkedHashMap<String, String> generateAllEdges1(int numberOfAttack) throws IOException {
        LinkedHashMap<String, String> eList = new LinkedHashMap<>();
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        int attackNumber = 1;
        for(File file : files) {
//            System.out.println("FilePath: " + file.getName());
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;
            ArrayList<String> tac = new ArrayList<>();
            ArrayList<String> nTtac = new ArrayList<>();
            String sTac = "asdf";
            String eTac = "";
            
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(mColumn[4].trim().toLowerCase().contains("pre-")) continue;
                if(tac.size() == 0 || mColumn[3].toLowerCase().trim().contains(sTac)) {
                    sTac = mColumn[3].toLowerCase().trim();
                    tac.add(mColumn[4].toLowerCase().trim());
                } else {
                    if(eTac.isEmpty() || eTac.contentEquals(mColumn[3].toLowerCase().trim())) {
                        eTac = mColumn[3].toLowerCase().trim();
                    }
                    else if(!eTac.contentEquals(mColumn[3].toLowerCase().trim())) {
                        sTac = eTac;
                        tac.clear();
                        tac.addAll(nTtac);
                        nTtac.clear();
                        eTac = mColumn[3].toLowerCase().trim();
                    }
                    for(int i = 0; i < tac.size(); i++) {
                        if(tac.get(i).contentEquals(mColumn[4].toLowerCase().trim())) continue;
                        if(!eList.containsKey(tac.get(i) + "/" + mColumn[4].toLowerCase().trim()) && !eList.containsKey(mColumn[4].toLowerCase().trim() + "/" + tac.get(i))) {
                            eList.put(tac.get(i) + "/" + mColumn[4].toLowerCase().trim(), sTac + "/" + eTac);
                        }
                    }
                    nTtac.add(mColumn[4].toLowerCase().trim());
                }
            }
            in.close();
            attackNumber++;
            if(attackNumber > numberOfAttack) break;
        }
        return eList;
    }
    
    public static AttackGraphV2 generateGraph(LinkedHashMap<String, String> eList) {
        
        ArrayList<TechniqueEdgeV2> edgeList = new ArrayList();
        ArrayList<TechniqueNodeV2> nodeList = new ArrayList();
        LinkedHashMap<String, ArrayList<TechniqueNodeV2>> adjacencyList = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<TechniqueNodeV2>> reverseAdjacencyList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> posteriorProbability = new LinkedHashMap<>();
        LinkedHashMap<String, Double> priorProbability = new LinkedHashMap<>();

        for(String key: eList.keySet()) {
            String[] tmpValue = eList.get(key).split("/");
            String[] tmpKey = key.split("/");
            if(tmpKey[0].contentEquals(tmpKey[1]) && tmpValue[0].contentEquals(tmpValue[1])) continue;
            TechniqueNodeV2 sNode = GraphGeneratorV2.getNode(tmpKey[0],tmpValue[0], nodeList);
            TechniqueNodeV2 eNode = GraphGeneratorV2.getNode(tmpKey[1],tmpValue[1], nodeList);
            TechniqueEdgeV2 edge = new TechniqueEdgeV2(sNode, eNode, 0.33);
            edgeList.add(edge);

            if(!adjacencyList.containsKey(tmpKey[0])) {
                adjacencyList.put(tmpKey[0], new ArrayList<>());
            }
            if(!reverseAdjacencyList.containsKey(tmpKey[1])) {
                
                reverseAdjacencyList.put(tmpKey[1], new ArrayList<>());
            }
            adjacencyList.get(tmpKey[0]).add(eNode);
            reverseAdjacencyList.get(tmpKey[1]).add(sNode);
        }

        return new AttackGraphV2(nodeList, edgeList, adjacencyList, reverseAdjacencyList);
    }
    

    public static TechniqueNodeV2 getNode(String techniqueId, String tactic, ArrayList<TechniqueNodeV2> nodeList) {
        for(int i= 0; i < nodeList.size(); i++) {
            if(nodeList.get(i).getTechniqueId().contentEquals(techniqueId.trim().toLowerCase()) 
                    && nodeList.get(i).getTactic().contentEquals(tactic.toLowerCase().trim())) {
                return nodeList.get(i);
            }
        }
        TechniqueNodeV2 node = new TechniqueNodeV2(techniqueId.trim().toLowerCase(), tactic);
        nodeList.add(node);
        return node;
    }
    
    public static ArrayList<String> initialAccessPoint(AttackGraphV2 graph) {
        ArrayList<String> iAList = new ArrayList<String>();
        iAList.add("t1189");
        GraphGeneratorV2.getNode("t1189", "initial-access", graph.getNodeList());
        iAList.add("t1190");
        GraphGeneratorV2.getNode("t1190", "initial-access", graph.getNodeList());
        iAList.add("t1200");
        GraphGeneratorV2.getNode("t1200", "initial-access", graph.getNodeList());
        iAList.add("t1193");
        GraphGeneratorV2.getNode("t1193", "initial-access", graph.getNodeList());
        iAList.add("t1192");
        GraphGeneratorV2.getNode("t1192", "initial-access", graph.getNodeList());
        iAList.add("t1194");
        GraphGeneratorV2.getNode("t1194", "initial-access", graph.getNodeList());
        iAList.add("t1195");
        GraphGeneratorV2.getNode("t1195", "initial-access", graph.getNodeList());
        iAList.add("t1199");
        GraphGeneratorV2.getNode("t1199", "initial-access", graph.getNodeList());
        iAList.add("t1078");
        GraphGeneratorV2.getNode("t1078", "initial-access", graph.getNodeList());
        return iAList;
    }
    
    public static ArrayList<String> achieveableGoalList(AttackGraphV2 graph) {
        ArrayList<String> aGList = new ArrayList<>();
        aGList.add("t1020");
        GraphGeneratorV2.getNode("t1020", "exfiltration", graph.getNodeList());
        aGList.add("t1002");
        GraphGeneratorV2.getNode("t1002", "exfiltration", graph.getNodeList());
        aGList.add("t1022");
        GraphGeneratorV2.getNode("t1022", "exfiltration", graph.getNodeList());
        aGList.add("t1030");
        GraphGeneratorV2.getNode("t1030", "exfiltration", graph.getNodeList());
        aGList.add("t1048");
        GraphGeneratorV2.getNode("t1048", "exfiltration", graph.getNodeList());
        aGList.add("t1041");
        GraphGeneratorV2.getNode("t1041", "exfiltration", graph.getNodeList());
        aGList.add("t1011");
        GraphGeneratorV2.getNode("t1011", "exfiltration", graph.getNodeList());
        aGList.add("t1052");
        GraphGeneratorV2.getNode("t1052", "exfiltration", graph.getNodeList());
        aGList.add("t1029");
        GraphGeneratorV2.getNode("t1029", "exfiltration", graph.getNodeList());
        
        aGList.add("t1043");
        GraphGeneratorV2.getNode("t1043", "command-and-control", graph.getNodeList());
        aGList.add("t1092");
        GraphGeneratorV2.getNode("t1092", "command-and-control", graph.getNodeList());
        aGList.add("t1090");
        GraphGeneratorV2.getNode("t1090", "command-and-control", graph.getNodeList());
        aGList.add("t1094");
        GraphGeneratorV2.getNode("t1094", "command-and-control", graph.getNodeList());
        aGList.add("t1024");
        GraphGeneratorV2.getNode("t1024", "command-and-control", graph.getNodeList());
        aGList.add("t1132");
        GraphGeneratorV2.getNode("t1132", "command-and-control", graph.getNodeList());
        aGList.add("t1001");
        GraphGeneratorV2.getNode("t1001", "command-and-control", graph.getNodeList());
        aGList.add("t1172");
        GraphGeneratorV2.getNode("t1172", "command-and-control", graph.getNodeList());
        aGList.add("t1008");
        GraphGeneratorV2.getNode("t1008", "command-and-control", graph.getNodeList());
        aGList.add("t1104");
        GraphGeneratorV2.getNode("t1104", "command-and-control", graph.getNodeList());
        aGList.add("t1188");
        GraphGeneratorV2.getNode("t1188", "command-and-control", graph.getNodeList());
        aGList.add("t1026");
        GraphGeneratorV2.getNode("t1026", "command-and-control", graph.getNodeList());
        aGList.add("t1079");
        GraphGeneratorV2.getNode("t1079", "command-and-control", graph.getNodeList());
        aGList.add("t1205");
        GraphGeneratorV2.getNode("t1205", "command-and-control", graph.getNodeList());
        aGList.add("t1219");
        GraphGeneratorV2.getNode("t1219", "command-and-control", graph.getNodeList());
        aGList.add("t1105");
        GraphGeneratorV2.getNode("t1105", "command-and-control", graph.getNodeList());
        aGList.add("t1071");
        GraphGeneratorV2.getNode("t1071", "command-and-control", graph.getNodeList());
        aGList.add("t1032");
        GraphGeneratorV2.getNode("t1032", "command-and-control", graph.getNodeList());
        aGList.add("t1095");
        GraphGeneratorV2.getNode("t1095", "command-and-control", graph.getNodeList());
        aGList.add("t1065");
        GraphGeneratorV2.getNode("t1065", "command-and-control", graph.getNodeList());
        aGList.add("t1102");
        GraphGeneratorV2.getNode("t1102", "command-and-control", graph.getNodeList());
        return aGList;
    }
    
    public static LinkedHashMap<String, Double> setPriorProbability(AttackGraphV2 graph) {
        LinkedHashMap<String, Double> priorProbability = new LinkedHashMap<>();
        for(TechniqueNodeV2 node : graph.getNodeList()) {
            priorProbability.put(node.getTechniqueId(), 0.1);
        }
        return priorProbability;
    }
    
    public static LinkedHashMap<String, Double> setPosteriorProbability(AttackGraphV2 graph, ArrayList<String> aGList) throws IOException {
        LinkedHashMap<String, Double> posteriorProbability = new LinkedHashMap<>();
        LinkedHashMap<String, Double>  postProb = GraphGeneratorV2.collectPosteriorProbability();
        for(String goalId : aGList) {
            for(TechniqueNodeV2 node : graph.getNodeList()) {
                if(GraphGeneratorV2.isReachable(graph, node, goalId)) {
//                    posteriorProbability.put(node.getTechniqueId() + "|" + goalId, 0.35);
                    Double d = 0.0;//0.00011;
                    if(postProb.containsKey(node.getTechniqueId() + "/" + goalId)) {
                         d = postProb.get(node.getTechniqueId() + "/" + goalId);
                    }
                    posteriorProbability.put(node.getTechniqueId() + "|" + goalId, d);
                }
            }
        }
        return posteriorProbability;
    }
    
    public static boolean isReachable(AttackGraphV2 graph, TechniqueNodeV2 sNode, String dId) {
        if(sNode.getTechniqueId().contentEquals(dId)) {
            return true;
        }
        ArrayList<String> visited =  new ArrayList<>();
        LinkedList<String> queue = new LinkedList<>();
        visited.add(sNode.getTechniqueId());
        queue.add(sNode.getTechniqueId());
        
        while(!queue.isEmpty()) {
            String current = queue.poll();
            ArrayList<TechniqueNodeV2> adjList = graph.getAdjacencyList().get(current);
            if(adjList == null || adjList.isEmpty()) continue;
            for(TechniqueNodeV2 n : adjList) {
                if(n.getTechniqueId().contentEquals(dId)) {
                    return true;
                }
                if(!visited.contains(n.getTechniqueId())) {
                    visited.add(n.getTechniqueId());
                    queue.add(n.getTechniqueId());
                }
            }
        }
        return false;
    }
    
    public static LinkedHashMap<String, Double> parseCorrelation(String filePath) throws FileNotFoundException, IOException {
         LinkedHashMap<String, Double> correlation = new LinkedHashMap<>();
         BufferedReader in = new BufferedReader(new FileReader(new File(filePath)));
         String line = "";
         boolean skipFirstLine = false;
         String[] header = null;
         while((line = in.readLine()) != null) {
             line = line.replaceAll("\"", "");
            if(!skipFirstLine ) {
                header = line.toLowerCase().trim().split(",");
                skipFirstLine = true;
                continue;
            }
            if(line.trim().toLowerCase().contains("pre")) continue;
            
            String[] mColumn = line.toLowerCase().trim().split(",");
            for(int i = 1; i < mColumn.length; i++ ) {
                if(header[i].contains("pre") || mColumn[i].contains("na")) continue;
                correlation.put(mColumn[0] + "/" + header[i], Double.valueOf(mColumn[i]));
            }
            if(mColumn[4].trim().toLowerCase().contains("pre")) continue;
             
         }
         return correlation;
     }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import AttackTree.AttackGraph;
import AttackTree.TechniqueEdge;
import AttackTree.TechniqueNode;
import static Helper.CSVProcessor.writeToFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *
 * @author mahmed27
 */
public class GraphGenerator {
    public static void main(String[] args) throws IOException {
        HashMap<String, Double> correlation = parseCorrelation("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/CorrelationamongFeaturesPearson.csv");
        GraphGenerator.collectAlledges2(60, correlation);
//        LinkedHashMap<String, Boolean> eList = GraphGenerator.generateAllEdges(100);
//        AttackGraph g = GraphGenerator.generateGraph(eList);
//        ArrayList<String> iAList = GraphGenerator.initialAccessPoint(g);
//        ArrayList<String> aGList = GraphGenerator.achieveableGoalList(g);
//        HashMap<String, Double> posteriorP = GraphGenerator.setPosteriorProbability(g, aGList);
//        HashMap<String, Double> priorP = GraphGenerator.setPriorProbability(g);
//        g.setPriorProbability(priorP);
//        g.setPosteriorProbability(posteriorP);
        
    }
    

    
    public static void collectAlledges2(int numberOfAttack, HashMap<String, Double> correlation) throws FileNotFoundException, IOException {
        
        Double corThreshold = 0.5;
        
        String color = "\'255,1,255\'";
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, Boolean> eList = new LinkedHashMap<>();
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
                        for(int j = 0; j < techniqueListE.size(); j++) {
                            if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
//                            if(k >= 3) break;
                            if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                    && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                                if(correlation.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j))
                                        && correlation.get(techniqueListS.get(i) + "/" + techniqueListE.get(j)) >= corThreshold ) {
                                    k++;
                                    eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), true);
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
                for(int j = 0; j < techniqueListE.size(); j++) {
                    if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
//                    if(k == 2) break;
                    if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                            && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                        if(correlation.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j))
                                && correlation.get(techniqueListS.get(i) + "/" + techniqueListE.get(j)) >= corThreshold ) {
                            k++;
                            eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), true);
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
        
        for(String k : eList.keySet()) {
            lineToWrite += k.split("/")[0] + "," + k.split("/")[1] + ",true," + color + "\n";
        }
        writeToFile("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/gephiEdgeByStagepearson2.gdf", lineToWrite);
        eList.clear();
        
    }
    
    public static void GraphGenerator1(int numberOfAttack) throws FileNotFoundException, IOException {
        String color = "\'255,1,255\'";
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, Boolean> eList = new LinkedHashMap<>();
        int attackNumber = 0;
        
        for(File file : files) {
            System.out.println("FilePath: " + file.getName());
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
                            eList.put(tac.get(i) + "/" + mColumn[4].toLowerCase().trim(), true);
                        }
                    }
                    nTtac.add(mColumn[4].toLowerCase().trim());
                }
            }
            in.close();
            attackNumber++;
            if(attackNumber > numberOfAttack) break;
        }
        
        for(String k : eList.keySet()) {
            lineToWrite += k.split("/")[0] + "," + k.split("/")[1] + ",true," + color + "\n";
        }
        writeToFile("/Users/mahmed27/Google Drive/Slide to merge/gephiEdgeByStageWithoutLoop5.gdf", lineToWrite);
        eList.clear();
    }
    
    public static LinkedHashMap<String, Boolean> generateAllEdgesOld(int numberOfAttack) throws IOException {
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, Boolean> eList = new LinkedHashMap<>();
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
                        for(int j = 0; j < techniqueListE.size(); j++) {
                            if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
                            if(k >= 1) break;
                            if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                    && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                                k++;
                                eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), true);
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
                for(int j = 0; j < techniqueListE.size(); j++) {
                    if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
                    if(k == 2) break;
                    if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                            && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                        k++;
                        eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), true);
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
    
    public static LinkedHashMap<String, Boolean> generateAllEdges(int numberOfAttack) throws IOException {
        Double corThreshold = 0.3;
        HashMap<String, Double> correlation = parseCorrelation("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/CorrelationamongFeaturesPearson.csv");
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        LinkedHashMap<String, Boolean> eList = new LinkedHashMap<>();
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
                        for(int j = 0; j < techniqueListE.size(); j++) {
                            if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
//                            if(k >= 1) break;
                            if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                                    && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                                if(correlation.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j))
                                        && correlation.get(techniqueListS.get(i) + "/" + techniqueListE.get(j)) >= corThreshold ) {
                                    k++;
                                    eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), true);
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
                for(int j = 0; j < techniqueListE.size(); j++) {
                    if(techniqueListS.get(i).contains(techniqueListE.get(j))) continue;
//                    if(k == 2) break;
                    if(!eList.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j)) 
                            && !eList.containsKey(techniqueListE.get(j) + "/" + techniqueListS.get(i))) {
                        if(correlation.containsKey(techniqueListS.get(i) + "/" + techniqueListE.get(j))
                                && correlation.get(techniqueListS.get(i) + "/" + techniqueListE.get(j)) >= corThreshold ) {
                            k++;
                            eList.put(techniqueListS.get(i) + "/" + techniqueListE.get(j), true);
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
    public static LinkedHashMap<String, Boolean> generateAllEdges1(int numberOfAttack) throws IOException {
        LinkedHashMap<String, Boolean> eList = new LinkedHashMap<>();
        
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
                            eList.put(tac.get(i) + "/" + mColumn[4].toLowerCase().trim(), true);
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
    
    public static AttackGraph generateGraph(LinkedHashMap<String, Boolean> eList) {
        
        ArrayList<TechniqueEdge> edgeList = new ArrayList();
        ArrayList<TechniqueNode> nodeList = new ArrayList();
        HashMap<String, ArrayList<TechniqueNode>> adjacencyList = new HashMap<String, ArrayList<TechniqueNode>>();
        HashMap<String, ArrayList<TechniqueNode>> reverseAdjacencyList = new HashMap<String, ArrayList<TechniqueNode>>();
        HashMap<String, Double> posteriorProbability = new HashMap<String, Double>();
        HashMap<String, Double> priorProbability = new HashMap<String, Double>();

        for(String key: eList.keySet()) {
            String[] tmpKey = key.split("/");
            TechniqueNode sNode = GraphGenerator.getNode(tmpKey[0], nodeList);
            TechniqueNode eNode = GraphGenerator.getNode(tmpKey[1], nodeList);
            TechniqueEdge edge = new TechniqueEdge(sNode, eNode, 0.33);
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

        return new AttackGraph(nodeList, edgeList, adjacencyList, reverseAdjacencyList);
    }
    

    public static TechniqueNode getNode(String techniqueId, ArrayList<TechniqueNode> nodeList) {
        for(int i= 0; i < nodeList.size(); i++) {
            if(nodeList.get(i).getTechniqueId().contentEquals(techniqueId.trim().toLowerCase())) {
                return nodeList.get(i);
            }
        }
        TechniqueNode node = new TechniqueNode(techniqueId.trim().toLowerCase());
        nodeList.add(node);
        return node;
    }
    
    public static ArrayList<String> initialAccessPoint(AttackGraph graph) {
        ArrayList<String> iAList = new ArrayList<String>();
        iAList.add("t1189");
        GraphGenerator.getNode("t1189", graph.getNodeList());
        iAList.add("t1190");
        GraphGenerator.getNode("t1190", graph.getNodeList());
        iAList.add("t1200");
        GraphGenerator.getNode("t1200", graph.getNodeList());
        iAList.add("t1193");
        GraphGenerator.getNode("t1193", graph.getNodeList());
        iAList.add("t1192");
        GraphGenerator.getNode("t1192", graph.getNodeList());
        iAList.add("t1194");
        GraphGenerator.getNode("t1194", graph.getNodeList());
        iAList.add("t1195");
        GraphGenerator.getNode("t1195", graph.getNodeList());
        iAList.add("t1199");
        GraphGenerator.getNode("t1199", graph.getNodeList());
        iAList.add("t1078");
        GraphGenerator.getNode("t1078", graph.getNodeList());
        return iAList;
    }
    
    public static ArrayList<String> achieveableGoalList(AttackGraph graph) {
        ArrayList<String> aGList = new ArrayList<>();
        aGList.add("t1020");
        GraphGenerator.getNode("t1020", graph.getNodeList());
        aGList.add("t1002");
        GraphGenerator.getNode("t1002", graph.getNodeList());
        aGList.add("t1022");
        GraphGenerator.getNode("t1022", graph.getNodeList());
        aGList.add("t1030");
        GraphGenerator.getNode("t1030", graph.getNodeList());
        aGList.add("t1048");
        GraphGenerator.getNode("t1048", graph.getNodeList());
        aGList.add("t1041");
        GraphGenerator.getNode("t1041", graph.getNodeList());
        aGList.add("t1011");
        GraphGenerator.getNode("t1011", graph.getNodeList());
        aGList.add("t1052");
        GraphGenerator.getNode("t1052", graph.getNodeList());
        aGList.add("t1029");
        GraphGenerator.getNode("t1029", graph.getNodeList());
        
        aGList.add("t1043");
        GraphGenerator.getNode("t1043", graph.getNodeList());
        aGList.add("t1092");
        GraphGenerator.getNode("t1092", graph.getNodeList());
        aGList.add("t1090");
        GraphGenerator.getNode("t1090", graph.getNodeList());
        aGList.add("t1094");
        GraphGenerator.getNode("t1094", graph.getNodeList());
        aGList.add("t1024");
        GraphGenerator.getNode("t1024", graph.getNodeList());
        aGList.add("t1132");
        GraphGenerator.getNode("t1132", graph.getNodeList());
        aGList.add("t1001");
        GraphGenerator.getNode("t1001", graph.getNodeList());
        aGList.add("t1172");
        GraphGenerator.getNode("t1172", graph.getNodeList());
        aGList.add("t1008");
        GraphGenerator.getNode("t1008", graph.getNodeList());
        aGList.add("t1104");
        GraphGenerator.getNode("t1104", graph.getNodeList());
        aGList.add("t1188");
        GraphGenerator.getNode("t1188", graph.getNodeList());
        aGList.add("t1026");
        GraphGenerator.getNode("t1026", graph.getNodeList());
        aGList.add("t1079");
        GraphGenerator.getNode("t1079", graph.getNodeList());
        aGList.add("t1205");
        GraphGenerator.getNode("t1205", graph.getNodeList());
        aGList.add("t1219");
        GraphGenerator.getNode("t1219", graph.getNodeList());
        aGList.add("t1105");
        GraphGenerator.getNode("t1105", graph.getNodeList());
        aGList.add("t1071");
        GraphGenerator.getNode("t1071", graph.getNodeList());
        aGList.add("t1032");
        GraphGenerator.getNode("t1032", graph.getNodeList());
        aGList.add("t1095");
        GraphGenerator.getNode("t1095", graph.getNodeList());
        aGList.add("t1065");
        GraphGenerator.getNode("t1065", graph.getNodeList());
        aGList.add("t1102");
        GraphGenerator.getNode("t1102", graph.getNodeList());
        return aGList;
    }
    
    public static HashMap<String, Double> setPriorProbability(AttackGraph graph) {
        HashMap<String, Double> priorProbability = new HashMap<>();
        for(TechniqueNode node : graph.getNodeList()) {
            priorProbability.put(node.getTechniqueId(), 0.1);
        }
        return priorProbability;
    }
    
    public static HashMap<String, Double> setPosteriorProbability(AttackGraph graph, ArrayList<String> aGList) {
        HashMap<String, Double> posteriorProbability = new HashMap<>();
        for(String goalId : aGList) {
            for(TechniqueNode node : graph.getNodeList()) {
                if(GraphGenerator.isReachable(graph, node, goalId)) {
                    posteriorProbability.put(node.getTechniqueId() + "|" + goalId, 0.35);
                }
            }
        }
        return posteriorProbability;
    }
    
    public static boolean isReachable(AttackGraph graph, TechniqueNode sNode, String dId) {
        if(sNode.getTechniqueId().contentEquals(dId)) {
            return true;
        }
        ArrayList<String> visited =  new ArrayList<>();
        LinkedList<String> queue = new LinkedList<>();
        visited.add(sNode.getTechniqueId());
        queue.add(sNode.getTechniqueId());
        
        while(!queue.isEmpty()) {
            String current = queue.poll();
            ArrayList<TechniqueNode> adjList = graph.getAdjacencyList().get(current);
            if(adjList == null || adjList.isEmpty()) continue;
            for(TechniqueNode n : adjList) {
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
    public static HashMap<String, Double> parseCorrelation(String filePath) throws FileNotFoundException, IOException {
         HashMap<String, Double> correlation = new HashMap<>();
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

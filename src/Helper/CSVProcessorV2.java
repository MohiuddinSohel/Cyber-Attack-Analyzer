/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import AttackTree.AttackGraphV2;
import AttackTree.TechniqueEdgeV2;
import AttackTree.TechniqueNodeV2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author mahmed27
 */
public class CSVProcessorV2 {
    public static HashMap<String, Integer> tacticTolevelMap = new HashMap<>();
    
    public static void createTacticLevel() {
        CSVProcessorV2.tacticTolevelMap.put("initial-access", 1);
        CSVProcessorV2.tacticTolevelMap.put("execution", 2);
        CSVProcessorV2.tacticTolevelMap.put("persistence", 3);
        CSVProcessorV2.tacticTolevelMap.put("privilege-escalation", 3);
        CSVProcessorV2.tacticTolevelMap.put("defense-evasion", 3);
        CSVProcessorV2.tacticTolevelMap.put("credential-access", 3);
        CSVProcessorV2.tacticTolevelMap.put("discovery", 4);
        CSVProcessorV2.tacticTolevelMap.put("lateral-movement", 5);
        CSVProcessorV2.tacticTolevelMap.put("collection", 5);
        CSVProcessorV2.tacticTolevelMap.put("exfiltration", 6);
        CSVProcessorV2.tacticTolevelMap.put("command-and-control", 6);
    } 
    public static void readCSVPriorProbability(AttackGraphV2 graph, String csvPath) throws FileNotFoundException, IOException {
        
        BufferedReader in = new BufferedReader(new FileReader(csvPath));
        String line;
        boolean skipFirstLine = false;
        while((line = in.readLine()) != null) {
            if(!skipFirstLine) {
                skipFirstLine = true;
                continue;
            }
//            System.out.println(line);
            String[] mColumn = line.trim().split(",");
            Double prior = Double.valueOf(mColumn[2].trim());
            if(prior > 1.0) {
                prior = 1.0;
            }
            graph.getPriorProbability().put(mColumn[0].trim().toLowerCase(), prior);
        }
        in.close();
    }
    
    public static TechniqueNodeV2 getNode(String techniqueId, String tactic, ArrayList<TechniqueNodeV2> nodeList, boolean createNode) {
        for(int i= 0; i < nodeList.size(); i++) {
            if(tactic == null || tactic.isEmpty()) {
                if(nodeList.get(i).getTechniqueId().contentEquals(techniqueId.trim().toLowerCase())) {
                    return nodeList.get(i);
                }
            }
            else if(nodeList.get(i).getTechniqueId().contentEquals(techniqueId.trim().toLowerCase()) 
                    && nodeList.get(i).getTactic().contentEquals(tactic.toLowerCase().trim())) {
                return nodeList.get(i);
            }
        }
        System.out.println("*****No node found****");
        return null;        
    }
    
    public static void generateAllPath(AttackGraphV2 graph, ArrayList<ArrayList<String>> pathList, String destinationId
            , ArrayList<String> initialAccessPoint, ArrayList<String> reportedTechniques) throws IOException {
        ArrayList<String> currentPath = new ArrayList();
        currentPath.add(destinationId);
//        String tactic = CSVProcessorV2.getNode(destinationId, "", graph.getNodeList(), false).getTactic();
        findAllPath(graph, pathList, currentPath, destinationId, "0", initialAccessPoint, reportedTechniques);
//        System.out.println("Destination Id: " + destinationId + " path Size: " + pathList.size());    
    }
    
    public static boolean isallNodeInListVisited(ArrayList<TechniqueNodeV2> adjacentList) {
        if(adjacentList == null || adjacentList.isEmpty()) return true;
        boolean ret = true;
        for(TechniqueNodeV2 node : adjacentList) {
            if(!node.isIsVisited()) {
                ret = false;
                break;
            }
        }
        return ret;
    }
    
    public static void findAllPath(AttackGraphV2 graph, ArrayList<ArrayList<String>> pathList
            , ArrayList<String> currentPath, String destinationId, String tactic, ArrayList<String> initialAccessPoint
            , ArrayList<String> reportedTechniques) throws IOException {
        
        HashMap<String, ArrayList<TechniqueNodeV2>> adjList = graph.getReverseAdjacencyList();
        
        TechniqueNodeV2 node = getNode(destinationId, tactic, graph.getNodeList(), false);
        node.setIsVisited(true);
        
        ArrayList<TechniqueNodeV2> adjacentList = adjList.get(destinationId);
        
        if(initialAccessPoint.contains(destinationId) || isallNodeInListVisited(adjacentList)) {
            
            node.setIsVisited(false);
            if(isThePathValid(currentPath, reportedTechniques)) {
                ArrayList<String> c = new ArrayList(currentPath);
                pathList.add(c);
//                System.out.println("new path added: " + c.size());
            }
            return;
        }
        
        

        for(int i = 0; i < adjacentList.size(); i++) {
            if(!adjacentList.get(i).isIsVisited() 
                    /*&& !(CSVProcessorV2.tacticTolevelMap.get(adjacentList.get(i).getTactic()) > CSVProcessorV2.tacticTolevelMap.get(tactic)) */
                    /*&& graph.getPosteriorProbability().containsKey(adjacentList.get(i).getTechniqueId() + "|" + currentPath.get(0))
                    && (graph.getPosteriorProbability().get(adjacentList.get(i).getTechniqueId() + "|" + currentPath.get(0)) > 0.1)*/) {
                
                currentPath.add(adjacentList.get(i).getTechniqueId());
                findAllPath(graph, pathList, currentPath, adjacentList.get(i).getTechniqueId(), adjacentList.get(i).getTactic()
                        , initialAccessPoint, reportedTechniques);
                currentPath.remove(adjacentList.get(i).getTechniqueId());
            }
        }
        node.setIsVisited(false);
    }
    
    public static boolean isThePathValid(ArrayList<String> currentPath, ArrayList<String> reportedTechniques) {
        if(currentPath.size() > 20) {
//            System.out.println("Invalid path");
            return false;
        }
        for(String technique : reportedTechniques) {
            if(currentPath.contains(technique)) {
                return true;
            }
        }
//        System.out.println("Invalid path");
        return false;
    }

    
    
    public static String readFile(AttackGraphV2 graph) throws FileNotFoundException {
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        HashMap<String, ArrayList<TechniqueNodeV2>> adjList = graph.getReverseAdjacencyList();
        String color = "\'255,1,255\'";
        for(String key : adjList.keySet()) {
            ArrayList<TechniqueNodeV2> list = adjList.get(key);
            for(int i = 0 ; i< list.size(); i++) {
                lineToWrite += key + "," + list.get(i).getTechniqueId() + ",true," + color + "\n";
            }
            
        }
        return lineToWrite;
    }
    public static void generateGephiFileByStage() throws FileNotFoundException, IOException {
        String color = "\'255,1,255\'";
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        for(File file : files) {
//            System.out.println("FilePath: " + file.getName());
            String lineToWrite = init;
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;
            ArrayList<String> tac = new ArrayList<>();
            ArrayList<String> nTtac = new ArrayList<>();
            String sTac = "asdf";
            String eTac = "";
            LinkedHashMap<String, Boolean> eList = new LinkedHashMap<>();
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
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
            for(String k : eList.keySet()) {
                lineToWrite += k.split("/")[0] + "," + k.split("/")[1] + ",true," + color + "\n";
            }
            writeToFile("/Users/mahmed27/Google Drive/Slide to merge/gephiEdgeByStage.gdf", lineToWrite);
            init = "";
            eList.clear();
        }
    }
    
    public static void generateGephiFileSequencial() throws FileNotFoundException, IOException {
        
        
        String color = "\'255,1,255\'";
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        for(File file : files) {
           
            String lineToWrite = init;
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;
            String st = "";
            String ed = "";
            LinkedHashMap<String, Boolean> hmap = new LinkedHashMap<>();
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(st.isEmpty()) {
                    st = mColumn[4].trim().toLowerCase();
                    continue;
                }
                else if(ed.isEmpty()) {
                    ed = mColumn[4].trim().toLowerCase();;

                } 
                if(!ed.isEmpty() && !st.contentEquals(ed) && !hmap.containsKey(st + "/"+ ed) && !hmap.containsKey(ed + "/"+ st)) {

                    hmap.put(st + "/"+ ed, true);
                    st = ed;
                    ed = "";
                }
                if(st.contentEquals(ed) || hmap.containsKey(st + "/"+ ed) || hmap.containsKey(ed + "/"+ st)) {
                    st = ed;
                    ed = "";
                }
                
            }
            in.close();
            for(String k : hmap.keySet()) {
                lineToWrite += k.split("/")[0] + "," + k.split("/")[1] + ",true," + color + "\n";
            }
            writeToFile("/Users/mahmed27/Google Drive/Slide to merge/gephiEdgeBySequence.gdf", lineToWrite);
            init = "";
        }
    }
        public static void generateGephiFilewithoutLoop() throws FileNotFoundException, IOException {
        
        
        String color = "\'255,1,255\'";
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        for(File file : files) {
           
            String lineToWrite = init;
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;
            String st = "";
            String ed = "";
            LinkedHashMap<String, Boolean> hmap = new LinkedHashMap<>();
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(mColumn[4].trim().toLowerCase().contains("pre-")) continue;
                if(st.isEmpty()) {
                    st = mColumn[4].trim().toLowerCase();
                    continue;
                }
                else if(ed.isEmpty()) {
                    ed = mColumn[4].trim().toLowerCase();;

                } 
                if(!ed.isEmpty() && !st.contentEquals(ed) && !hmap.containsKey(st + "/"+ ed) && !isItLoopEdge(hmap, ed)) {

                    hmap.put(st + "/"+ ed, true);
                    st = ed;
                    ed = "";
                }
                else if(st.contentEquals(ed) || hmap.containsKey(st + "/"+ ed) || isItLoopEdge(hmap, ed)) {
                    st = ed;
                    ed = "";
                }
                
            }
            in.close();
            for(String k : hmap.keySet()) {
                lineToWrite += k.split("/")[0] + "," + k.split("/")[1] + ",true," + color + "\n";
            }
            writeToFile("/Users/mahmed27/Google Drive/Slide to merge/gephiEdgeBywithoutLoopAndPreT.gdf", lineToWrite);
            init = "";
        }
    }
    
   public static boolean isItLoopEdge(LinkedHashMap<String, Boolean> hmap, String ed) {
       for(String h : hmap.keySet()) {
           if(h.split("/")[0].contentEquals(ed) || h.split("/")[1].contentEquals(ed)) return true;
       }
       return false;
   }
    
    
    public static String readFile1(AttackGraphV2 graph) throws FileNotFoundException {
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        HashMap<String, ArrayList<TechniqueNodeV2>> adjList = graph.getAdjacencyList();
        String color = "\'255,1,255\'";
        for(String key : adjList.keySet()) {
            ArrayList<TechniqueNodeV2> list = adjList.get(key);
            for(int i = 0 ; i< list.size(); i++) {
                lineToWrite += key + "," + list.get(i).getTechniqueId() + ",true," + color + "\n";
            }
            
        }
        return lineToWrite;
    }
    
    public static void writeToFile(String filePath, String test) throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter(new File(filePath), true));
        w.write(test);
        w.close();
    }
    
    public static String parseCSV(String line, String[] header, Random rn) {
        String ret = "";
        String color = "\'" +(Math.abs(rn.nextInt()) % 255) + "," + (Math.abs(rn.nextInt()) % 255) + "," + (Math.abs(rn.nextInt()) % 255) + "\'";
        String[] lineArr = line.split(",");
        String previousNode = null;
        for(int i = 175; i < lineArr.length; i++){
            if(lineArr[i].trim().contentEquals("1")) {
                if(previousNode != null) {
                    ret += previousNode + "," + header[i].trim().toLowerCase() + ",true," + color + "\n";
                }
                previousNode = header[i].trim().toLowerCase();
            }
        }
        return ret;
    }
    
    public static Double getPosteriorProbability(String node,  String goal
            , ArrayList<TechniqueEdgeV2> edgeList) {
        Double posteriorProb = 0.0;
        for(TechniqueEdgeV2 edge : edgeList) {
            if(edge.getStartingNode().getTechniqueId().contentEquals(node) &&
                    edge.getEndingNode().getTechniqueId().contentEquals(goal)) {
                posteriorProb = edge.getPosteriorProbability();
                break;
            }
        }
        return posteriorProb;
    }
    
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        AttackGraphV2 graph = CSVProcessor.readCSVCorrelationMatrix("/My Things/Working Project/Evidential Reasoning/matrix1prob.csv", "/My Things/Working Project/Evidential Reasoning/PriorProbability.csv");
//        ArrayList<ArrayList<String>> pathList = new ArrayList<ArrayList<String>>();
//        String destinationId = "t1002";
//        
//        ArrayList<String> initialAccessPoint = new ArrayList<>();
////        initialAccessPoint.add("t1078");
////        initialAccessPoint.add("t1194");
////        initialAccessPoint.add("t1192");
////        initialAccessPoint.add("t1189");
////        initialAccessPoint.add("t1190");
////        initialAccessPoint.add("t1200");
////        initialAccessPoint.add("t1091");
//        initialAccessPoint.add("t1078");
//        
//        ArrayList<String> reportedTechniques = new ArrayList();
//        reportedTechniques.add("t1110");
//        reportedTechniques.add("t1086");
//        
//        CSVProcessor.findAllPath(graph, pathList, destinationId, initialAccessPoint, reportedTechniques);

//        test();
//        generateGraphForGephi();
//            generateGephiFileSequencial();
//            generateGephiFileByStage();
          generateGephiFilewithoutLoop();
    }
}

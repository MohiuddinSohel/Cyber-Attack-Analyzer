/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import AttackTree.AttackGraph;
import AttackTree.TechniqueEdge;
import AttackTree.TechniqueNode;
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
public class CSVProcessor {
    
    public static AttackGraph readCSVCorrelationMatrix(String csvPathC, String csvPathP) throws FileNotFoundException, IOException {
        
        ArrayList<TechniqueNode> nodeList = new ArrayList();
        ArrayList<TechniqueEdge> edgeList = new ArrayList();
        HashMap<String, ArrayList<TechniqueNode>> adjacencyList =  new HashMap();
        HashMap<String, ArrayList<TechniqueNode>> reverseAdjacencyList = new HashMap();
        AttackGraph graph = new AttackGraph(nodeList, edgeList, adjacencyList, reverseAdjacencyList);
        
        BufferedReader in = new BufferedReader(new FileReader(csvPathC));
        String line;
        boolean skipFirstLine = false;
        String[] header = null;
        while((line = in.readLine()) != null) {
            if(!skipFirstLine) {
                header = line.split(",");
                skipFirstLine = true;
                
                for(int i = 1; i < header.length; i++) {
                    if(header[i].contains("PRE-")) continue;
                    graph.getReverseAdjacencyList().put(header[i].trim().toLowerCase(), new ArrayList());
                    graph.getAdjacencyList().put(header[i].trim().toLowerCase(), new ArrayList());
                }
                continue;
            }
            String[] matrixColumn = line.split(",");
            if(matrixColumn[0].trim().contains("PRE-")) continue;
            generateGraph(graph, header, matrixColumn);
        }

        in.close();
        readCSVPriorProbability(graph, csvPathP);
        return graph;
    }
    
    public static void readCSVPriorProbability(AttackGraph graph, String csvPath) throws FileNotFoundException, IOException {
        
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
    
    public static void generateGraph(AttackGraph graph, String[] header, String[] matrixColumn) {
        ArrayList<TechniqueNode> nodeList = graph.getNodeList();
        
        HashMap<String, ArrayList<TechniqueNode>> reverseAdjacencyList = graph.getReverseAdjacencyList();
        
        ArrayList<TechniqueEdge> edgeList = graph.getEdgeList();
        HashMap<String, ArrayList<TechniqueNode>> adjacencyList =  graph.getAdjacencyList();
//        TechniqueNode endingNode = getNode(matrixColumn[0].trim().toLowerCase(), nodeList);
        TechniqueNode startingNode = getNode(matrixColumn[0].trim().toLowerCase(), nodeList);
        
        HashMap<String, Double> posteriorProbability = graph.getPosteriorProbability();
        
        for(int i = 1; i < matrixColumn.length; i++) {
            if(matrixColumn[i].trim().contentEquals("0") || matrixColumn[i].trim().contentEquals("0.0") || header[i].trim().contains("PRE-")) {
                continue;
            }
            Double probability = Double.valueOf(matrixColumn[i].trim());
            posteriorProbability.put(matrixColumn[0].trim().toLowerCase() + "|" + header[i].trim().toLowerCase(), probability);
//            if(probability < 0.31) continue;
            
//            TechniqueNode startingNode = getNode(header[i].trim().toLowerCase(), nodeList);

            TechniqueNode endingNode = getNode(header[i].trim().toLowerCase(), nodeList);
            TechniqueEdge edge = new TechniqueEdge(startingNode, endingNode, probability);
            edgeList.add(edge);
            
            adjacencyList.get(matrixColumn[0].trim().toLowerCase()).add(endingNode);
            reverseAdjacencyList.get(header[i].trim().toLowerCase()).add(startingNode);
        }
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
    
    public static TechniqueNode getNode(String techniqueId, ArrayList<TechniqueNode> nodeList, boolean createNode) {
        for(int i= 0; i < nodeList.size(); i++) {
            if(nodeList.get(i).getTechniqueId().contentEquals(techniqueId.trim().toLowerCase())) {
                return nodeList.get(i);
            }
        }
        return null;        
    }
    
    public static void printAllPath(AttackGraph graph, ArrayList<ArrayList<String>> pathList, String destinationId
            , ArrayList<String> initialAccessPoint, ArrayList<String> reportedTechniques) throws IOException {
        ArrayList<String> currentPath = new ArrayList();
        currentPath.add(destinationId);
        findAllPath(graph, pathList, currentPath, destinationId, initialAccessPoint, reportedTechniques);
        System.out.println("Destination Id: " + destinationId + " path Size: " + pathList.size());    
    }
    
    public static boolean isallNodeInListVisited(ArrayList<TechniqueNode> adjacentList) {
        if(adjacentList == null || adjacentList.isEmpty()) return true;
        boolean ret = true;
        for(TechniqueNode node : adjacentList) {
            if(!node.isIsVisited()) {
                ret = false;
                break;
            }
        }
        return ret;
    }
    
    public static void findAllPath(AttackGraph graph, ArrayList<ArrayList<String>> pathList
            , ArrayList<String> currentPath, String destinationId, ArrayList<String> initialAccessPoint, ArrayList<String> reportedTechniques) throws IOException {
        
        HashMap<String, ArrayList<TechniqueNode>> adjList = graph.getReverseAdjacencyList();
        
        TechniqueNode node = getNode(destinationId, graph.getNodeList(), false);
        node.setIsVisited(true);
        
        ArrayList<TechniqueNode> adjacentList = adjList.get(destinationId);
        
        if(initialAccessPoint.contains(destinationId) || isallNodeInListVisited(adjacentList)) {
            
            node.setIsVisited(false);
            if(isThePathValid(currentPath, reportedTechniques)) {
                ArrayList<String> c = new ArrayList(currentPath);
                pathList.add(c);
                /*if((pathList.size() % 10000) == 0) {
                    for(ArrayList<String> p :  pathList) {
                        writeToFile("/Users/mahmed27/Desktop/qsort/path.txt", p.toString() + "\n");
                    }
                    pathList.clear();
                    
                }*/
//                System.out.println("new path added: " + c.size());
            }
            return;
        }
        
        
        for(int i = 0; i < adjacentList.size(); i++) {
            if(!adjacentList.get(i).isIsVisited()) {
                currentPath.add(adjacentList.get(i).getTechniqueId());
                findAllPath(graph, pathList, currentPath, adjacentList.get(i).getTechniqueId(), initialAccessPoint, reportedTechniques);
                currentPath.remove(adjacentList.get(i).getTechniqueId());
            }
        }
        node.setIsVisited(false);
    }
    
    public static boolean isThePathValid(ArrayList<String> currentPath, ArrayList<String> reportedTechniques) {
        if(currentPath.size() > 10) {
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
    
    public static void generateGraphForGephi() throws IOException {
        AttackGraph graph = CSVProcessor.readCSVCorrelationMatrix("/My Things/Working Project/Evidential Reasoning/matrix1prob.csv"
                , "/My Things/Working Project/Evidential Reasoning/PriorProbability.csv");
        ArrayList<ArrayList<String>> pathList = new ArrayList<ArrayList<String>>();
        String  testR = readFile(graph);
        writeToFile("/My Things/Working Project/Evidential Reasoning/reversegoaltoInitial.gdf", testR);
        String  testO = readFile1(graph);
        writeToFile("/My Things/Working Project/Evidential Reasoning/initialToGoal.gdf", testO);
    }
    
    
    public static String readFile(AttackGraph graph) throws FileNotFoundException {
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        HashMap<String, ArrayList<TechniqueNode>> adjList = graph.getReverseAdjacencyList();
        String color = "\'255,1,255\'";
        for(String key : adjList.keySet()) {
            ArrayList<TechniqueNode> list = adjList.get(key);
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
    
    
    public static String readFile1(AttackGraph graph) throws FileNotFoundException {
        String init = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";
        String lineToWrite = init;
        HashMap<String, ArrayList<TechniqueNode>> adjList = graph.getAdjacencyList();
        String color = "\'255,1,255\'";
        for(String key : adjList.keySet()) {
            ArrayList<TechniqueNode> list = adjList.get(key);
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
            , ArrayList<TechniqueEdge> edgeList) {
        Double posteriorProb = 0.0;
        for(TechniqueEdge edge : edgeList) {
            if(edge.getStartingNode().getTechniqueId().contentEquals(node) &&
                    edge.getEndingNode().getTechniqueId().contentEquals(goal)) {
                posteriorProb = edge.getPosteriorProbability();
                break;
            }
        }
        return posteriorProb;
    }
    
    public static void test() throws IOException {
        AttackGraph g = new AttackGraph(new ArrayList(), new ArrayList(), new HashMap(), new HashMap() );
        TechniqueNode n0 =  new TechniqueNode("0");
        TechniqueNode n1 =  new TechniqueNode("1");
        TechniqueNode n2 =  new TechniqueNode("2");
        TechniqueNode n3 =  new TechniqueNode("3");
        TechniqueNode n4 =  new TechniqueNode("4");
        TechniqueNode n5 =  new TechniqueNode("5");
        
        TechniqueEdge e1 = new TechniqueEdge(n0, n2, 2.0);
        TechniqueEdge e2 = new TechniqueEdge(n0, n3, 2.0);
        TechniqueEdge e3 = new TechniqueEdge(n0, n1, 2.0);
        TechniqueEdge e4 = new TechniqueEdge(n2, n0, 2.0);
        TechniqueEdge e5 = new TechniqueEdge(n2, n1, 2.0);
        TechniqueEdge e6 = new TechniqueEdge(n1, n3, 2.0);
        TechniqueEdge e7 = new TechniqueEdge(n1, n5, 2.0);
        TechniqueEdge e8 = new TechniqueEdge(n2, n5, 2.0);
        TechniqueEdge e9 = new TechniqueEdge(n2, n4, 2.0);
        TechniqueEdge e10 = new TechniqueEdge(n4, n5, 2.0);
        TechniqueEdge e11 = new TechniqueEdge(n3, n4, 2.0);
        TechniqueEdge e12 = new TechniqueEdge(n3, n5, 2.0);
        TechniqueEdge e13 = new TechniqueEdge(n5, n0, 2.0);
        TechniqueEdge e14 = new TechniqueEdge(n5, n4, 2.0);
        
        g.getNodeList().add(n0);
        g.getNodeList().add(n1);
        g.getNodeList().add(n2);
        g.getNodeList().add(n3);
        g.getNodeList().add(n4);
        g.getNodeList().add(n5);
        
        g.getEdgeList().add(e1);
        g.getEdgeList().add(e2);
        g.getEdgeList().add(e3);
        g.getEdgeList().add(e4);
        g.getEdgeList().add(e5);
        g.getEdgeList().add(e6);
        
        g.getAdjacencyList().put("0", new ArrayList());
        g.getAdjacencyList().get("0").add(n2);
        g.getAdjacencyList().get("0").add(n3);
        g.getAdjacencyList().get("0").add(n1);
        
        g.getAdjacencyList().put("2", new ArrayList());
        g.getAdjacencyList().get("2").add(n0);
        g.getAdjacencyList().get("2").add(n1);
        g.getAdjacencyList().get("2").add(n4);
        g.getAdjacencyList().get("2").add(n5);
        
        g.getAdjacencyList().put("1", new ArrayList());
        g.getAdjacencyList().get("1").add(n3);
        g.getAdjacencyList().get("1").add(n5);
        
        g.getAdjacencyList().put("3", new ArrayList());
        g.getAdjacencyList().get("3").add(n4);
        g.getAdjacencyList().get("3").add(n5);
        
        g.getAdjacencyList().put("4", new ArrayList());
        g.getAdjacencyList().get("4").add(n5);
        
        g.getAdjacencyList().put("5", new ArrayList());
        g.getAdjacencyList().get("5").add(n0);
        
        g.getReverseAdjacencyList().put("0", new ArrayList());
        g.getReverseAdjacencyList().get("0").add(n2);
        g.getReverseAdjacencyList().get("0").add(n5);
        
        g.getReverseAdjacencyList().put("2", new ArrayList());
        g.getReverseAdjacencyList().get("2").add(n0);
        
        g.getReverseAdjacencyList().put("1", new ArrayList());
        g.getReverseAdjacencyList().get("1").add(n0);
        g.getReverseAdjacencyList().get("1").add(n2);
        
        g.getReverseAdjacencyList().put("3", new ArrayList());
        g.getReverseAdjacencyList().get("3").add(n0);
        g.getReverseAdjacencyList().get("3").add(n1);
        
        g.getReverseAdjacencyList().put("4", new ArrayList());
        g.getReverseAdjacencyList().get("4").add(n2);
        g.getReverseAdjacencyList().get("4").add(n3);
        g.getReverseAdjacencyList().get("4").add(n5);
        
        g.getReverseAdjacencyList().put("5", new ArrayList());
        g.getReverseAdjacencyList().get("5").add(n1);
        g.getReverseAdjacencyList().get("5").add(n3);
        g.getReverseAdjacencyList().get("5").add(n4);
        g.getReverseAdjacencyList().get("5").add(n2);
        
        ArrayList<ArrayList<String>> pathList = new ArrayList<ArrayList<String>>();
        String destinationId = "2";
        ArrayList<String> initialAccessPoint = new ArrayList<>();
        initialAccessPoint.add("1");
        ArrayList<String> reportedTec = new ArrayList();
        reportedTec.add("3");
        CSVProcessor.printAllPath(g, pathList, destinationId, initialAccessPoint, reportedTec);
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        AttackGraph graph = CSVProcessor.readCSVCorrelationMatrix("/My Things/Working Project/Evidential Reasoning/matrix1prob.csv", "/My Things/Working Project/Evidential Reasoning/PriorProbability.csv");
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

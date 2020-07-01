package SyntheticAttackChain;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mahmed27
 */
public class SyntheticAttackChain {
    
    public static String gdfFileName = "C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\1080ChainIfixed05.gdf";// /My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/AttackData/10Attack.gdf";
    public static String attackDataFile = "C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\1080ChainIfixed05.txt";//"/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/AttackData/10attack.txt";
    public static String reportedTFile = "C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\reportedTechnique.txt";
    public static String dataSet = "50Chain05Overlap";
    public static String resultStorePath = "C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\ResultStoreLogRandom.txt";
    
    public SyntheticAttackChain() {
        String osi = System.getProperty("os.name").toLowerCase();
        System.out.print(osi);
        if(osi.contains("win")) {
            gdfFileName = "D:\\MohiuddinSohel\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\1080ChainIfixed05.gdf";//"C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\10Attack.gdf";
            attackDataFile = "D:\\MohiuddinSohel\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\1080ChainIfixed05.txt";//"C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\10Attack.txt";
            reportedTFile = "C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\reportedTechnique.txt";
            resultStorePath = "C:\\DropBox\\Dropbox\\Research-Project\\EvidentialReasoning\\Evidential Reasoning\\AttackData\\1080ResultStoreLogRandom.txt";
        } else if(osi.contains("mac")) {
            gdfFileName = "/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/1080ChainIfixed05.gdf";
            attackDataFile = "/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/1080ChainIfixed05.txt";
            reportedTFile = "/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/reportedTechnique.txt";
            resultStorePath = "/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/1080ResultStoreLogRandom.txt";
        } else if (osi.contains("nix") || osi.contains("nux") || osi.indexOf("aix") > 0) {
        } else if(osi.contains("sunos")) {
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        LinkedHashMap<Integer, ArrayList<ArrayList<Integer>>> attackList = geenrateAttackData();
        LinkedHashMap<String, LinkedHashMap<String, Double>> stats = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<String>> ret = generateAllEdgesAndAttackData(attackList, stats);
        gephiFileGenerator(ret.get("edge"));

    }

    public static LinkedHashMap<Integer, ArrayList<ArrayList<Integer>>> geenrateAttackData() {
        int noOfAttack = 10;
        LinkedHashMap<Integer, ArrayList<ArrayList<Integer>>> attack = new LinkedHashMap<>();
        int totalT = 280;

        double correlationRatio = 0.1;

        Random rNoChain = new Random();
        Random rChain = new Random();
        Random rGeneral = new Random();
        Random rChainLength = new Random();

        ArrayList<Integer> initialTechnique = new ArrayList<>();
        ArrayList<Integer> finalGoal = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            initialTechnique.add(i);
        }
        for (int i = 300; i < 350; i++) {
            finalGoal.add(i);
        }
        
        ArrayList<Integer> corrTechnique = new ArrayList<>();
        double totatCorrelatedT = totalT * correlationRatio;
        for(int corI = 0; corI < totatCorrelatedT; ) {
            Integer temp1 =  20 + rGeneral.nextInt(299 - 20 + 1) ;
            if(!corrTechnique.contains(temp1)) {
                corrTechnique.add(temp1);
                corI++; 
            }
        }

        for (int i = 0; i < noOfAttack; i++) {

            /*ArrayList<Integer> tInAttackI = new ArrayList<>();
            ArrayList<Integer> corrTechnique = new ArrayList<>();
            double toTakeT = 0;

            if (i > 0 && corrTechnique.isEmpty()) {
                for (int h = 0; h < attack.get(i - 1).size(); h++) {

                    ArrayList<Integer> temp = new ArrayList<>(attack.get(i - 1).get(h).subList(1, attack.get(i - 1).get(h).size() - 1));
                    temp.removeAll(tInAttackI);
                    tInAttackI.addAll(temp);
                }
                toTakeT = Math.ceil(Double.valueOf(tInAttackI.size()) * correlationRatio);
//                System.out.println(toTakeT);
                for (int l = 0; l < toTakeT;) {
                    int tem = 0 + rGeneral.nextInt(tInAttackI.size() - 1 - 0 + 1);
                    if (!corrTechnique.contains(tInAttackI.get(tem))) {
                        corrTechnique.add(tInAttackI.get(tem));
                        l++;
                    }
                }
            }*/

            int noOfChainPerAttack = 1 + rNoChain.nextInt(5 - 1 + 1);
            ArrayList<ArrayList<Integer>> cList = new ArrayList<>();
            int preInitialT = -1;
            int preGoal = -1;
            boolean shouldCorrelate = rGeneral.nextBoolean();
            int noOfCorrelated = 0;
            for (int j = 0; j < noOfChainPerAttack; j++) {

                ArrayList<Integer> chain = new ArrayList<>();

                int chainLength = 5 + rChainLength.nextInt(10 - 5 + 1);

                if (rGeneral.nextBoolean() && preInitialT != -1) {
                    chain.add(preInitialT);
                } else {
                    int temInit = initialTechnique.get(0 + rChain.nextInt(initialTechnique.size() - 1 - 0 + 1));
                    chain.add(temInit);
                    preInitialT = temInit;
                }
                
                int correlationInTecI = 0;
                for (int k = 0; k < chainLength - 2;) {
                    if (rGeneral.nextBoolean() && !corrTechnique.isEmpty() && shouldCorrelate && correlationInTecI < 3 && noOfCorrelated < 5) {
                        int tem = 0 + rGeneral.nextInt(corrTechnique.size() - 1 - 0 + 1);
                        if (!chain.contains(corrTechnique.get(tem))) {
                            chain.add(corrTechnique.get(tem));
                            k++;
                            noOfCorrelated++;
                            correlationInTecI++;
                        }
                    } else {
                        int t = 21 + rChain.nextInt(298 - 21 + 1);
                        if (!chain.contains(t)) {
                            chain.add(t);
                            k++;
                        }
                    }
                }
                if (rGeneral.nextBoolean() && preGoal != -1) {
                    chain.add(preGoal);
                } else {
                    int tempG = finalGoal.get(0 + rChain.nextInt(finalGoal.size() - 1 - 0 + 1));
                    chain.add(tempG);
                    preGoal = tempG;
                }
                cList.add(chain);
                
            }
            if(shouldCorrelate) {
                corrTechnique.clear();
            }
            attack.put(i, cList);
        }
//        System.out.println(attack);
        return attack;
    }

    public static LinkedHashMap<String, ArrayList<String>> generateAllEdgesAndAttackData(LinkedHashMap<Integer, ArrayList<ArrayList<Integer>>> attackList,
            LinkedHashMap<String, LinkedHashMap<String, Double>> stats) throws IOException {

        String attack200DataFile = SyntheticAttackChain.attackDataFile;
        String lineToWrite = "";
        ArrayList<String> edgeList = new ArrayList<>();
        ArrayList<String> initialTechniqueList = new ArrayList<>();
        ArrayList<String> finalGoalList = new ArrayList<>();

        LinkedHashMap<String, Double> priorProbabilityList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> posteriorProbabilityList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> techniqueCount = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<String>> ret = new LinkedHashMap<>();

        for (int i = 0; i < attackList.size(); i++) {
            lineToWrite += "******** " + "Attack No: " + i + " ******\n";
            for (int j = 0; j < attackList.get(i).size(); j++) {
                lineToWrite += attackList.get(i).get(j) + "\n";
                String previousT = "";
                int chainSize = attackList.get(i).get(j).size();
                String goal = attackList.get(i).get(j).get(chainSize - 1).toString();
                for (int k = 0; k < chainSize; k++) {

                    if (!previousT.isEmpty()) {

                        String n2 = attackList.get(i).get(j).get(k).toString();
                        if (!edgeList.contains(previousT + "," + n2)
                                && !edgeList.contains(n2 + "," + previousT)) {
                            edgeList.add(previousT + "," + n2);
                        }

                        previousT = n2;

                        if (i == attackList.get(i).get(j).size() - 1) {
                            if (!finalGoalList.contains(previousT)) {
                                finalGoalList.add(previousT);
                            }
                        }
                    } else {

                        previousT = attackList.get(i).get(j).get(k).toString();
                        if (!initialTechniqueList.contains(previousT)) {
                            initialTechniqueList.add(previousT);
                        }
                    }

                    if (techniqueCount.containsKey(previousT)) {
                        techniqueCount.put(previousT, techniqueCount.get(previousT) + 1.0);
                    } else {
                        techniqueCount.put(previousT, 1.0);

                    }
                    if (!goal.contentEquals(previousT)) {

                        String posteriorCountString = previousT + "," + goal;
                        if (techniqueCount.containsKey(posteriorCountString)) {
                            techniqueCount.put(posteriorCountString, techniqueCount.get(posteriorCountString) + 1.0);
                        } else {
                            techniqueCount.put(posteriorCountString, 1.0);
                        }
                    }
                }
            }
        }
        ret.put("edge", edgeList);
        ret.put("init", initialTechniqueList);
        ret.put("final", initialTechniqueList);

        stats.put("prior", priorProbabilityList);
        stats.put("posterior", posteriorProbabilityList);
        stats.put("count", techniqueCount);

        BufferedWriter w = new BufferedWriter(new FileWriter(new File(attack200DataFile), true));
        w.write(lineToWrite);
        w.close();

        return ret;
    }

    public static void gephiFileGenerator(ArrayList<String> edgeList) throws IOException {
        String gephiFileName = SyntheticAttackChain.gdfFileName;
        String lineToWrite = "nodedef> name VARCHAR,label VARCHAR\n" + "edgedef> node1,node2,directed BOOLEAN,color VARCHAR\n";;

        String color = "\'255,1,255\'";

        for (String edge : edgeList) {
            String nodeL[] = edge.split(",");
            lineToWrite += nodeL[0] + "," + nodeL[1] + ",true," + color + "\n";
        }

        BufferedWriter w = new BufferedWriter(new FileWriter(new File(gephiFileName), true));
        w.write(lineToWrite);
        w.close();
    }

    public static LinkedHashMap<String, ArrayList<String>> calculatePosteriorAndPriorProbability(LinkedHashMap<String
            , LinkedHashMap<String, Double>> stats) throws FileNotFoundException, IOException {
        
        String fileName = SyntheticAttackChain.attackDataFile;

        ArrayList<String> initialTechniqueList = new ArrayList<>();
        ArrayList<String> finalGoalList = new ArrayList<>();

        LinkedHashMap<String, Double> priorProbabilityList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> posteriorProbabilityList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> techniqueCount = new LinkedHashMap<>();
        
        LinkedHashMap<String, ArrayList<String>> ret = new LinkedHashMap<> ();

        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line = "";
        Double probabilitySpace = 0.0;
        while ((line = reader.readLine()) != null) {
            if (line.contains("***")) {
                continue;
            }
            String[] chain = line.replaceAll("[\\[\\]]", "").split(",");

            String goal = chain[chain.length - 1].trim();
            if (!finalGoalList.contains(goal)) {
                finalGoalList.add(goal);
            }

            String initialTech = chain[0].trim();
            if (!initialTechniqueList.contains(initialTech)) {
                initialTechniqueList.add(initialTech);
            }

            probabilitySpace += chain.length;
            for (int i = 0; i < chain.length; i++) {
                String combinedT = chain[i].trim() + "," + goal;
                if (!chain[i].trim().contentEquals(goal)) {
                    if (!techniqueCount.containsKey(combinedT)) {
                        techniqueCount.put(combinedT, 1.0);
                    } else {
                        techniqueCount.put(combinedT, techniqueCount.get(combinedT) + 1.0);
                    }
                }
                if (!techniqueCount.containsKey(chain[i].trim())) {
                    techniqueCount.put(chain[i].trim(), 1.0);
                } else {
                    techniqueCount.put(chain[i].trim(), techniqueCount.get(chain[i].trim()) + 1.0);
                }

            }
        }
        reader.close();
        String probability = "";
        
        for(String key : techniqueCount.keySet()) {
            if(key.contains(",")) {
                String[] keys = key.split(",");
                Double d = techniqueCount.get(key)/techniqueCount.get(keys[1]);
                posteriorProbabilityList.put(key, d == 1.0 ? 0.7 : d);
                probability += key + " = " + (d == 1.0 ? 0.7 : d) + "\n";
            } else {
                priorProbabilityList.put(key, techniqueCount.get(key)/probabilitySpace);
                probability += key + " = " + (techniqueCount.get(key)/probabilitySpace) + "\n";
            }
        }
        
        BufferedWriter w = new BufferedWriter(new FileWriter(new File("/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/1080ChainProbabilityIfixed05.txt")));
        w.write(probability);
        w.close();
        
        stats.put("prior", priorProbabilityList);
        stats.put("posterior", posteriorProbabilityList);
        
        ret.put("init", initialTechniqueList);
        ret.put("goal", finalGoalList);
        
        return ret;
    }
    
    public static LinkedHashMap<String, String> generateAllEdges() throws FileNotFoundException, IOException {
        String fileName = SyntheticAttackChain.gdfFileName;
        LinkedHashMap<String, String> edgeList = new LinkedHashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.contains("nodedef") || line.contains("edgedef")) {
                continue;
            }
            String[] nodeList = line.split(",");
            edgeList.put(nodeList[0].trim() + "," + nodeList[1].trim(), nodeList[1].trim());
        }
        return edgeList;
    }
    
    
    
    public static AttackGraphV2 generateGraph(LinkedHashMap<String, String> eList) {
        
        ArrayList<TechniqueEdgeV2> edgeList = new ArrayList();
        ArrayList<TechniqueNodeV2> nodeList = new ArrayList();
        LinkedHashMap<String, ArrayList<TechniqueNodeV2>> adjacencyList = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<TechniqueNodeV2>> reverseAdjacencyList = new LinkedHashMap<>();
        LinkedHashMap<String, Double> posteriorProbability = new LinkedHashMap<>();
        LinkedHashMap<String, Double> priorProbability = new LinkedHashMap<>();

        for(String key: eList.keySet()) {
//            String tmpValue = eList.get(key);
            String[] tmpKey = key.split(",");
            TechniqueNodeV2 sNode = getNode(tmpKey[0].trim(), "0", nodeList);
            TechniqueNodeV2 eNode = getNode(tmpKey[1].trim(), "0", nodeList);
            TechniqueEdgeV2 edge = new TechniqueEdgeV2(sNode, eNode, 0.33);
            edgeList.add(edge);

            if(!adjacencyList.containsKey(tmpKey[0].trim())) {
                adjacencyList.put(tmpKey[0].trim(), new ArrayList<>());
            }
            
            if(!reverseAdjacencyList.containsKey(tmpKey[1].trim())) {
                reverseAdjacencyList.put(tmpKey[1].trim(), new ArrayList<>());
            }
            adjacencyList.get(tmpKey[0].trim()).add(eNode);
            reverseAdjacencyList.get(tmpKey[1].trim()).add(sNode);
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
    
    public static LinkedHashMap<String, Double> setProbability(AttackGraphV2 graph, ArrayList<String> aGList
            , LinkedHashMap<String, Double>  postProbrobability, LinkedHashMap<String, Double>  priorProbrobability) throws IOException {
        LinkedHashMap<String, Double> posteriorProbability = new LinkedHashMap<>();
        LinkedHashMap<String, Double>  postProb = postProbrobability;
        for(String goalId : aGList) {
            for(TechniqueNodeV2 node : graph.getNodeList()) {
                if(isReachable(graph, node, goalId)) {
                    Double d = 0.0;
                    if(postProb.containsKey(node.getTechniqueId() + "," + goalId)) {
                         d = postProb.get(node.getTechniqueId() + "," + goalId);
                    } else {
                        d = priorProbrobability.get(node.getTechniqueId());
                    }
                    posteriorProbability.put(node.getTechniqueId() + "|" + goalId, d);
                }
            }
        }
        return posteriorProbability;
    }
    
    public static LinkedHashMap<String, Double> setProbabilityRandom(AttackGraphV2 graph, ArrayList<String> aGList
            , LinkedHashMap<String, Double>  postProbrobability, LinkedHashMap<String, Double>  priorProbrobability) throws IOException {
        
        String filePath = SyntheticAttackChain.attackDataFile;//"/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/AttackData/100ChainIfixed05.txt";
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = "";
        
        ArrayList<ArrayList<String>> chainList = new ArrayList<>();
        ArrayList<String> uniqueTechnique = new ArrayList<>();
        ArrayList<String> commonTechnique = new ArrayList<>();
        
        LinkedHashMap<String, Double> posteriorPInChain = new LinkedHashMap<>();
        
        while ((line = reader.readLine()) != null) {
            
            if (line.contains("***")) {
                continue;
            }
            
            String[] chain = line.replaceAll("[\\[\\]\\s]", "").split(",");
            chainList.add(new ArrayList<>(Arrays.asList(chain)));
            
            for(String t : chain) {
                if(!uniqueTechnique.contains(t.trim())) {
                    uniqueTechnique.add(t.trim());
                } else {
                    commonTechnique.add(t.trim());
                }
            }
        }
        reader.close();
        
        Random rand = new Random();
        
        for(ArrayList<String> chain : chainList) {
            int count = 0;
            for(String t : chain) {
                if(t.trim().contentEquals(chain.get(chain.size() -1).trim())) continue;
                
                Double d = 0.0;
                if(!commonTechnique.contains(t.trim()) && count < 3 && rand.nextBoolean()) {
                    d = generateRandomWithInARangeDouble(0.35, 0.7);
                    count++;
                } else {
                    d = generateRandomWithInARangeDouble(0.1, 0.35);
                }
                posteriorPInChain.put(t.trim() + "," + chain.get(chain.size() - 1).trim(), d);
            }
        }
        

        LinkedHashMap<String, Double> posteriorProbability = new LinkedHashMap<>();
        LinkedHashMap<String, Double>  postProb = postProbrobability;
        for(String goalId : aGList) {
            for(TechniqueNodeV2 node : graph.getNodeList()) {
                if(isReachable(graph, node, goalId)) {
                    Double d = 0.0;
                    if(posteriorPInChain.containsKey(node.getTechniqueId() + "," + goalId)) {
                         d = posteriorPInChain.get(node.getTechniqueId() + "," + goalId);
                    } else {
                        if(postProb.containsKey(node.getTechniqueId() + "," + goalId)) {
                            d = postProb.get(node.getTechniqueId() + "," + goalId);
                        } else {
                            d = 0.1;  //priorProbrobability.get(node.getTechniqueId());
                        }
                    }
                    posteriorProbability.put(node.getTechniqueId() + "|" + goalId, d);
                }
            }
        }
        
        String probability = "";
        for(String key : posteriorPInChain.keySet()) {
            probability += key + " : " + posteriorPInChain.get(key) + "\n";
        }
        
        BufferedWriter w = new BufferedWriter(new FileWriter(new File("/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/1080RandomPosteriorProbability.txt")));
        w.write(probability);
        w.close();
        
        return posteriorProbability;
    }
    
    public static Double generateRandomWithInARangeDouble(Double min, Double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
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
    
    public static TechniqueNodeV2 getNode(String techniqueId, String tactic, ArrayList<TechniqueNodeV2> nodeList, boolean createNode) {
        for(int i= 0; i < nodeList.size(); i++) {
            if(nodeList.get(i).getTechniqueId().contentEquals(techniqueId.trim().toLowerCase())) {
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
        findAllPath(graph, pathList, currentPath, destinationId, "0", initialAccessPoint, reportedTechniques);
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
        
        LinkedHashMap<String, ArrayList<TechniqueNodeV2>> adjList = graph.getReverseAdjacencyList();
        
        TechniqueNodeV2 node = getNode(destinationId, tactic, graph.getNodeList(), false);
        node.setIsVisited(true);
        
        ArrayList<TechniqueNodeV2> adjacentList = adjList.get(destinationId);
        
        if((initialAccessPoint.contains(destinationId)) || isallNodeInListVisited(adjacentList) || currentPath.size() > 15) {
            
            node.setIsVisited(false);
            if(isThePathValid(currentPath, reportedTechniques)) {
                ArrayList<String> c = new ArrayList(currentPath);
                pathList.add(c);
                
                //System.out.println(pathList);
            }
            return;
        }
        
        

        for(int i = 0; i < adjacentList.size(); i++) {
            if(!adjacentList.get(i).isIsVisited()) {
                
                currentPath.add(adjacentList.get(i).getTechniqueId());
                findAllPath(graph, pathList, currentPath, adjacentList.get(i).getTechniqueId(), adjacentList.get(i).getTactic()
                        , initialAccessPoint, reportedTechniques);
                currentPath.remove(adjacentList.get(i).getTechniqueId());
            }
        }
        node.setIsVisited(false);
    }
    
    public static boolean isThePathValid(ArrayList<String> currentPath, ArrayList<String> reportedTechniques) {
        if(currentPath.size() > 15) {
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
    
    public static ArrayList<String> generateObserverdTechniqueList(Double portionOfAllT
            , String filePath, String distributionType) throws FileNotFoundException, IOException {
        
        ArrayList<String> observedT = new ArrayList<>();
        ArrayList<String> tList = new ArrayList<> ();
        ArrayList<ArrayList<String>> chainList = new ArrayList<> ();
        HashMap<String, ArrayList<ArrayList<String>>> chainListByGoal =  new HashMap();
        ArrayList<String> uniqueTList = new ArrayList<> ();
        String line ="";
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        Random rand = new Random();
        int cCount = 0; 
        while ((line = reader.readLine()) != null) {
            
            if (line.contains("***")) {
                continue;
            }
            
            String[] chain = line.replaceAll("[\\[\\]\\s]", "").split(",");
            chainList.add(new ArrayList<>(Arrays.asList(chain)));
            for(int i =0; i < chain.length - 1; i++) {
                if(!tList.contains(chain[i].trim())) {
                    tList.add(chain[i].trim());
                } else {
                    if(!uniqueTList.contains(chain[i].trim())) {
                        uniqueTList.add(chain[i].trim());
                    }
                }
            }
        }
        
        
        Double noOfT = Math.floor(tList.size() * portionOfAllT);
        
        if(distributionType.contains("uniform")) {
            observedT = generateTWithUniformDistribution(noOfT, tList, uniqueTList, chainList);
        } else if(distributionType.contains("pareto")) {
            observedT = generateTWithParetoDistribution(noOfT, tList, uniqueTList, chainList);
        } else {
            observedT = generateTWithUniformDistribution(noOfT, tList, uniqueTList, chainList);
        }
        return observedT;
    }
    
    public static ArrayList<String> generateTWithUniformDistribution(Double numberOfT
            , ArrayList<String> allT, ArrayList<String> uniqueT, ArrayList<ArrayList<String>> chainList) {
        ArrayList<String> observedT = new ArrayList<>();
        
        System.out.println("No of Chain: " + chainList.size());
        Random rand1 = new Random();
        Random rand2 = new Random();
        for(int i = 0; i < numberOfT;) {
            int chainIndex = generateRandomWithInARange(rand1, 0, chainList.size() - 1);
            int tIndex = generateRandomWithInARange(rand2,0, chainList.get(chainIndex).size() - 2);
            if(!observedT.contains(chainList.get(chainIndex).get(tIndex))) {
                observedT.add(chainList.get(chainIndex).get(tIndex));
                i++;
                System.out.println("Chain: " + chainIndex+ "listZI: " + tIndex);
            }
        }
        
        return observedT;
    }
    public static ArrayList<String> generateTWithParetoDistribution(Double numberOfT
            , ArrayList<String> allT, ArrayList<String> uniqueT, ArrayList<ArrayList<String>> chainList) {
        ArrayList<String> observedT = new ArrayList<>();
        Double rule80 = Math.floor(numberOfT * 0.8);
        Double rule20 = Math.floor(chainList.size() * 0.2);
        
        Random rand1 = new Random();
        Random rand2 = new Random();
        ArrayList<Integer> chainIndex = new ArrayList<>();
        for(int i = 0; i < rule20;) {
           int index = generateRandomWithInARange(rand1, 0, chainList.size() - 1);
           if(!chainIndex.contains(index)) {
               i++;
               chainIndex.add(index);
           }
        }
        
        for(int i = 0; i < rule80;) {
            int cIndex = generateRandomWithInARange(rand1, 0, chainIndex.size() - 1);
            int tIndex = generateRandomWithInARange(rand2, 0, chainList.get(chainIndex.get(cIndex)).size() - 2);
            if(!observedT.contains(chainList.get(chainIndex.get(cIndex)).get(tIndex))) {
                i++;
                observedT.add(chainList.get(chainIndex.get(cIndex)).get(tIndex));
            }
        }
        
        for(int i = 0; i < (numberOfT - rule80); ) {
            int cIndex = generateRandomWithInARange(rand1, 0, chainList.size() - 1);
            if(chainIndex.contains(cIndex)) continue;
            int tIndex = generateRandomWithInARange(rand2, 0, chainList.get(cIndex).size() - 2);
            if(!observedT.contains(chainList.get(cIndex).get(tIndex))) {
                i++;
                observedT.add(chainList.get(cIndex).get(tIndex));
            }
        }
        
        return observedT;
    }
    
    
    public static int generateRandomWithInARange(Random rand, int min, int max) {
//        Random rand = new Random();
        return min + rand.nextInt(max - min + 1);
    }

}

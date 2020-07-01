/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import AttackTree.AttackGraph;
import AttackTree.AttackGraphV2;
import AttackTree.TechniqueEdge;
import AttackTree.TechniqueEdgeV2;
import AttackTree.TechniqueNode;
import AttackTree.TechniqueNodeV2;
import TTPHunt.TTPHuntManagerV2;
import TTPHunt.ThreatReasoning;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author mahmed27
 */
public class UseCase_One {    
    private ThreatReasoning threatReasoning;
    private ArrayList<String> candidateGoalList;
    private ArrayList<String> initialAccessPoint;
    AttackGraph graph;
    
    public UseCase_One() throws IOException {
        this.init();
    }
    
    
    private void init() throws IOException {
        this.graph = generateGraph();
        
        this.generateInitialAccessPoint();
        this.generateAchieveableGoalList();
        this.threatReasoning = new ThreatReasoning(this.graph, this.candidateGoalList, this.initialAccessPoint);
    }
    
    public Double huntTTP(ArrayList<String> finalH) throws IOException {
        
        ArrayList<String> reportedTechnique = this.generateReportedTechnique();
        LinkedHashMap<String, Double> candidateContributionMap = new LinkedHashMap();
        for(String goali: this.candidateGoalList) {
            candidateContributionMap.put(goali, this.threatReasoning.calculateContributionOfGoalForAllChain(goali, reportedTechnique));
        }
        
        //Hypothesis Generation
        ArrayList<ArrayList<String>> hypothesisSet = new ArrayList();
        ArrayList<String> hypothesis = new ArrayList();
        this.threatReasoning.generateHypothesis(hypothesisSet, hypothesis, this.candidateGoalList, candidateContributionMap, reportedTechnique);
        
        //Fidelity Score calculation
//        ArrayList<String> finalH;
        Double currentScore = 0.0;
        for(ArrayList<String> h :  hypothesisSet) {
            Double tmp = this.threatReasoning.calculateFidelityScore(h, reportedTechnique);
            if(tmp > currentScore) {
                finalH = h;
                currentScore = tmp;
            }
        }
        return currentScore;
    }
    
    public void generateInitialAccessPoint() {
        this.initialAccessPoint = new ArrayList();
        this.initialAccessPoint.add("t1078");
        this.initialAccessPoint.add("t1192");
        this.initialAccessPoint.add("t1110");
        
    }
    
    
    public ArrayList<String> generateReportedTechnique() {
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1059");
//        reportedTechnique.add("t1012");
//        reportedTechnique.add("t1018");
        reportedTechnique.add("t1136");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1012");
        return reportedTechnique;
    }
    
    public void generateAchieveableGoalList() {
        this.candidateGoalList = new ArrayList();
        this.candidateGoalList.add("exfiltration");
        this.candidateGoalList.add("c2_communicaion");
        this.candidateGoalList.add("collect_credentials");
        this.candidateGoalList.add("lateral_movement");
        
    }
    
    /*public static void main(String[] args) throws IOException {
        UseCase_One manager = new UseCase_One();
        ArrayList<String> h = null;
        Double score = manager.huntTTP(h);
        System.out.println("Final Fidelity Score: " + score);
    }*/
    
    public static AttackGraph generateGraph() {
        AttackGraph g = new AttackGraph(new ArrayList(), new ArrayList(), new LinkedHashMap(), new LinkedHashMap() );
        
        TechniqueNode n0 =  new TechniqueNode("t1078");
        TechniqueNode n1 =  new TechniqueNode("t1192");
        TechniqueNode n2 =  new TechniqueNode("t1110");
        
        TechniqueNode n3 =  new TechniqueNode("t1098");
        TechniqueNode n4 =  new TechniqueNode("t1136");
        
        TechniqueNode n5 =  new TechniqueNode("t1059");
        TechniqueNode n6 =  new TechniqueNode("t1086");
        TechniqueNode n7 =  new TechniqueNode("t1064");
        
        TechniqueNode n8 =  new TechniqueNode("t1083");
        
        TechniqueNode n9 =  new TechniqueNode("t1113");
        TechniqueNode n10 =  new TechniqueNode("t1056");
        TechniqueNode n11 =  new TechniqueNode("t1005");
        TechniqueNode n12 =  new TechniqueNode("t1012");
        TechniqueNode n13 =  new TechniqueNode("t1087");
        TechniqueNode n14 =  new TechniqueNode("t1018");
        TechniqueNode n15 =  new TechniqueNode("t1135");
        
        TechniqueNode n16 =  new TechniqueNode("t1074");
        
        TechniqueNode n17 =  new TechniqueNode("t1002");
        TechniqueNode n18 =  new TechniqueNode("t1022");
        TechniqueNode n19 =  new TechniqueNode("t1094");
        TechniqueNode n20 =  new TechniqueNode("t1081");
        TechniqueNode n21 =  new TechniqueNode("t1003");
        TechniqueNode n22 =  new TechniqueNode("t1105");
        TechniqueNode n23 =  new TechniqueNode("t1077");
        TechniqueNode n24 =  new TechniqueNode("t1076");
        TechniqueNode n25 =  new TechniqueNode("t1028");
        
        TechniqueNode g1 = new TechniqueNode("exfiltration");
        TechniqueNode g2 = new TechniqueNode("c2_communicaion");
        TechniqueNode g3 = new TechniqueNode("collect_credentials");
        TechniqueNode g4 = new TechniqueNode("lateral_movement");
        
        
        TechniqueEdge e1 = new TechniqueEdge(n0, n3, 0.26);
        TechniqueEdge e2 = new TechniqueEdge(n1, n4, 0.3);
        TechniqueEdge e3 = new TechniqueEdge(n2, n4, 0.33);
        
        TechniqueEdge e4 = new TechniqueEdge(n3, n5, 2.0);
        TechniqueEdge e5 = new TechniqueEdge(n3, n6, 2.0);
        TechniqueEdge e6 = new TechniqueEdge(n4, n5, 2.0);
        TechniqueEdge e7 = new TechniqueEdge(n4, n7, 2.0);
        
        TechniqueEdge e8 = new TechniqueEdge(n5, n8, 2.0);
        TechniqueEdge e9 = new TechniqueEdge(n5, n9, 2.0);
        TechniqueEdge e10 = new TechniqueEdge(n5, n12, 2.0);
        TechniqueEdge e11 = new TechniqueEdge(n5, n13, 2.0);
        TechniqueEdge e12 = new TechniqueEdge(n5, n14, 2.0);
        TechniqueEdge e13 = new TechniqueEdge(n6, n10, 2.0);
        TechniqueEdge e14 = new TechniqueEdge(n6, n15, 2.0);
        TechniqueEdge e15 = new TechniqueEdge(n7, n8, 2.0);
        TechniqueEdge e16 = new TechniqueEdge(n7, n20, 2.0);
        TechniqueEdge e17 = new TechniqueEdge(n7, n21, 2.0);
        
        TechniqueEdge e18 = new TechniqueEdge(n8, n11, 2.0);
        
        TechniqueEdge e19 = new TechniqueEdge(n9, n16, 2.0);
        TechniqueEdge e20 = new TechniqueEdge(n10, n18, 2.0);
        TechniqueEdge e21 = new TechniqueEdge(n10, n19, 2.0);
        TechniqueEdge e22 = new TechniqueEdge(n11, n17, 2.0);
        TechniqueEdge e23 = new TechniqueEdge(n11, n18, 2.0);
        TechniqueEdge e24 = new TechniqueEdge(n12, n19, 2.0);
        TechniqueEdge e25 = new TechniqueEdge(n13, n19, 2.0);
        TechniqueEdge e26 = new TechniqueEdge(n14, n22, 2.0);
        TechniqueEdge e27 = new TechniqueEdge(n14, n23, 2.0);
        TechniqueEdge e28 = new TechniqueEdge(n14, n24, 2.0);
        TechniqueEdge e29 = new TechniqueEdge(n15, n25, 2.0);
        
        TechniqueEdge eg1 = new TechniqueEdge(n17, g1, 2.0);
        TechniqueEdge eg2 = new TechniqueEdge(n18, g1, 2.0);
        TechniqueEdge eg3 = new TechniqueEdge(n19, g2, 2.0);
        TechniqueEdge eg4 = new TechniqueEdge(n10, g3, 2.0);
        TechniqueEdge eg5 = new TechniqueEdge(n20, g3, 2.0);
        TechniqueEdge eg6 = new TechniqueEdge(n21, g3, 2.0);
        TechniqueEdge eg7 = new TechniqueEdge(n22, g4, 2.0);
        TechniqueEdge eg8 = new TechniqueEdge(n23, g4, 2.0);
        TechniqueEdge eg9 = new TechniqueEdge(n24, g4, 2.0);
        TechniqueEdge eg10 = new TechniqueEdge(n25, g4, 2.0);
        
        g.getNodeList().add(n0);g.getNodeList().add(n1);g.getNodeList().add(n2);
        g.getNodeList().add(n3);g.getNodeList().add(n4);g.getNodeList().add(n5);
        g.getNodeList().add(n6);g.getNodeList().add(n7);g.getNodeList().add(n8);
        g.getNodeList().add(n9);g.getNodeList().add(n10);g.getNodeList().add(n11);g.getNodeList().add(n12);
        g.getNodeList().add(n13);g.getNodeList().add(n14);g.getNodeList().add(n15);g.getNodeList().add(n16);
        g.getNodeList().add(n17);g.getNodeList().add(n18);g.getNodeList().add(n19);g.getNodeList().add(n20);
        g.getNodeList().add(n21);g.getNodeList().add(n22);g.getNodeList().add(n23);g.getNodeList().add(n24);
        g.getNodeList().add(n25);g.getNodeList().add(g1);g.getNodeList().add(g2);g.getNodeList().add(g3);g.getNodeList().add(g4);
        
        g.getEdgeList().add(e1);g.getEdgeList().add(e2);g.getEdgeList().add(e3);g.getEdgeList().add(e4);g.getEdgeList().add(e5);
        g.getEdgeList().add(e6);g.getEdgeList().add(e7);g.getEdgeList().add(e8);g.getEdgeList().add(e9);g.getEdgeList().add(e10);
        g.getEdgeList().add(e11);g.getEdgeList().add(e12);g.getEdgeList().add(e13);g.getEdgeList().add(e14);g.getEdgeList().add(e15);
        g.getEdgeList().add(e16);g.getEdgeList().add(e17);g.getEdgeList().add(e18);g.getEdgeList().add(e19);g.getEdgeList().add(e20);
        g.getEdgeList().add(e22);g.getEdgeList().add(e23);g.getEdgeList().add(e24);g.getEdgeList().add(e25);g.getEdgeList().add(e26);
        g.getEdgeList().add(e27);g.getEdgeList().add(e28);g.getEdgeList().add(e29);
        g.getEdgeList().add(eg1);g.getEdgeList().add(eg2);g.getEdgeList().add(eg3);g.getEdgeList().add(eg4);g.getEdgeList().add(eg5);
        g.getEdgeList().add(eg6);g.getEdgeList().add(eg7);g.getEdgeList().add(eg8);g.getEdgeList().add(eg9);g.getEdgeList().add(eg10);
        
        g.getEdgeList().add(e21);
        
        g.getAdjacencyList().put("t1078", new ArrayList());g.getAdjacencyList().get("t1078").add(n3);
        g.getAdjacencyList().put("t1192", new ArrayList());g.getAdjacencyList().get("t1192").add(n4);
        g.getAdjacencyList().put("t1110", new ArrayList());g.getAdjacencyList().get("t1110").add(n4);
        g.getAdjacencyList().put("t1098", new ArrayList());g.getAdjacencyList().get("t1098").add(n5);g.getAdjacencyList().get("t1098").add(n6);
        g.getAdjacencyList().put("t1136", new ArrayList());g.getAdjacencyList().get("t1136").add(n5);g.getAdjacencyList().get("t1136").add(n7);
        g.getAdjacencyList().put("t1059", new ArrayList());g.getAdjacencyList().get("t1059").add(n8);g.getAdjacencyList().get("t1059").add(n9);g.getAdjacencyList().get("t1059").add(n12);g.getAdjacencyList().get("t1059").add(n13);g.getAdjacencyList().get("t1059").add(n14);
        g.getAdjacencyList().put("t1086", new ArrayList());g.getAdjacencyList().get("t1086").add(n10);g.getAdjacencyList().get("t1086").add(n15);
        g.getAdjacencyList().put("t1064", new ArrayList());g.getAdjacencyList().get("t1064").add(n8);g.getAdjacencyList().get("t1064").add(n20);g.getAdjacencyList().get("t1064").add(n21);
        g.getAdjacencyList().put("t1083", new ArrayList());g.getAdjacencyList().get("t1083").add(n11);
        g.getAdjacencyList().put("t1113", new ArrayList());g.getAdjacencyList().get("t1113").add(n16);
        g.getAdjacencyList().put("t1056", new ArrayList());g.getAdjacencyList().get("t1056").add(n18);g.getAdjacencyList().get("t1056").add(g3);
        g.getAdjacencyList().put("t1005", new ArrayList());g.getAdjacencyList().get("t1005").add(n17);g.getAdjacencyList().get("t1005").add(n18);
        g.getAdjacencyList().put("t1012", new ArrayList());g.getAdjacencyList().get("t1012").add(n19);
        g.getAdjacencyList().put("t1087", new ArrayList());g.getAdjacencyList().get("t1087").add(n19);
        g.getAdjacencyList().put("t1018", new ArrayList());g.getAdjacencyList().get("t1018").add(n22);g.getAdjacencyList().get("t1018").add(n23);g.getAdjacencyList().get("t1018").add(n24);
        g.getAdjacencyList().put("t1135", new ArrayList());g.getAdjacencyList().get("t1135").add(n25);
        g.getAdjacencyList().put("t1074", new ArrayList());g.getAdjacencyList().get("t1074").add(n17);
        
        g.getAdjacencyList().put("t1002", new ArrayList());g.getAdjacencyList().get("t1002").add(g1);
        g.getAdjacencyList().put("t1022", new ArrayList());g.getAdjacencyList().get("t1022").add(g1);
        g.getAdjacencyList().put("t1094", new ArrayList());g.getAdjacencyList().get("t1094").add(g2);
        g.getAdjacencyList().put("t1081", new ArrayList());g.getAdjacencyList().get("t1081").add(g3);
        g.getAdjacencyList().put("t1003", new ArrayList());g.getAdjacencyList().get("t1003").add(g3);
        g.getAdjacencyList().put("t1105", new ArrayList());g.getAdjacencyList().get("t1105").add(g4);
        g.getAdjacencyList().put("t1077", new ArrayList());g.getAdjacencyList().get("t1077").add(g4);
        g.getAdjacencyList().put("t1076", new ArrayList());g.getAdjacencyList().get("t1076").add(g4);
        g.getAdjacencyList().put("t1028", new ArrayList());g.getAdjacencyList().get("t1028").add(g4);
        
        
        g.getReverseAdjacencyList().put("exfiltration", new ArrayList());g.getReverseAdjacencyList().get("exfiltration").add(n17);g.getReverseAdjacencyList().get("exfiltration").add(n18);
        g.getReverseAdjacencyList().put("c2_communicaion", new ArrayList());g.getReverseAdjacencyList().get("c2_communicaion").add(n19);
        g.getReverseAdjacencyList().put("collect_credentials", new ArrayList());g.getReverseAdjacencyList().get("collect_credentials").add(n10);g.getReverseAdjacencyList().get("collect_credentials").add(n20);g.getReverseAdjacencyList().get("collect_credentials").add(n21);
        g.getReverseAdjacencyList().put("lateral_movement", new ArrayList());g.getReverseAdjacencyList().get("lateral_movement").add(n22);g.getReverseAdjacencyList().get("lateral_movement").add(n23);g.getReverseAdjacencyList().get("lateral_movement").add(n24);g.getReverseAdjacencyList().get("lateral_movement").add(n25);
        
        g.getReverseAdjacencyList().put("t1002", new ArrayList());g.getReverseAdjacencyList().get("t1002").add(n16);g.getReverseAdjacencyList().get("t1002").add(n11);
        g.getReverseAdjacencyList().put("t1022", new ArrayList());g.getReverseAdjacencyList().get("t1022").add(n10);g.getReverseAdjacencyList().get("t1022").add(n11);
        g.getReverseAdjacencyList().put("t1094", new ArrayList());g.getReverseAdjacencyList().get("t1094").add(n12);g.getReverseAdjacencyList().get("t1094").add(n13);
        g.getReverseAdjacencyList().put("t1081", new ArrayList());g.getReverseAdjacencyList().get("t1081").add(n7);
        g.getReverseAdjacencyList().put("t1003", new ArrayList());g.getReverseAdjacencyList().get("t1003").add(n7);
        g.getReverseAdjacencyList().put("t1105", new ArrayList());g.getReverseAdjacencyList().get("t1105").add(n14);
        g.getReverseAdjacencyList().put("t1077", new ArrayList());g.getReverseAdjacencyList().get("t1077").add(n14);
        g.getReverseAdjacencyList().put("t1076", new ArrayList());g.getReverseAdjacencyList().get("t1076").add(n14);
        g.getReverseAdjacencyList().put("t1028", new ArrayList());g.getReverseAdjacencyList().get("t1002").add(n15);
        
        g.getReverseAdjacencyList().put("t1074", new ArrayList());g.getReverseAdjacencyList().get("t1074").add(n9);
        
        g.getReverseAdjacencyList().put("t1113", new ArrayList());g.getReverseAdjacencyList().get("t1113").add(n5);
        g.getReverseAdjacencyList().put("t1056", new ArrayList());g.getReverseAdjacencyList().get("t1056").add(n6);
        g.getReverseAdjacencyList().put("t1005", new ArrayList());g.getReverseAdjacencyList().get("t1005").add(n8);
        g.getReverseAdjacencyList().put("t1012", new ArrayList());g.getReverseAdjacencyList().get("t1012").add(n5);
        g.getReverseAdjacencyList().put("t1087", new ArrayList());g.getReverseAdjacencyList().get("t1087").add(n5);
        g.getReverseAdjacencyList().put("t1018", new ArrayList());g.getReverseAdjacencyList().get("t1018").add(n5);
        g.getReverseAdjacencyList().put("t1135", new ArrayList());g.getReverseAdjacencyList().get("t1135").add(n6);
        
        g.getReverseAdjacencyList().put("t1083", new ArrayList());g.getReverseAdjacencyList().get("t1083").add(n5);g.getReverseAdjacencyList().get("t1083").add(n7);
        
        g.getReverseAdjacencyList().put("t1059", new ArrayList());g.getReverseAdjacencyList().get("t1059").add(n3);g.getReverseAdjacencyList().get("t1059").add(n4);
        g.getReverseAdjacencyList().put("t1086", new ArrayList());g.getReverseAdjacencyList().get("t1086").add(n3);
        g.getReverseAdjacencyList().put("t1064", new ArrayList());g.getReverseAdjacencyList().get("t1064").add(n4);
        
        g.getReverseAdjacencyList().put("t1098", new ArrayList());g.getReverseAdjacencyList().get("t1098").add(n0);
        g.getReverseAdjacencyList().put("t1136", new ArrayList());g.getReverseAdjacencyList().get("t1136").add(n1);g.getReverseAdjacencyList().get("t1136").add(n2);
        
        g.getReverseAdjacencyList().put("t1078", new ArrayList());
        g.getReverseAdjacencyList().put("t1192", new ArrayList());
        g.getReverseAdjacencyList().put("t1110", new ArrayList());
        
        
        //posteriorProbability
        g.getPosteriorProbability().put("t1078|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1192|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1110|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1098|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1136|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1059|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1086|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1064|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1083|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1113|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1056|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1005|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1074|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1002|exfiltration", 0.1);
        g.getPosteriorProbability().put("t1022|exfiltration", 0.1);
        
        
        g.getPosteriorProbability().put("t1078|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1192|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1110|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1098|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1136|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1059|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1012|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1087|c2_communicaion", 0.1);
        g.getPosteriorProbability().put("t1094|c2_communicaion", 0.1);
        
        
        g.getPosteriorProbability().put("t1078|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1192|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1110|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1098|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1136|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1059|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1086|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1064|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1087|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1056|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1081|collect_credentials", 0.1);
        g.getPosteriorProbability().put("t1003|collect_credentials", 0.1);
        
        
        g.getPosteriorProbability().put("t1078|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1192|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1110|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1098|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1136|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1059|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1086|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1018|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1135|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1105|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1077|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1076|lateral_movement", 0.1);
        g.getPosteriorProbability().put("t1028|lateral_movement", 0.1);
        
        //priorProbability
        g.getPriorProbability().put("t1078", 0.1);
        g.getPriorProbability().put("t1192", 0.1);
        g.getPriorProbability().put("t1110", 0.1);
        
        g.getPriorProbability().put("t1098", 0.1);
        g.getPriorProbability().put("t1136", 0.1);
        
        g.getPriorProbability().put("t1059", 0.1);
        g.getPriorProbability().put("t1086", 0.1);
        g.getPriorProbability().put("t1064", 0.1);g.getPriorProbability().put("t1083", 0.1);
        g.getPriorProbability().put("t1113", 0.1);g.getPriorProbability().put("t1056", 0.1);g.getPriorProbability().put("t1005", 0.1);g.getPriorProbability().put("t1012", 0.1);
        g.getPriorProbability().put("t1087", 0.1);g.getPriorProbability().put("t1018", 0.1);g.getPriorProbability().put("t1135", 0.1);g.getPriorProbability().put("t1074", 0.1);
        
        g.getPriorProbability().put("t1002", 0.1);g.getPriorProbability().put("t1022", 0.1);g.getPriorProbability().put("t1094", 0.1);g.getPriorProbability().put("t1081", 0.1);
        g.getPriorProbability().put("t1003", 0.1);g.getPriorProbability().put("t1105", 0.1);g.getPriorProbability().put("t1077", 0.1);g.getPriorProbability().put("t1076", 0.1);
        g.getPriorProbability().put("t1028", 0.1);
        
        g.getPriorProbability().put("exfiltration", 0.1);g.getPriorProbability().put("c2_communicaion", 0.1);g.getPriorProbability().put("collect_credentials", 0.1);g.getPriorProbability().put("lateral_movement", 0.1);
        
        return g;
    }
    
    public static LinkedHashMap<String, String> generateAllEdgesTest(String filename) throws IOException {
        LinkedHashMap<String, String> edgeList = new LinkedHashMap<String, String>();
        BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
        String line = null;
        while((line = in.readLine()) != null) {
//            System.out.println(line);
            String lines[] = line.split(",");
//            System.out.println(lines);
            edgeList.put(lines[0].trim() + "/" + lines[1].trim(), "1/2");
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
    
    public static ArrayList<String> initialAccessPoint(AttackGraphV2 graph) {
        ArrayList<String> iAList = new ArrayList<>();
        return iAList;
    }
    
    public static ArrayList<String> achieveableGoalList(AttackGraphV2 graph) {
        ArrayList<String> aGList = new ArrayList<>();
        
        aGList.add("17");
        GraphGeneratorV2.getNode("17", "command-and-control", graph.getNodeList());
        aGList.add("19");
        GraphGeneratorV2.getNode("19", "command-and-control", graph.getNodeList());
        aGList.add("18");
        GraphGeneratorV2.getNode("18", "command-and-control", graph.getNodeList());
        aGList.add("16");
        GraphGeneratorV2.getNode("16", "command-and-control", graph.getNodeList());
        return aGList;
    }
    
    public static LinkedHashMap<String, Double> setPosteriorProbability(AttackGraphV2 graph, ArrayList<String> aGList) throws IOException {

        LinkedHashMap<String, Double> posteriorProbability = new LinkedHashMap<>();
        for(String goalId : aGList) {
            for(TechniqueNodeV2 node : graph.getNodeList()) {
                if(GraphGeneratorV2.isReachable(graph, node, goalId)) {
                    posteriorProbability.put(node.getTechniqueId() + "|" + goalId, 0.35);
                }
            }
        }
        return posteriorProbability;
    }
    
    public static LinkedHashMap<String, Double> setPriorProbability(AttackGraphV2 graph) {
        LinkedHashMap<String, Double> priorProbability = new LinkedHashMap<>();
        for(TechniqueNodeV2 node : graph.getNodeList()) {
            priorProbability.put(node.getTechniqueId(), 0.1);
        }
        return priorProbability;
    }
    
    public static void main(String[] args) throws IOException {
        
        LinkedHashMap<String, String> eList = UseCase_One.generateAllEdgesTest("/My Things/Git/Research-Project/EvidentialReasoning/Evidential Reasoning/testCase/testcase1.txt");
        AttackGraphV2 g = UseCase_One.generateGraph(eList);
        ArrayList<String> iAList = UseCase_One.initialAccessPoint(g);
        ArrayList<String> aGList = UseCase_One.achieveableGoalList(g);
        LinkedHashMap<String, Double> posteriorP = UseCase_One.setPosteriorProbability(g, aGList);
        LinkedHashMap<String, Double> priorP = UseCase_One.setPriorProbability(g);
        g.setPriorProbability(priorP);
        g.setPosteriorProbability(posteriorP);
        Helper.CSVProcessorV2.createTacticLevel();
        
        TTPHuntManagerV2 manager = new TTPHuntManagerV2(g, iAList, aGList);
        manager.huntTTP();
    }
    
}

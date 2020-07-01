/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author mahmed27
 */
public class DataStorage {
    public static ArrayList<String> generateReportedTechniqueAttack01() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
        reportedTechnique.add("t1059");
//        reportedTechnique.add("t1036"); not included in graph
//        reportedTechnique.add("t1064");
        reportedTechnique.add("t1003");
        
        reportedTechnique.add("t1076");
//        reportedTechnique.add("t1075");
        
//        reportedTechnique.add("t1114"); not included in graph
        reportedTechnique.add("t1005");
        
//        reportedTechnique.add("t1002");
        return reportedTechnique;
    }
    public static ArrayList<String> generateReportedTechniqueTestCast1() {
        ArrayList<String> reportedTechnique = new ArrayList<>();
        reportedTechnique.add("5");
        reportedTechnique.add("245");
        reportedTechnique.add("100");
        reportedTechnique.add("75");
        reportedTechnique.add("132");
//        reportedTechnique.add("314");
        
//        reportedTechnique.add("5");
//        reportedTechnique.add("5");
//        reportedTechnique.add("9");
//        reportedTechnique.add("7");
//        reportedTechnique.add("19");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttack03() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1086");
        reportedTechnique.add("t1050");
        reportedTechnique.add("t1107");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1033");
        reportedTechnique.add("t1077");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttack18() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1053");
        reportedTechnique.add("t1107");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttack28() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1078");
        reportedTechnique.add("t1193");
        reportedTechnique.add("t1085");
        reportedTechnique.add("t1099");
        reportedTechnique.add("t1081");
        reportedTechnique.add("t1082");
        reportedTechnique.add("t1113");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttack29() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1078");
        reportedTechnique.add("t1193");
        reportedTechnique.add("t1085");
        reportedTechnique.add("t1099");
        reportedTechnique.add("t1081");
        reportedTechnique.add("t1082");
        reportedTechnique.add("t1113");
        return reportedTechnique;
    }
    
    
    public static ArrayList<String> generateReportedTechniqueAttack32() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1094");
        reportedTechnique.add("t1017");
        return reportedTechnique;
    }
    
    
    public static ArrayList<String> generateReportedTechniqueAttack34() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1032");
        reportedTechnique.add("t1086");
        reportedTechnique.add("t1100");
        reportedTechnique.add("t1110");
        reportedTechnique.add("t1201");
        reportedTechnique.add("t1076");
        reportedTechnique.add("t1056");
        return reportedTechnique;
    }
    
    
    public static ArrayList<String> generateReportedTechniqueAttack37() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1059");
        reportedTechnique.add("t1060");
        reportedTechnique.add("t1102");
        reportedTechnique.add("t1123");
        return reportedTechnique;
    }
    
    
    public static ArrayList<String> generateReportedTechniqueAttackAxiom() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1015");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1076");
        return reportedTechnique;
    }
    
    
    public static ArrayList<String> generateReportedTechniqueAttackBronzeButtler() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1036");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1097");
        reportedTechnique.add("t1005");
        return reportedTechnique;
    }
    
    
    public static ArrayList<String> generateReportedTechniqueAttackCarBanak() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1050");
        reportedTechnique.add("t1089");
        reportedTechnique.add("t1085");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackKittens() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1086");
        reportedTechnique.add("t1116");
        reportedTechnique.add("t1089");
        reportedTechnique.add("t1002");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackDeepPanda() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1086");
        reportedTechnique.add("t1047");
        reportedTechnique.add("t1015");
        reportedTechnique.add("t1100");
        reportedTechnique.add("t1057");
        reportedTechnique.add("t1077");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackDragonFly() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1133");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1100");
        reportedTechnique.add("t1107");
        reportedTechnique.add("t1135");
        reportedTechnique.add("t1076");
        reportedTechnique.add("t1113");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackDustStrom() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1027");
        reportedTechnique.add("t1083");
        reportedTechnique.add("t1005");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackElderWood() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1027");
//        reportedTechnique.add("t1083");
        reportedTechnique.add("t1105");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackFIN5() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1108");
        reportedTechnique.add("t1074");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackFIN6() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1053");
        reportedTechnique.add("t1060");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1018");
        reportedTechnique.add("t1119");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackFIN7() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1173");
        reportedTechnique.add("t1138");
        reportedTechnique.add("t1027");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackFIN8() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1047");
        reportedTechnique.add("t1068");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1063");
        reportedTechnique.add("t1074");
        reportedTechnique.add("t1048");
        reportedTechnique.add("t1043");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackFIN10() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1053");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1033");
        reportedTechnique.add("t1105");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackGamaredonGroup() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1082");
        reportedTechnique.add("t1033");
        reportedTechnique.add("t1025");
        reportedTechnique.add("t1041");
        reportedTechnique.add("t1071");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackGroup5() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1027");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1113");
        reportedTechnique.add("t1065");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackKe3Chang() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1059");
        reportedTechnique.add("t1069");
        reportedTechnique.add("t1077");
        reportedTechnique.add("t1005");
        reportedTechnique.add("t1002");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackLazarusGroup() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1059");
        reportedTechnique.add("t1050");
        reportedTechnique.add("t1055");
        reportedTechnique.add("t1110");
//        reportedTechnique.add("t1057");
        reportedTechnique.add("t1105");
        reportedTechnique.add("t1002");
        reportedTechnique.add("t1032");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackLeviathan() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1192");
        reportedTechnique.add("t1117");
        reportedTechnique.add("t1023");
        reportedTechnique.add("t1102");
        reportedTechnique.add("t1057");
        reportedTechnique.add("t1074");
        reportedTechnique.add("t1102");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackMagicHound() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1194");
        reportedTechnique.add("t1204");
        reportedTechnique.add("t1023");
        reportedTechnique.add("t1060");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1102");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackMenupass() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1199");
        reportedTechnique.add("t1053");
        reportedTechnique.add("t1038");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1087");
        reportedTechnique.add("t1021");
        reportedTechnique.add("t1039");
        reportedTechnique.add("t1002");
        reportedTechnique.add("t1090");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackMuddyWater() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1193");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1060");
        reportedTechnique.add("t1027");
        reportedTechnique.add("t1003");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackOilRig() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1108");
        reportedTechnique.add("t1100");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1016");
        reportedTechnique.add("t1021");
        reportedTechnique.add("t1119");
        reportedTechnique.add("t1048");
        reportedTechnique.add("t1094");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackPatchWork() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1189");
        reportedTechnique.add("t1086");
        reportedTechnique.add("t1060");
        reportedTechnique.add("t1088");
        reportedTechnique.add("t1102");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1083");
        reportedTechnique.add("t1076");
        reportedTechnique.add("t1005");
        reportedTechnique.add("t1132");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackPlatinum() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1189");
        reportedTechnique.add("t1204");
        reportedTechnique.add("t1179");
        reportedTechnique.add("t1055");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1105");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1105");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackSnowBug() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1059");
        reportedTechnique.add("t1036");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1135");
        reportedTechnique.add("t1039");
        reportedTechnique.add("t1002");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueAttackStealthFalcon() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1047");
        reportedTechnique.add("t1053");
        reportedTechnique.add("t1064");
        reportedTechnique.add("t1003");
        reportedTechnique.add("t1033");
        reportedTechnique.add("t1005");
//        reportedTechnique.add("t1041");
//        reportedTechnique.add("t1032");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueThreatGroup1314() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1072");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1077");
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueThreatGroup3390() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1028");
        reportedTechnique.add("t1133");
        reportedTechnique.add("t1053");
        reportedTechnique.add("t1089");
        reportedTechnique.add("t1056");
        reportedTechnique.add("t1016");
        reportedTechnique.add("t1028");
        reportedTechnique.add("t1030");
        reportedTechnique.add("t1043");
        
        
        return reportedTechnique;
    }
    
    public static ArrayList<String> generateReportedTechniqueTurla() {
        
        ArrayList<String> reportedTechnique = new ArrayList<>();
//        reportedTechnique.add("t1043");
        reportedTechnique.add("t1078");
        reportedTechnique.add("t1066");
        reportedTechnique.add("t1110");
        reportedTechnique.add("t1049");
        reportedTechnique.add("t1077");
        
        
        return reportedTechnique;
    }
    
    public static ArrayList<String> randomReportedTechniqueGenerator(Double percentage, int noOfAttack) throws FileNotFoundException, IOException {
        ArrayList<String> rTechniques = new ArrayList<>();
        
        HashMap<Integer, ArrayList<String>> collector = new HashMap<> ();
        HashMap<Integer, String> attackMap = new HashMap<> ();
        
        File[] files = new File("/Users/mahmed27/Google Drive/Slide to merge/re55atackfiles").listFiles();
        Integer  k = 0;
        for(File file : files) {
            attackMap.put(k, file.getName());
            BufferedReader in = new BufferedReader(new FileReader(file));
            boolean skipFirstLine = false;
            String line = null;
            while((line = in.readLine()) != null) {
                if(!skipFirstLine) {
                    skipFirstLine = true;
                    continue;
                }
                String[] mColumn = line.trim().split(",");
                if(mColumn[4].trim().toLowerCase().contains("pre-")) continue;
                if(collector.containsKey(k)) {
                    if (!collector.get(k).contains(mColumn[4].trim().toLowerCase())) {
                        collector.get(k).add(mColumn[4].trim().toLowerCase());
                    }
                } else {
                    ArrayList<String> tList = new ArrayList<>();
                    tList.add(mColumn[4].trim().toLowerCase());
                    collector.put(k, tList);
                }
                
            }
            in.close();
            k++;
        }
        
        
        for(int g = 0; g < noOfAttack; g++) {
            
            Random rand = new Random();
            Random rand2 = new Random();
            Random rand3 = new Random();
            int attackno = rand.nextInt(collector.size());
            
            System.out.println("Attack Under Consideration: " + attackMap.get(attackno));
            
            ArrayList<String> t = collector.get(attackno);
            int noTToTake = (int) Math.ceil(t.size() * percentage);
            
            for(int  i = 0; i < noTToTake; i++) {
                int ttN = rand2.nextInt(t.size());
                String tS = t.get(ttN);
                if(!rTechniques.contains(tS)) {
                    rTechniques.add(tS);
                    t.remove(ttN);

                }
            }
            collector.remove(attackno);
        }
        
        return rTechniques;
    }
}

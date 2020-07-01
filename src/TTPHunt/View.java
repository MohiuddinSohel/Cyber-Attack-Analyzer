/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TTPHunt;

import SyntheticAttackChain.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author mahmed27
 */
public class View implements ActionListener {
    
    private TTPHuntManagerV3 ttpManager;
    private Boolean buttonClickedBefore = false;
    JButton buttongenerateObservables = new JButton("GenerateObservables");
    JTextField portionText = new JTextField();
    JTextField distributionText = new JTextField();
    
    public View(TTPHuntManagerV3 manager) {
        this.ttpManager = manager;
    }
    
    public void selectGui() {
        JFrame frame = new JFrame("Press Button");
        frame.setLayout(new GridLayout(8,8));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JButton button = new JButton("Run");
        button.setBackground(Color.DARK_GRAY);
        frame.getContentPane().add(button);
        button.setToolTipText("Click me to generate hypothesis and evaluate it. "
                + "\nOnly for the first click, a new hypothesis is generated."
                + "\n In later clikes, it will only evalute already generated hipothesis with updated observables");
        button.setVisible(true);
        button.addActionListener((ActionListener) this);
        
        JButton buttonReRun = new JButton("ReGenerate");
        button.setBackground(Color.DARK_GRAY);
        frame.getContentPane().add(buttonReRun);
        buttonReRun.setVisible(true);
        buttonReRun.setToolTipText("Click me to Change hypothesis and evaluate it.\n"
                + "Equivalent to first click of run button");
        buttonReRun.addActionListener((ActionListener) this);
        
        
        
        portionText.setVisible(true);
        frame.getContentPane().add(portionText);
        
        distributionText.setVisible(true);
        frame.getContentPane().add(distributionText);
        
        
        buttongenerateObservables.setBackground(Color.DARK_GRAY);
        buttongenerateObservables.addActionListener(this);
        frame.getContentPane().add(buttongenerateObservables);
        buttongenerateObservables.setVisible(true);
        
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = ((JButton)e.getSource()).getActionCommand();
        ((JButton)e.getSource()).setEnabled(false);
        if(command.toLowerCase().trim().equals("run")) {
            
            if(this.buttonClickedBefore) {
                try {
                    this.ttpManager.reEvaluateCurrentHypothesisLog(this.readTheReportedTechnique(), this.ttpManager.threatReasoning.cProbability);
                    // this.ttpManager.threatReasoning.normalizedcProbability
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.buttonClickedBefore = true;
                try {
                    this.ttpManager.huntTTP(this.readTheReportedTechnique());
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if(command.toLowerCase().trim().equals("regenerate")) {
            try {
//                this.ttpManager = new TTPHuntManagerV3();
                this.ttpManager.huntTTP(this.readTheReportedTechnique());
                this.buttonClickedBefore = true;
            } catch (IOException ex) {
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if(command.toLowerCase().trim().equals("generateobservables")) {
            Double portion = Double.parseDouble(portionText.getText().trim());
            String distribution = distributionText.getText().trim();
            try {
                String filePath = "/Users/mahmed27/Dropbox/Git/Research-Project/AutoTTPHunt-EvidentialReasoning/Evidential Reasoning/AttackData/observedTG.txt";
                ArrayList<String> observable = SyntheticAttackChain.generateObserverdTechniqueList(portion, filePath, distribution);
                String t = "";
                for (int i = 0; i < observable.size(); i++) {
                    t += observable.get(i);
                    if(i != observable.size() - 1) {
                        t += ", ";
                    }
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(SyntheticAttackChain.reportedTFile)));
                writer.write(t);
                writer.close();
                this.buttonClickedBefore = false;
            } catch (IOException ex) {
                ((JButton)e.getSource()).setEnabled(true);
                Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
        ((JButton)e.getSource()).setEnabled(true);
    }
    
    public ArrayList<String> readTheReportedTechnique() throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(SyntheticAttackChain.reportedTFile)));
        String line = "";
        ArrayList<String> techniquelist = new ArrayList<>();
        while((line = reader.readLine()) != null) {
            String[] Tlist = line.trim().split(",");
            for(String t : Tlist) {
                techniquelist.add(t.trim());
            }
        }
        reader.close();
        return techniquelist;
    }
    
}

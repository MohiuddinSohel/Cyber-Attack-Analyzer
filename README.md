# Cyber-Attack-Analyzer
Cyber-Attack-Analyzer analyzes attacker activities to do following tasks-
 - Detects high-level attack techniques used by the attacker
 - Predicts what is the next attack technique the attacker going to execute

In the process of attack detection and prediction, it uses a correlation matrix among different attack techniques reported by Mitre and takes observed attack technique as input. Using the technique correlation matrix and observed attacker techniques, Cyber-Attack-Analyzer at-first generates attack hypothesis that explains observed attack techniques, calculates fidelity score of the generated hypothesis. If the score is not satisfactory, it sugggests to perform active investigation to verify unobserved techniques present in the hypothesis and recalculates the fidelity score. This process continues until the fidelity score is satisfactory

## How to Build and Run the Cyber-Attack-Analyzer
This is a java project built on using java JDK 8.1. The entry point of this project is the src/TTPHunt/View.java. Before building and runing the code, you have to make following changes to set approprite input data file-
 - Change appropriate observed technique file name
 - Change the attack data file which will be used to generate the attack technique correlation matrix 

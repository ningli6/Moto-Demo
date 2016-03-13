package javaEmail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SendEmail {  
	/**
	 * Start a Python script to send email and includes all the results
	 * @param plotDir TODO
	 * @param from       sender
	 * @param to         receiver
	 * @param message    additional email content
	 * @param noc        number of channels
	 * @param icVSq      to plot ic vs q
	 * @param gMapNo     to include google map for no countermeasure
	 * @param gMapAd     to include google map for additive noise
	 * @param gMapTf     to include google map for transfiguration
	 * @param gMapKa     to include google map for k anonymity
	 * @param gMapKc     to include google map for k clustering
	 * @param tradeOffAd to include trade-off curve for additive noise
	 * @param tradeOffTf to include trade-off curve for transfiguration
	 * @param tradeOffKA to include trade-off bar for k anonymity
	 * @param tradeOffKC to include trade-off bar for k clustering
	 * @return           true if everything works
	 */
	public static boolean send(String plotDir, String from, String to, String message,
			int noc, 
			boolean icVSq, boolean gMapNo, boolean gMapAd, boolean gMapTf, boolean gMapKa, 
			boolean gMapKc, boolean tradeOffAd, boolean tradeOffTf, boolean tradeOffKA,
			boolean tradeOffKC) {
		System.out.println("Start sending email...");
		if (from == null || from.length() == 0 || to == null || to.length() == 0) return false;
        String s = null;
        String content = "Hi!\n\nThanks for using our Web-based Simulation Tool for Evaluating incumbent user's location Privacy in Spectrum sharing (STEPS).\n";
        content += "\nSimulation results are attached to this email.\n";
        content += "Your simulation configurations is recorded in the text file as an attachment.\n";
        if (message != null && message.length() > 0) {
            content += message;
        }
        content += "Please refer to our website for more instructions and examples.\n";
        content = content.replaceAll(" ", "_");
        content = content.replaceAll("\n", "<br>");

        try {
        	String cmd = "python C:\\Users\\Pradeep\\Desktop\\motoDemo\\python\\send.py " + plotDir + " " + from + " " + to + " " + content;
            if (icVSq) {
            	cmd += " ICvsQ.png";                 // ic vs q with random query
            	cmd += " ICvsQ_Smart.png";           // ic vs q with smart query
            	cmd += " ICvsQ_Random_Smart.png";    // random vs smart query without countermeasure
            	cmd += " cmpbar.png";                // random vs smart query with countermeasures
            }
            if (gMapNo) {
            	for (int k = 0; k < noc; k++) {
            		cmd += " No_Countermeasure_" + k + "_gMaps.png";
            		cmd += " smart_No_Countermeasure_" + k + "_gMaps.png";
            	}
            }
            if (gMapAd) {
            	for (int k = 0; k < noc; k++) {
            		cmd += " Additive_Noise_" + k + "_gMaps.png";
            		cmd += " smart_Additive_Noise_" + k + "_gMaps.png";
            	}
            }
            if (gMapTf) {
            	for (int k = 0; k < noc; k++) {
            		cmd += " Transfiguration_" + k + "_gMaps.png";
            		cmd += " smart_Transfiguration_" + k + "_gMaps.png";
            	}
            }
            if (gMapKa) {
            	for (int k = 0; k < noc; k++) {
            		cmd += " K_Anonymity_" + k + "_gMaps.png";
            		cmd += " smart_K_Anonymity_" + k + "_gMaps.png";
            	}
            }
            if (gMapKc) {
            	for (int k = 0; k < noc; k++) {
            		cmd += " K_Clustering_" + k + "_gMaps.png";
            		cmd += " smart_K_Clustering_" + k + "_gMaps.png";
            	}
            }
            if (tradeOffAd) {
            	cmd += " traddOff_AdditiveNoise.png";
            	cmd += " traddOff_smart_AdditiveNoise.png";
            }
            if (tradeOffTf) {
            	cmd += " traddOff_Transfiguration.png";
            	cmd += " traddOff_smart_Transfiguration.png";
            }
            if (tradeOffKA) {
            	cmd += " traddOff_KAnonymity.png";
            	cmd += " traddOff_smart_KAnonymity.png";
            }
            if (tradeOffKC) {
            	cmd += " traddOff_KClustering.png";
            	cmd += " traddOff_smart_KClustering.png";
            }
            cmd += " emailInfo.txt";
        	// using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(cmd);
            
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
            
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
            
            // read the output from the command
            // System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
             
            // read any errors from the attempted command
            // System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
}
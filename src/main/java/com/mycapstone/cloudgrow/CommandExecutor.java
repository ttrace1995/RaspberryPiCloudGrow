
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycapstone.cloudgrow;

import com.hopding.jrpicam.RPiCamera;
import com.hopding.jrpicam.enums.AWB;
import com.hopding.jrpicam.enums.DRC;
import com.hopding.jrpicam.enums.Encoding;
import com.hopding.jrpicam.exceptions.FailedToRunRaspistillException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;

/**
 *
 * @author Tyler Tracey
 */
public class CommandExecutor {
    
    
    public static String Image_File_Name;
    public static RPiCamera piCamera;
    
    public static synchronized void executeStandardPythonCommand(String scriptName) throws IOException, InterruptedException {
        String command = Constants.PYTHON_CMD + Constants.PROJECT_DIRECTORY + Constants.PYTHON_PROJECT_PATH + scriptName;
        Process p = Runtime.getRuntime().exec(command);
    } 
    
    public static synchronized String executeReturnPythonCommand( String scriptName ) throws IOException, InterruptedException {
        String s;
        String command = Constants.PYTHON_CMD + Constants.PROJECT_DIRECTORY + Constants.PYTHON_PROJECT_PATH + scriptName;
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader stdInput = new BufferedReader(new
                         InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new
                         InputStreamReader(p.getErrorStream()));
        // read the output from the command
        while ((s = stdInput.readLine()) != null) {
            return s;
        }
        // read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            return s;
        }
        return s;
    }
    
    public static synchronized void takePhoto() throws FailedToRunRaspistillException, IOException, InterruptedException {
        
        Constants.IMAGE_LOCK.lock();
        
        Image_File_Name = Instant.now().toString()+".png";
        piCamera = new RPiCamera(Constants.IMAGE_FILE_PATH);
        
        if (piCamera != null) {
            piCamera.setEncoding(Encoding.PNG);
            piCamera.setAWB(AWB.AUTO);
            piCamera.setDRC(DRC.OFF);
            piCamera.setQuality(100);
            piCamera.setSharpness(100);
		
            piCamera.takeStill(Image_File_Name, 650, 650);
			
        }  
    }
}

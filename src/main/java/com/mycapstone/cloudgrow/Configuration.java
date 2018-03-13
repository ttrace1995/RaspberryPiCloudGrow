/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycapstone.cloudgrow;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pi
 */
public class Configuration {
    
    public static synchronized void changeStateSettings (String val) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "states.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            switch (val) {
                case "lightOn.py":
                    jsonObject.replace("LIGHT_STATE", 1);
                    break;
                case "lightOff.py":
                    jsonObject.replace("LIGHT_STATE", 0);
                    break;
                case "fanOn.py":
                    jsonObject.replace("FAN_STATE", 1);
                    break;
                case "fanOff.py":
                    jsonObject.replace("FAN_STATE", 0);
                    break;
                case "pumpOn.py":
                    jsonObject.replace("PUMP_STATE", 1);
                    break;
                case "pumpOff.py":
                    jsonObject.replace("PUMP_STATE", 0);
                    break;
                default:
                    break;
            }
                    
            FileWriter writer = new FileWriter(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "states.json");
            writer.write(jsonObject.toJSONString());
            writer.flush();
            
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static synchronized void loadPreviousStates() {
        
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "states.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            long lightSetting = (long) jsonObject.get("LIGHT_STATE");
            long fanSetting = (long) jsonObject.get("FAN_STATE");
            long pumpSetting = (long) jsonObject.get("PUMP_STATE");
            
            System.out.println(lightSetting);
            System.out.println(fanSetting);
            System.out.println(pumpSetting);
            
            
            if (lightSetting == 1) {
                CommandExecutor.executeStandardPythonCommand("lightOn.py");
            }
            else {
                CommandExecutor.executeStandardPythonCommand("lightOff.py");
            }
            
            if (fanSetting == 1) {
                CommandExecutor.executeStandardPythonCommand("fanOn.py");
            }
            else {
                CommandExecutor.executeStandardPythonCommand("fanOff.py");
            }
            
            if (pumpSetting == 1) {
                CommandExecutor.executeStandardPythonCommand("pumpOn.py");
            }
            else {
                CommandExecutor.executeStandardPythonCommand("pumpOff.py");
            }
            
                    
            
            
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        
        } catch (InterruptedException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

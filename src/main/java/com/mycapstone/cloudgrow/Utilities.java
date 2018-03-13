/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycapstone.cloudgrow;

import java.io.File;
import java.util.Timer;
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
 * @author Tyler Tracey
 */
public class Utilities {
    
    public static void StartDataScheduler() {
        Timer timer = new Timer();
        timer.schedule(new DataScheduler(), 0, Constants.DEFAULT_TIME_DELAY);
    }
    
    public static String getMessageKey( String message ) {
        return message.split(":")[0];
    }
    
    public static String getMessageValue( String message ) {
        return message.split(":")[1];
    }
    
    public static void deleteLocalImage() {
        boolean success = (new File(Constants.IMAGE_FILE_PATH + File.separator + CommandExecutor.Image_File_Name)).delete();
        if (success == true) {
            System.out.println(CommandExecutor.Image_File_Name + " deleted");
        }
        else {
            System.out.println("Something went wrong trying to delete " + CommandExecutor.Image_File_Name);
        }
        Constants.IMAGE_LOCK.unlock();
    }
    
    public static double formatTempData(String data) {

        String[] splitData = data.split("  ");
        
        String temp = splitData[0];
         
        temp = temp.split("=")[1];
        temp = temp.substring(0, temp.length()-2);
        
        double doubleTemp = Double.valueOf(temp);
        doubleTemp = (doubleTemp * 1.8) + 32;
        doubleTemp = Math.round(doubleTemp * 100);
        doubleTemp = (doubleTemp / 100);
        
        return doubleTemp;
        
    }
    
    public static double formatHumidData(String data) {
        
        String[] splitData = data.split("  ");
        
        String humid = splitData[1];
        
        humid = humid.split("=")[1];
        humid = humid.substring(0, humid.length()-2);
        double doubleHumid = Double.valueOf(humid);
        
        return doubleHumid;
    }
    
    public static synchronized void changeStateSettings (String val) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "state.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            switch (val) {
                case "lightOn.py":
                    jsonObject.replace("LIGHT_SETTING", 1);
                    break;
                case "lightOff.py":
                    jsonObject.replace("LIGHT_SETTING", 0);
                    break;
                case "fanOn.py":
                    jsonObject.replace("FAN_SETTING", 1);
                    break;
                case "fanOff.py":
                    jsonObject.replace("FAN_SETTING", 0);
                    break;
                case "pumpOn.py":
                    jsonObject.replace("PUMP_SETTING", 1);
                    break;
                case "pumpOff.py":
                    jsonObject.replace("PUMP_SETTING", 0);
                    break;
                default:
                    break;
            }
                    
            FileWriter writer = new FileWriter(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "state.json");
            writer.write(jsonObject.toJSONString());
            writer.flush();
            
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static synchronized void changeStateCurrent (String val) {
      
        try {
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "state.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            if (val.contains("light") == true) {
                String lightState = CommandExecutor.executeReturnPythonCommand("stateCheckLight.py");
                jsonObject.replace("LIGHT_STATE", Integer.valueOf(lightState));
            }
            else if (val.contains("fan") == true) {
                String fanState = CommandExecutor.executeReturnPythonCommand("stateCheckFan.py");
                jsonObject.replace("FAN_STATE", Integer.valueOf(fanState));
            }
            else {
                String pumpState = CommandExecutor.executeReturnPythonCommand("stateCheckPump.py");
                jsonObject.replace("PUMP_STATE", Integer.valueOf(pumpState));
            }
            
            FileWriter writer = new FileWriter(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "state.json");
            writer.write(jsonObject.toJSONString());
            writer.flush();
            
        } catch (IOException | ParseException | InterruptedException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}

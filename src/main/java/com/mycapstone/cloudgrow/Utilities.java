/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycapstone.cloudgrow;

import java.util.Timer;
import java.io.File;




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
        //Constants.IMAGE_LOCK.unlock();
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
}

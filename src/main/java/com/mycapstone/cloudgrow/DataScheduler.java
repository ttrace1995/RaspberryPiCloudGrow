/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycapstone.cloudgrow;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.microsoft.azure.sdk.iot.device.*;
import java.io.File;
import java.io.FileReader;
import java.sql.Time;
import java.time.Instant;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author pi
 */
public class DataScheduler extends TimerTask {

    @Override
    public void run() {
        getTempHumidData();
    }
    
    public static void getTempHumidData() {
        String data;
        try {
            data = CommandExecutor.executeReturnPythonCommand("temp.py");
             double temp = Utilities.formatTempData(data);
            double humid = Utilities.formatHumidData(data);
            sendDataToCloud(temp, humid);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(DataScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private static synchronized void sendDataToCloud(double temp, double humid) {
        TelemetryDataPoint telemetryDataPoint = new TelemetryDataPoint();
        telemetryDataPoint.deviceId = Constants.DEVICE_ID;
        telemetryDataPoint.temperature = temp;
        telemetryDataPoint.humidity = humid;
        telemetryDataPoint.timestamp = Instant.now().toString();
        
        String msgStr = telemetryDataPoint.serialize();
        Message msg = new Message(msgStr);
        msg.setProperty("messageType", "temp_humid_data");
        msg.setMessageId(java.util.UUID.randomUUID().toString()); 
        System.out.println("Sending: " + msgStr);
        
        Object lockobj = new Object();
        EventCallback callback = new EventCallback();
        App.client.sendEventAsync(msg, callback, lockobj);

//        synchronized (lockobj) {
//            try {
//                lockobj.wait();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(DataScheduler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }  
    }
    
    public static synchronized void sendStateInfoToCloud() {
        
        StateInfoDataPoint sidp = new StateInfoDataPoint();
        
        try {
            
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(Constants.PROJECT_DIRECTORY + File.separator + Constants.CONFIG_PROJECT_PATH + "states.json"));
            JSONObject jsonObject = (JSONObject) obj;
            
            sidp.lightState = (long) jsonObject.get("LIGHT_STATE");
            sidp.fanState = (long) jsonObject.get("FAN_STATE");
            sidp.pumpState = (long) jsonObject.get("PUMP_STATE");
            sidp.timestamp = Instant.now().toString();
            
        } catch (IOException | ParseException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        
        } 
        
        String msgStr = sidp.serialize();
        Message msg = new Message(msgStr);
        msg.setProperty("messageType", "state_data");
        msg.setMessageId(java.util.UUID.randomUUID().toString()); 
        System.out.println("Sending: " + msgStr);
        
        Object lockobj = new Object();
        EventCallback callback = new EventCallback();
        App.client.sendEventAsync(msg, callback, lockobj);

//        synchronized (lockobj) {
//            try {
//                lockobj.wait();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(DataScheduler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }
    
    private static class TelemetryDataPoint {
        public String deviceId;
        public double temperature;
        public double humidity;
        public String timestamp;

        public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
        }
    }
    
    private static class StateInfoDataPoint {
        public long lightState;
        public long fanState;
        public long pumpState;
        public String timestamp;
        
        public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
        }
    }
    
    public static class EventCallback implements IotHubEventCallback {
        @Override
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to message with status: " + status.name());

            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }
    
}



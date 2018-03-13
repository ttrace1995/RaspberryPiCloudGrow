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
import java.io.*;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author pi
 */
public class DataScheduler extends TimerTask {

    @Override
    public void run() {
        try {
            String data = CommandExecutor.executeReturnPythonCommand("temp.py");
            double temp = Utilities.formatTempData(data);
            double humid = Utilities.formatHumidData(data);
            sendDataToCloud(temp, humid);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(DataScheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void sendDataToCloud(double temp, double humid) {
        TelemetryDataPoint telemetryDataPoint = new TelemetryDataPoint();
        telemetryDataPoint.deviceId = Constants.DEVICE_ID;
        telemetryDataPoint.temperature = temp;
        telemetryDataPoint.humidity = humid;
        
        String msgStr = telemetryDataPoint.serialize();
        Message msg = new Message(msgStr);
        msg.setProperty("temperatureAlert", (temp > 30) ? "true" : "false");
        msg.setMessageId(java.util.UUID.randomUUID().toString()); 
        System.out.println("Sending: " + msgStr);
        
        Object lockobj = new Object();
        EventCallback callback = new EventCallback();
        App.client.sendEventAsync(msg, callback, lockobj);

        synchronized (lockobj) {
            try {
                lockobj.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(DataScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    }
    
    private static class TelemetryDataPoint {
        public String deviceId;
        public double temperature;
        public double humidity;

        public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
        }
    }
    
    private static class EventCallback implements IotHubEventCallback {
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



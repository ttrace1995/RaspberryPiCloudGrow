/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycapstone.cloudgrow;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pi
 */
public class Constants {
    
    private Constants() { }
    
    public static final Lock IMAGE_LOCK = new ReentrantLock();
    public static final Lock CONFIG_LOCK = new ReentrantLock();
    
    //20 seconds
    public static int DEFAULT_TIME_DELAY = 60000;
    
    public static final String PROJECT_DIRECTORY = "/home/pi/NetBeansProjects/CloudGrow";
    public static final String PYTHON_PROJECT_PATH = "/src/main/java/com/mycapstone/cloudgrow/python/";
    public static final String CONFIG_PROJECT_PATH = "/src/main/java/com/mycapstone/cloudgrow/configuration/";
    public static final String IMAGE_PROJECT_PATH = "/src/main/java/com/mycapstone/cloudgrow/image/";
    public static final String PYTHON_CMD = "python ";
    
    public static final IotHubClientProtocol PROTOCOL = IotHubClientProtocol.MQTT;
    public static final String IOTHUB_CONNECTION_STRING = "HostName=CapstoneHub.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=dEcKcNThk3JKf1Ord2xjjdwbi7you4f362d0rmIT4X8=";
    public static final String DEVICE_ID = "CloudGrowDevice";
    
    public static final String OK_EMPTY = "OK_EMPTY";
    
    public static final String IMAGE_FILE_PATH = PROJECT_DIRECTORY + IMAGE_PROJECT_PATH;
    
}

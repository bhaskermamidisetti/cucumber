package com.org.ui.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class dataStorage {


    private static final Logger logger = LoggerFactory.getLogger(dataStorage.class);

    private static ThreadLocal<String> portalName = new ThreadLocal<>();
    private static ThreadLocal<String> customErrmsg = new ThreadLocal<>();
    private static ThreadLocal<String> ScenarioName = new ThreadLocal<>();

    private static ThreadLocal<HashMap<String, String>> userdetails = new ThreadLocal<HashMap<String, String>>() {
        @Override
        protected HashMap<String, String> initialValue() {
            return new HashMap< >();
        }
    };


    private dataStorage() {
    }

    public static void resetAllDataStorage() {
        resetCustomErrmsg();
        resetPortalName();
        resetuserdetails();
        resetScenarioName();

    }

    public static void resetuserdetails() {
        userdetails.get().clear();
    }

    /**
     * Generic storage function into userdetails Map
     *
     * @param key
     * @param value
     */

    public static void setUserDetails(String key, String value) {
        userdetails.get().put(key, value);
    }

    public static String getUserDetails(String key) {
        return userdetails.get().get(key);
    }

    public static void setBinId(String binId) {
        setUserDetails("BinId", binId);
    }

    public static void setPlant(String plant) {
        setUserDetails("Plant", plant);
    }

    public static void setFloor(String floor) {
        setUserDetails("Floor", floor);
    }

    public static void setZone(String zone) {
        setUserDetails("Zone", zone);
    }

    public static void setRow(String row) {
        setUserDetails("Row", row);
    }

    public static void setColumn(String column) {
        setUserDetails("Column", column);
    }

    public static void setCustomer(String customername) {
        setUserDetails("CustomerName", customername);
    }

    public static void setBinstyle(String binstyle) {
        setUserDetails("BinStyle", binstyle);
    }

    public static void setBinstatus(String binstatus) {
        setUserDetails("BinStatus", binstatus);
    }

    public static String getBinId() {
        return userdetails.get().get("BinId");
    }

    public static String getPlantName() {
        return userdetails.get().get("Plant");
    }

    public static String getFloorName() {
        return userdetails.get().get("Floor");
    }

    public static String getZoneName() {
        return userdetails.get().get("Zone");
    }

    public static String getRowName() {
        return userdetails.get().get("Row");
    }

    public static String getColumnName() {
        return userdetails.get().get("Column");
    }

    public static String getCustomerName() {
        return userdetails.get().get("CustomerName");
    }

    public static String getBinStyle() {
        return userdetails.get().get("BinStyle");
    }

    public static String getBinStatus() {
        return userdetails.get().get("BinStatus");
    }

    public static void setCustomErrmsg(String customErrmsg) {
        if (dataStorage.customErrmsg.get() != null) {
            dataStorage.customErrmsg.set(dataStorage.customErrmsg.get() + "\n" + customErrmsg);
        } else {
            dataStorage.customErrmsg.set(customErrmsg);
        }
    }

    public static void resetCustomErrmsg() {
        dataStorage.customErrmsg.set("");
    }

    public static String getCustomErrmsg() {
        return dataStorage.customErrmsg.get();
    }

    public static String getPortalName() {
        return portalName.get();
    }

    public static void setPortalName(String portalname) {
        dataStorage.portalName.set(portalname);
    }

    public static void resetPortalName() {
        dataStorage.portalName.set(null);
    }

    public static String getScenarioName() {
        return dataStorage.ScenarioName.get();
    }

    public static void setScenarioName(String name) {
        dataStorage.ScenarioName.set(name);
    }

    public static void resetScenarioName() {
        dataStorage.ScenarioName.set(null);
    }
}

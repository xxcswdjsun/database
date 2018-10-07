package org.jieheng.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

public class HelpUtl {	

    
	HelpUtl() {
		
	}
	
    public static void logPrint(String info, Exception e) {
    	if (info == null && e != null) {
    		System.out.println("ERROR: " + e.toString());
    		e.printStackTrace();
    	} else if (info != null && e == null){
    		System.out.println(info);
    	} else {
    		System.out.println(info + e.toString());
    		e.printStackTrace();
    	}
    }
    
    public static void printMapList(Map<String, String>[] map_list) {
    	int n = 0;
    	for (Map<String, String> m : map_list) {
    		n++;
    		for (Entry<String, String> entry : m.entrySet()) { 
    			logPrint(n + ": " + "Key = " + entry.getKey() + ", Value = " + entry.getValue(), null); 
    		}
    	}
    }
    
	public static void printMap(Map<String, String> map) {
		for (Entry<String, String> entry : map.entrySet()) { 
			logPrint("Key = " + entry.getKey() + ", Value = " + entry.getValue(), null); 
		}
	}
	
    public static void writeLogToFile(JSONObject obj, String filename, String msg) {
    	if (obj != null) {
	    	File file = new File(filename);
	    	if (!file.exists()) {
	    		try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logPrint(null, e);
				}
	    	}
	    	
			try {
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
		    	BufferedWriter bw = new BufferedWriter(fw);
		    	bw.write(msg);
		    	bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logPrint(null, e);
			}
			logPrint("Write to file " + filename + " complete!", null);
    	} else {
    		logPrint("Nothing write to file!!!!", null);
    	}
    }
    
    public static boolean isValidInt(String value) {  
        try {  
            Integer.parseInt(value);  
        } catch (NumberFormatException e) {  
            return false;  
        }  
        return true;  
    }  
    
    public static Map<String, String>[] connectArray(Map<String, String>[] arr0, Map<String, String>[] arr1) {
    	if(arr0 == null || arr1 == null) {
    		return null;
    	}
    	int arr0_len = arr0.length;
    	int arr1_len = arr1.length;
    	
    	Map<String, String>[] tem_arr = new Map[arr0_len + arr1_len];
    	System.arraycopy(arr0, 0, tem_arr, 0, arr0_len);
    	System.arraycopy(arr1, 0, tem_arr, arr0_len, arr1_len);
    	
    	return tem_arr;
    }
    
    public static Timestamp getTimestamp( ) {
    	Timestamp time = new Timestamp(System.currentTimeMillis());
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
    	String timeStr = df.format(time);   
    	time = Timestamp.valueOf(timeStr);   
    	//System.out.println(time);
    	return time;
    }
    
}
package org.jieheng.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Main {
    
    //private static JSONArray farms = null;
    private static JSONArray workers_list = null;
    private static JSONArray tags_list = null;
    //private static String farm_id = null;
    private static Map<String, String>[] total_workers_list = null;
    private static Map<String, String>[] jieheng_workers_list = null;
    private static Map<String, String>[] niuhang_workers_list = null;
    private static Map<String, String>[] tealeaf_workers_list = null;
    private static Map<String, String>[] timchien_workers_list = null;
    
    public static void main(String[] args) throws Exception {
    	HiveAPI h = new HiveAPI();
    	//token = h.logIn();
    	//HelpUtl.logPrint(token, null);
    	
    	/*
    	farms = h.getFarms();
    	if(farms.length()>0){
    		for(int i=0;i<farms.length();i++){
    			JSONObject job = farms.getJSONObject(i); 
    			System.out.println("id = " + job.get("id") + ", name = " + job.get("name")) ;  
    		}
    	}
    	*/
    	
    	/*
    	workers_list = h.getWorkers(AllStr.API_JIEHENG_FARM_ID);
    	if(workers_list.length()>0){
    		for(int i=0;i<workers_list.length();i++){
    			JSONObject job = workers_list.getJSONObject(i); 
    			System.out.println(job.get("id")) ; 
    			
    			if (job.get("id").toString().equals("257013")) {
    				HelpUtl.logPrint(job.toString(2), null);
    			}

    		}
    	}*/
    	
    	/*
    	tags_list = h.getTags();
		for(int i=0;i<tags_list.length();i++){
			JSONObject job = tags_list.getJSONObject(i); 
			System.out.println("id = " + job.get("id") + ", name = " + job.get("name")) ;  
		}*/
    	//HelpUtl.logPrint(tags_list.toString(2), null);
    	
    	
    	workers_list = h.getWorkers(AllStr.API_JIEHENG_FARM_ID);
    	jieheng_workers_list = new GetWorkersUtilities(workers_list).getJsonResult();
    	
    	workers_list = h.getWorkers(AllStr.API_NIUHANG_FARM_ID);
    	niuhang_workers_list = new GetWorkersUtilities(workers_list).getJsonResult();
    	
    	workers_list = h.getWorkers(AllStr.API_TEALEAF_FARM_ID);
    	tealeaf_workers_list = new GetWorkersUtilities(workers_list).getJsonResult();
    	
    	workers_list = h.getWorkers(AllStr.API_TIMCHIEN_FARM_ID);
    	timchien_workers_list = new GetWorkersUtilities(workers_list).getJsonResult();
    	
    	total_workers_list = HelpUtl.connectArray(HelpUtl.connectArray(HelpUtl.connectArray(jieheng_workers_list, 
    		niuhang_workers_list), 
    		tealeaf_workers_list), 
    		timchien_workers_list);
    	
    	workers_list = null;
    	jieheng_workers_list = null;
    	niuhang_workers_list = null;
    	tealeaf_workers_list = null;
    	timchien_workers_list = null;

    	DatabaseOption db_option = new DatabaseOption();
    	db_option.insertDataToDataBase(total_workers_list);
    	
    	total_workers_list = null;
    	
    }
    
}

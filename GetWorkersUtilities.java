package org.jieheng.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class GetWorkersUtilities {
	private static Map<String, String> info_map = null;
	private static Map<String, String>[] info_map_list = null;
	
	GetWorkersUtilities(JSONArray array) {
		if(array != null) {
			info_map_list = new Map[array.length()];
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject obj = array.getJSONObject(i);
					if (obj != null) {
						info_map = new HashMap<String, String>();
						addToMap(obj, "id", "rig", info_map);
						info_map.put("rig_num", getRigsNum(obj.get("id").toString()));
						addToMap(obj, "active", null, info_map);
						addToMap(obj, "name", "rig", info_map);
						resolveGpuSummary(obj);
						resolveStats(obj);
						resolveTags(obj);
						resolveMinersSummary(obj);
						resolveMinersStats(obj);
						info_map_list[i] = info_map;
					}
					//HelpUtl.logPrint(info_map.toString(), null);
					//HelpUtl.printMap(info_map);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					info_map_list = null;
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void addToMap(JSONObject o, String key, String prefix, Map<String, String> map) {
		try {
			if(o.has(key) && !o.get(key).toString().equals("null")) {
				if (prefix == null) {
					map.put(key, o.get(key).toString());
				} else {
					map.put(prefix + "_" + key, o.get(key).toString());
				}
			} 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			HelpUtl.logPrint(null, e);
		}
	}
	
	private static void resolveGpuSummary(JSONObject obj) {
		JSONArray gpu_summary_arr = null;
		int gpu_coun = 0;
		try {
			if (obj.has("gpu_summary") && !obj.get("gpu_summary").toString().equals("null")) {
				gpu_summary_arr = obj.getJSONObject("gpu_summary").getJSONArray("gpus");
				if (gpu_summary_arr.length() > 0) {
					for(int i=0;i<gpu_summary_arr.length();i++){
						JSONObject job = gpu_summary_arr.getJSONObject(i); 
						gpu_coun +=  Integer.parseInt(job.get(AllStr.JSON_GPU_SUMMARY_KEY[0]).toString());
						for (int j = 0; j < AllStr.JSON_GPU_SUMMARY_KEY.length; j++) {
							addToMap(job, AllStr.JSON_GPU_SUMMARY_KEY[j], "gpus_type" + i, info_map);
						}
					}
					info_map.put("gpu_actual_count", String.valueOf(gpu_coun));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private static void resolveStats(JSONObject obj) {
		JSONObject stats_obj = null;
		try {
			if (obj.has("stats") && !obj.get("stats").toString().equals("null")) {
				stats_obj = obj.getJSONObject("stats");
				for(int i = 0; i < AllStr.JSON_STATS_KEY.length; i++) {
					addToMap(stats_obj, AllStr.JSON_STATS_KEY[i], null, info_map);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	private static void resolveTags(JSONObject obj) {
		JSONArray tags = null;
		String all_tags = null;
		try {
			if (obj.has("tag_ids") && !obj.get("tag_ids").toString().equals("null")) {
				tags = obj.getJSONArray("tag_ids");
				for(int i = 0; i < tags.length(); i++) {
					if(i == 0) {
						all_tags = tags.get(i).toString();
					}
					all_tags += "/" + tags.get(i).toString();
				}
				if(all_tags != null) {
					info_map.put("tag_ids", all_tags);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
	
	private static void resolveMinersSummary(JSONObject obj) {
		JSONArray miners_summary = null;
		try {
			if (obj.has("miners_summary") && !obj.get("miners_summary").toString().equals("null")) {
				miners_summary = obj.getJSONObject("miners_summary").getJSONArray("hashrates");
				if (miners_summary.length() > 0) {
					for(int i=0;i<miners_summary.length();i++){
						JSONObject job = miners_summary.getJSONObject(i); 
						if(job.has("hash")) {
							info_map.put("hash", cutString(job.get("hash").toString()));
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private static void resolveMinersStats(JSONObject obj) {
		JSONArray miners_stats = null;
		JSONArray hashes = null;
		JSONArray temps = null;
		JSONArray fans = null;
		int mark = 1;
		
		try {
			if (obj.has("miners_stats") && !obj.get("miners_stats").toString().equals("null")) {
				miners_stats = obj.getJSONObject("miners_stats").getJSONArray("hashrates");
				if (miners_stats.length() > 0) {
					for(int i=0;i<miners_stats.length();i++){
						JSONObject job = miners_stats.getJSONObject(i); 
						if(job.has("hashes")) {
							hashes = job.getJSONArray("hashes");
							for(int j = 0; j < hashes.length(); j++) {
								info_map.put("hash_gpu" + mark, cutString(hashes.get(j).toString()));
								mark++;
							}
							mark = 1;
						}
						
						if(job.has("temps")) {
							temps = job.getJSONArray("temps");
							for(int j = 0; j < temps.length(); j++) {
								info_map.put("temp_gpu" + mark, temps.get(j).toString());
								mark++;
							}
							mark = 1;
						}
						
						if(job.has("fans")) {
							fans = job.getJSONArray("fans");
							for(int j = 0; j < fans.length(); j++) {
								info_map.put("fan_gpu" + mark, fans.get(j).toString());
								mark++;
							}
							mark = 1;
						}
						
						if(job.has("miner")) {
							addToMap(job, "miner", null, info_map);
						}
						
						if(job.has("algo")) {
							addToMap(job, "algo", null, info_map);
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private static String cutString(String s) {
		String str = null;
		if(s.length() >= 8) {
			str = s.substring(0, 5);
		} else {
			str = s;
		}
		return str;
	}
	
	private static String getRigsNum(String rig_id) {
		String rig_num = null;
		
		for(int i = 0; i < AllStr.WORKERS_ID.length; i++) {
			if(rig_id.equals(AllStr.WORKERS_ID[i])) {
				rig_num = AllStr.RIGS_NUMBER[i];
				break;
			}
		}
		return rig_num;
	}
	
	public Map<String, String>[] getJsonResult() {
		//HelpUtl.printMapList(tem_rigs_info_map_list);
		return info_map_list;
	}
}
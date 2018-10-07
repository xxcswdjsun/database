package org.jieheng.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
  
public class DatabaseOption {
	private static Connection conn = null;
	private static Statement stmt = null;

    DatabaseOption() {
    	try {
    		//载入驱动
			Class.forName(AllStr.DB_JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			HelpUtl.logPrint(null, e);
		}
    	createDatabaseAndTable();
    }
    
    private boolean createDatabaseAndTable() {
    	try {
    		//连接到MySQL服务器，创建数据库RIG_INFO_DB
	        connectDB(AllStr.DB_SERVER_URL);
	        stmt.execute(AllStr.DB_CREATE_DATABASE_STATEMENT);
	        closeDB();
	        HelpUtl.logPrint("Create DataBase RIG_INFO_DB success!", null);
	        
	        //重新连接新建好的数据库RIG_INFO_DB，在其中创建table
	        connectDB(AllStr.DB_RIG_INFO_DB_URL);
	        stmt.execute(AllStr.DB_CREATE_STATS_INFO_TABLE_STATEMENT);
	        closeDB();
	        HelpUtl.logPrint("Create table rigs_stats_info_table success!", null);
	        return true;
		} catch (Exception e) {
			HelpUtl.logPrint("DataBase option faile!", e);
			return false;
		}
    }
    
    private boolean connectDB(String url) {
        try {
        	conn = DriverManager.getConnection(url, AllStr.DB_USER_NAME, AllStr.DB_USER_PASSWORD);
			stmt = conn.createStatement();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			HelpUtl.logPrint(null, e);
			return false;
		}
    }
    
    private boolean closeDB() {
        try {
			stmt.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			HelpUtl.logPrint(null, e);
			return false;
		}
    }
    
    /*
     * 只用一条插入语句执行全部数据插入，调用多次INSERT语句可以插入多条记录，
     * 但使用这种方法要增加服务器的负荷，因为，执行每一次 SQL服务器都要同样对SQL进行分析、优化等操作。
     */
    private String insertDataStatement(String[] item, String table, Map<String, String>[] map_list) {
    	String sql = "REPLACE INTO " + table + " VALUES ";
    	int map_len = map_list.length;
		int item_len = item.length;
		
    	for(int n = 0; n < map_len; n++) {
    		sql += "(";
    		for(int m = 0; m < item_len; m++) {
    			if(m != item_len - 1) {
    				if ((String)map_list[n].get(item[m]) == null) {
    					sql += (String)map_list[n].get(item[m]) + ", ";
    				} else {
    					sql += "'" + (String)map_list[n].get(item[m]) + "', ";
    				}
    				
    			} else {
    				sql += "'" + HelpUtl.getTimestamp() + "')";
    			}
    		}
    		
    		if(n == map_len - 1) {
    			sql += ";";
    		} else {
    			sql += ", ";
    		}
    	}
    	return sql;
    }
    
    public void insertDataToDataBase(Map<String, String>[] stats_info_list) {
    	connectDB(AllStr.DB_RIG_INFO_DB_URL);
    	try {
    		if (stats_info_list != null) {//API取到数据，清空数据库，写入新数据
    			stmt.execute(AllStr.DB_EMPTY_RIGS_STATS_INFO_TABLE);
    			stmt.execute(insertDataStatement(AllStr.current_stats_info_item, "rigs_stats_info_table", stats_info_list));
    			HelpUtl.logPrint("Insert data to rigs_stats_info_table success!!!", null);
    		} else {//API没有取到数据，不更新数据库，保留上次的数据
    			HelpUtl.logPrint("!!!No data get, do not update rigs_stats_info_table!!!", null);
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			closeDB();
			HelpUtl.logPrint("Insert data to rigs_stats_info_table failed!!!", e);
		}
    	
    	closeDB();
    }
}
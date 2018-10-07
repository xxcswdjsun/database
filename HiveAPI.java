package org.jieheng.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class HiveAPI {
    private static final String API_UTF_8 = "UTF-8";
    
    static class Response{
        int responseCode = -1;
        JSONObject responseData = null;
    }


    public HiveAPI() {
		// TODO Auto-generated constructor stub
	}

    public String logIn(){
        Map<String, String> httpParams = new HashMap<>();
        httpParams.put("login", AllStr.API_LOGIN);
        httpParams.put("password", AllStr.API_PASSWORD);
        String url = AllStr.API_BASEURL + AllStr.API_URL_LOGIN;
        Response result = sendPOST(url, httpParams);
        if(result.responseCode == 200) {
            try {
				return (String) result.responseData.get("access_token");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
            
        return null;
    }

    
    public JSONArray getFarms(){
        //Map<String, String> httpParams = new HashMap<>();
        //httpParams.put("Authorization", "Bearer " + token);
        //httpParams.put("token", token);
    	String url = AllStr.API_BASEURL + AllStr.API_URL_FARMS_LIST;
        //Response result = sendGET(url, AllStr.API_XXCSWD_TOKEN);
        Response result = sendGET(url, AllStr.API_JIEHENG_TOKEN);
        if(result.responseCode == 200) {
            try {
				return ((JSONArray) result.responseData.get("data"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
            
        return null;
    }
    
    
    public JSONArray getWorkers(String farm_id){
        //Map<String, String> httpParams = new HashMap<>();
        //httpParams.put("Authorization", "Bearer " + token);
        //httpParams.put("token", token);
    	
    	//"https://api2.hiveos.farm/api/v2/farms/{farmId}/workers"
    	String url = AllStr.API_BASEURL + AllStr.API_URL_FARMS_LIST + "/" + AllStr.API_JIEHENG_FARM_ID + "/workers";
        //Response result = sendGET(url, AllStr.API_XXCSWD_TOKEN);
        Response result = sendGET(url, AllStr.API_JIEHENG_TOKEN);
        if(result.responseCode == 200) {
            try {
				return ((JSONArray) result.responseData.get("data"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
            
        return null;
    }
    
    public JSONArray getTags(){
        //Map<String, String> httpParams = new HashMap<>();
        //httpParams.put("Authorization", "Bearer " + token);
        //httpParams.put("token", token);
    	
    	//"https://api2.hiveos.farm/api/v2/farms/{farmId}/workers"
    	String url = AllStr.API_BASEURL + AllStr.API_URL_FARMS_LIST + "/" + AllStr.API_JIEHENG_FARM_ID + "/tags";
        //Response result = sendGET(url, AllStr.API_XXCSWD_TOKEN);
        Response result = sendGET(url, AllStr.API_JIEHENG_TOKEN);
        if(result.responseCode == 200) {
            try {
				return ((JSONArray) result.responseData.get("data"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
            
        return null;
    }
        
    
    static Response sendGET(String url, String para) {
        Response response = new Response();
        StringBuffer buf = new StringBuffer();

        URL obj = null;
        try {

        	//https请求， https是对链接加了安全证书SSL的，如果服务器中没有相关链接的SSL证书，它就不能够信任那个链接，也就不会访问到了。
        	//所以我们第一步是自定义一个信任管理器。自要实现自带的X509TrustManager接口就可以了。
        	/*
    		//创建SSLContext
    		SSLContext sslContext=SSLContext.getInstance("SSL");
    		TrustManager[] tm={new MyX509TrustManager()};
    		//初始化
    		sslContext.init(null, tm, new java.security.SecureRandom());;
    		//获取SSLSocketFactory对象
    		SSLSocketFactory ssf=sslContext.getSocketFactory();
    		*/
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add request header
            //if (para != null) {
            	con.setRequestProperty("Authorization", "Bearer " + para);
            //}
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setSSLSocketFactory(ssf);//https请求


            // Get result
            response.responseCode = con.getResponseCode();
            InputStream is;
            if(response.responseCode == 200) {
                is = con.getInputStream();
            }else{
                is = con.getErrorStream();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                buf.append(inputLine);
            }
            in.close();
            if(response.responseCode == 200) {
                response.responseData = new JSONObject(buf.toString());
            }else{
                try{
                    response.responseData = new JSONObject(buf.toString());
                    HelpUtl.logPrint("ERROR: HTTP " + response.responseCode + ": \n" + response.responseData.toString(4), null);
                }catch (JSONException e) {
                	HelpUtl.logPrint("ERROR: HTTP " + response.responseCode + ": " + buf.toString(), null);
                }
            }
        } catch (MalformedURLException e) {
        	HelpUtl.logPrint("ERROR: Invalid URL: " + url, null);
            e.printStackTrace();
        } catch (ProtocolException e) {
        	HelpUtl.logPrint("ERROR: Invalid request method", null);
            e.printStackTrace();
        } catch (IOException e) {
        	HelpUtl.logPrint("ERROR: input/output operation", null);
            e.printStackTrace();
        } catch (JSONException e) {
        	HelpUtl.logPrint("ERROR: Invalid json response: " + buf.toString(), null);
            e.printStackTrace();
        } /*catch (NoSuchAlgorithmException e) {
            log("ERROR: Can't find HmacSHA256 algorithm");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            log("ERROR: Invalid secret key");
            e.printStackTrace();
        }*/ /*catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        return response;
    }
    
    
    
    
    /**
     * Make API request with given params. Signs the request with secret key.
     * @param params
     * @return
     */
    static Response sendPOST(String url, Map<String, String> params) {
        String urlParameters = buildQueryString(params, API_UTF_8);
        Response response = new Response();
        StringBuffer buf = new StringBuffer();

        URL obj = null;
        try {

        	//https请求， https是对链接加了安全证书SSL的，如果服务器中没有相关链接的SSL证书，它就不能够信任那个链接，也就不会访问到了。
        	//所以我们第一步是自定义一个信任管理器。自要实现自带的X509TrustManager接口就可以了。
        	/*
    		//创建SSLContext
    		SSLContext sslContext=SSLContext.getInstance("SSL");
    		TrustManager[] tm={new MyX509TrustManager()};
    		//初始化
    		sslContext.init(null, tm, new java.security.SecureRandom());;
    		//获取SSLSocketFactory对象
    		SSLSocketFactory ssf=sslContext.getSocketFactory();
    		*/
            obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add request header
            //con.setRequestProperty("HMAC", encodeHMAC(AllStr.API_SECRET_KEY, urlParameters));
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.setSSLSocketFactory(ssf);//https请求

            // Send post request
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();


            // Get result
            response.responseCode = con.getResponseCode();
            InputStream is;
            if(response.responseCode == 200) {
                is = con.getInputStream();
            }else{
                is = con.getErrorStream();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                buf.append(inputLine);
            }
            in.close();
            if(response.responseCode == 200) {
                response.responseData = new JSONObject(buf.toString());
            }else{
                try{
                    response.responseData = new JSONObject(buf.toString());
                    HelpUtl.logPrint("ERROR: HTTP " + response.responseCode + ": \n" + response.responseData.toString(4), null);
                }catch (JSONException e) {
                	HelpUtl.logPrint("ERROR: HTTP " + response.responseCode + ": " + buf.toString(), null);
                }
            }
        } catch (MalformedURLException e) {
        	HelpUtl.logPrint("ERROR: Invalid URL: " + url, null);
            e.printStackTrace();
        } catch (ProtocolException e) {
        	HelpUtl.logPrint("ERROR: Invalid request method", null);
            e.printStackTrace();
        } catch (IOException e) {
        	HelpUtl.logPrint("ERROR: input/output operation", null);
            e.printStackTrace();
        } catch (JSONException e) {
        	HelpUtl.logPrint("ERROR: Invalid json response: " + buf.toString(), null);
            e.printStackTrace();
        } /*catch (NoSuchAlgorithmException e) {
            log("ERROR: Can't find HmacSHA256 algorithm");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            log("ERROR: Invalid secret key");
            e.printStackTrace();
        }*/ /*catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        return response;
    }

    static String encodeHMAC(String key, String data) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(API_UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return 	DatatypeConverter.printHexBinary(sha256_HMAC.doFinal(data.getBytes(API_UTF_8))).toLowerCase();
    }

    static String buildQueryString(Map<String, String> parameters, String encoding) {
        return (String) parameters.entrySet().stream()
                .map(entry -> encodeParameter(entry.getKey(), entry.getValue(), encoding))
                .reduce((param1, param2) -> param1 + "&" + param2)
                .orElse("");
    }

    static String encodeParameter(String key, String value, String encoding) {
        return urlEncode(key, encoding) + "=" + urlEncode(value, encoding);
    }

    static String urlEncode(String value, String encoding) {
        try {
            return URLEncoder.encode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Cannot url encode " + value, e);
        }
    }

}

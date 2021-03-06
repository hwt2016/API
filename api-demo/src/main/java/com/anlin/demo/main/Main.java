package com.anlin.demo.main;

import com.anlin.demo.utils.AESUtils;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * demo
 *
 * @author AnLin
 * @version V1.0.0
 * @since 2018/1/18
 */
public class Main {

    private static final String partnerNo = "TEST000001";

    private static final String key = "8EFF9AB5485F41259FD2287B581B3E9F";

    public static void main(String[] args) {
//        getRecognition();
//        luhn();
        idCard();
    }

    /**
     * 身份证算法
     */
    public static void idCard(){
        try {
            // 请求参数组装
            JSONObject info = new JSONObject();
            Map<String, Object> parmas = new HashMap<>();
            String signKey = key.substring(16);
            String dataKey = key.substring(0, 16);
            info.put("idCard", "511701199101317047");
            String sign = DigestUtils.shaHex(info.toString() + signKey);
            parmas.put("partnerNo", partnerNo);
            parmas.put("action", "idCardCheck");
            parmas.put("orderId", getOrderIdByUUId());
            parmas.put("signData", sign);
            parmas.put("encryptData", AESUtils.encode(AESUtils.encode(info.toString(), dataKey)));
            JSONObject res = sendReq("http://open.alblog.cn/tool/interface", JSONObject.fromObject(parmas).toString(), "POST");
            System.out.println(AESUtils.decode(res, key, true));
        } catch (Exception e) {

        }
    }

    /**
     * Luhn 算法
     */
    public static void luhn(){
        try {
            // 请求参数组装
            JSONObject info = new JSONObject();
            Map<String, Object> parmas = new HashMap<>();
            String signKey = key.substring(16);
            String dataKey = key.substring(0, 16);
            info.put("cardNo", "6212262201023557228");
            String sign = DigestUtils.shaHex(info.toString() + signKey);
            parmas.put("partnerNo", partnerNo);
            parmas.put("action", "checkCard");
            parmas.put("orderId", getOrderIdByUUId());
            parmas.put("signData", sign);
            parmas.put("encryptData", AESUtils.encode(AESUtils.encode(info.toString(), dataKey)));
            JSONObject res = sendReq("http://open.alblog.cn/tool/interface", JSONObject.fromObject(parmas).toString(), "POST");
            System.out.println(AESUtils.decode(res, key, true));
        } catch (Exception e) {

        }
    }

    /**
     * 卡识别 Demo
     */
    public static void getRecognition() {
        try {
            // 请求参数组装
            JSONObject info = new JSONObject();
            Map<String, Object> parmas = new HashMap<>();
            String signKey = key.substring(16);
            String dataKey = key.substring(0, 16);
            info.put("cardNo", "6212262201023557228");
            info.put("code", "Y");
            info.put("length", "Y");
            info.put("panLength", "Y");
            info.put("providerCode", "Y");
            info.put("cardType", "Y");
            info.put("cardName", "Y");
            info.put("agencyCode", "Y");
            info.put("agencyName", "Y");
            String sign = DigestUtils.shaHex(info.toString() + signKey);
            parmas.put("partnerNo", partnerNo);
            parmas.put("action", "recognition");
            parmas.put("orderId", getOrderIdByUUId());
            parmas.put("signData", sign);
            parmas.put("encryptData", AESUtils.encode(AESUtils.encode(info.toString(), dataKey)));
            JSONObject res = sendReq("http://open.alblog.cn/tool/interface", JSONObject.fromObject(parmas).toString(), "POST");
            System.out.println(AESUtils.decode(res, key, true));
        } catch (Exception e) {

        }
    }

    public static String getOrderIdByUUId() {
        int machineId = 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        return machineId + String.format("%011d", hashCodeV);
    }

    /**
     * HTTP 请求发送
     * @param url
     * @param data
     * @param menthod
     * @return
     */
    public static JSONObject sendReq(String url, String data, String menthod) {
        java.net.HttpURLConnection urlConnection = null;
        BufferedOutputStream out;
        StringBuffer respContent = new StringBuffer();
        try {
            java.net.URL aURL = new java.net.URL(url);
            urlConnection = (java.net.HttpURLConnection) aURL.openConnection();
            urlConnection.setRequestMethod(menthod);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Connection", "close");
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes("UTF-8").length));
            urlConnection.setRequestProperty("Content-Type", "text/html");
            urlConnection.setConnectTimeout(500000);
            urlConnection.setReadTimeout(500000);
            out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(data.getBytes("UTF-8"));
            out.flush();
            out.close();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("请求失败");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                respContent.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        urlConnection.disconnect();
        return JSONObject.fromObject(respContent.toString());
    }
}
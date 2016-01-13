package com.yjh.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yjh on 16-1-7.
 */
public class Connector {
    enum METHOD {
        GET, POST
    }
    public static String request(String urlStr, METHOD method, Map<String, String> params) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(method.toString());

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setConnectTimeout(30000);
        connection.setReadTimeout(5000);

        StringBuilder sb = new StringBuilder();
        //组装参数
        for (Map.Entry<String, String> p : params.entrySet()) {
            sb.append(p.getKey()).append("=").append(p.getValue()).append("&");
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        String paramStr = sb.toString();

        connection.setRequestProperty("Content-Length", String.valueOf(paramStr.length()));
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");

        String responseContent;
        try (PrintWriter out = new PrintWriter(connection.getOutputStream());
             BufferedReader br = new BufferedReader(new
                InputStreamReader(connection.getInputStream(), "utf8"))) {
            out.write(paramStr);
            out.flush();
            String line;
            sb.setLength(0);
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            connection.disconnect();
        }
        return (responseContent = sb.toString()).equals("") ? null : responseContent;
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("t", "1");
        params.put("s", "2");
        params.put("p", "3");
        System.out.println(Connector.request("http://localhost:8080/s/nonBlocked/1?s=1", METHOD.POST, params));
    }
}

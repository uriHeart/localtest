package com.argo.address.controller;


import java.io.*;
import java.net.*;

public class Cafe24ApiCallListAllCategories {

    public static void main(String[] args) {
        String accessToken = "bc2deca571b127fce2e32f7e394a8518";//<---------------------- {access_token} modify
        String mallId = "goalstudio";//<-------------------------------- {mall_id} modify

        String authorization = "Bearer " + accessToken; // {Bearer} Next required space!
        String endpointUrl = "https://" + mallId + ".cafe24api.com/api/v2/admin/categories";

        BufferedReader br = null;
        HttpURLConnection con = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(endpointUrl);

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", authorization);
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            System.err.println(responseCode);

            if (responseCode == 200) {    // Correct call
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {                      // Error
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {br.close();} catch (IOException e) {e.printStackTrace();}
            con.disconnect();
        }
    }
}


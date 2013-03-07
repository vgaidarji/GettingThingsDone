package com.donvigo.GettingThingsDone.Services;

import com.donvigo.GettingThingsDone.Services.ServiceUtils.GetCityCodeResponse;
import com.donvigo.GettingThingsDone.Services.ServiceUtils.GetFaresResponse;
import com.donvigo.GettingThingsDone.Services.ServiceUtils.NewRequestResponse;
import com.donvigo.GettingThingsDone.Services.ServiceUtils.RequestStateResponse;
import com.donvigo.GettingThingsDone.Utils.Constants;
import com.donvigo.GettingThingsDone.Wrappers.Airline;
import com.donvigo.GettingThingsDone.Wrappers.CityInfo;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class WebService {
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int READ_TIMEOUT = 30000;

    /**
     * Opens <code>InputStream</code> according to <code>serviceURL</code> and <code>params</code>
     * <br/>Request method - GET<br/>
     * Note: params and response message are in JSON format.
     * @param serviceURL URL to web service
     * @param params method name with parameters
     * @return <code>InputStream</code> for corresponding URL
     */
    static InputStream sendRequestGetResponse_JSON_GET(String serviceURL, String params) {
        URL url;
        URLConnection connection;

        InputStream inputStream = null;

        String strURL = String.format("%s%s", serviceURL, params);
        try{
            url = new URL(strURL);
            connection = url.openConnection();
//            connection.setConnectTimeout(CONNECTION_TIMEOUT);
//            connection.setReadTimeout(READ_TIMEOUT);
            connection.connect();

            inputStream = new BufferedInputStream(url.openStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        return inputStream;
    }

    /**
     * Opens <code>InputStream</code> according to <code>serviceURL</code> and <code>params</code>.
     * <br/>Request method - POST<br/>
     * Note: params and response message are in JSON format.
     * @param serviceURL URL to web service with method
     * @param params method parameters (contains serialized object)
     * @return <code>HttpURLConnection</code> for corresponding URL
     */
    static HttpURLConnection sendRequestGetResponse_JSON_POST(String serviceURL, String params) {
        URL url;
        HttpURLConnection conn;

        try{
            url = new URL(serviceURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes());
            os.flush();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                 return conn;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns information about city by name from server.
     */
    public static CityInfo getCityInfo(String cityName){
        String params = "";
        String language = "ru";
        try{
            params = String.format(
                    "AirportNames/?filter=%s&language=%s&_Serialize=JSON",
                    URLEncoder.encode(cityName, "UTF-8"), language);
        }catch(Exception e){
            e.printStackTrace();
        }

        Gson gson = new Gson();

        InputStream source = sendRequestGetResponse_JSON_GET(Constants.SERVICE_URL, params);

        if (source != null) {
            Reader reader = new InputStreamReader(source);
            GetCityCodeResponse result = gson.fromJson(reader,
                    GetCityCodeResponse.class);

            if(result != null && result.getCount() > 0){
                return result.getCityInfo().get(0);
            }
        }
        return null;
    }

    /**
     * Creates new tickets finding request on server.
     * Returns ID, which will be used for checking request state.
     */
    public static String newRequest(String shortDateString, String cityCode){
        String params = "";
        String destinationCityCode = "LON";
        try{
            params = String.format(
                    "NewRequest/?Route=%s%s%s&AD=1&CN=0&CS=E&Partner=testapic&_Serialize=JSON",
                    shortDateString, cityCode, destinationCityCode);
        }catch(Exception e){
            e.printStackTrace();
        }

        Gson gson = new Gson();

        InputStream source = sendRequestGetResponse_JSON_GET(Constants.API_SERVICE_URL, params);

        if (source != null) {
            Reader reader = new InputStreamReader(source);
            NewRequestResponse result = gson.fromJson(reader,
                    NewRequestResponse.class);

            if(result != null && result.getError() == null){
                return result.getID();
            }
        }
        return null;
    }

    /**
     * Returns request status from server.
     */
    public static String requestState(String requestID){
        String params = "";
        try{
            params = String.format(
                    "RequestState/?R=%s&_Serialize=JSON",
                    requestID);
        }catch(Exception e){
            e.printStackTrace();
        }

        Gson gson = new Gson();

        InputStream source = sendRequestGetResponse_JSON_GET(Constants.API_SERVICE_URL, params);

        if (source != null) {
            Reader reader = new InputStreamReader(source);
            RequestStateResponse result = gson.fromJson(reader,
                    RequestStateResponse.class);

            if(result != null && result.getError() == null){
                return result.getCompleted();
            }
        }
        return null;
    }

    public static ArrayList<Airline> getAirlines(String ID){
        String params = "";
        try{
            params = String.format("Fares/?R=%s&V=Matrix&VB=true&L=ru&_Serialize=JSON", ID);
        }catch(Exception e){
            e.printStackTrace();
        }

        Gson gson = new Gson();

        InputStream source = sendRequestGetResponse_JSON_GET(Constants.API_SERVICE_URL, params);

        if (source != null) {
            Reader reader = new InputStreamReader(source);
            GetFaresResponse result = gson.fromJson(reader,
                    GetFaresResponse.class);
            if(result != null)
                return result.getAirlines();
        }
        return null;
    }
}
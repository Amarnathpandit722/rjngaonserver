package gov.municipal.suda.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageHelper {
    public String createOTP(String mobile_no) {
        int randomPin   =(int) (Math.random()*9000)+1000;
        String otp  = String.valueOf(randomPin);
        try {
            String apiKey = "apikey=" + " ";
            String message = "&message=" + "Your OTP is "+otp+" for login into ERP. Do not share OTP for security reasons";
            String sender = "&sender=" + " ";
            String numbers = "&numbers=" + mobile_no;

            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            return otp;
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return otp;
        }
    }
}

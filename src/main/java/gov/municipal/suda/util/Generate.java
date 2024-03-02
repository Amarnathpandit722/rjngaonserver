package gov.municipal.suda.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Generate {

    public static final  String generateUserName(String prefix,String symbol1,String name,
                                                 String symbol2, String designation,String userId) {

        StringBuilder concat=new StringBuilder();
        concat.append(prefix);
        concat.append(symbol1);
        concat.append(name);
        concat.append(symbol2);
        concat.append(designation);
        concat.append(userId);
        final String  result= String.valueOf(concat);
        return result;
    }

    public static String createOTPByPropertyNo(String mobile_no,String ownerName) throws ClientProtocolException, IOException {
        int randomPin   =(int) (Math.random()*9000)+1000;
        String otp  = String.valueOf(randomPin);
        String ServerDomainApiEndPoint = "http://mysms.arvitaglobal.com/SMSApi/rest/send";

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(ServerDomainApiEndPoint);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("userId", "pixel"));
        params.add(new BasicNameValuePair("password", "Pixels@2023"));
        params.add(new BasicNameValuePair("senderId", "CGBMCO"));
        params.add(new BasicNameValuePair("dltEntityId", "1401510850000057423"));
        params.add(new BasicNameValuePair("sendMethod", "simpleMsg"));
        params.add(new BasicNameValuePair("msgType", "text"));
        //params.add(new BasicNameValuePair("msg", "Dear Customer, Your OTP for ARVITA login is "+otp+". The OTP is valid for 5 mins. by SUDA"));
       //Dear {#var#}, use this {#var#} OTP to log in to your Bhilai Municipal Corporation Online Payment System. This OTP will be valid for the next {#var#} mins.
        params.add(new BasicNameValuePair("msg", "Dear "+ownerName+", use this "+otp+" OTP to log in to your Bhilai Municipal Corporation Online Payment System. This OTP will be valid for the next 5 mins."));
       // log.info("Message {} Dear "+ownerName+", use this "+otp+" OTP to log in to your Bhilai Municipal Corporation Online Payment System. This OTP will be valid for the next 5 mins.");
        params.add(new BasicNameValuePair("mobile", mobile_no));
        params.add(new BasicNameValuePair("duplicateCheck", "true"));
        params.add(new BasicNameValuePair("dltTemplateId", "1407167869430625150"));
        params.add(new BasicNameValuePair("format", "json"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        // Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

       // log.info("StatusCode: " + response.getStatusLine().getStatusCode());

        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
               // log.info(EntityUtils.toString(entity, "utf-8"));
            }
        }
        return otp;
    }
    public static String createOTPByMobileNo(String mobile_no) throws ClientProtocolException, IOException {
        String ownerName="Customer";
        int randomPin   =(int) (Math.random()*9000)+1000;
        String otp  = String.valueOf(randomPin);
        String ServerDomainApiEndPoint = "http://mysms.arvitaglobal.com/SMSApi/rest/send";

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(ServerDomainApiEndPoint);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("userId", "pixel"));
        params.add(new BasicNameValuePair("password", "Pixels@2023"));
        params.add(new BasicNameValuePair("senderId", "CGBMCO"));
        params.add(new BasicNameValuePair("dltEntityId", "1401510850000057423"));
        params.add(new BasicNameValuePair("sendMethod", "simpleMsg"));
        params.add(new BasicNameValuePair("msgType", "text"));
        //params.add(new BasicNameValuePair("msg", "Dear Customer, Your OTP for ARVITA login is "+otp+". The OTP is valid for 5 mins. by SUDA"));
        //Dear {#var#}, use this {#var#} OTP to log in to your Bhilai Municipal Corporation Online Payment System. This OTP will be valid for the next {#var#} mins.
        params.add(new BasicNameValuePair("msg", "Dear "+ownerName+", use this "+otp+" OTP to log in to your Bhilai Municipal Corporation Online Payment System. This OTP will be valid for the next 5 mins."));
      //  log.info("Message {} Dear "+ownerName+", use this "+otp+" OTP to log in to your Bhilai Municipal Corporation Online Payment System. This OTP will be valid for the next 5 mins.");
        params.add(new BasicNameValuePair("mobile", mobile_no));
        params.add(new BasicNameValuePair("duplicateCheck", "true"));
        params.add(new BasicNameValuePair("dltTemplateId", "1407167869430625150"));
        params.add(new BasicNameValuePair("format", "json"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        // Execute and get the response.
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

       // log.info("StatusCode: " + response.getStatusLine().getStatusCode());

        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
              //  log.info(EntityUtils.toString(entity, "utf-8"));
            }
        }
        return otp;
    }

    public static final String capitalize(String str)
    {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static final String generateNewPropertyNo(String prefix,Integer zone_id, Integer ward_id,
                                                     Integer midFix, Integer increment) {
        StringBuilder tempValue=new StringBuilder();
        tempValue.append(prefix);
        tempValue.append(zone_id);
        tempValue.append(ward_id);
        String temp=tempValue.substring(1);
        return null;

    }

    public static final  String generatePropertyNo(Long zone_id,Long ward_id) throws SQLException {
        ResultSet rs=null;
        Connection conn=null;
        Statement stmt=null;
        String prefix=null;
        String zone=null;
        String ward=null;
        int sl_no=0;
        String increment_id=null;
        conn= DBConnections.getConnection();
        stmt=conn.createStatement();
        String q="SELECT sl_no FROM adm_sl_no where year_cd='2023-2024' and doc_type='property_no'";
        rs=stmt.executeQuery(q);
        while(rs.next())
        {
            sl_no=rs.getInt(1);

        }
        sl_no++;
//        if(zone_id>=0 && zone_id<10){
//            zone="0"+zone_id;
//        }
//        if(zone_id>=10 && zone_id<100){
//            zone=""+zone_id;
//        }
//        if(ward_id>=0 && ward_id<10){
//            ward="0"+ward_id;
//        }
//        if(ward_id>=10 && ward_id<100){
//            ward=""+ward_id;
//        }
        if (sl_no >= 0 && sl_no < 10) {
            increment_id ="00000"+sl_no;
        }
        if (sl_no >= 10 && sl_no < 100) {
            increment_id ="0000"+sl_no;
        }
        if (sl_no >= 100 && sl_no < 1000) {
            increment_id ="000"+sl_no;
        }
        if (sl_no >= 1000 && sl_no < 10000) {
            increment_id ="00"+sl_no;
        }
        if (sl_no >= 10000 && sl_no < 100000) {
            increment_id ="0"+sl_no;
        }
        if (sl_no >= 100000 && sl_no < 1000000) {
            increment_id = ""+ sl_no;
        }
        String propertNo=String.valueOf(ward_id)+String.valueOf(zone_id)+increment_id;
        if(propertNo.length()!=10){
            propertNo=String.valueOf(ward_id)+String.valueOf(zone_id)+"00"+increment_id;
        }
       // log.info("Property No"+propertNo);
        Statement statement1 = conn.createStatement();
        String q1="update adm_sl_no set sl_no='"+sl_no+"' where year_cd='2023-2024' and doc_type='property_no'";
        statement1.executeUpdate(q1);
        statement1.close();
        conn.close();
        return propertNo;
    }

    public static final String fiscalYear() {
        LocalDate today = LocalDate.now(); // Get the current date
        int year = today.getYear(); // Get the year component of the date
        LocalDate aprilFirst = LocalDate.of(year, 4, 1); // Set April 1st as the start of the financial year
        if (today.isBefore(aprilFirst)) { // Check if today is before April 1st
            year--; // If yes, then the financial year is the previous year
        }
        String financialYear = year + "-" + (year + 1); // Create the financial year string
        System.out.println("Current financial year: " + financialYear);
        return financialYear;
    }

    public static final String generateCodeForTrackTransactions(Integer previousValue) {
        Integer finalCounter=previousValue+1;
        return "CODE"+finalCounter;
    }
    public static final  String generateConsumerNo() throws SQLException {
        ResultSet rs=null;
        Connection conn=null;
        Statement stmt=null;
        String prefix=null;
        String zone=null;
        String ward=null;
        Long sl_no=0L;
        String increment_id=null;
        conn=DBConnections.getConnection();
        stmt=conn.createStatement();
        String q="SELECT sl_no FROM public.adm_sl_no where doc_type='consumer_no'";
        rs=stmt.executeQuery(q);
        while(rs.next())
        {
            sl_no=rs.getLong(1);

        }
        sl_no++;
        if (sl_no >= 0 && sl_no < 10) {
            increment_id ="000000"+sl_no;
        }
        if (sl_no >= 10 && sl_no < 100) {
            increment_id ="00000"+sl_no;
        }
        if (sl_no >= 100 && sl_no < 1000) {
            increment_id ="0000"+sl_no;
        }
        if (sl_no >= 1000 && sl_no < 10000) {
            increment_id ="000"+sl_no;
        }
        if (sl_no >= 10000 && sl_no < 100000) {
            increment_id ="00"+sl_no;
        }
        if (sl_no >= 100000 && sl_no < 1000000) {
            increment_id = "0"+ sl_no;
        }
        if (sl_no >= 1000000 && sl_no < 10000000) {
            increment_id = ""+ sl_no;
        }
        String consumerNo="BMCS"+increment_id;
       // log.info("Consumer No"+consumerNo);
        Statement statement1 = conn.createStatement();
        String q1="update public.adm_sl_no set sl_no='"+sl_no+"' where doc_type='consumer_no'";
//        log.info(q1);
        statement1.executeUpdate(q1);
        statement1.close();
        conn.close();
        return consumerNo;
    }

}

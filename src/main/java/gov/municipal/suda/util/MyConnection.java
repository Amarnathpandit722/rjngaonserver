package gov.municipal.suda.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.TimeZone;
@Component
public class MyConnection {
    public Connection con=null;
    @Value("${spring.datasource.url}") String db_url;
    public Connection getConnection(String db)
    {
        try{
            Class.forName("org.postgresql.Driver");
           /// con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/"+db_url, "postgres", "postgres");
           con = DriverManager.getConnection("jdbc:postgresql://ec2-43-204-29-97.ap-south-1.compute.amazonaws.com:5432/"+db, "root", "postgres");
            //jdbc:postgresql://csuda.cds23szhj6ys.ap-south-1.rds.amazonaws.com:5432/suda
            return con;
        }catch(Exception e)
        {

            System.out.println("error");
            return null;
        }


    }

    public void disconnect()
    {

        try {
            this.con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet(String qry,String db)
    {
        ResultSet resultSet=null;
        try {
            Statement st= this.getConnection(db).createStatement();
            resultSet=st.executeQuery(qry);
            //this.disconnect();


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultSet;

    }


}

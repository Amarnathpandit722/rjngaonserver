package gov.municipal.suda.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Slf4j
public class DBConnections {
    @Value("${spring.db.driver}")
    private static String DB_DRIVER_CLASS;
    @Value("${spring.datasource.url}")
    private static String DB_URL;
    @Value("${spring.datasource.username}")
    private static String DB_USERNAME;
    @Value("${spring.datasource.password}")
    private  static String DB_PASSWORD;

    public DBConnections(@Value("${spring.db.driver}") String db_driver_class, @Value("${spring.datasource.url}") String db_url,
                         @Value("${spring.datasource.username}") String db_username, @Value("${spring.datasource.password}") String db_password) {
        this.DB_DRIVER_CLASS = db_driver_class;
        this.DB_URL = db_url;
        this.DB_USERNAME = db_username;
        this.DB_PASSWORD = db_password;

        //log.info(DB_DRIVER_CLASS);
    }

    public static final Connection getConnection() {
        Connection con = null;
        try {
            // load the Driver Class
            Class.forName(DB_DRIVER_CLASS);

            // create the connection now
            con = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            //con = DriverManager.getConnection("jdbc:postgresql://csuda.cpkge0chbw04.us-east-1.rds.amazonaws.com:5432/suda", "postgres", "Csuda2022");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("From Connection page : con ====> ",con);
        return con;
    }
}

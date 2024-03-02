package gov.municipal.suda.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UtilHelper {
    ResultSet rs=null;
    Connection conn=null;
    Statement stmt=null;
    public void updateUserRole(String userId,String roleId) {
        //MyConnection mc=new MyConnection();
        int id=0;
        try{
            conn=DBConnections.getConnection();
            stmt=conn.createStatement();
            String sql="update user_role set role_id ='"+roleId+"' where user_id='"+userId+"'";
            stmt.executeUpdate(sql);
            conn.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

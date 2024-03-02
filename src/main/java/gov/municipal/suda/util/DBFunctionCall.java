package gov.municipal.suda.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.procedure.ProcedureParameter;
import org.springframework.jdbc.object.StoredProcedure;
//import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class DBFunctionCall {

    public static CachedRowSet simpleFunctionCallWithoutParameter(final String functionName,int outputIndex ) throws Exception{

        CachedRowSet crs=null;
        Connection con=null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        try {
            RowSetFactory factory = RowSetProvider.newFactory();
            crs=factory.createCachedRowSet();
            con = DBConnections.getConnection();
            con.setAutoCommit(false);
            stmt=con.prepareCall("{? = call "+functionName+" }");
            stmt.registerOutParameter(1, Types.OTHER);
            stmt.execute();
            rs=(ResultSet) stmt.getObject(outputIndex);
            crs.populate(rs);

        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return crs;
    }

    public static CachedRowSet simpleFunctionCallWithParameter(final String functionName, int outputIndex, Map<Object, Object> inputParameter ) throws Exception {

        CachedRowSet crs = null;
        Connection con = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        try { 
            RowSetFactory factory = RowSetProvider.newFactory();
            crs = factory.createCachedRowSet();
            con = DBConnections.getConnection();
            log.info("Connection of DB =====> {}",con);
            con.setAutoCommit(false);
            stmt = con.prepareCall("{? = call " + functionName + " }");
            stmt.registerOutParameter(1, Types.OTHER);
            AtomicInteger i = new AtomicInteger(2);
            for(Map.Entry<Object, Object> entry: inputParameter.entrySet()) {
                int index = i.getAndIncrement();
                if(Integer.class.equals(entry.getKey().getClass())) {
                    stmt.setInt(index, (Integer) entry.getValue());
                } else if(String.class.equals(entry.getKey().getClass())) {
                    stmt.setString(index, (String) entry.getValue());
                } else if (Boolean.class.equals(entry.getKey().getClass())) {
                    stmt.setBoolean(index,(Boolean) entry.getValue());
                } else if(Long.class.equals(entry.getKey().getClass())) {
                    stmt.setLong(index,(Long) entry.getValue());
                } else if (Double.class.equals(entry.getKey().getClass())) {
                    stmt.setDouble(index,(Double) entry.getValue());
                }
                else if(Timestamp.class.equals(entry.getKey().getClass())) {
                    stmt.setTimestamp(index,(Timestamp) entry.getValue());
                }
                else if(Date.class.equals(entry.getKey().getClass())) {
                    stmt.setDate(index, (java.sql.Date) entry.getValue());
                }

            }
                stmt.execute();
                rs = (ResultSet) stmt.getObject(outputIndex);
                crs.populate(rs);


        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            return crs;
        }
    }

    }

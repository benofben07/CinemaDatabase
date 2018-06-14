package logic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

    // don't instantiate it
public abstract class DAOBase {
    
    protected Connection conn;
    protected Statement s;

    protected void closeConnection() throws SQLException{
        if ( conn != null && !conn.isClosed()) conn.close();
    }
    
    /**
     * Connects to MySql database server and returns a Statement object
     * for data manipulation.
     * @return Statement object ready to be used for database interaction
     * @throws SQLException
     */
    protected Statement getStatement() throws SQLException{
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test?useUnicode=true" + 
            "&useJDBCCompliantTimezoneShift=true" + 
            "&useLegacyDatetimeCode=false&serverTimezone=UTC");
        //objektum az sql állításoknak
        return conn.createStatement();
    }
}

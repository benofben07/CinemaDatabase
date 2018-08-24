package cinemadatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public final class DBInit{
    
    // don't instantiate
    private DBInit() {}
    
    private static Connection conn;
    
    /**
     * Sets up database.
     * Creates tables and fills them with data.
     */
    public static void setupDB(){
        //elérhető-e a ClientDriver oszály
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException ex){
            System.out.println("MySQL driver not found.");
        }
        
        try {
            
            Statement s = getStatement();
            
            // create tables
            createTables(s, "setup.sql");
            // fill tables with data
            fillUpTables(s, "insert.sql");
            
            s.close();
            conn.close();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    private static void createTables(Statement s, String filename) throws SQLException {
        dropExistingTables(s);
        readAndExecuteSQL(s, filename);
    }
    
    private static void fillUpTables(Statement s, String filename) throws SQLException {
        readAndExecuteSQL(s, filename);
    }
    
    private static void dropExistingTables(Statement s) throws SQLException {
        s.execute("DROP TABLE IF EXISTS SEAT");
        s.execute("DROP TABLE IF EXISTS SCREENING");
        s.execute("DROP TABLE IF EXISTS CINEMA_HALL");
        s.execute("DROP TABLE IF EXISTS MOVIE");
    }
    
    private static void readAndExecuteSQL (Statement s, String filename) throws SQLException{
        try (
            Scanner sc = new Scanner(new File(filename));
        ) {
            // read whole file
            while(sc.hasNextLine()) {
                // read an sql statement
                createSQLStatement(s, sc);
            }    
        } catch (FileNotFoundException e) {
            System.out.println("SQL Script not found.");
        }
    }
    
    private static void createSQLStatement(Statement s, Scanner sc) throws SQLException {
        StringBuilder line = new StringBuilder(sc.nextLine());
        if (line.length() != 0) {
            // if reached end of a statement
            while (line.charAt(line.length() - 1) != ';' && sc.hasNextLine()) {
                line.append(sc.nextLine());
            }
            System.out.println("Executing:" + line.toString());
                
            // executes read SQL Statement
            s.execute(line.toString());
        }
    }
    
    /**
     * Connects to MySql database server and returns a Statement object
     * for data manipulation.
     * @return Statement object ready to be used for database interaction
     * @throws SQLException
     */
    public static Statement getStatement() throws SQLException{
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/test?useUnicode=true" + 
            "&useJDBCCompliantTimezoneShift=true" + 
            "&useLegacyDatetimeCode=false&serverTimezone=UTC");
        //objektum az sql állításoknak
        return conn.createStatement();
    }
}

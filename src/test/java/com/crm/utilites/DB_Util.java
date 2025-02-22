package com.crm.utilites;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.crm.utilites.ConfigReader.*;

public class DB_Util {

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
    private static ResultSetMetaData rsmd;

    public static void createConnection(String url, String userName, String password){

        try{

            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("CONNECTION SUCCESSFUL (url: "+url+")");

        }catch(Exception e){

            System.out.println("CONNECTION HAS FAILED "+e.getMessage());

        }

    }

    public static void createConnection(){

        String url = getProperty("library2DbUrl"), userName = getProperty("library2Username"), pass = getProperty("library2Password");

        createConnection(url, userName, pass);

    }

    public static void runQuery(String sql){

        try{

            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rsmd = rs.getMetaData();

        }catch (Exception e){

            System.out.println("ERROR OCCURRED WHILE RUNNING QUERY " + e.getMessage());

        }

    }

    public static void destroy() {
        // WE HAVE TO CHECK IF WE HAVE THE VALID OBJECT FIRST BEFORE CLOSING THE RESOURCE
        // BECAUSE WE CAN NOT TAKE ACTION ON AN OBJECT THAT DOES NOT EXIST
        try {

            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();

        } catch (Exception e) {

            System.out.println("ERROR OCCURRED WHILE CLOSING RESOURCES " + e.getMessage());

        }

    }

    /**
     * This method will reset the cursor to before first location
     */
    private static void resetCursor() {

        try {

            rs.beforeFirst();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static int getColumnCount() {

        int columnCount = 0;

        try {
            columnCount = rsmd.getColumnCount();

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE GETTING COLUMN COUNT " + e.getMessage());
        }

        return columnCount;

    }

    public static Map<String, Object> getRowMap(String query, int rowNum) {

        runQuery(query);

        Map<String, Object> rowMap = new LinkedHashMap<>();
        int columnCount = getColumnCount();

        try {

            rs.absolute(rowNum);

            for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                String columnName = rsmd.getColumnName(colIndex);
                String cellValue = rs.getString(colIndex);
                rowMap.put(columnName, cellValue);
            }

        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getRowMap " + e.getMessage());
        } finally {
            resetCursor();
        }


        return rowMap;
    }

    public static Map<String, Object> getMap(String query, int row) {
        runQuery(query);

        try {
            Map<String, Object> map = new LinkedHashMap<>();

            System.out.println("Column count = " + rsmd.getColumnCount());
            System.out.println("You picked row = " + row);

            int rowIndex = 0;

            while (rs.next()) {
                rowIndex++;

                if (rowIndex == row) {
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String columnName = rsmd.getColumnName(i); // Header (column name)
                        String cellValue = rs.getString(i);       // Cell value

                        // Debugging each column and value
                        System.out.println("Column: " + columnName + ", Value: " + cellValue);

                        map.put(columnName, cellValue);
                    }
                    break; // Stop after fetching the desired row
                }
            }

            if (map.isEmpty()) {
                throw new InputMismatchException("Row " + row + " does not exist.");
            }

            System.out.println("Final Retrieved Map: " + map); // Debug final map
            return map;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error with query execution or data retrieval.");
        }

        throw new InputMismatchException("Invalid row or query.");
    }


}

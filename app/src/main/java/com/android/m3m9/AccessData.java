package com.android.m3m9;

import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AccessData {

    private static AccessData sAccessData;
    private Connection conn;
    private String user = "sqladmin"; //Security increased due not the standard "postgre" username
    private String pwd = "12341234"; //You can't hack this
    private String url = "jdbc:postgresql://database-4.cwwws5ulgj4q.us-east-1.rds.amazonaws.com:5432/testDB"; //Extremely tired of fighting with a VM I created a db instance.
    private ArrayList<ExampleElement> eeList = new ArrayList<>();
    final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");


    private AccessData(Context ctx) {
    }

    public static AccessData getAccessData(Context ctx) {
        if (sAccessData == null) sAccessData = new AccessData(ctx);
        return sAccessData;
    }

    public ArrayList<ExampleElement> getExampleElements() {
        return eeList;
    }

    public void loadExampleElements() {
        try {
            ArrayList<ExampleElement> newEEList = new ArrayList<>();

            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, pwd);

            String stsql = "Select * FROM ExampleElements;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(stsql);
            while (rs.next()) {
                int attributeInt = Integer.parseInt(rs.getString("attributeint"));
                Date attributeDate = format.parse(rs.getString("attributedate"));
                String attributeString = rs.getString("attributestring");
                newEEList.add(new ExampleElement(attributeInt, attributeDate, attributeString));
            }

            eeList = newEEList;
        } catch (SQLException se) {
            System.out.println("oops! No se puede conectar. Error: " + se.toString());
        } catch (ClassNotFoundException e) {
            System.out.println("oops! No se encuentra la clase. Error: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Parsing failure: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failure closing the connection;");
            }
        }
    }


    public void saveExampleElement(final int a1, final String a2, final String a3) {
        try {
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(url, user, pwd);
            String stsql = "INSERT INTO ExampleElements (attributeInt, attributeDate, attributeString) VALUES (" + a1 + ",'" + a2 + "', '" + a3 + "');";
            Statement st = conn.createStatement();
            st.executeUpdate(stsql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failure closing the connection;");
            }
        }
    }

    public void updateExampleElementByAttributeElement(int a1, String a2, String a3) {
        try {
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(url, user, pwd);
            String stsql = "UPDATE ExampleElements " +
                    "SET AttributeDate = '" + a2 + "', " +
                    "AttributeString = '" + a3 + "' " +
                    "WHERE AttributeInt = " + a1 + ";";
            Statement st = conn.createStatement();
            st.executeUpdate(stsql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failure closing the connection;");
            }
        }
    }

    public void deleteExampleElement(int a1) {
        try {
            Class.forName("org.postgresql.Driver");

            conn = DriverManager.getConnection(url, user, pwd);
            String stsql = "DELETE FROM ExampleElements " +
                    "WHERE AttributeInt = " + a1 + ";";
            Statement st = conn.createStatement();
            st.executeUpdate(stsql);
            conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failure closing the connection;");
            }
        }
    }
}

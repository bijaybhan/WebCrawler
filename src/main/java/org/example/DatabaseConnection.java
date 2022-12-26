package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection
{
    static Connection connection = null;
    public static  Connection getConnection()
    {
        if (connection!=null)
        {
            return connection;
        }
        String db = "searchaccio";
        String user = "root";
        String pwd = "1234";
        return getConnection(db,user,pwd);
    }
    //make connection static so we can call from anywhere without creating object
    private static Connection getConnection( String db,String user, String pwd )
    {
         try
         {
             //load the driver
             Class.forName("com.mysql.cj.jdbc.Driver");
             //create connection
             connection=DriverManager.getConnection("jdbc:mysql://localhost/"+db+"?user="+user+"&password="+pwd);
         }
         catch (Exception exception)
         {
             exception.printStackTrace();
         }
         return connection;
    }
}

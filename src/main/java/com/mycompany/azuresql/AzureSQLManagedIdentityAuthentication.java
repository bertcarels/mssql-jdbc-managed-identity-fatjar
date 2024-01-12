package com.mycompany.azuresql;




import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AzureSQLManagedIdentityAuthentication {
    public static void main(String[] args) {
        try {

            Logger logger = LoggerFactory.getLogger(AzureSQLManagedIdentityAuthentication.class);

            logger.info("Running query on:");
            logger.info("server: " + args[0]);
            logger.info("db: " + args[1]);
            logger.info("table: " + args[2]);
            logger.info("auth: " + args[3]);
            

            // Set up the Azure SQL Database DataSource
            SQLServerDataSource ds = new SQLServerDataSource();
            ds.setServerName(args[0] +".database.windows.net");
            ds.setDatabaseName(args[1]);
            ds.setAuthentication(args[3]);

            try (Connection connection = ds.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUSER_SNAME()")) {
                if (rs.next()) {
                    System.out.println("You have successfully logged on as: " + rs.getString(1));
                }
            }


            // Establish the connection
            Connection connection = ds.getConnection();

            // Execute a simple query
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT TOP 5 * FROM " + args[2] );

            // Process the results
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



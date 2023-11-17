package encomendasapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Carrega o driver JDBC
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Configura as credenciais e a URL do banco de dados
            String dbURL = "jdbc:oracle:thin:@192.168.0.25:1522:ERP";
            String username = "PEDRO";
            String password = "PG08E11A";

            // Estabelece a conexão com o banco de dados
            conn = DriverManager.getConnection(dbURL, username, password);

            if (conn != null) {
                System.out.println("Conectado com sucesso ao banco de dados.");
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return conn;
    }
}

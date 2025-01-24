package SQL;

import java.sql.*;

public class SQLConnection {
    protected Connection connection;

    public SQLConnection(Connection c) {
        try {
            this.connection = c;
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }
}
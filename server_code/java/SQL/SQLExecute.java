package SQL;

import java.sql.*;

final public class SQLExecute extends SQLConnection {
    private Statement statement;

    public SQLExecute(Connection c, String query) {
        super(c);
        
        try {
            this.statement = connection.createStatement();
            this.statement.executeUpdate(query);
            this.statement.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }
}
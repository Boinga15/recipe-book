package SQL;

import java.sql.*;

public class SQLFactory {
    protected Connection connection;
    protected SQLQuery query;

    public SQLFactory() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:utilityItems/database.db");

            this.query = null;
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }

    public void doExecute(String query) {
        new SQLExecute(this.connection, query);
    }

    public void doQuery(String query) {
        if(this.query != null) {
            this.query.finish();
        }

        this.query = new SQLQuery(this.connection, query);
    }

    public SQLQuery fetchQuery() {
        return this.query;
    }

    public void closeQuery() {
        this.query.finish();
    }
}
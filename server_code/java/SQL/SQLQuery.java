package SQL;

import java.sql.*;

final public class SQLQuery extends SQLConnection {
    private Statement statement;
    private ResultSet result;

    public SQLQuery(Connection c, String query) {
        super(c);
        
        try {
            this.statement = connection.createStatement();
            this.result = this.statement.executeQuery(query);
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

    public void finish() {
        try {
            this.result.close();
            this.statement.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

    public ResultSet getResult() {
        return this.result;
    }

    public int getUserID(String username) {
        int userID = 0;

        try {
            while (this.result.next()) {
                if (username.equals(this.result.getString("Username"))) {
                    userID = this.result.getInt("UserID");
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }

        return userID;
    }

    public int queryNextAvailable(String fieldName) {
        int currentAvailable = 0;

        try {
            while (this.result.next()) {
                if (currentAvailable <= this.result.getInt(fieldName)) {
                    currentAvailable = this.result.getInt(fieldName) + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }

        return currentAvailable;
    }
}
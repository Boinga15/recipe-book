import java.sql.*;

public class Store {
    static void createUser(Connection c, String username, String password) {
        try {
            Statement stmt = null;

            stmt = c.createStatement();
            String sql = "SELECT UserID FROM Users;";
            var sqlResult = stmt.executeQuery(sql);

            int result = 0;
            
            while (sqlResult.next()) {
                if (result < sqlResult.getInt("UserID")) {
                    result = sqlResult.getInt("UserID");
                }
            }

            Boolean uniqueUsername = true;

            sqlResult.close();
            stmt.close();

            stmt = c.createStatement();
            sql = "SELECT Username FROM Users WHERE Username = '" + username + "';";
            var sqlResult2 = stmt.executeQuery(sql);

            while (sqlResult2.next()) {
                uniqueUsername = false;
            }

            sqlResult2.close();
            stmt.close();

            if (!uniqueUsername) {
                System.out.println("-1"); // User already exists.
            } else {
                stmt = c.createStatement();
                sql = "INSERT INTO Users (UserID, Username, Passmain, Passkey) VALUES ('" + (result + 1) + "', '" + username + "', '" + password + "', '" + password + "');";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1"); // User successfully added.
            }

        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

    public static void main(String[] args) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            createUser(c, "Username", "Password");

            c.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: Couldn't reach database. === " + e);
        }
    }
}
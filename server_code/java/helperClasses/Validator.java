package helperClasses;

import java.sql.*;

public class Validator {
    public Validator() {}

    public static boolean validateUser(Connection c, String username, String password) {
        try {
            Splitter splitter = new Splitter();
            String eUsername = splitter.apostropheEscape(username);
            String ePassword = splitter.apostropheEscape(password);
            
            Statement stmt = null;

            boolean isValid = false;

            Encoder encoder = new Encoder();

            stmt = c.createStatement();
            String sql = "SELECT Username, Passmain, Passkey FROM Users WHERE Username = '" + eUsername + "';";
            var sqlResult = stmt.executeQuery(sql);
            
            while (sqlResult.next()) {
                if (password.equals(encoder.decode(sqlResult.getString("Passmain"), sqlResult.getString("Passkey")))) {
                    isValid = true;
                }
            }

            sqlResult.close();
            stmt.close();

            return isValid;
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
            return false;
        }
    }

    public static void main(String[] args) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            if(validateUser(c, args[0], args[1])) {
                System.out.println("1"); // User validated.
            } else {
                System.out.println("-1"); // User not validated.
            }

            c.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }
}
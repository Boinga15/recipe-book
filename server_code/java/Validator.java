import java.sql.*;

public class Validator {
    public Validator() {}

    static boolean validateUser(Connection c, String username, String password) {
        try {
            Statement stmt = null;

            boolean isValid = false;

            Encoder encoder = new Encoder();

            stmt = c.createStatement();
            String sql = "SELECT Username, Passmain, Passkey FROM Users WHERE Username = '" + username + "';";
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
}
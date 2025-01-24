package helperClasses;

import java.sql.*;

import SQL.*;

public class Validator {
    public Validator() {}

    public static boolean validateUser(SQLFactory factory, String username, String password) {
        Splitter splitter = new Splitter();
        String eUsername = splitter.apostropheEscape(username);
        String ePassword = splitter.apostropheEscape(password);

        boolean isValid = false;

        Encoder encoder = new Encoder();

        factory.doQuery("SELECT Username, Passmain, Passkey FROM Users WHERE Username = '" + eUsername + "';");
        ResultSet results = factory.fetchQuery().getResult();
        try {
            while (results.next()) {
                if (password.equals(encoder.decode(results.getString("Passmain"), results.getString("Passkey")))) {
                    isValid = true;
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }

        return isValid;
    }

    public static void main(String[] args) {
        SQLFactory sqlFactory = new SQLFactory();

        if (validateUser(sqlFactory, args[0], args[1])) {
            System.out.println("1");
        } else {
            System.out.println("-1");
        }
    }
}
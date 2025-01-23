import java.sql.*;

public class Delete {
    static void deleteShopping(Connection c, String username, String password, String itemOrder) {
        Validator validator = new Validator();
        if (validator.validateUser(c, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                Statement stmt = null;

                stmt = c.createStatement();
                String sql = "SELECT UserID, Username FROM Users WHERE Username = '" + eUsername + "';";
                var sqlResult = stmt.executeQuery(sql);

                int userID = 0;
                
                while (sqlResult.next()) {
                    if (username.equals(sqlResult.getString("Username"))) {
                        userID = sqlResult.getInt("UserID");
                    }
                }

                sqlResult.close();
                stmt.close();

                stmt = c.createStatement();
                sql = "DELETE FROM ShoppingList WHERE ItemOrder == " + itemOrder + " AND UserID == " + userID + ";";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1");
            } catch (Exception e) {
                System.out.println("Error Encountered: " + e);
            }
        } else {
            System.out.println("Login failed.");
        }
    }

    static void deleteRecipe(Connection c, String username, String password, String recipeName) {
        Validator validator = new Validator();
        if (validator.validateUser(c, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                Statement stmt = null;

                stmt = c.createStatement();
                String sql = "SELECT UserID, Username FROM Users WHERE Username = '" + eUsername + "';";
                var sqlResult = stmt.executeQuery(sql);

                int userID = 0;
                
                while (sqlResult.next()) {
                    if (username.equals(sqlResult.getString("Username"))) {
                        userID = sqlResult.getInt("UserID");
                    }
                }

                sqlResult.close();
                stmt.close();

                String eRecipeName = splitter.apostropheEscape(recipeName);

                stmt = c.createStatement();
                sql = "SELECT RecipeID FROM Recipes INNER JOIN Users ON Users.UserID == Recipes.UserID WHERE Users.UserID == " + userID + " AND Recipes.Name == '" + eRecipeName + "';";
                sqlResult = stmt.executeQuery(sql);

                int recipeID = 0;

                while (sqlResult.next()) {
                    recipeID = sqlResult.getInt("RecipeID");
                }

                sqlResult.close();
                stmt.close();

                // Deletion Step
                stmt = c.createStatement();
                sql = "DELETE FROM Steps WHERE RecipeID == " + recipeID + ";";
                stmt.executeUpdate(sql);
                stmt.close();

                stmt = c.createStatement();
                sql = "DELETE FROM Ingredients WHERE RecipeID == " + recipeID + ";";
                stmt.executeUpdate(sql);
                stmt.close();

                stmt = c.createStatement();
                sql = "DELETE FROM Recipes WHERE RecipeID == " + recipeID + " AND UserID == " + userID + ";";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1");
            } catch (Exception e) {
                System.out.println("Error Encountered: " + e);
            }
        } else {
            System.out.println("Login failed.");
        }
    }

    static void deleteUser(Connection c, String username, String password) {
        Validator validator = new Validator();
        if (validator.validateUser(c, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                Statement stmt = null;

                stmt = c.createStatement();
                String sql = "SELECT UserID, Username FROM Users WHERE Username = '" + eUsername + "';";
                var sqlResult = stmt.executeQuery(sql);

                int userID = 0;
                
                while (sqlResult.next()) {
                    if (username.equals(sqlResult.getString("Username"))) {
                        userID = sqlResult.getInt("UserID");
                    }
                }

                sqlResult.close();
                stmt.close();

                stmt = c.createStatement();
                sql = "DELETE FROM ShoppingList WHERE UserID == " + userID + ";";
                stmt.executeUpdate(sql);
                stmt.close();

                
                // Recipes
                stmt = c.createStatement();
                sql = "SELECT Name FROM Recipes WHERE UserID == " + userID + ";";
                sqlResult = stmt.executeQuery(sql);

                int recipeID = 0;

                while (sqlResult.next()) {
                    String name = sqlResult.getString("Name");
                    deleteRecipe(c, username, password, name);
                }

                sqlResult.close();
                stmt.close();


                // User
                stmt = c.createStatement();
                sql = "DELETE FROM Users WHERE UserID == " + userID + ";";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1");
            } catch (Exception e) {
                System.out.println("Error Encountered: " + e);
            }
        } else {
            System.out.println("Login failed.");
        }
    }

    public static void main(String[] args) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            switch (args[0]) {
                case "DeleteShopping": // "DeleteShopping", Username, Password, Item Order
                    deleteShopping(c, args[1], args[2], args[3]);
                    break;

                case "DeleteRecipe": // "DeleteRecipe", Username, Password, Recipe Name
                    deleteRecipe(c, args[1], args[2], args[3]);
                    break;

                case "DeleteUser": // "DeleteRecipe", Username, Password, Recipe Name
                    deleteUser(c, args[1], args[2]);
                    break;
                
                default:
                    System.out.println("ERROR ENCOUNTERED: Invalid get command.");
                    break;
            }

            c.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }
}
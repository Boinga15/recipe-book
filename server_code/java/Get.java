import java.sql.*;

import helperClasses.Splitter;
import helperClasses.Validator;

public class Get {
    static String getShoppingList(Connection c, String username, String password) {
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
                sql = "SELECT Item FROM ShoppingList INNER JOIN Users ON ShoppingList.UserID = Users.UserID WHERE Users.UserID = " + userID + ";";
                sqlResult = stmt.executeQuery(sql);

                String finalResult = "";

                while (sqlResult.next()) {
                    finalResult += sqlResult.getString("Item");
                    finalResult += "|";
                }

                sqlResult.close();
                stmt.close();

                return finalResult;
            } catch (Exception e) {
                System.out.println("Error Encountered: " + e);
                return "-1";
            }
        } else {
            System.out.println("Login failed.");
            return "-1";
        }
    }

    static String getRecipes(Connection c, String username, String password) {
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

                String finalResult = "";

                /*
                | ---> New Element
                || ---> Ingredients to Steps
                ||| ---> Next Recipe
                */

                stmt = c.createStatement();
                sql = "SELECT RecipeID, Name, Description, TimeToBake, ServingSize FROM Recipes INNER JOIN Users ON Users.UserID == Recipes.UserID  WHERE Users.UserID == " + userID + " ORDER BY Recipes.Name;";
                sqlResult = stmt.executeQuery(sql);

                while (sqlResult.next()) {
                    // Basic Information
                    finalResult += sqlResult.getString("Name");
                    finalResult += "|";

                    finalResult += sqlResult.getString("Description");
                    finalResult += "|";

                    finalResult += sqlResult.getString("TimeToBake");
                    finalResult += "|";

                    finalResult += sqlResult.getString("ServingSize");
                    finalResult += "|";

                    // Ingredients
                    int recipeID = sqlResult.getInt("RecipeID");

                    Statement stmt2 = c.createStatement();
                    String sql2 = "SELECT Ingredients.Name FROM Ingredients INNER JOIN Recipes ON Ingredients.RecipeID == Recipes.RecipeID WHERE Recipes.RecipeID == " + recipeID + " ORDER BY Ingredients.ItemOrder;";
                    var sqlResult2 = stmt2.executeQuery(sql2);

                    while (sqlResult2.next()) {
                        finalResult += sqlResult2.getString("Name");
                        finalResult += "|";
                    }

                    sqlResult2.close();
                    stmt2.close();

                    finalResult += "|";

                    // Steps
                    stmt2 = c.createStatement();
                    sql2 = "SELECT Steps.Step FROM Steps INNER JOIN Recipes ON Steps.RecipeID == Recipes.RecipeID WHERE Recipes.RecipeID == " + recipeID + " ORDER BY Steps.ItemOrder;";
                    sqlResult2 = stmt2.executeQuery(sql2);

                    while (sqlResult2.next()) {
                        finalResult += sqlResult2.getString("Step");
                        finalResult += "|";
                    }

                    sqlResult2.close();
                    stmt2.close();

                    finalResult += "||";
                }

                sqlResult.close();
                stmt.close();

                return finalResult;
            } catch (Exception e) {
                System.out.println("Error Encountered: " + e);
                return "-1";
            }
        } else {
            System.out.println("Login failed.");
            return "-1";
        }
    }

    public static void main(String[] args) {
                Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:utilityItems/sqlite:database.db");

            String result = "";

            switch (args[0]) {
                case "GetShopping": // "GetShopping", Username, Password
                    result = getShoppingList(c, args[1], args[2]);
                    break;

                case "GetRecipes": // "GetRecipes", Username, Password
                    result = getRecipes(c, args[1], args[2]);
                    break;
                
                default:
                    System.out.println("ERROR ENCOUNTERED: Invalid get command.");
                    break;
            }

            System.out.println(result);

            c.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }
}
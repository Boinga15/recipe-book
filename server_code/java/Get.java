import java.sql.*;

import helperClasses.Splitter;
import helperClasses.Validator;

import SQL.*;

public class Get {
    static String getShoppingList(SQLFactory factory, String username, String password) {
        Validator validator = new Validator();
        if (validator.validateUser(factory, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                factory.doQuery("SELECT UserID, Username FROM Users;");
                int userID = factory.fetchQuery().getUserID(username);

                factory.doQuery("SELECT Item FROM ShoppingList INNER JOIN Users ON ShoppingList.UserID = Users.UserID WHERE Users.UserID = " + userID + ";");

                String finalResult = "";

                ResultSet result = factory.fetchQuery().getResult();
                while (result.next()) {
                    finalResult += result.getString("Item");
                    finalResult += "|";
                }

                factory.closeQuery();
                return finalResult;
            } catch (Exception e) {
                System.out.println("Error Encountered: " + e);
                factory.closeQuery();
                return "-1";
            }
        } else {
            System.out.println("Login failed.");
            return "-1";
        }
    }

    static String getRecipes(SQLFactory factory, String username, String password) {
        Validator validator = new Validator();
        if (validator.validateUser(factory, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                factory.doQuery("SELECT UserID, Username FROM Users;");
                int userID = factory.fetchQuery().getUserID(username);

                /*
                | ---> New Element
                || ---> Ingredients to Steps
                ||| ---> Next Recipe
                */

                factory.doQuery("SELECT RecipeID, Name, Description, TimeToBake, ServingSize FROM Recipes INNER JOIN Users ON Users.UserID == Recipes.UserID  WHERE Users.UserID == " + userID + " ORDER BY Recipes.Name;");

                String finalResult = "";

                ResultSet result = factory.fetchQuery().getResult();

                while (result.next()) {
                    // Basic Information
                    finalResult += result.getString("Name");
                    finalResult += "|";

                    finalResult += result.getString("Description");
                    finalResult += "|";

                    finalResult += result.getString("TimeToBake");
                    finalResult += "|";

                    finalResult += result.getString("ServingSize");
                    finalResult += "|";

                    // Ingredients
                    int recipeID = result.getInt("RecipeID");
                    SQLFactory sqlFactoryMinor = new SQLFactory();

                    sqlFactoryMinor.doQuery("SELECT Ingredients.Name FROM Ingredients INNER JOIN Recipes ON Ingredients.RecipeID == Recipes.RecipeID WHERE Recipes.RecipeID == " + recipeID + " ORDER BY Ingredients.ItemOrder;");
                    ResultSet result2 = sqlFactoryMinor.fetchQuery().getResult();

                    while (result2.next()) {
                        finalResult += result2.getString("Name");
                        finalResult += "|";
                    }

                    finalResult += "|";

                    // Steps
                    sqlFactoryMinor.doQuery("SELECT Steps.Step FROM Steps INNER JOIN Recipes ON Steps.RecipeID == Recipes.RecipeID WHERE Recipes.RecipeID == " + recipeID + " ORDER BY Steps.ItemOrder;");
                    result2 = sqlFactoryMinor.fetchQuery().getResult();

                    while (result2.next()) {
                        finalResult += result2.getString("Step");
                        finalResult += "|";
                    }

                    sqlFactoryMinor.closeQuery();

                    finalResult += "||";
                }

                factory.closeQuery();
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
        SQLFactory sqlFactory = new SQLFactory();

        String result = "";

        switch (args[0]) {
            case "GetShopping": // "GetShopping", Username, Password
                result = getShoppingList(sqlFactory, args[1], args[2]);
                break;

            case "GetRecipes": // "GetRecipes", Username, Password
                result = getRecipes(sqlFactory, args[1], args[2]);
                break;
            
            default:
                System.out.println("ERROR ENCOUNTERED: Invalid get command.");
                break;
        }

        System.out.println(result);
    }
}
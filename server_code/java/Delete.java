import java.sql.*;

import helperClasses.Splitter;
import helperClasses.Validator;

import SQL.*;

public class Delete {
    static void deleteRecipe(SQLFactory factory, String username, String password, String recipeName) {
        Validator validator = new Validator();
        if (validator.validateUser(factory, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                factory.doQuery("SELECT UserID, Username FROM Users;");
                int userID = factory.fetchQuery().getUserID(username);

                String eRecipeName = splitter.apostropheEscape(recipeName);

                int recipeID = 0;

                factory.doQuery("SELECT RecipeID FROM Recipes INNER JOIN Users ON Users.UserID == Recipes.UserID WHERE Users.UserID == " + userID + " AND Recipes.Name == '" + eRecipeName + "';");

                ResultSet result = factory.fetchQuery().getResult();

                while (result.next()) {
                    recipeID = result.getInt("RecipeID");
                }

                // Deletion Step
                factory.doExecute("DELETE FROM Steps WHERE RecipeID == " + recipeID + ";");
                factory.doExecute("DELETE FROM Ingredients WHERE RecipeID == " + recipeID + ";");
                factory.doExecute("DELETE FROM Recipes WHERE RecipeID == " + recipeID + " AND UserID == " + userID + ";");

                factory.closeQuery();
                System.out.println("1");
            } catch (Exception e) {
                factory.closeQuery();
                System.out.println("Error Encountered: " + e);
            }
        } else {
            factory.closeQuery();
            System.out.println("Login failed.");
        }
    }

    static void deleteUser(SQLFactory factory, String username, String password) {
        Validator validator = new Validator();
        if (validator.validateUser(factory, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                factory.doQuery("SELECT UserID, Username FROM Users;");
                int userID = factory.fetchQuery().getUserID(username);

                // Shopping List
                factory.doExecute("DELETE FROM ShoppingList WHERE UserID == " + userID + ";");
                
                // Recipes
                factory.doQuery("SELECT Name FROM Recipes WHERE UserID == " + userID + ";");

                ResultSet result = factory.fetchQuery().getResult();

                while (result.next()) {
                    String name = result.getString("Name");
                    deleteRecipe(factory, username, password, name);
                }

                // User
                factory.doExecute("DELETE FROM Users WHERE UserID == " + userID + ";");

                factory.closeQuery();
                System.out.println("1");
            } catch (Exception e) {
                factory.closeQuery();
                System.out.println("Error Encountered: " + e);
            }
        } else {
            factory.closeQuery();
            System.out.println("Login failed.");
        }
    }

    public static void main(String[] args) {
        SQLFactory sqlFactory = new SQLFactory();

        switch (args[0]) {
            case "DeleteRecipe": // "DeleteRecipe", Username, Password, Recipe Name
                deleteRecipe(sqlFactory, args[1], args[2], args[3]);
                break;

            case "DeleteUser": // "DeleteRecipe", Username, Password, Recipe Name
                deleteUser(sqlFactory, args[1], args[2]);
                break;
            
            default:
                System.out.println("ERROR ENCOUNTERED: Invalid get command.");
                break;
        }
    }
}
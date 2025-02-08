import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import helperClasses.*;
import SQL.*;

public class Store {
    static void addIngredient(SQLFactory factory, String ingredient, int order, int RecipeID) {
        factory.doQuery("SELECT IngredientID FROM Ingredients;");
        int ingredientID = factory.fetchQuery().queryNextAvailable("IngredientID");

        Splitter splitter = new Splitter();
        String eIngredient = splitter.apostropheEscape(ingredient);

        factory.doExecute("INSERT INTO Ingredients (IngredientID, RecipeID, ItemOrder, Name) VALUES ('" + ingredientID + "', '" + RecipeID + "', '" + order + "', '" + eIngredient + "');");
        factory.closeQuery();
    }

    static void addStep(SQLFactory factory, String step, int order, int RecipeID) {
        factory.doQuery("SELECT StepID FROM Steps;");
        int stepID = factory.fetchQuery().queryNextAvailable("StepID");

        Splitter splitter = new Splitter();
        String eStep = splitter.apostropheEscape(step);

        factory.doExecute("INSERT INTO Steps (StepID, RecipeID, ItemOrder, Step) VALUES ('" + stepID + "', '" + RecipeID + "', '" + order + "', '" + eStep + "');");
        factory.closeQuery();
    }

    static void createUser(SQLFactory factory, String username, String password, String confirmPassword) {
        try {
            // Get next UserID.
            factory.doQuery("SELECT UserID FROM Users;");
            int userID = factory.fetchQuery().queryNextAvailable("UserID");

            // Ensure that the username is unique.
            Boolean uniqueUsername = true;

            Splitter splitter = new Splitter();
            String eUsername = splitter.apostropheEscape(username);
            factory.doQuery("SELECT Username FROM Users WHERE Username = '" + eUsername + "';");

            while (factory.fetchQuery().getResult().next()) {
                uniqueUsername = false;
            }

            if (!uniqueUsername) {
                factory.closeQuery();
                System.out.println("-1"); // User already exists.
            } else if (!password.equals(confirmPassword)) {
                factory.closeQuery();
                System.out.println("-2"); // User already exists.
            } else {
                Encoder encoder = new Encoder();
                String[] passwordParts = encoder.encode(password);

                String ePasswordMain = splitter.apostropheEscape(passwordParts[0]);
                String ePasswordKey = splitter.apostropheEscape(passwordParts[1]);

                factory.doExecute("INSERT INTO Users (UserID, Username, Passmain, Passkey) VALUES ('" + (userID) + "', '" + eUsername + "', '" + ePasswordMain + "', '" + ePasswordKey + "');");
                factory.closeQuery();

                System.out.println("1"); // User successfully added.
            }

        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

    static void refreshShopping(SQLFactory factory, String username, String password, String[] items) {
        Validator validator = new Validator();
        if (validator.validateUser(factory, username, password)) {
            factory.doQuery("SELECT Username, UserID FROM Users;");
            int userID = factory.fetchQuery().getUserID(username);

            factory.doExecute("DELETE FROM ShoppingList WHERE UserID == " + userID + ";");
            
            int currentOrder = 0;

            for (String item : items) {
                factory.doQuery("SELECT ItemID FROM ShoppingList;");
                int itemID = factory.fetchQuery().queryNextAvailable("ItemID");

                factory.doExecute("INSERT INTO ShoppingList ('ItemID', 'UserID', 'ItemOrder', 'Item') VALUES (" + itemID + ", " + userID + ", " + currentOrder + ", '" + item + "');");
            
                currentOrder += 1;
            }

            factory.closeQuery();

            System.out.println("1"); // Shopping items added.
        } else {
            System.out.println("ERROR: Login failed.");
        }
    }

    static void addRecipe(SQLFactory factory, String username, String password, String[] arguments) {
        Validator validator = new Validator();
        if (validator.validateUser(factory, username, password)) {
            Splitter splitter = new Splitter();
            String eUsername = splitter.apostropheEscape(username);

            ArrayList<ArrayList<String>> finalArguments = splitter.recipeArgumentSplit(arguments);

            factory.doQuery("SELECT UserID, Username FROM Users;");
            int userID = factory.fetchQuery().getUserID(username);

            factory.doQuery("SELECT RecipeID FROM Recipes;");
            int recipeID = factory.fetchQuery().queryNextAvailable("RecipeID");

            // Ingredients
            for (int i = 0; i < finalArguments.get(1).size(); i++) {
                addIngredient(factory, finalArguments.get(1).get(i), i, recipeID);
            }

            // Steps
            for (int i = 0; i < finalArguments.get(2).size(); i++) {
                addStep(factory, finalArguments.get(2).get(i), i, recipeID);
            }

            // Recipe
            String eName = splitter.apostropheEscape(finalArguments.get(0).get(0));
            String eDescription = splitter.apostropheEscape(finalArguments.get(0).get(1));
            String eTimeToBake = splitter.apostropheEscape(finalArguments.get(0).get(2));
            String eServingSize = splitter.apostropheEscape(finalArguments.get(0).get(3));

            factory.doExecute("INSERT INTO Recipes (RecipeID, UserID, Name, Description, TimeToBake, ServingSize) VALUES ('" + recipeID + "', '" + userID + "', '" + eName + "', '" + eDescription + "', '" + eTimeToBake + "', '" + eServingSize + "');");
            
            factory.closeQuery();

            System.out.println("1"); // Recipe successfully added.

        } else {
            System.out.println("ERROR: Login failed.");
        }
    }

    public static void main(String[] args) {
        SQLFactory sqlFactory = new SQLFactory();

        switch (args[0]) {
            case "CreateUser": // "CreateUser", Username, Password, Confirm Password
                createUser(sqlFactory, args[1], args[2], args[3]);
                break;
            
            case "RefreshShopping": // "RefreshShopping", Username, Password, Item
                String[] shopItems = Arrays.copyOfRange(args, 3, args.length);
                refreshShopping(sqlFactory, args[1], args[2], shopItems);
                break;
            
            case "AddRecipe": // "AddRecipe", Username, Password, Recipe Arguments
                String[] recipeArgumets = Arrays.copyOfRange(args, 3, args.length);
                addRecipe(sqlFactory, args[1], args[2], recipeArgumets);
                break;
            
            default:
                System.out.println("ERROR ENCOUNTERED: Invalid store command.");
                break;
        }
    }
}
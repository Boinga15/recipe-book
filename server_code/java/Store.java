import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import helperClasses.Splitter;
import helperClasses.Validator;
import helperClasses.Encoder;

public class Store {
    static void addIngredient(Connection c, String ingredient, int order, int RecipeID) {
        try {
            Statement stmt = null;

            stmt = c.createStatement();
            String sql = "SELECT IngredientID FROM Ingredients;";
            var sqlResult = stmt.executeQuery(sql);

            int ingredientID = 0;
            
            while (sqlResult.next()) {
                if (ingredientID < sqlResult.getInt("IngredientID")) {
                    ingredientID = sqlResult.getInt("IngredientID");
                }
            }

            ingredientID += 1;

            sqlResult.close();
            stmt.close();

            Splitter splitter = new Splitter();
            String eIngredient = splitter.apostropheEscape(ingredient);

            stmt = c.createStatement();
            sql = "INSERT INTO Ingredients (IngredientID, RecipeID, ItemOrder, Name) VALUES ('" + ingredientID + "', '" + RecipeID + "', '" + order + "', '" + eIngredient + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

    static void addStep(Connection c, String step, int order, int RecipeID) {
        try {
            Statement stmt = null;

            stmt = c.createStatement();
            String sql = "SELECT StepID FROM Steps;";
            var sqlResult = stmt.executeQuery(sql);

            int stepID = 0;
            
            while (sqlResult.next()) {
                if (stepID < sqlResult.getInt("StepID")) {
                    stepID = sqlResult.getInt("StepID");
                }
            }

            stepID += 1;

            sqlResult.close();
            stmt.close();

            Splitter splitter = new Splitter();
            String eStep = splitter.apostropheEscape(step);

            stmt = c.createStatement();
            sql = "INSERT INTO Steps (StepID, RecipeID, ItemOrder, Step) VALUES ('" + stepID + "', '" + RecipeID + "', '" + order + "', '" + eStep + "');";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

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

            Splitter splitter = new Splitter();
            String eUsername = splitter.apostropheEscape(username);

            stmt = c.createStatement();
            sql = "SELECT Username FROM Users WHERE Username = '" + eUsername + "';";
            var sqlResult2 = stmt.executeQuery(sql);

            while (sqlResult2.next()) {
                uniqueUsername = false;
            }

            sqlResult2.close();
            stmt.close();

            if (!uniqueUsername) {
                System.out.println("-1"); // User already exists.
            } else {
                Encoder encoder = new Encoder();
                String[] passwordParts = encoder.encode(password);

                String ePasswordMain = splitter.apostropheEscape(passwordParts[0]);
                String ePasswordKey = splitter.apostropheEscape(passwordParts[1]);

                stmt = c.createStatement();
                sql = "INSERT INTO Users (UserID, Username, Passmain, Passkey) VALUES ('" + (result + 1) + "', '" + eUsername + "', '" + ePasswordMain + "', '" + ePasswordKey + "');";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1"); // User successfully added.
            }

        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: " + e);
        }
    }

    static void addShoppingItem(Connection c, String username, String password, String item) {
        Validator validator = new Validator();
        if (validator.validateUser(c, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                Statement stmt = null;

                stmt = c.createStatement();
                String sql = "SELECT UserID, Username FROM Users;";
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
                sql = "SELECT ItemID FROM ShoppingList;";
                sqlResult = stmt.executeQuery(sql);

                int shoppingID = 0;
                
                while (sqlResult.next()) {
                    if (shoppingID < sqlResult.getInt("ItemID")) {
                        shoppingID = sqlResult.getInt("ItemID");
                    }
                }

                shoppingID += 1;

                sqlResult.close();
                stmt.close();

                stmt = c.createStatement();
                sql = "SELECT ItemOrder FROM ShoppingList WHERE UserID = " + userID + ";";
                sqlResult = stmt.executeQuery(sql);

                int shoppingOrder = 0;
                
                while (sqlResult.next()) {
                    if (shoppingOrder < sqlResult.getInt("ItemOrder")) {
                        shoppingOrder = sqlResult.getInt("ItemOrder");
                    }
                }

                shoppingOrder += 1;

                sqlResult.close();
                stmt.close();

                String editedItem = splitter.apostropheEscape(item);

                stmt = c.createStatement();
                sql = "INSERT INTO ShoppingList (ItemID, UserID, ItemOrder, Item) VALUES ('" + shoppingID + "', '" + userID + "', '" + shoppingOrder + "', '" + editedItem + "');";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1"); // User successfully added.
            } catch (Exception e) {
                System.out.println("ERROR ENCOUNTERED: " + e);
            }
        } else {
            System.out.println("ERROR: Login failed.");
        }
    }

    static void addRecipe(Connection c, String username, String password, String[] arguments) {
        Validator validator = new Validator();
        if (validator.validateUser(c, username, password)) {
            try {
                Splitter splitter = new Splitter();
                String eUsername = splitter.apostropheEscape(username);

                ArrayList<ArrayList<String>> finalArguments = splitter.recipeArgumentSplit(arguments);

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
                sql = "SELECT RecipeID FROM Recipes;";
                sqlResult = stmt.executeQuery(sql);

                int recipeID = 0;
                
                while (sqlResult.next()) {
                    if (recipeID < sqlResult.getInt("RecipeID")) {
                        recipeID = sqlResult.getInt("RecipeID");
                    }
                }

                recipeID += 1;

                sqlResult.close();
                stmt.close();

                // Ingredients
                for (int i = 0; i < finalArguments.get(1).size(); i++) {
                    addIngredient(c, finalArguments.get(1).get(i), i, recipeID);
                }

                // Steps
                for (int i = 0; i < finalArguments.get(2).size(); i++) {
                    addStep(c, finalArguments.get(2).get(i), i, recipeID);
                }

                String eName = splitter.apostropheEscape(finalArguments.get(0).get(0));
                String eDescription = splitter.apostropheEscape(finalArguments.get(0).get(1));
                String eTimeToBake = splitter.apostropheEscape(finalArguments.get(0).get(2));
                String eServingSize = splitter.apostropheEscape(finalArguments.get(0).get(3));

                // Recipe
                stmt = c.createStatement();
                sql = "INSERT INTO Recipes (RecipeID, UserID, Name, Description, TimeToBake, ServingSize) VALUES ('" + recipeID + "', '" + userID + "', '" + eName + "', '" + eDescription + "', '" + eTimeToBake + "', '" + eServingSize + "');";
                stmt.executeUpdate(sql);
                stmt.close();

                System.out.println("1"); // Recipe successfully added.
            } catch (Exception e) {
                System.out.println("ERROR ENCOUNTERED: " + e);
            }

        } else {
            System.out.println("ERROR: Login failed.");
        }
    }

    public static void main(String[] args) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:utilityItems/database.db");

            switch (args[0]) {
                case "CreateUser": // "CreateUser", Username, Password
                    createUser(c, args[1], args[2]);
                    break;
                
                case "AddShopping": // "AddShopping", Username, Password, Item
                    addShoppingItem(c, args[1], args[2], args[3]);
                    break;
                
                case "AddRecipe": // "AddRecipe", Username, Password, Recipe Arguments
                    String[] recipeArgumets = Arrays.copyOfRange(args, 3, args.length);
                    addRecipe(c, args[1], args[2], recipeArgumets);
                    break;
                
                default:
                    System.out.println("ERROR ENCOUNTERED: Invalid store command.");
                    break;
            }

            c.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED:" + e);
        }
    }
}
import java.sql.*;
import java.util.ArrayList;

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

            stmt = c.createStatement();
            sql = "INSERT INTO Ingredients (IngredientID, RecipeID, ItemOrder, Name) VALUES ('" + ingredientID + "', '" + RecipeID + "', '" + order + "', '" + ingredient + "');";
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

            stmt = c.createStatement();
            sql = "INSERT INTO Steps (StepID, RecipeID, ItemOrder, Step) VALUES ('" + stepID + "', '" + RecipeID + "', '" + order + "', '" + step + "');";
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
                Encoder encoder = new Encoder();
                String[] passwordParts = encoder.encode(password);

                stmt = c.createStatement();
                sql = "INSERT INTO Users (UserID, Username, Passmain, Passkey) VALUES ('" + (result + 1) + "', '" + username + "', '" + passwordParts[0] + "', '" + passwordParts[1] + "');";
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
                Statement stmt = null;

                stmt = c.createStatement();
                String sql = "SELECT UserID, Username FROM Users WHERE Username = '" + username + "';";
                var sqlResult = stmt.executeQuery(sql);

                int userID = 0;
                
                while (sqlResult.next()) {
                    if (username == sqlResult.getString("Username")) {
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

                stmt = c.createStatement();
                sql = "INSERT INTO ShoppingList (ItemID, UserID, ItemOrder, Item) VALUES ('" + shoppingID + "', '" + userID + "', '" + shoppingOrder + "', '" + item + "');";
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

                ArrayList<ArrayList<String>> finalArguments = splitter.recipeArgumentSplit(arguments);

                Statement stmt = null;

                stmt = c.createStatement();
                String sql = "SELECT UserID, Username FROM Users WHERE Username = '" + username + "';";
                var sqlResult = stmt.executeQuery(sql);

                int userID = 0;
                
                while (sqlResult.next()) {
                    if (username == sqlResult.getString("Username")) {
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

                // Recipe
                stmt = c.createStatement();
                sql = "INSERT INTO Recipes (RecipeID, UserID, Name, Description, TimeToBake, ServingSize) VALUES ('" + recipeID + "', '" + userID + "', '" + finalArguments.get(0).get(0) + "', '" + finalArguments.get(0).get(1) + "', '" + finalArguments.get(0).get(2) + "', '" + finalArguments.get(0).get(3) + "');";
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
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            c.close();
        } catch (Exception e) {
            System.out.println("ERROR ENCOUNTERED: Couldn't reach database. === " + e);
        }
    }
}
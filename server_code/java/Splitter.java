import java.util.ArrayList;

public class Splitter {
    public Splitter() {}

    public ArrayList<ArrayList<String>> recipeArgumentSplit(String[] arguments) {
         ArrayList<ArrayList<String>> finalArguments = new ArrayList<ArrayList<String>>();

        for (int i = 0; i < 3; i++) {
            finalArguments.add(new ArrayList<String>());
        }

        // First array list: Name, Description, Bake Time, Serving Side
        for (int i = 0; i < 4; i++) {
            finalArguments.get(0).add(arguments[i]);
        }

        // Second array list: Ingredients (Goes until |||||)
        int cIndex = 4;

        while (!(arguments[cIndex].equals("|||||"))) {
            finalArguments.get(1).add(arguments[cIndex]);
            cIndex += 1;
        }

        // Final array list: Steps (Goes to end).
        while (cIndex < arguments.length - 1) {
            cIndex += 1;
            finalArguments.get(2).add(arguments[cIndex]);
        }

        return finalArguments;
    }

    public String apostropheEscape(String ogString) {
        String newString = "";

        for(int i = 0; i < ogString.length(); i++) {
            if (ogString.charAt(i) == '\'') {
                newString += "''";
            } else {
                newString += ogString.charAt(i);
            }
        }

        return newString;
    }
}
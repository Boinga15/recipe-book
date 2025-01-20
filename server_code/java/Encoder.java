public class Encoder {
    public Encoder() {}

    static char offsetCharacter(char inputChar, int offset) {
        int asciiCode = (int) inputChar;
        
        asciiCode += offset;

        // Lower loop.
        int difference = 32 - asciiCode;

        while(difference > 0) {
            if (difference > 94) {
                difference -= 94;
            } else {
                asciiCode = 126 - difference;
                difference = 0;
            }
        }

        // Upper loop.
        difference = asciiCode - 126;

        while(difference > 0) {
            if (difference > 94) {
                difference -= 94;
            } else {
                asciiCode = 32 + difference;
                difference = 0;
            }
        }

        return (char) asciiCode;
    }

    static int getOffset(char inputChar) {
        int asciiValue = (int) inputChar;

        asciiValue -= 32;

        return asciiValue - 47;
    }

    public String[] encode(String inputString) {
        String[] result = {"", ""};

        int initialOffset = (int)(Math.random() * 93 + 32);
        result[1] += (char) initialOffset;

        for (char ch: inputString.toCharArray()) {
            int randomOffset = (int)(Math.random() * 93 + 32);
            result[1] += (char) randomOffset;
            result[0] += (char) offsetCharacter(ch, getOffset((char) randomOffset) + getOffset((char) initialOffset));
        }

        return result;
    }

    public String decode(String inputPassword, String passwordKey) {
        int initialOffset = getOffset(passwordKey.charAt(0));

        String result = "";

        for (int i = 0; i < inputPassword.length(); i++) {
            result += offsetCharacter(inputPassword.charAt(i), -1 * (initialOffset + getOffset(passwordKey.charAt(i + 1))));
        }

        return result;
    }
}
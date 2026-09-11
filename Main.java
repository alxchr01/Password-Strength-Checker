import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * This Password-Strength-Checker intends to receive an input of a password using any combination of english letters, digits or special characters.
 * The program analises the password characters and gives numerical and verbal scores to inidcate how effective this password would be at protecting data from potential attackers.
 * The current version only measures the score on the number of total characters, uppercase and lowercase letters, digits, and special characters.
 * 
 * Future imporvements could be to include a word matcher with common words that should also be avoided.
 */

public class Main{

      // Regular Colors
      public static final String BLACK  = "\033[0;30m";   // BLACK
      public static final String RED    = "\033[0;31m";   // RED
      public static final String GREEN  = "\033[0;32m";   // GREEN
      public static final String YELLOW = "\033[0;33m";   // YELLOW
      public static final String BLUE   = "\033[0;34m";   // BLUE
      public static final String PURPLE = "\033[0;35m";   // PURPLE
      public static final String CYAN   = "\033[0;36m";   // CYAN
      public static final String WHITE  = "\033[0;37m";   // WHITE

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your password for evaluation: ");
        String userPassword = scanner.nextLine();

        int numericalScore = getNumericalScore(userPassword);
        String verbalScore = getVerbalScore(numericalScore);

        System.out.println("Your password is: " + userPassword); 
        System.out.println("Your numerical password score is: " + numericalScore);
        System.out.println("Your verbal password score is: " + verbalScore);

        scanner.close();
    }

    /**
     * Method that gets the String password and returns the numerical score.
     * @param password String that contains the users' password
     * @return Integer numerical score of the users' password
     */
    private static int getNumericalScore(String password){
                            
        int numericalScore  =   calculateLengthScore(password)
                            +   calculateDigitScore(password)
                            +   calculateUppercaseScore(password)
                            +   calculateLowercaseScore(password)
                            +   calculateSpecialCharScore(password)
                            +   calculateSequentialCharacterPenalty(password)
                            +   calculateLongestRepeatedCharacters(password);

        if(numericalScore < 0)   return 0;
        if(numericalScore > 100) return 100;        
        return numericalScore;
    }

    /**
     * Calculates the score of the password out of 20 based on the passowrd length
     * for a length less than 16 score = length / 16 * 40
     * otherwise score = length / 16 * 20
     * @param password - String - the users' password 
     * @return int score of the password based on length
     */
    private static int calculateLengthScore(String password) {
        int length = password.length(); // gets password length
        
        if (length < 16) return (int) ((double) length / 16 * 40);;
        return (int) ((double) length / 16 * 20);
    }

    /**
     * Calculates the score of the password out of 20 based on the passowrd Uppercase cardinality
     * for a cardinality over 4 the score is maxed out at 20
     * otherwise score = cardinality / 4 * 20
     * @param password - String - the users' password 
     * @return int  score of the password based on Uppercase cardinality
     */
    private static int calculateUppercaseScore(String password) {
        double count = password.chars().filter(Character::isUpperCase).count(); // gets cardinality of uppercase letters

        if (count > 4) return 20;
        return (int) ((double) count / 4 * 20);
    }
     
    /**
     * Calculates the score of the password out of 20 based on the passowrd Lowercase cardinality
     * for a cardinality over 4 the score is maxed out at 20
     * otherwise score = cardinality / 4 * 20
     * @param password - String - the users' password 
     * @return int  score of the password based on Lowercase cardinality
     */
    private static int calculateLowercaseScore(String password) {
        double count = password.chars().filter(Character::isLowerCase).count(); // gets cardinality of lowercase letters

        if (count > 4) return 20;
        return (int) ((double) count / 4 * 20);
    }

    /** 
     * Calculates the score of the password out of 20 based on the passowrd digit cardinality
     * for a cardinality over 4 the score is maxed out at 20
     * otherwise score = cardinality / 4 * 20
     * @param password - String - the users' password 
     * @return int  score of the password based on digit cardinality
     */ 
    private static int calculateDigitScore(String password) {
        double count = password.chars().filter(Character::isDigit).count(); // gets cardinality of digit

        if (count > 4) return 20;
        return (int) ((double) count / 4 * 20);
    }

    /**
     * Calculates the score of the password out of 20 based on the passowrd special character cardinality
     * for a cardinality over 4 the score is maxed out at 20
     * otherwise score = cardinality / 4 * 20
     * special characters are all characters that are not letters or digits
     * @param password - String - the users' password 
     * @return int  score of the password based on special character cardinality
     */
    private static int calculateSpecialCharScore(String password) {
        double count = password.chars().filter(ch -> !Character.isLetterOrDigit(ch)).count(); // gets cardinality of special character

        if (count > 4) return 20;
        return (int) ((double) count / 4 * 20);
    }

    /**
     * Gets the numerical score of the users' password and for the prediefined ranges returns the corresponding verbal score
     * @param numericalScore - int - passwords' numerical score
     * @return String - passwords' verbal score
     */
    private static String getVerbalScore(int numericalScore){
        if(isInRange(numericalScore, 0, 20))
            return RED + "Poor";
        if(isInRange(numericalScore, 20, 40))
            return YELLOW + "Not Great";
        if(isInRange(numericalScore, 40, 60))
            return GREEN + "Average";
        if(isInRange(numericalScore, 60, 80))
            return BLUE + "Better Than Average";
        if(isInRange(numericalScore, 80, 90))
            return PURPLE + "Almost Fantastic";
        if(isInRange(numericalScore, 90, 100))
            return CYAN + "Fantastic";
        return "Error";
        
    }

    /**
     * Helper method that checks if the given number is within the upper and lower boundaries
     * @param number - int - any integer
     * @param lower - int - lower boundary
     * @param upper - int - upper boundary
     * @return true - if number is in range / false - if number is not in range
     */
    public static boolean isInRange(int number, int lower, int upper) {
        return number >= lower && number <= upper;
    }

    /**
     * Finds the longest sequence of sequencial or inversly sequencial letters or digits in the password
     * @param password - String - the users' password
     * @return - int - the cardinality of the longest sequence in the password
     */
    public static int calculateSequentialCharacterPenalty(String password){
        String alphabet         = "abcdefghijklmnopqrstuvwxyz";
        String alphabetInverse  = "zyxwvutsrqponmlkjihgfedcba";
        String digits           = "01234567890";
        String digitsInverse    = "09876543210";

        int alphabetLS          = calculateLongestCommonSequence(password, alphabet);
        int alphabetInverseLS   = calculateLongestCommonSequence(password, alphabetInverse);
        int digitsLS            = calculateLongestCommonSequence(password, digits);
        int digitsInverseLS     = calculateLongestCommonSequence(password, digitsInverse);

        int longestSequence = maxOfFour(alphabetLS, alphabetInverseLS, digitsLS, digitsInverseLS);

        if (longestSequence == 1) return 0;
        return -1 * longestSequence;
    }

    /**
     * Finds the Longest Common Sequence among two given Strings
     * this sequence doesnt have to be continouus,
     * eg. LCS(abc, a1b2c3) == abc
     * @param s1 - String - first string
     * @param s2 - String - second string
     * @return - int - cardinality of the longest sequence bewtween the two strings
     */
    private static int calculateLongestCommonSequence(String s1, String s2){
        int maxR = s2.length();
        int maxC = s1.length();
        int maxLength = 0;
        int[][] arr = new int[maxR + 1][maxC + 1];

        for (int i = 0; i <= maxR; i++){
            for (int j = 0; j <= maxC; j++){
                if (i == 0 || j == 0)
                    arr[i][j] = 0;
                else if (s1.charAt(j - 1) == s2.charAt(i - 1))
                    arr[i][j] = arr[i -1][j - 1] + 1;
                else
                    arr[i][j] = Math.max(arr[i - 1][j], arr[i][j - 1]);

                maxLength = Math.max(maxLength, arr[i][j]);
            }
        }
        return maxLength;
    }
    
    /**
     * Helper method that calculates the largest one out of the four integer parameters
     * @param a - int - number 1
     * @param b - int - number 2
     * @param c - int - number 3
     * @param d - int - number 4
     * @return - int - the largest number
     */
    private static int maxOfFour(int a, int b, int c, int d){
        int maxOfab = Math.max(a,b);
        int maxOfcd = Math.max(c,d);

        return Math.max(maxOfab,maxOfcd);
    }

    /**
     * Calculates the longest sequence of repeated characters in the password string
     * the characters are counted even if they are not next to each other
     * @param password - String - users' password
     * @return - int - the cardinality of the longest repeated charcter string
     */
    private static int calculateLongestRepeatedCharacters(String password) {
        Map<Character, Integer> charCountMap = new HashMap<>();

        for (char ch : password.toCharArray()) {
            charCountMap.put(ch, charCountMap.getOrDefault(ch, 0) + 1);
        }

        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : charCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
            }
        }

        if(maxCount <= 2) return 0;
        return -1 * (int)Math.pow(maxCount, 2);
    }

}
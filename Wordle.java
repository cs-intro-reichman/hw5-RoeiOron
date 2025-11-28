public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        In file = new In(filename);
        String[] words = file.readAllLines();
        return words;
    }

    // Choose a random secret word from the dictionary. 
    // Hint: Pick a random index between 0 and dict.length (not including) using Math.random()
    public static String chooseSecretWord(String[] dict) {
		int range = dict.length; // Use the actual length
        int randomIndex = (int) (Math.random() * range); 
        return dict[randomIndex];
    }


    public static boolean containsWord(String guess, String[] dict) {
    // Searches the dictionary (dict) for the input guess.
    for (String dictWord : dict) {
        if (guess.equals(dictWord)) {
            return true; 
        }
    }
    return false;
}

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
		if(secret.indexOf(c)!= -1) {
            return true; } else {
                return false;
            }
    }

    public static void computeFeedback(String secret, String guess, char[] resultRow) {
    for (int i = 0; i < secret.length(); i++) {
        if (secret.charAt(i) == guess.charAt(i)) {
            resultRow[i] = 'G';  
        } else {
            resultRow[i] = '_'; 
        }
    }
    for (int i = 0; i < secret.length(); i++) {

        if (resultRow[i] == '_') {
            if (secret.indexOf(guess.charAt(i)) != -1) {
                resultRow[i] = 'Y';  
            }
        }
    }
}
    
    

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function 
    // guesses should look like:
    // guesses[2][0] // 'H'
	// guesses[2][1] // 'E'
	// guesses[2][2] // 'L'
	// guesses[2][3] // 'L'
	// guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
        for (int j = 0; j < guess.length(); j++) {
            guesses[row][j] = guess.charAt(j);
            }   
        }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        for(int i = 0; i < resultRow.length; i++ ){
            if(resultRow[i] != 'G'){
                return false;
            }
            }
            return true;
            }
        

    public static void main(String[] args) {

    int MAX_ATTEMPTS = 6; // Player has 6 guesses [cite: 9, 31]
    
    // Read dictionary
    String[] dict = readDictionary("dictionary.txt");

    // Choose secret word
    String secret = chooseSecretWord(dict);

    // CRITICAL FIX: Base WORD_LENGTH on the secret word, not the hardcoded constant.
    final int GAME_WORD_LENGTH = secret.length(); 

    // Prepare 2D arrays using the dynamic length
    char[][] guesses = new char[MAX_ATTEMPTS][GAME_WORD_LENGTH];
    char[][] results = new char[MAX_ATTEMPTS][GAME_WORD_LENGTH];
    
    // Prepare to read from the standard input 
    In inp = new In();

    int attempt = 0;
    boolean won = false;

    while (attempt < MAX_ATTEMPTS && !won) {

        String guess = "";
        boolean valid = false;

        // Loop until you read a valid guess
        while (!valid) {
            // Use the dynamic length in the prompt
            System.out.print("Enter your guess (" + GAME_WORD_LENGTH + "-letter word): ");
            
            // Read input and convert to uppercase
            guess = inp.readString().toUpperCase();
            
            // Validation Check (Must be correct length AND in the dictionary)
            if (guess.length() != GAME_WORD_LENGTH || !containsWord(guess, dict)) { 
                System.out.println("Invalid word. Please try again.");
            } else {
                valid = true;
            }
        }

        // Store guess and compute feedback
        storeGuess(guess, guesses, attempt);              
        computeFeedback(secret, guess, results[attempt]);

        // Print board
        printBoard(guesses, results, attempt);

        // Check win
        if (isAllGreen(results[attempt])) {
            System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
            won = true;
        }

        attempt++;
    }

    if (!won) {
        System.out.println("Sorry, you did not guess the word."); 
        System.out.println("The secret word was: " + secret); 
    }

    inp.close();
} // <-- Ensure the main method is closed
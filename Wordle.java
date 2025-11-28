public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) {
        // Assumes the external 'In' class is available.
        In file = new In(filename);
        String[] words = file.readAllLines();
        return words;
    }

    // Choose a random secret word from the dictionary.
    public static String chooseSecretWord(String[] dict) {
        int range = dict.length; // Use the actual length of the dictionary array
        int randomIndex = (int) (Math.random() * range); 
        return dict[randomIndex];
    }

    // Checks if the guess word exists in the dictionary array.
    public static boolean containsWord(String guess, String[] dict) {
        for (String dictWord : dict) {
            // Compare the content of the guess to the dictionary word
            if (guess.equals(dictWord)) {
                return true; 
            }
        }
        return false;
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
        // Returns true if indexOf returns 0 or greater (meaning found)
        return secret.indexOf(c) != -1;
    }

    // Compute feedback for a single guess into resultRow using the simplified rules:
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        
        // --- PASS 1: Find 'G' (Green/Exact Matches) ---
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                resultRow[i] = 'G'; // Correct letter, correct position
            } else {
                resultRow[i] = '_'; // Initialize non-matches to Gray/Blank
            }
        }
        
        // --- PASS 2: Find 'Y' (Yellow/Misplaced Matches) ---
        // Uses the simplified rule: check if the character exists ANYWHERE in the secret.
        for (int i = 0; i < secret.length(); i++) {
            
            // Only process positions that were NOT already marked 'G'
            if (resultRow[i] == '_') {
                
                // Check if the character exists anywhere in the original secret word
                if (secret.indexOf(guess.charAt(i)) != -1) {
                    resultRow[i] = 'Y'; // Letter is in the word but wrong position
                }
            }
        } 
    } 

    // Store guess string (chars) into the given row of guesses 2D array.
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
        
    // The main game loop function
    public static void main(String[] args) {

        int MAX_ATTEMPTS = 6; [cite_start]// The player has 6 guesses [cite: 9]
        
        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Define the word length dynamically based on the secret word for robustness.
        final int GAME_WORD_LENGTH = secret.length(); 

        [cite_start]// Prepare 2D arrays for guesses and results (rows=attempts, columns=letters) [cite: 80]
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
                // Prompt uses the dynamic word length
                System.out.print("Enter your guess (" + GAME_WORD_LENGTH + "-letter word): ");
                
                // Read input and convert to UPPERCASE
                guess = inp.readString().toUpperCase();
                
                // Validation Check: Must be correct length OR must be in the dictionary
                if (guess.length() != GAME_WORD_LENGTH || !containsWord(guess, dict)) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            storeGuess(guess, guesses, attempt);              
            computeFeedback(secret, guess, results[attempt]);

            [cite_start]// Print board after each valid guess [cite: 81]
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        [cite_start]// Loss condition: Game ends if loop finishes without a win [cite: 84]
        if (!won) {
            System.out.println("Sorry, you did not guess the word."); 
            System.out.println("The secret word was: " + secret); 
        }

        inp.close();
    }
}
package Hangman;

import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;

public class hangman {
    public static void main(String[] args) {
        System.out.println("HANGMAN");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Type \"play\" to play the game, \"exit\" to quit: > ");
            String choice = scanner.nextLine();

            if (choice.equals("play")) {
                playGame(scanner);
                System.out.println();
            } else if (choice.equals("exit")) {
                break;
            }
        }

        scanner.close();
    }

    private static void playGame(Scanner scanner) {
        String[] words = {"python", "java", "javascript", "kotlin"};
        Random random = new Random();
        String correctWord = words[random.nextInt(words.length)];

        char[] displayWord = new char[correctWord.length()];
        Arrays.fill(displayWord, '-');

        HashSet<Character> guessedLetters = new HashSet<>();

        int lives = 8;

        while (lives > 0) {
            System.out.println(displayWord);
            System.out.print("Input a letter: > ");
            String input = scanner.nextLine();

            if (input.length() != 1) {
                System.out.println("You should input a single letter");
                continue;
            }

            char guess = input.charAt(0);

            if (!Character.isLowerCase(guess) || !Character.isLetter(guess)) {
                System.out.println("Please enter a lowercase English letter");
                continue;
            }

            if (guessedLetters.contains(guess)) {
                System.out.println("You've already guessed this letter");
                continue;
            }

            guessedLetters.add(guess);

            boolean found = false;
            boolean improvement = false;

            for (int i = 0; i < correctWord.length(); i++) {
                if (correctWord.charAt(i) == guess) {
                    if (displayWord[i] == '-') {
                        displayWord[i] = guess;
                        improvement = true;
                    }
                    found = true;
                }
            }

            if (!found) {
                System.out.println("That letter doesn't appear in the word");
                lives--;
            } else if (!improvement) {
                System.out.println("No improvements");
            }

            if (new String(displayWord).equals(correctWord)) {
                System.out.println(displayWord);
                System.out.println("You guessed the word " + correctWord + "!");
                System.out.println("You survived!");
                return;
            }
        }

        System.out.println("You lost!");
    }
}

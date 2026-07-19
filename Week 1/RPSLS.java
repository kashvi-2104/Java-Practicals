import java.util.Random;
import java.util.Scanner;

enum Move {
    ROCK, PAPER, SCISSORS, LIZARD, SPOCK
}

public class RPSLS {

    static int winner(Move a, Move b) {

        if (a == b)
            return 0;

        switch (a) {
            case ROCK:
                return (b == Move.SCISSORS || b == Move.LIZARD) ? 1 : -1;

            case PAPER:
                return (b == Move.ROCK || b == Move.SPOCK) ? 1 : -1;

            case SCISSORS:
                return (b == Move.PAPER || b == Move.LIZARD) ? 1 : -1;

            case LIZARD:
                return (b == Move.PAPER || b == Move.SPOCK) ? 1 : -1;

            case SPOCK:
                return (b == Move.ROCK || b == Move.SCISSORS) ? 1 : -1;
        }

        return 0;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        while (true) {

            System.out.print("Enter move (ROCK, PAPER, SCISSORS, LIZARD, SPOCK) or DONE: ");
            String input = sc.next().toUpperCase();

            if (input.equals("DONE"))
                break;

            Move player = Move.valueOf(input);
            Move computer = Move.values()[random.nextInt(5)];

            int result = winner(player, computer);

            System.out.println("Your Move: " + player);
            System.out.println("Computer Move: " + computer);

            if (result == 1) {
                System.out.println("You win this round!");
                playerScore++;
            } else if (result == -1) {
                System.out.println("Computer wins this round!");
                computerScore++;
            } else {
                System.out.println("Tie!");
            }
        }

        System.out.println("\n===== FINAL RESULT =====");
        System.out.println("You: " + playerScore);
        System.out.println("Computer: " + computerScore);

        if (playerScore > computerScore)
            System.out.println("Overall Winner: You");
        else if (computerScore > playerScore)
            System.out.println("Overall Winner: Computer");
        else
            System.out.println("Match Draw");

        sc.close();
    }
}
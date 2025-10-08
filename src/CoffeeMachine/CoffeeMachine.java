package CoffeeMachine;

import java.util.Scanner;

public class CoffeeMachine {
    public static void main(String[] args) {
        Machine machine = new Machine();
        Scanner scanner = new Scanner(System.in);

        //noinspection InfiniteLoopStatement
        while (true) {
            String input = scanner.nextLine();
            machine.processInput(input);
        }
    }
}

class Coffee {
    String name;
    int water;
    int milk;
    int beans;
    int price;

    public Coffee(String name, int water, int milk, int beans, int price) {
        this.name = name;
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.price = price;
    }
}

class Machine {
    private int water = 400;
    private int milk = 540;
    private int beans = 120;
    private int cups = 9;
    private int money = 550;

    private enum State {ACTION, BUY, FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS}
    private State state = State.ACTION;

    private final Coffee[] coffees = {
            new Coffee("espresso", 250, 0, 16, 4),
            new Coffee("latte", 350, 75, 20, 7),
            new Coffee("cappuccino", 200, 100, 12, 6)
    };

    public Machine() {
        printPrompt();
    }

    public void processInput(String input) {
        switch (state) {
            case ACTION -> handleAction(input);
            case BUY -> handleBuy(input);
            case FILL_WATER, FILL_MILK, FILL_BEANS, FILL_CUPS -> handleFill(input);
        }
    }

    private void handleAction(String input) {
        switch (input) {
            case "buy" -> {
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                state = State.BUY;
            }
            case "fill" -> {
                System.out.println("Write how many ml of water you want to add:");
                state = State.FILL_WATER;
            }
            case "take" -> {
                System.out.println("I gave you " + money);
                money = 0;
                printPrompt();
            }
            case "remaining" -> {
                printState();
                printPrompt();
            }
            case "exit" -> System.exit(0);
            default -> {
                System.out.println("Unknown command");
                printPrompt();
            }
        }
    }

    private void handleBuy(String input) {
        if (input.equals("back")) {
            state = State.ACTION;
            printPrompt();
            return;
        }
        try {
            int choice = Integer.parseInt(input) - 1;
            if (choice >= 0 && choice < coffees.length) {
                Coffee coffee = coffees[choice];
                if (water < coffee.water) {
                    System.out.println("Sorry, not enough water!");
                } else if (milk < coffee.milk) {
                    System.out.println("Sorry, not enough milk!");
                } else if (beans < coffee.beans) {
                    System.out.println("Sorry, not enough coffee beans!");
                } else if (cups < 1) {
                    System.out.println("Sorry, not enough disposable cups!");
                } else {
                    System.out.println("I have enough resources, making you a coffee!");
                    water -= coffee.water;
                    milk -= coffee.milk;
                    beans -= coffee.beans;
                    cups--;
                    money += coffee.price;
                }
                state = State.ACTION;
                printPrompt();
            } else {
                System.out.println("Invalid choice, try again:");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, try again:");
        }
    }

    private void handleFill(String input) {
        int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            return;
        }
        switch (state) {
            case FILL_WATER -> {
                water += value;
                System.out.println("Write how many ml of milk you want to add:");
                state = State.FILL_MILK;
            }
            case FILL_MILK -> {
                milk += value;
                System.out.println("Write how many grams of coffee beans you want to add:");
                state = State.FILL_BEANS;
            }
            case FILL_BEANS -> {
                beans += value;
                System.out.println("Write how many disposable coffee cups you want to add:");
                state = State.FILL_CUPS;
            }
            case FILL_CUPS -> {
                cups += value;
                state = State.ACTION;
                printPrompt();
            }
        }
    }

    private void printState() {
        System.out.println("The coffee machine has:");
        System.out.println(water + " of water");
        System.out.println(milk + " of milk");
        System.out.println(beans + " of coffee beans");
        System.out.println(cups + " of disposable cups");
        System.out.println(money + " of money");
    }

    private void printPrompt() {
        if (state == State.ACTION) {
            System.out.println("Write action (buy, fill, take, remaining, exit):");
        }
    }
}

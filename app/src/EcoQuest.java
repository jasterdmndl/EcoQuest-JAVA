import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EcoQuest {
    private static final int GRID_SIZE = 15;
    private static final char PLAYER = 'P';
    private static final char BIN = 'B';
    private static final char TRASH = 'T';
    private static final char EMPTY = '.';
    // Primary high score file in user's home directory; fallback to current directory
    private static final String HIGHSCORE_FILE_PRIMARY = System.getProperty("user.home") + System.getProperty("file.separator") + ".ecoquest_highscore.txt";
    private static final String HIGHSCORE_FILE_FALLBACK = ".ecoquest_highscore.txt";  // Current directory
    private String highScoreFile;  // Will be set to the working path

    // ANSI color codes for terminal
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";  // Player
    private static final String BLUE = "\033[34m";   // Bin
    private static final String RED = "\033[31m";    // Trash
    private static final String YELLOW = "\033[33m"; // Warnings/Highlights
    private static final String CYAN = "\033[36m";   // Success messages

    private char[][] grid;
    private int playerX, playerY;
    private int binX, binY;
    private List<int[]> trashPositions;  // List of [x, y] for each trash
    private List<String> currentTrashItems;
    private List<String> currentTrashTypes;
    private List<String> carriedTrashItems;  // Trash items being carried
    private List<String> carriedTrashTypes;  // Types of trash being carried
    private boolean carryingTrash;
    private int numTrash;  // Number of trash items in current round
    private int score;
    private int streak;
    private int highScore;
    private Random random;
    private Scanner scanner;

    // Listahan ng mga basura
    private String[] trashItems = {
        "Balat ng Saging", "Apple", "Orange", "Bread", "Dahon", "Buto ng Mangga", "Karne",  // Nabubulok
        "Plastic Bag", "Styrofoam", "Cigarette Butt", "Lata",  // Di Nabubulok
        "Glass Bottle", "Aluminum Can", "Dyaryo", "Cardboard", "Plastik ng Softdrinks", // Recyclable
        "Battery", "Paint Can", "Light Bulb", "Expired na Gamot", "Karayom"         // Hazardous
    };
    private String[] trashTypes = {
        "Nabubulok", "Nabubulok", "Nabubulok", "Nabubulok", "Nabubulok", "Nabubulok", "Nabubulok",
        "Hindi Nabubulok", "Hindi Nabubulok", "Hindi Nabubulok", "Hindi Nabubulok",
        "Recyclable", "Recyclable", "Recyclable", "Recyclable", "Recyclable",
        "Hazardous", "Hazardous", "Hazardous", "Hazardous", "Hazardous"
    };

    // Concstructor - Sets everything up before the game starts
    public EcoQuest() {
        grid = new char[GRID_SIZE][GRID_SIZE];
        random = new Random();
        scanner = new Scanner(System.in);
        initializeGrid();
        score = 0;
        streak = 0;
        numTrash = 1;
        carryingTrash = false;
        trashPositions = new ArrayList<>();
        currentTrashItems = new ArrayList<>();
        currentTrashTypes = new ArrayList<>();
        carriedTrashItems = new ArrayList<>();
        carriedTrashTypes = new ArrayList<>();
        // Determine the working high score file path
        determineHighScoreFile();
        loadHighScore();
        randomizePositions();
    }

    // ensure mag-save yung high score
    private void determineHighScoreFile() {
        // Try primary path first
        try {
            Files.createDirectories(Paths.get(HIGHSCORE_FILE_PRIMARY).getParent());  // Ensure directory exists
            highScoreFile = HIGHSCORE_FILE_PRIMARY;
        } catch (IOException e) {
            System.out.println(YELLOW + "Primary high score path failed (" + HIGHSCORE_FILE_PRIMARY + "). Using fallback." + RESET);
            try {
                Files.createDirectories(Paths.get(HIGHSCORE_FILE_FALLBACK).getParent());  // Ensure directory exists
                highScoreFile = HIGHSCORE_FILE_FALLBACK;
            } catch (IOException e2) {
                System.out.println(RED + "Fallback path also failed. High scores may not persist." + RESET);
                highScoreFile = HIGHSCORE_FILE_FALLBACK;  // Still try
            }
        }
        System.out.println(CYAN + "Using high score file: " + highScoreFile + RESET);
    }
    // Resets the Grid
    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = EMPTY;
            }
        }
    }

    private void loadHighScore() {
        try {
            if (Files.exists(Paths.get(highScoreFile))) {
                String content = Files.readString(Paths.get(highScoreFile)).trim();
                highScore = Integer.parseInt(content);
                if (highScore < 0) highScore = 0;  // Ensure non-negative
                System.out.println(CYAN + "Loaded high score: " + highScore + RESET);
            } else {
                highScore = 0;
                System.out.println(YELLOW + "No high score file found. Starting with 0." + RESET);
            }
        } catch (IOException | NumberFormatException e) {
            highScore = 0;
            System.out.println(RED + "Error loading high score: " + e.getMessage() + ". Starting with 0." + RESET);
        }
    }

    private void saveHighScore() {
        try {
            // Use Files.writeString if Java 11+, otherwise fallback to FileWriter
            if (javaVersionAtLeast(11)) {
                Files.writeString(Paths.get(highScoreFile), String.valueOf(highScore), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                // Fallback for Java 8
                try (FileWriter writer = new FileWriter(highScoreFile)) {
                    writer.write(String.valueOf(highScore));
                    writer.flush();
                }
            }
            System.out.println(CYAN + "High score saved: " + highScore + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error saving high score: " + e.getMessage() + ". Check permissions or run as admin." + RESET);
        }
    }

    private boolean javaVersionAtLeast(int version) {
        String javaVersion = System.getProperty("java.version");
        return Integer.parseInt(javaVersion.split("\\.")[0]) >= version;
    }

    // random player, bin and trash position
    private void randomizePositions() {
        // Clear grid
        initializeGrid();
        trashPositions.clear();
        currentTrashItems.clear();
        currentTrashTypes.clear();

        // Randomize bin position
        binX = random.nextInt(GRID_SIZE);
        binY = random.nextInt(GRID_SIZE);
        grid[binX][binY] = BIN;

        // Randomize player position, ensuring not on bin
        do {
            playerX = random.nextInt(GRID_SIZE);
            playerY = random.nextInt(GRID_SIZE);
        } while (grid[playerX][playerY] != EMPTY);
        grid[playerX][playerY] = PLAYER;

        // Randomize trash positions, ensuring no overlaps
        for (int i = 0; i < numTrash; i++) {
            int[] pos = new int[2];
            do {
                pos[0] = random.nextInt(GRID_SIZE);
                pos[1] = random.nextInt(GRID_SIZE);
            } while (grid[pos[0]][pos[1]] != EMPTY);
            grid[pos[0]][pos[1]] = TRASH;
            trashPositions.add(pos);

            // Set current trash item and type
            int index = random.nextInt(trashItems.length);
            currentTrashItems.add(trashItems[index]);
            currentTrashTypes.add(trashTypes[index]);
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // main interface
    private void displayGrid() {
        System.out.println(CYAN + "=====================================" + RESET);
        System.out.println(CYAN + "         WELCOME TO ECO QUEST!" + RESET);
        System.out.println(CYAN + "=====================================" + RESET);
        System.out.println("Pick up " + RED + "T" + RESET + "(Trash), bring it to " + BLUE + "B" + RESET + "(Bin), and identify its type!");
        System.out.println("Legend: " + GREEN + "P" + RESET + "=Player, " + BLUE + "B" + RESET + "=Bin, " + RED + "T" + RESET + "=Trash, .=Empty");
        System.out.println("Score: " + score + " | High Score: " + highScore + " | Streak: " + streak + " | Trash Count: " + numTrash);
        System.out.println("Use WASD to move. Q to quit.");
        printGridWithColors();
        if (carryingTrash) {
            System.out.println(YELLOW + "You are carrying trash! Head to the bin." + RESET);
        }
        System.out.println(CYAN + "=====================================" + RESET);
    }

    private void printGridWithColors() {
        // Top border
        System.out.print("+");
        for (int j = 0; j < GRID_SIZE; j++) {
            System.out.print("--");
        }
        System.out.println("+");

        // Grid rows with colors
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print("|");
            for (int j = 0; j < GRID_SIZE; j++) {
                char c = grid[i][j];
                String color = RESET;
                switch (c) {
                    case PLAYER: color = GREEN; break;
                    case BIN: color = BLUE; break;
                    case TRASH: color = RED; break;
                }
                System.out.print(color + c + RESET + " ");
            }
            System.out.println("|");
        }

        // Bottom border
        System.out.print("+");
        for (int j = 0; j < GRID_SIZE; j++) {
            System.out.print("--");
        }
        System.out.println("+");
    }

    // logic sa pag move ng player
    private void movePlayer(char direction) {
        int newX = playerX;
        int newY = playerY;

        switch (Character.toLowerCase(direction)) {
            case 'w': newX--; break;
            case 's': newX++; break;
            case 'a': newY--; break;
            case 'd': newY++; break;
            default:
                System.out.println(RED + "Invalid move! Use W/A/S/D." + RESET);
                pause(1000);
                return;
        }

        
        if (newX < 0 || newX >= GRID_SIZE || newY < 0 || newY >= GRID_SIZE) {
            System.out.println(YELLOW + "Can't move out of bounds!" + RESET);
            pause(1000);
            return; // Can't move out of bounds
        }

        // Clear old position
        grid[playerX][playerY] = EMPTY;

        boolean actionTaken = false;

        // Check if moving to trash
        for (int i = 0; i < trashPositions.size(); i++) {
            int[] pos = trashPositions.get(i);
            if (newX == pos[0] && newY == pos[1]) {
                // Add this trash to carried items
                carriedTrashItems.add(currentTrashItems.get(i));
                carriedTrashTypes.add(currentTrashTypes.get(i));
                carryingTrash = true;
                grid[pos[0]][pos[1]] = EMPTY;  // Remove trash from grid
                trashPositions.remove(i);  // Remove from list
                currentTrashItems.remove(i);
                currentTrashTypes.remove(i);
                System.out.println(CYAN + "Trash picked up! Now take it to the bin." + RESET);
                pause(1500);
                playerX = newX;
                playerY = newY;
                grid[playerX][playerY] = PLAYER;
                actionTaken = true;
                break;
            }
        }

        // Check if moving to bin with trash
        if (newX == binX && newY == binY && carryingTrash && trashPositions.isEmpty()) {
            identifyTrash();
            carryingTrash = false;
            carriedTrashItems.clear();
            carriedTrashTypes.clear();
            randomizePositions();
            actionTaken = true;
        }
        // Check if moving to bin without trash
        else if (newX == binX && newY == binY && !carryingTrash) {
            System.out.println(YELLOW + "Warning: Pick up the trash first before coming to the bin!" + RESET);
            pause(2000);
            actionTaken = true;  // Don't update position, let player try again
        }

        // Update position only if no special action
        if (!actionTaken) {
            playerX = newX;
            playerY = newY;
            grid[playerX][playerY] = PLAYER;
        }
    }

    private void identifyTrash() {
        clearScreen();
        System.out.println(CYAN + "You deposited the trash! Now identify its type(s)." + RESET);
        boolean allCorrect = true;
        for (int i = 0; i < carriedTrashItems.size(); i++) {
            System.out.println("Trash " + (i + 1) + ": " + carriedTrashItems.get(i));
            System.out.println("Which bin does it belong to?");
            System.out.println("1. Nabubulok");
            System.out.println("2. Hindi Nabubulok");
            System.out.println("3. Recyclable");
            System.out.println("4. Hazardous");
            System.out.print("Enter your choice (1-4): ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input! Please enter a number 1-4." + RESET);
                allCorrect = false;
                continue;
            }

            String guessedType = "";
            switch (choice) {
                case 1: guessedType = "Nabubulok"; break;
                case 2: guessedType = "Hindi Nabubulok"; break;
                case 3: guessedType = "Recyclable"; break;
                case 4: guessedType = "Hazardous"; break;
                default:
                    System.out.println(RED + "Invalid choice! Must be 1-4." + RESET);
                    allCorrect = false;
                    continue;
            }

            if (!guessedType.equals(carriedTrashTypes.get(i))) {
                allCorrect = false;
                System.out.println(RED + "Wrong! The correct type was: " + carriedTrashTypes.get(i) + RESET);
            } else {
                System.out.println(GREEN + "Correct!" + RESET);
            }
        }

        if (allCorrect) {
            score++;
            streak++;
            System.out.println(GREEN + "All correct! Score +1, Streak: " + streak + RESET);
            if (streak % 5 == 0) {
                numTrash++;
                System.out.println(CYAN + "Streak of 5! Trash count increased to " + numTrash + RESET);
            }
        } else {
            // If the current score is a new high, save it before resetting
            if (score > highScore) {
                highScore = score;
                saveHighScore();
                System.out.println(GREEN + "New High Score saved: " + highScore + " (before reset)" + RESET);
            }
            score = 0;
            streak = 0;
            numTrash = 1;  // Reset trash count
            System.out.println(RED + "One or more wrong! Score and streak reset to 0. Trash count reset to 1." + RESET);
        }

        pause(2500);  // Longer pause for feedback
    }

    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // Handle interruption if needed
        }
    }

    public void play() {
        while (true) {
            clearScreen();
            displayGrid();
            System.out.print("Move (WASD) or Q to quit: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Q")) {
                break;
            }
            if (input.length() > 0) {
                movePlayer(input.charAt(0));
            } else {
                System.out.println(RED + "No input detected. Try again." + RESET);
                pause(1000);
            }
        }

        clearScreen();
        System.out.println(CYAN + "=====================================" + RESET);
        System.out.println(CYAN + "             GAME OVER!" + RESET);
        System.out.println(CYAN + "=====================================" + RESET);
        System.out.println("Final Score: " + score);
        
        // Ensure we save the high score if current score is better
        if (score > highScore) {
            highScore = score;
            saveHighScore();
            pause(500);  // Give file time to write
            System.out.println(GREEN + "New High Score: " + highScore + "!" + RESET);
        } else {
            System.out.println("High Score: " + highScore);
        }
        // Always save the high score at the end for safety
        saveHighScore();
        System.out.println("Thanks for playing Eco Quest!");
        System.out.print("Play again? (Y/N): ");
        String replay = scanner.nextLine().trim();
        if (replay.equalsIgnoreCase("Y")) {
            // Reset game state but keep high score
            score = 0;
            streak = 0;
            numTrash = 1;
            carryingTrash = false;
            carriedTrashItems.clear();
            carriedTrashTypes.clear();
            randomizePositions();
            play();  // Recursive restart
        }
    }

    public static void main(String[] args) {
        EcoQuest game = new EcoQuest();
        game.play();
    }

}

import java.util.Scanner;
import java.util.Arrays;

public class EcoQuestMain {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            clearScreen();
            displayMenu();
            String choice = scanner.nextLine().trim().toLowerCase();
            switch (choice) {
                case "1":
                case "play":
                    playGame();
                    break;
                case "2":
                case "about":
                    showAbout();
                    break;
                case "3":
                case "exit":
                    System.out.println("Thank you for playing Eco Quest! Goodbye.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                    break;
            }
        }
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void displayMenu() {
        System.out.println("=====================================");
        System.out.println("         WELCOME TO ECO QUEST!");
        System.out.println("=====================================");
        System.out.println("1. Play");
        System.out.println("2. About");
        System.out.println("3. Exit");
        System.out.print("Enter your choice (1-3 or type 'play', 'about', 'exit'): ");
    }

    private static void playGame() {
        EcoQuest game = new EcoQuest();
        game.play();
    }

    private static void showAbout() {
        clearScreen();
        // Display about text at the top
        System.out.println("=====================================");
        System.out.println("             ABOUT ECO QUEST");
        System.out.println("=====================================");
        System.out.println("Eco Quest is an educational game designed to teach players about proper waste management.");
        System.out.println("In the game, you control a player (P) on a grid to collect trash (T) and deposit it into the correct bin (B).");
        System.out.println("You must identify the type of trash: Nabubulok, Hindi Nabubulok, Recyclable, or Hazardous.");
        System.out.println("Correct identifications increase your score and streak. Wrong ones reset them!");
        System.out.println("The game features increasing difficulty with more trash as your streak grows.");
        System.out.println("High scores are saved for future sessions.");
        System.out.println("Developed as a fun way to learn about environmental responsibility.");
        System.out.println("=====================================");
        System.out.println("Press Enter to return to the menu.");

        // Anchor the animation just below the top block. We printed 11 header lines above,
        // so start the animation at row 12 (1-based terminal rows).
        int anchorRow = 12;
        AnimationThread animThread = new AnimationThread(anchorRow);
        animThread.start();

        scanner.nextLine();  // Wait for user to press Enter

        // Stop the animation thread and clear its area
        animThread.stopAnimation();
        try {
            animThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Clear the animation area after stopping
        clearAnimationArea(anchorRow, 12);
    }
    
    private static void clearAnimationArea(int startRow, int numRows) {
        for (int i = 0; i < numRows; i++) {
            System.out.print("\033[" + (startRow + i) + ";1H\033[K");
        }
        System.out.flush();
    }
    
    static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}

/* Animation thread that runs the trash thrower animation continuously in the bottom portion */
class AnimationThread extends Thread {
    private volatile boolean running = true;
    private static final int SCREEN_WIDTH = 70;
    private static final int SCREEN_HEIGHT = 10; // smaller screen for bottom animation

    private final int anchorRow;

    public AnimationThread(int anchorRow) {
        this.anchorRow = Math.max(1, anchorRow);
    }

    @Override
    public void run() {
        while (running) {
            try {
                playTrashThrowerAnimation();
            } catch (Exception e) {
                // silently continue
            }
        }
    }

    private void playTrashThrowerAnimation() throws Exception {
        Screen screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT);

        // Bin location (right side)
        int binX = SCREEN_WIDTH - 9;
        int binTop = SCREEN_HEIGHT - 5;

        // Character initial location (left)
        CharacterSprite hero = new CharacterSprite(4, SCREEN_HEIGHT - 4);

        boolean holdingTrash = true;

        // Walk to throwing position
        int throwX = binX - 8;
        while (hero.x < throwX && running) {
            hero.x++;
            renderToBottom(screen, hero, binX, binTop, holdingTrash);
            Thread.sleep(80);
        }

        // Throw with parabolic arc
        int startX = hero.x + 3;
        int startY = hero.y - 1;
        int landingX = binX + 3;
        int frames = 18;

        for (int f = 0; f <= frames && running; f++) {
            double t = (double) f / frames;
            double h = 6.0;
            double x = EcoQuestMain.lerp(startX, landingX, t);
            double y = startY - (h * 4 * t * (1 - t));

            renderToBottomWithTrash(screen, hero, binX, binTop, (int) Math.round(x), (int) Math.round(y));
            Thread.sleep(80);
        }

        // Celebration: show a short "Let's Go!" then "Yay!" sequence
        for (int i = 0; i < 4 && running; i++) {
            renderToBottomWithMessage(screen, hero, binX, binTop, "Let's Go!");
            Thread.sleep(350);
        }
    }

    private void renderToBottom(Screen screen, CharacterSprite hero, int binX, int binTop, boolean holdingTrash) {
        screen.clear();
        screen.drawBin(binX, binTop);
        screen.drawCharacter(hero, holdingTrash);
        String output = screen.getBuffer();
        // Clear from anchor row to end and print
        printAtAnchor(output);
    }

    private void renderToBottomWithTrash(Screen screen, CharacterSprite hero, int binX, int binTop, int trashX, int trashY) {
        screen.clear();
        screen.drawBin(binX, binTop);
        screen.drawCharacter(hero, false);
        screen.drawTrash(trashX, trashY);
        String output = screen.getBuffer();
        printAtAnchor(output);
    }

    private void renderToBottomWithMessage(Screen screen, CharacterSprite hero, int binX, int binTop, String message) {
        screen.clear();
        screen.drawBin(binX, binTop);
        screen.drawCharacter(hero, false);
        // draw trash in bin for celebration
        screen.drawTrash(binX + 3, binTop + 1);
        // Draw message above the character
        screen.drawText(hero.x - 1, hero.y - 4, message);
        String output = screen.getBuffer();
        printAtAnchor(output);
    }
    
    private void printAtAnchor(String content) {
        // Save cursor position
        System.out.print("\033[s");
        // Move to anchor row and clear from there to end of screen
        System.out.print("\033[" + anchorRow + ";1H\033[J");
        // Print the content
        System.out.print(content);
        // Restore cursor position
        System.out.print("\033[u");
        System.out.flush();
    }

    public void stopAnimation() {
        running = false;
    }
}

/* Helper classes for drawing to a text buffer and rendering to console.
   Simple approach: a 2D char buffer of size WIDTH x HEIGHT. */
class Screen {
    int w, h;
    char[][] buf;

    Screen(int w, int h) {
        this.w = w;
        this.h = h;
        buf = new char[h][w];
        clear();
    }

    void clear() {
        for (int r = 0; r < h; r++) {
            Arrays.fill(buf[r], ' ');
        }
        // draw ground line
        for (int c = 0; c < w; c++) {
            buf[h - 2][c] = '_';
        }
    }

    void drawCharacter(CharacterSprite c, boolean holdingTrash) {
        int x = c.x;
        int y = c.y;
        putString(x, y - 2, " o ");
        putString(x, y - 1, "/|\\");
        putString(x, y, "/ \\");
        if (holdingTrash) {
            put(x + 3, y - 1, 'T');
        }
    }

    void drawBin(int bx, int bt) {
        putString(bx, bt,  " ____ ");
        putString(bx, bt + 1,"|    |");
        putString(bx, bt + 2,"|____|");
        // Label the bin with a 'B' in the center
        put(bx + 2, bt + 1, 'B');
        for (int c = bx; c < bx + 6 && c < w; c++) {
            if (bt + 3 < h) buf[bt + 3][c] = '~';
        }
    }

    void drawTrash(int tx, int ty) {
        put(tx, ty, '*');
    }

    void drawText(int tx, int ty, String s) {
        putString(tx, ty, s);
    }

    void put(int x, int y, char ch) {
        if (x < 0 || x >= w || y < 0 || y >= h) return;
        buf[y][x] = ch;
    }

    void putString(int x, int y, String s) {
        if (y < 0 || y >= h) return;
        for (int i = 0; i < s.length(); i++) {
            int xx = x + i;
            if (xx >= 0 && xx < w) buf[y][xx] = s.charAt(i);
        }
    }

    String getBuffer() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < h; r++) {
            sb.append(buf[r]);
            sb.append('\n');
        }
        return sb.toString();
    }

    void render() {
        String clearSeq = "\033[H\033[2J";
        try {
            System.out.print(clearSeq);
        } catch (Exception e) {
            // ignore
        }
        System.out.print(getBuffer());
        System.out.flush();
    }
}

class CharacterSprite {
    int x;
    int y;
    CharacterSprite(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
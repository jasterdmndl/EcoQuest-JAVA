🌱 EcoQuest

EcoQuest is an educational console-based game designed to teach players about proper waste management. In the game, you control a player (P) on a 15×15 grid to collect trash (T) and deposit it into the correct bin (B). You must identify the type of trash: Nabubulok, Hindi Nabubulok, Recyclable, or Hazardous. Correct identifications increase your score and streak. Wrong ones reset them! The game features increasing difficulty with more trash as your streak grows. High scores are saved for future sessions. Developed as a fun way to learn about environmental responsibility.

✦ Features

❖ Interactive Gameplay – Explore the grid, pick up trash, and deposit it into the correct bin.
❖ Trash Identification Quiz – Identify trash types correctly to gain points.
❖ Score and Streak Tracking – Maintain score, streak, and increasing difficulty.
❖ High Score Persistence – Save your high score for future sessions.
❖ Simple Console Interface – Runs entirely in the terminal using Java.

✧ Gameplay Overview

☘ Move the player using WASD keys.
☘ Pick up trash (T) and carry it to the bin (B).
☘ Identify the type of trash when depositing it.
☘ Each correct answer increases your score and streak; wrong answers reset them.
☘ After a streak of 5 correct deposits, the number of trash items increases, making the game more challenging.

❂ Grid Legend
P = Player
B = Bin
T = Trash
. = Empty


➺ Project Structure
EcoQuest/
│
├─ EcoQuest.java          # Main game logic and user interface
└─ .ecoquest_highscore.txt  # Stores high score

✧ How to Run

Ensure Java 8+ is installed.

Compile the file:

javac EcoQuest.java


Run the program:

java EcoQuest

❖ Object-Oriented Principles

✦ Encapsulation – Player position, trash lists, and score are private fields, preventing direct modification.
✦ Abstraction – High score management and grid logic are separated from user interface methods.
✦ Modularity – Clear separation of methods for movement, trash handling, and grid rendering allows easier maintenance and extension.

☘ Example Output
=====================================
         WELCOME TO ECO QUEST!
=====================================
Pick up T(Trash), bring it to B(Bin), and identify its type!
Legend: P=Player, B=Bin, T=Trash, .=Empty
Score: 0 | High Score: 5 | Streak: 0 | Trash Count: 1
Use WASD to move. Q to quit.
+------------------------------+
|. . . . . . . . . . . . . . .|
|. . . . . . . . . . . . . . .|
|. . . . P . . . . . . . . . .|
|. . . . . T . . . . . . . . .|
|. . . . . . . . . B . . . . .|
|. . . . . . . . . . . . . . .|
+------------------------------+
You are carrying trash! Head to the bin.

✦ Requirements

❖ Java Development Kit (JDK) version 8 or above
❖ Terminal or command-line interface

✧ Contributing

❂ Contributions are welcome! You can:

✦ Submit pull requests
✦ Report issues
✦ Suggest new features or improvements

☘ Acknowledgements

EcoQuest was created to inspire environmental awareness through interactive learning. Special thanks to our instructor and classmates for guidance, support, and collaboration.

❂ Disclaimer

This project is provided for learning and demonstration purposes only.

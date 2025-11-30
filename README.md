<div align="center">

🌱 𝔼𝕔𝕠𝕼𝕦𝕖𝕤𝕥 🌱<br>

</div>

─────────────────────────────────────────────────────────────────────────────────────────

<div align="center">

𝐼𝒯–𝟚𝟙𝟘𝟠 <br>

𝔅𝔢𝔯𝔫𝔞𝔩, 𝒥𝒶𝓈𝓂𝒾𝓃 𝒟.<br>

𝒟𝒾𝓂𝒶𝒶𝓃𝒹𝒶𝓁, 𝒥𝒽𝑒𝓈𝓉𝑒𝓇 𝐿𝑒𝓂𝓊𝑒𝓁 𝒟.<br>

𝒫𝑒𝓇𝒸𝒾𝓃𝒸𝓊𝓁𝒶, 𝒫𝓇𝒾𝓃𝒸𝑒𝓈𝓈 𝐿𝒶𝓇𝒶 𝐵.<br>

𝑅𝑜𝒹𝓇𝒾𝑔𝓊𝑒𝓏, 𝒥𝒶𝒽𝓇𝑜𝓂𝑒

</div>

─────────────────────────────────────────────────────────────────────────────────────────

<p align="center">✦ 𝒪𝓋𝑒𝓇𝓋𝒾𝑒𝓌 ✦</p>

<p align="center">
EcoQuest is an educational console-based game designed to teach players about proper waste management. In the game, you control a player (P) on a 15×15 grid to collect trash (T) and deposit it into the correct bin (B). You must identify the type of trash: Nabubulok, Hindi Nabubulok, Recyclable, or Hazardous. Correct identifications increase your score and streak. Wrong ones reset them! High scores are saved for future sessions. Developed as a fun way to learn about environmental responsibility.
</p>

<div align="center">

─────────────────────────────────────────────────────────────────────────────────────────

✦ 𝐹𝑒𝒶𝓉𝓊𝓇𝑒𝓈 ✦

🎮 Interactive Gameplay – Explore the grid, pick up trash, and deposit it into the correct bin.  
🗑️ Trash Identification Quiz – Identify trash types correctly to gain points.  
⭐ Score & Streak Tracking – Keep track of score and streak; wrong answers reset them.  
🏆 High Score Persistence – Save your high score for future sessions.  
💻 Simple Console Interface – Runs entirely in the terminal using Java.

</div>

─────────────────────────────────────────────────────────────────────────────────────────
<div align="center">

✧ 𝐺𝒶𝓂𝑒𝓅𝓁𝒶𝓎 𝒪𝓋𝑒𝓇𝓋𝒾𝑒𝓌 ✧

🍀 Move the player using WASD keys.  

🧺 Pick up trash (T) and bring it to the bin (B).  

🔍 Identify the trash type correctly  

⭐ Correct answers boost your score & streak.  

💥 Streak of 5 = more trash appears!  

</div>


─────────────────────────────────────────────────────────────────────────────────────────
❂ 𝐺𝓇𝒾𝒹 𝐿𝑒𝑔𝑒𝓃𝒹

         P = Player
         B = Bin
         T = Trash
         . = Empty

─────────────────────────────────────────────────────────────────────────────────────────


➺ 𝒫𝓇𝑜𝒿𝑒𝒸𝓉 𝒮𝓉𝓇𝓊𝒸𝓉𝓊𝓇𝑒

         EcoQuest/
         │
         ├─ EcoQuest.java          # Main game logic and user interface
         └─ .ecoquest_highscore.txt  # Stores high score

─────────────────────────────────────────────────────────────────────────────────────────

✧ 𝐻𝑜𝓌 𝓉𝑜 𝑅𝓊𝓃

1. Ensure Java 8+ is installed.

2. Compile the file:

        javac EcoQuest.java

3. Run the program:

        java EcoQuest

─────────────────────────────────────────────────────────────────────────────────────────

❖ 𝒪𝒷𝒿𝑒𝒸𝓉-𝒪𝓇𝒾𝑒𝓃𝓉𝑒𝒹 𝒫𝓇𝒾𝓃𝒸𝒾𝓅𝓁𝑒

EcoQuest is designed using key Object-Oriented Programming principles to ensure clean structure, modularity, and maintainability. Below are the OOP concepts applied and how they are implemented in the project.

🔷 Abstraction

Abstraction means hiding complex logic and showing only essential features to the user or other parts of the program.

✔ How EcoQuest Uses Abstraction

Classes such as Screen, AnimationThread, CharacterSprite, and EcoQuest hide internal details like:

  - console rendering

  - animation logic

  - movement calculations

  - buffer operations

Example:

         screen.drawCharacter(hero, true);


🔶 Encapsulation

Encapsulation means bundling data and methods together in a class and protecting internal states.

✔ How EcoQuest Uses Encapsulation

Each class manages its own data:

  - CharacterSprite manages x and y position

  - Screen manages the rendering buffer

  - AnimationThread manages animation state

Example:

         public void stopAnimation() {
             running = false;
         }

🟥 Exception Handling

Exception handling prevents the game from crashing by managing unexpected errors.

✔ How EcoQuest Uses Exception Handling

Inside animations:

         try {
             playTrashThrowerAnimation();
         } catch (Exception e) {
             // Avoid crash during animation
         }

─────────────────────────────────────────────────────────────────────────────────────────

☘ 𝐸𝓍𝒶𝓂𝓅𝓁𝑒 𝒪𝓊𝓉𝓅𝓊𝓉

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

─────────────────────────────────────────────────────────────────────────────────────────

✦ 𝑅𝑒𝓆𝓊𝒾𝓇𝑒𝓂𝑒𝓃𝓉𝓈

--Java Development Kit (JDK) version 8+

--Terminal or command-line interface

─────────────────────────────────────────────────────────────────────────────────────────

☘ 𝐴𝒸𝓀𝓃𝑜𝓌𝓁𝑒𝒹𝑔𝑒𝓂𝑒𝓃𝓉𝓈

🌱EcoQuest inspires environmental awareness. Special thanks to our instructor Sir 𝓔mmanuel 𝓒harlie 𝓔nriquez.

─────────────────────────────────────────────────────────────────────────────────────────

❂ 𝐷𝒾𝓈𝒸𝓁𝒶𝒾𝓂𝑒𝓇

For learning and demonstration purposes only.

─────────────────────────────────────────────────────────────────────────────────────────

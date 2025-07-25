# fatal_fantasy_mco2

Fatal Fantasy: Tactics — CCPROG3 MCO2
Overview
Fatal Fantasy: Tactics is a Java-based, turn-based tactical RPG, developed for the culminating output of CCPROG3 at De La Salle University. The project is built in two phases:

MCO1: Core logic and CLI (text) interface.

MCO2: Major expansion to a Java GUI application (Swing/JavaFX), new features, and full MVC refactor.

Major Requirements (MCO2)
1. Core Functionalities
Character Creation

Race selection (Human, Elf, Dwarf, Gnome; each has unique bonuses)

Class selection (Mage, Warrior, Rogue; each with unique abilities)

Ability selection (up to 4 per character, some can be from other classes if gnome)

Character Management

Add, edit, view, and delete characters

Magic Item Inventory (unlimited size)

Equip/unequip a magic item (1 equipped at a time)

Magic Items System

Items are won every 3rd win (randomly selected from a loot table)

Single-use (activated in battle) or passive (auto-apply)

Turn-Based Battle

Strategic EP/HP management, using moves/abilities/magic items

Battle log for all game events

Persistence

All game state (players, characters, inventory, hall of fame) is saved/loaded from file

GUI

All features accessed via GUI, not CLI

MVC Architecture

Model, View, and Controller packages (strict separation)

Bonus Features (for maximum score)

Hall of Fame with persistent leaderboard

Player vs Bot (AI)

Auto Character Generator

Status Effects (e.g., stun, poison)

Leveling system (XP and rewards)

Magic Item Trading GUI

More classes/races

2. Implementation Structure
Java Only: No frameworks or third-party libraries (except Swing/JavaFX)

No global variables, no exit/goto, no break/continue outside switch

Code must be modular, OOP-driven, well-documented (Javadoc)

MVC file structure:

css
Copy
Edit
src/
  app/
    Main.java
  controller/
    GameManagerController.java
    SceneManager.java
    (etc)
  model/
    core/
    item/
    battle/
  view/
    mainmenu/
    character/
    battle/
    (etc)
  persistence/
    SaveLoadService.java
    GameData.java
  util/
    Constants.java
3. Documentation & Testing
All code must be fully documented (class/method Javadocs)

A test plan in table format is required (PDF)

Demo & defense required; code will be inspected for quality

4. Key Entities
Player: Holds characters and stats

Character: Race, class, abilities, inventory, equipped item, stats (HP/EP), win record

Ability: Name, description, effect, EP cost

MagicItem: Single-use or passive; different effects (see Appendix F)

Inventory: Holds magic items

HallOfFame: Stores player and character records

GameData: Root persistence class for all data

5. Grading/Bonus
Bonus Points: For extra features, robust version control (GitHub), meaningful GUI improvements, etc.

Code quality and maintainability are heavily weighted.

References (appendices from the PDF)
Appendix D: Class abilities/costs

Appendix E: Race options/bonuses

Appendix F: Magic item list and effects

Instructions for Codex
Understand the above architecture and requirements as immutable constraints.

All new code, improvements, and tests must strictly adhere to the MVC pattern, modular OOP, and the feature set.

DO NOT suggest or use CLI-based interfaces, external frameworks, or non-Java API code.

When editing or adding features, always check the persistence and integration with controllers and views.

For any GUI code, prefer clear separation between view and controller logic.


# Fatal Fantasy: Tactics (MCO2 Project)

## 🏰 Project Overview

Fatal Fantasy: Tactics is a turn-based tactical RPG being developed for CCPROG3 MCO2.  
- **MCO1:** Text-based CLI version (core logic complete)
- **MCO2:** Transition to Java GUI (JavaFX or Swing) with MVC architecture, inventory system, persistent save/load, more features.

## 🛠️ Architecture & File Structure

- **src/**
  - **app/**: Main.java
  - **controller/**: SceneManager, GameManagerController, HallOfFameController, etc.
  - **model/**
    - **core/**: Player, Character, etc.
    - **item/**: MagicItem, Inventory
    - **battle/**: Battle, CombatLog
  - **view/**: All GUI screens (MainMenuView, PlayerRegistrationView, etc.)
  - **persistence/**: SaveLoadService, GameData
  - **util/**: Constants, InputValidator, GameException

## 🧑‍💻 Roles

- **Member A:** All GUI/view classes, Main.java
- **Member B (me):** Core logic (model, controller, persistence, util), game logic, file save/load, AI, unit tests, Git management

## 🚦 Project Status & Main Workflows

- Refactored from CLI to package-based MVC
- Single JFrame (no duplicate windows!), navigation handled by `SceneManager`
- Data persistence via `SaveLoadService`/`GameData`
- Input validation handled via `InputValidator` (ensures non-empty, unique player names, etc.)
- Current issue: [Describe any current bug here, e.g. duplicate windows, registration logic, etc.]

## ✅ Key Requirements & Specs

- **GUI only** (no CLI): JavaFX or Swing
- **MVC** enforced
- **Player Registration:** Two unique names required, error handling for empty/duplicate names, confirmation shown
- **Persistence:** Players and Hall of Fame saved to file, reloaded at startup
- **Magic Item Inventory:** One active item per character, inventory managed per player
- **Extensible:** Leveling system, status effects, AI, Hall of Fame screen
- **No forbidden Java patterns:** No global vars, no `System.exit()`, etc.

## 💾 Saving & Loading

- All players stored in `GameData` (see `persistence/GameData.java`)
- Use `SaveLoadService.saveGame()` and `SaveLoadService.loadGame()` for file operations
- Hall of Fame via `saveHallOfFame()` and `loadHallOfFame()`

## ⚡ Codex Usage

- **How to get started:**  
  1. Clone this repo or connect your GitHub to Codex
  2. Create a new Codex environment with this repo
  3. Use the "Describe a task" box to ask for:  
      - Bug fixes (e.g., "Fix duplicate windows on register")  
      - New features (e.g., "Implement inventory trading GUI")  
      - Refactors (e.g., "Refactor registration logic to prevent blank player names")  
      - Unit test generation (e.g., "Generate JUnit tests for Player class")
  4. Codex only "sees" the code and documentation—**so keep this README up to date with all context and rules**

- **Tips:**  
  - Write *clear*, *complete* prompts (e.g., "Enforce unique player names on registration and show an error dialog if duplicate")
  - When possible, reference file/class names
  - Keep all game rules and architectural constraints documented here for Codex and all collaborators

## 📝 Notes / Context (From ChatGPT tutoring)

- Member B is responsible for all code in model, controller, persistence, util
- We have discussed:  
  - MVC patterns  
  - Avoiding duplicate JFrames  
  - Proper error handling  
  - File-based persistence  
  - All class relationships must be OOP-correct (no anti-patterns)
- **For a full project summary, review ChatGPT conversation transcript!**

## 🗂️ Future To-Do

- [ ] Fix any outstanding bugs (see Issues or notes above)
- [ ] Implement remaining bonus features (leveling, auto character generator, etc.)
- [ ] Write/Update unit tests for new logic

---

> This README is designed to help Codex, AI agents, and all future developers understand every detail and expectation for this project!

---

**You can add, edit, or expand as you wish—this is just a complete starting point!**  
If you want, I can auto-generate an even more detailed section for a specific feature or bug, just tell me what you want Codex to know!

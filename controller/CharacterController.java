package controller;

import model.core.*;
import model.core.Character;
import model.service.ClassService;
import model.service.RaceService;
import model.util.GameException;
import model.util.InputValidator;
import view.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing all character-related actions
 * for a single Player instance in Fatal Fantasy: Tactics.
 */
public final class CharacterController {

    private final Player player;
    private final CharacterManagementView managementView;
    private final RaceService raceService;
    private final ClassService classService;

    /**
     * Constructs a controller for managing the given player's characters.
     *
     * @param player         the player whose characters are being managed
     * @param managementView the view used to display character information
     * @throws GameException if any parameter is {@code null}
     */
    public CharacterController(Player player, CharacterManagementView managementView) throws GameException {
        InputValidator.requireNonNull(player, "player");
        InputValidator.requireNonNull(managementView, "managementView");

        this.player = player;
        this.managementView = managementView;
        this.raceService = RaceService.INSTANCE;
        this.classService = ClassService.INSTANCE;

        bindCharacterManagementView();
    }

    // ------------------ CharacterManagementView Integration -----------------------

   private void bindCharacterManagementView() {
        managementView.addManagePlayer1Listener(_ -> openCharacterListView());
        managementView.addManagePlayer2Listener(_ -> openCharacterListView());
        managementView.addReturnToMenuListener(_ -> managementView.dispose());

        updateManagementViewCharacterList();
    }

    private void openCharacterListView() {
        CharacterListViewingView listView = new CharacterListViewingView(1);
        bindCharacterListViewingView(listView);
    }

    private void updateManagementViewCharacterList() {
        List<String> summaries = player.getCharacters().stream()
            .map(ch -> String.format("%s [%s | %s]", ch.getName(), ch.getRaceType(), ch.getClassType()))
            .collect(Collectors.toList());

        managementView.displayCharacterList(summaries);
    }

    // ------------------ CharacterManualCreationView Integration -----------------------

    /**
     * Wires a manual creation view to this controller and populates
     * its dropdown options.
     *
     * @param creationView the manual creation view to bind
     */
    public void bindCharacterManualCreationView(CharacterManualCreationView creationView) {
        creationView.setRaceOptions(
            getAvailableRaces().stream().map(Enum::name).toArray(String[]::new)
        );

        creationView.setClassOptions(
            ClassType.valuesAsStringArray()
        );

        for (int slot = 1; slot <= 3; slot++) {
            creationView.setAbilityOptions(slot, new String[] {"Select Class First"});
        }

        creationView.addCreateCharacterListener(_ -> handleCreateButton(creationView));
        creationView.addReturnListener(_ -> creationView.dispose());
        creationView.addClassDropdownListener(_ -> {
    String selectedClass = creationView.getSelectedClass();
    if (selectedClass != null && !selectedClass.isEmpty()) {
        ClassType classType = ClassType.valueOf(selectedClass);
        List<String> abilities = getAvailableAbilities(classType)
            .stream().map(Ability::getName).toList();
        for (int slot = 1; slot <= 3; slot++) {
            creationView.setAbilityOptions(slot, abilities.toArray(new String[0]));
        }
    }
});

        creationView.getClassDropdown().addActionListener(_ -> {
            String selectedClass = creationView.getSelectedClass();
            if (selectedClass != null && !selectedClass.isEmpty()) {
                ClassType classType = ClassType.valueOf(selectedClass);
                List<String> abilities = getAvailableAbilities(classType)
                    .stream().map(Ability::getName).toList();
                for (int slot = 1; slot <= 3; slot++) {
                    creationView.setAbilityOptions(slot, abilities.toArray(new String[0]));
                }
            }
        });
    }

    private void handleCreateButton(CharacterManualCreationView creationView) {
        String name = creationView.getCharacterName();
        String raceStr = creationView.getSelectedRace();
        String classStr = creationView.getSelectedClass();
        String[] abilitiesSelected = creationView.getSelectedAbilities();

        if (creationView.confirmCharacterCreation(name)) {
            try {
                RaceType race = RaceType.valueOf(raceStr);
                ClassType classType = ClassType.valueOf(classStr);
                List<Ability> abilities = classService.getAbilitiesByNames(abilitiesSelected);

                handleCreateCharacterRequest(name, race, classType, abilities);

                creationView.resetFields();
                creationView.showInfoMessage("Character \"" + name + "\" created successfully!");

            } catch (Exception e) {
                creationView.showErrorMessage("Invalid input or missing selection.");
            }
        }
    }

    // ------------------ Character Creation Logic -----------------------

    /**
     * Creates a new character for the player using the supplied attributes and
     * updates the management view.
     *
     * @param name       the character's name
     * @param race       chosen race
     * @param classType  chosen class
     * @param abilities  list of starting abilities
     */
    public void handleCreateCharacterRequest(String name, RaceType race, ClassType classType, List<Ability> abilities) {
        try {
            InputValidator.requireNonBlank(name, "Character name");
            InputValidator.requireNonNull(race, "race");
            InputValidator.requireNonNull(classType, "classType");
            InputValidator.requireNonNull(abilities, "abilities");

            Character character = new Character(name, race, classType);
            character.setAbilities(abilities);

            player.addCharacter(character);

            updateManagementViewCharacterList();
            managementView.showInfoMessage("Character \"" + name + "\" created successfully.");

        } catch (GameException e) {
            managementView.showErrorMessage(e.getMessage());
        }
    }

    // ------------------ Shared Accessors -----------------------

    /**
     * Retrieves all races available for character creation.
     *
     * @return list of selectable races
     */
    public List<RaceType> getAvailableRaces() {
        return raceService.getAvailableRaces();
    }

    /**
     * Returns the abilities allowed for the given class type.
     *
     * @param classType the chosen class
     * @return list of abilities available to that class
     */
    public List<Ability> getAvailableAbilities(ClassType classType) {
        InputValidator.requireNonNull(classType, "classType");
        return classService.getAvailableAbilities(classType);
    }

    // ------------------ CharacterListViewingView Integration -----------------------

    /**
     * Binds the character list viewing screen and sets up its actions.
     *
     * @param listView the list view to bind
     */
    public void bindCharacterListViewingView(CharacterListViewingView listView) {
        listView.setActionListener(e -> {
            String command = e.getActionCommand();
            if (CharacterListViewingView.VIEW_CHAR.equals(command)) {
                refreshCharacterListViewingView(listView);
            } else if (CharacterListViewingView.RETURN.equals(command)) {
                listView.dispose();
            }
        });

        refreshCharacterListViewingView(listView);
    }

    private void refreshCharacterListViewingView(CharacterListViewingView listView) {
        List<Character> characters = player.getCharacters();
        String details = characters.isEmpty()
            ? "No characters available."
            : characters.stream().map(Character::toString).collect(Collectors.joining("\n\n"));

        listView.updateCharacterList(details);
    }

    // ------------------ CharacterSpecViewingView Integration -----------------------

    /**
     * Binds the character specification viewing screen, enabling selection and
     * detail display.
     *
     * @param specView the specification view to bind
     */
    public void bindCharacterSpecViewingView(CharacterSpecViewingView specView) {
        specView.setCharacterOptions(
            player.getCharacters().stream()
                  .map(Character::getName)
                  .toArray(String[]::new)
        );

        specView.setActionListener(e -> {
            String command = e.getActionCommand();

            if (CharacterSpecViewingView.RETURN.equals(command)) {
                specView.dispose();
            } else {
                String selectedName = specView.getSelectedCharacter();
                String details;

                if (selectedName == null) {
                    details = "No character selected.";
                } else {
                    Character selectedChar = player.getCharacter(selectedName).orElse(null);
                    details = (selectedChar != null)
                        ? selectedChar.toString()
                        : "Character not found.";
                }

                specView.updateCharacterDetails(details);
            }
        });

        specView.resetView();
    }
}

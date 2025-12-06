package com.example.electionsinformationsystem.controllers;

import com.example.electionsinformationsystem.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.Objects;

public class HelloController {

    // linked lists for iteration, display, etc.
    private final LinkedList<Politician> politicianLinkedList = new LinkedList<>();
    private final LinkedList<Election> electionLinkedList = new LinkedList<>();

    // hash tables for fast lookups
    private final HashTableSC<String, Politician> nameHashTable = new HashTableSC<>(20);
    private final HashTableSC<String, LinkedList<Politician>> partyHashTable = new HashTableSC<>(20);
    private final HashTableSC<String, LinkedList<Politician>> countyHashTable = new HashTableSC<>(20);
    private final HashTableSC<String, LinkedList<Election>> electionTypeHashTable = new HashTableSC<>(20);
    private final HashTableSC<String, LinkedList<Election>> electionDateHashTable = new HashTableSC<>(20);

    // politician tab
    @FXML private ListView<Politician> politicianListView;
    @FXML private TextField politicianSearchResult;

    @FXML private TableView<Politician> politicianTableView;
    @FXML private TableColumn<Politician, String> nameColumn;
    @FXML private TableColumn<Politician, String> dobColumn;
    @FXML private TableColumn<Politician, String> partyColumn;
    @FXML private TableColumn<Politician, String> countyColumn;
    @FXML private TableColumn<Politician, String> photoColumn;

    @FXML private TableView<Election> politicianElectionTableView;

    @FXML private ComboBox<String> themePicker;

    // election tab
    @FXML public ListView<Election> electionListView;
    @FXML public TextField electionSearchResult;
    @FXML public ListView<Candidate> candidateListView;



    @FXML
    public void initialize() {
        // set up table columns to pull their values from Politician fields
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("politicianName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        partyColumn.setCellValueFactory(new PropertyValueFactory<>("politicalParty"));
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("homeCounty"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));

        politicianTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        politicianElectionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);


        politicianListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Politician item, boolean empty) {
                super.updateItem(item, empty);

                setText((empty || item == null) ? null : item.getPoliticianName());
            }

        });

        // listener to update TableView when a Politician is selected
        politicianListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
            politicianTableView.getItems().clear();
            if (newVal != null) {
                politicianTableView.getItems().add(newVal);
            }
        });

        themePicker.getItems().addAll("Light Mode", "Dark Mode", "Cotton Candy");
        themePicker.getSelectionModel().selectFirst();
    }

    @FXML
    public void onChangeTheme() {
        Scene scene = themePicker.getScene();
        if (scene == null) return;

        applyStylesheet(scene.getRoot());
    }

    // TODO NEED TO ADD FILTERING AND SORTING FOR SEARCH IDK

    // politician buttons

    @FXML
    public void onSearchPolitician() {
        String query = politicianSearchResult.getText().trim().toLowerCase();

        // clear old list view
        politicianListView.getItems().clear();


        // if search bar is empty, add all back to list
        if (query.isEmpty()) {
            for (Politician p : politicianLinkedList) {
                politicianListView.getItems().add(p);
            }
            return;
        }

        // exact search : hash lookup

        // name
        Politician exactMatchName = nameHashTable.get(query.toLowerCase());
        if (exactMatchName != null) {
            politicianListView.getItems().add(exactMatchName);
            return;
        }

        // party
        LinkedList<Politician> partyMatchList = partyHashTable.get(query);
        if (partyMatchList != null) {
            for (Politician exactMatchParty : partyMatchList) {
                politicianListView.getItems().add(exactMatchParty);
            }
            return;
        }

        // county
        LinkedList<Politician> countyMatchList = countyHashTable.get(query);
        if (countyMatchList != null) {
            for (Politician exactMatchCounty : countyMatchList) {
                politicianListView.getItems().add(exactMatchCounty);
            }
            return;
        }


        // partial search : iterate through linked list
        LinkedList<Politician> matches = new LinkedList<>();

        for (Politician partialMatch : politicianLinkedList) {
            boolean matchesName = partialMatch.getPoliticianName().toLowerCase().contains(query);
            boolean matchesParty = partialMatch.getPoliticalParty().toLowerCase().contains(query);
            boolean matchesCounty = partialMatch.getHomeCounty().toLowerCase().contains(query);

            if (matchesName || matchesParty || matchesCounty) {
                matches.add(partialMatch);
            }
        }

        for (Politician politician : matches) {
            politicianListView.getItems().add(politician);
        }
    }

    @FXML
    public void onAddPolitician() {
        Politician newPolitician = politicianDialog(null);
        if (newPolitician == null) return;

        // name hash table, no duplicated names
        nameHashTable.put(newPolitician.getPoliticianName().toLowerCase(), newPolitician);

        // party hash table
        LinkedList<Politician> partyList = partyHashTable.get(newPolitician.getPoliticalParty().toLowerCase());
        if (partyList == null) {
            partyList = new LinkedList<>();
            partyHashTable.put(newPolitician.getPoliticalParty().toLowerCase(), partyList);
        }
        partyList.add(newPolitician);

        // home county hash table
        LinkedList<Politician> countyList = countyHashTable.get(newPolitician.getHomeCounty().toLowerCase());
        if (countyList == null) {
            countyList = new LinkedList<>();
            countyHashTable.put(newPolitician.getHomeCounty().toLowerCase(), countyList);
        }
        countyList.add(newPolitician);


        politicianLinkedList.add(newPolitician);
        politicianListView.getItems().add(newPolitician);

        System.out.println("Number of politicians: " + politicianLinkedList.size());
        System.out.println(politicianLinkedList.display());
        //nameHashTable.printTable();
        //countyHashTable.printTable();
    }

    @FXML
    public void onEditPolitician() {
        Politician selected = politicianListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String oldName = selected.getPoliticianName().toLowerCase();
        String oldParty = selected.getPoliticalParty().toLowerCase();
        String oldCounty = selected.getHomeCounty().toLowerCase();

        Politician edited = politicianDialog(selected);
        if (edited == null) return;

        if (!oldName.equals(edited.getPoliticianName().toLowerCase())) {
            nameHashTable.remove(oldName);
            nameHashTable.put(edited.getPoliticianName().toLowerCase(), edited);
        }

        if (!oldParty.equals(edited.getPoliticalParty().toLowerCase())) {

            // remove from old list
            LinkedList<Politician> oldPartyList = partyHashTable.get(oldParty);
            oldPartyList.remove(selected);

            LinkedList<Politician> newPartyList = partyHashTable.get(edited.getPoliticalParty().toLowerCase());

            if (newPartyList == null) {
                newPartyList = new LinkedList<>();
                partyHashTable.put(edited.getPoliticalParty().toLowerCase(), newPartyList);
            }
            newPartyList.add(edited);
        }

        if (!oldCounty.equals(edited.getHomeCounty().toLowerCase())) {
            LinkedList<Politician> oldCountyList = countyHashTable.get(oldCounty);
            oldCountyList.remove(selected);

            LinkedList<Politician> newCountyList = countyHashTable.get(edited.getHomeCounty().toLowerCase());

            if (newCountyList == null) {
                newCountyList = new LinkedList<>();
                countyHashTable.put(edited.getHomeCounty().toLowerCase(), newCountyList);
            }
            newCountyList.add(edited);
        }

        politicianListView.refresh();

        System.out.println("Number of politicians: " + politicianLinkedList.size());
        System.out.println(politicianLinkedList.display());
    }

    @FXML
    public void onRemovePolitician() {
        Politician selected = politicianListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        nameHashTable.remove(selected.getPoliticianName().toLowerCase());

        LinkedList<Politician> partyList = partyHashTable.get(selected.getPoliticalParty().toLowerCase());
        if (partyList != null) partyList.remove(selected);

        LinkedList<Politician> countyList = countyHashTable.get(selected.getHomeCounty().toLowerCase());
        if (countyList != null) countyList.remove(selected);

        politicianLinkedList.remove(selected);
        politicianListView.getItems().remove(selected);

        System.out.println("Number of politicians: " + politicianLinkedList.size());
        System.out.println(politicianLinkedList.display());
    }

    // election buttons

    @FXML
    public void onSearchElection() {
    }

    @FXML
    public void onAddElection() {
        Election newElection = electionDialog(null);
        if (newElection == null) return;

        LinkedList<Election> electionTypeList = electionTypeHashTable.get(newElection.getElectionType());
        if (electionTypeList == null) {
            electionTypeList = new LinkedList<>();
            electionTypeHashTable.put(newElection.getElectionType(), electionTypeList);
        }
        electionTypeList.add(newElection);

        LinkedList<Election> electionDateList = electionDateHashTable.get(newElection.getElectionDate());
        if (electionDateList == null) {
            electionDateList = new LinkedList<>();
            electionDateHashTable.put(newElection.getElectionDate(), electionDateList);
        }
        electionDateList.add(newElection);

        electionLinkedList.add(newElection);
        electionListView.getItems().add(newElection);

        System.out.println("Number of elections: " + electionLinkedList.size());
        System.out.println(electionLinkedList.display());
        //System.out.println(newElection.toString());
        //System.out.println("Election Type Hash Table");
        //electionTypeHashTable.printTable();
        //System.out.println("Election Date Hash Table");
        //electionDateHashTable.printTable();
    }

    @FXML
    public void onEditElection() {
        Election selected = electionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String oldElectionType = selected.getElectionType();
        String oldElectionDate = selected.getElectionDate();

        Election edited = electionDialog(selected);
        if (edited == null) return;

        if (!oldElectionType.equals(edited.getElectionType())) {

            // remove from old list
            LinkedList<Election> oldElectionTypeList = electionTypeHashTable.get(oldElectionType);
            oldElectionTypeList.remove(selected);

            LinkedList<Election> newElectionTypeList = electionTypeHashTable.get(edited.getElectionType());

            if (newElectionTypeList == null) {
                newElectionTypeList = new LinkedList<>();
                electionTypeHashTable.put(edited.getElectionType(), newElectionTypeList);
            }
            newElectionTypeList.add(edited);
        }

        if (!oldElectionDate.equals(edited.getElectionDate())) {
            LinkedList<Election> oldElectionDateList = electionDateHashTable.get(oldElectionDate);
            oldElectionDateList.remove(selected);

            LinkedList<Election> newElectionDateList = electionDateHashTable.get(edited.getElectionDate());

            if (newElectionDateList == null) {
                newElectionDateList = new LinkedList<>();
                electionDateHashTable.put(edited.getElectionDate(), newElectionDateList);
            }
            newElectionDateList.add(edited);
        }

        electionListView.refresh();

        System.out.println("Number of elections: " + electionLinkedList.size());
        System.out.println(electionLinkedList.display());
    }

    @FXML
    public void onRemoveElection() {
        Election selected = electionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        LinkedList<Election> electionTypeList = electionTypeHashTable.get(selected.getElectionType().toLowerCase());
        if (electionTypeList != null) electionTypeList.remove(selected);

        LinkedList<Election> electionDateList = electionDateHashTable.get(selected.getElectionDate());
        if (electionDateList != null) electionTypeList.remove(selected);

        electionLinkedList.remove(selected);
        electionListView.getItems().remove(selected);

        System.out.println("Number of elections: " + electionLinkedList.size());
        System.out.println(electionLinkedList.display());
    }

    // candidate buttons

    @FXML
    public void onAddCandidate() {
    }

    @FXML
    public void onEditCandidate() {
    }

    @FXML
    public void onRemoveCandidate() {
    }

    // HELPER METHODS

    @FXML
    private void applyStylesheet(Parent root) {
        if (root == null) return;

        root.getStylesheets().clear();

        String selectedTheme = themePicker.getValue();
        if (selectedTheme == null) return;

        String cssFile = switch (selectedTheme) {
            case "Dark Mode" -> "/com/example/electionsinformationsystem/darkStyle.css";
            case "Cotton Candy" -> "/com/example/electionsinformationsystem/pinkStyle.css";
            default -> "/com/example/electionsinformationsystem/lightStyle.css";
        };

        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssFile)).toExternalForm());
    }

    @FXML
    private Politician politicianDialog(Politician existing) {

        // prefill values if editing
        String prefillName = existing != null ? existing.getPoliticianName() : "";
        String prefillDob = existing != null ? existing.getDateOfBirth() : "";
        String prefillPoliticalParty = existing != null ? existing.getPoliticalParty() : "";
        String prefillCounty = existing != null ? existing.getHomeCounty() : "";
        String prefillPhotoUrl = existing != null ? existing.getPhotoUrl() : "";

        String dialogTitle = (existing != null) ? "Edit Politician" : "Add Politician";

        // name dialog
        TextInputDialog nameDialog = new TextInputDialog(prefillName);
        nameDialog.setTitle(dialogTitle);
        nameDialog.setHeaderText("Enter Politician Name");
        nameDialog.setGraphic(null);
        applyStylesheet(nameDialog.getDialogPane());

        String politicianName = nameDialog.showAndWait().orElse("").trim();
        if (politicianName.isEmpty()) return null;

        // date of birth
        String dateOfBirth = calenderDialog(prefillDob, dialogTitle, "Enter Date of Birth:");
        if (dateOfBirth == null || dateOfBirth.isEmpty()) return null;

        // political party dialog
        TextInputDialog politicalPartyDialog = new TextInputDialog(prefillPoliticalParty);
        politicalPartyDialog.setTitle(dialogTitle);
        politicalPartyDialog.setHeaderText("Enter Political Party");
        politicalPartyDialog.setGraphic(null);
        applyStylesheet(politicalPartyDialog.getDialogPane());

        String politicalParty = politicalPartyDialog.showAndWait().orElse("").trim();
        if (politicalParty.isEmpty()) return null;

        // home county dialog
        String homeCounty = countyDialog(prefillCounty, dialogTitle);
        if (homeCounty == null || homeCounty.isEmpty()) return null;

        // photoUrl dialog
        TextInputDialog photoUrlDialog = new TextInputDialog(prefillPhotoUrl);
        photoUrlDialog.setTitle(dialogTitle);
        photoUrlDialog.setHeaderText("Enter Photo URL");
        photoUrlDialog.setGraphic(null);
        applyStylesheet(photoUrlDialog.getDialogPane());

        String photoUrl = photoUrlDialog.showAndWait().orElse("").trim();
        if (photoUrl.isEmpty()) return null;

        // create new
        if (existing == null) return new Politician(politicianName, dateOfBirth, politicalParty, homeCounty, photoUrl);

        // else update existing
        existing.setPoliticianName(politicianName);
        existing.setDateOfBirth(dateOfBirth);
        existing.setPoliticalParty(politicalParty);
        existing.setHomeCounty(homeCounty);
        existing.setPhotoUrl(photoUrl);

        return existing;
    }

    @FXML
    private Election electionDialog(Election existing) {
        // prefill values if editing
        String prefillElectionType = existing != null ? existing.getElectionType() : "";
        String prefillLocation = existing != null ? existing.getLocation() : "";
        String prefillElectionDate = existing != null ? existing.getElectionDate() : "";
        int prefillNumWinners = existing != null ? existing.getNumWinners() : 0;

        String dialogTitle = (existing != null) ? "Edit Election" : "Add Election";

        // election type
        String electionType = electionTypeDialog(prefillElectionType, dialogTitle);
        if (electionType == null || electionType.isEmpty()) return null;

        // election location
        String location = locationDialog(prefillLocation, dialogTitle, electionType);
        if (location == null || location.isEmpty()) return null;

        // election date
        String electionDate = calenderDialog(prefillElectionDate, dialogTitle, "Select Election Date:");
        if (electionDate == null || electionDate.isEmpty()) return null;

        // num winners
        int numWinners;
        if (electionType.equalsIgnoreCase("Presidential")) {
            numWinners = 1;
        } else {
            Integer userInput = numberDialog(
                    prefillNumWinners > 0 ? prefillNumWinners : 1, dialogTitle, "Enter Number of Winners:"
            );
            if (userInput == null) return null;
            numWinners = userInput;
        }
        if (existing == null) return new Election(electionType, location, electionDate, numWinners);

        existing.setElectionType(electionType);
        existing.setLocation(location);
        existing.setElectionDate(electionDate);
        existing.setNumWinners(numWinners);

        return existing;
    }

    @FXML
    private String calenderDialog(String prefillDob, String dialogTitle, String dialogHeader) {
        Dialog<String> calenderDialog = new Dialog<>();
        calenderDialog.setTitle(dialogTitle);
        calenderDialog.setHeaderText(dialogHeader);
        calenderDialog.setGraphic(null);
        applyStylesheet(calenderDialog.getDialogPane());

        DatePicker datePicker = new DatePicker();
        datePicker.setShowWeekNumbers(false);

        // prefill if editing
        if (prefillDob != null && !prefillDob.isEmpty()) datePicker.setValue(java.time.LocalDate.parse(prefillDob));

        calenderDialog.getDialogPane().setContent(datePicker);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        calenderDialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        calenderDialog.setResultConverter(button -> button == ok ? datePicker.getValue().toString() : "");

        return calenderDialog.showAndWait().orElse(null);
    }

    @FXML
    private String countyDialog(String prefillCounty, String dialogTitle) {
        Dialog<String> countyDialog = new Dialog<>();
        countyDialog.setTitle(dialogTitle);
        countyDialog.setHeaderText("Select County");
        countyDialog.setGraphic(null);
        applyStylesheet(countyDialog.getDialogPane());

        ComboBox<String> countyCombo = new ComboBox<>();
        countyCombo.getItems().addAll(
                "Antrim", "Armagh", "Carlow", "Cavan", "Clare", "Cork", "Derry",
                "Donegal", "Down", "Dublin", "Fermanagh", "Galway", "Kerry",
                "Kildare", "Kilkenny", "Laois", "Leitrim", "Limerick",
                "Longford", "Louth", "Mayo", "Meath", "Monaghan",
                "Offaly", "Roscommon", "Sligo", "Tipperary", "Tyrone",
                "Waterford", "Westmeath", "Wexford", "Wicklow"
        );


        // prefill if editing
        if (prefillCounty != null && !prefillCounty.isEmpty()) {
            countyCombo.setValue(prefillCounty);
        }

        countyDialog.getDialogPane().setContent(countyCombo);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        countyDialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        countyDialog.setResultConverter(button -> button == ok ? countyCombo.getValue() : null);

        return countyDialog.showAndWait().orElse(null);
    }

    @FXML
    private String generalDialog(String prefill) {
        Dialog<String> generalDialog = new Dialog<>();
        generalDialog.setTitle("Select Constituency");
        generalDialog.setHeaderText("Select General Election Constituency");
        generalDialog.setGraphic(null);
        applyStylesheet(generalDialog.getDialogPane());

        ComboBox<String> constituencyCombo = new ComboBox<>();
        constituencyCombo.getItems().addAll(
                "Carlow–Kilkenny", "Cavan–Monaghan", "Clare", "Cork East",
                "Cork North-Central", "Cork North-West", "Cork South-Central",
                "Cork South-West", "Donegal", "Dublin Bay North",
                "Dublin Bay South", "Dublin Central", "Dublin Fingal East", "Dublin Fingal West", "Dublin Mid-West",
                "Dublin North-West", "Dublin Rathdown", "Dublin South-Central", "Dublin South-West",
                "Dublin West", "Dún Laoghaire", "Galway East", "Galway West", "Kerry", "Kildare North",
                "Kildare South", "Laois", "Limerick City", "Limerick County", "Longford–Westmeath",
                "Louth", "Mayo", "Meath East", "Meath West", "Offaly", "Roscommon–Galway",
                "Sligo–Leitrim", "Tipperary", "Waterford", "Wexford", "Wicklow"
        );

        if (prefill != null && !prefill.isEmpty()) {
            constituencyCombo.setValue(prefill);
        }

        generalDialog.getDialogPane().setContent(constituencyCombo);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        generalDialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        generalDialog.setResultConverter(button -> button == ok ? constituencyCombo.getValue() : "");

        return generalDialog.showAndWait().orElse(null);
    }

    @FXML
    private String europeanDialog(String prefill) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Select European Constituency");
        dialog.setHeaderText("Select European Constituency");
        dialog.setGraphic(null);
        applyStylesheet(dialog.getDialogPane());

        ComboBox<String> euroCombo = new ComboBox<>();
        euroCombo.getItems().addAll("Dublin", "South", "Midlands–North-West");

        if (prefill != null && !prefill.isEmpty()) {
            euroCombo.setValue(prefill);
        }

        dialog.getDialogPane().setContent(euroCombo);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        dialog.setResultConverter(button -> button == ok ? euroCombo.getValue() : null);

        return dialog.showAndWait().orElse(null);
    }

    @FXML
    private String electionTypeDialog(String prefillElectionType, String dialogTitle) {
        Dialog<String> electionTypeDialog = new Dialog<>();
        electionTypeDialog.setTitle(dialogTitle);
        electionTypeDialog.setHeaderText("Select Election Type");
        electionTypeDialog.setGraphic(null);
        applyStylesheet(electionTypeDialog.getDialogPane());

        ComboBox<String> electionTypeCombo = new ComboBox<>();
        electionTypeCombo.getItems().addAll("Local", "General", "European", "Presidential");
        electionTypeCombo.setEditable(false);

        if (prefillElectionType != null && !prefillElectionType.isEmpty()) {
            electionTypeCombo.setValue(prefillElectionType);
        }

        electionTypeDialog.getDialogPane().setContent(electionTypeCombo);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        electionTypeDialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        electionTypeDialog.setResultConverter(button -> button == ok ? electionTypeCombo.getValue() : null);

        return electionTypeDialog.showAndWait().orElse(null);
    }

    @FXML
    private String locationDialog(String prefillLocation, String dialogTitle, String electionType) {
        Dialog<String> locationDialog = new Dialog<>();
        locationDialog.setTitle(dialogTitle);
        locationDialog.setHeaderText("Select Election Location");
        locationDialog.setGraphic(null);
        applyStylesheet(locationDialog.getDialogPane());

        return switch (electionType) {
            case "Local" -> countyDialog(prefillLocation, "Select County");
            case "General" -> generalDialog(prefillLocation);
            case "European" -> europeanDialog(prefillLocation);
            case "Presidential" -> "Nationwide";
            default -> "";
        };

    }

    @FXML
    private Integer numberDialog(Integer prefill, String dialogTitle, String headerText) {

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText(headerText);
        applyStylesheet(dialog.getDialogPane());

        Spinner<Integer> spinner = new Spinner<>(1, 10, prefill != null ? prefill : 1);
        spinner.setEditable(false);

        dialog.getDialogPane().setContent(spinner);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        dialog.setResultConverter(button -> button == ok ? spinner.getValue() : null);

        return dialog.showAndWait().orElse(null);
    }
}

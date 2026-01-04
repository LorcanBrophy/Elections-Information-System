package com.example.electionsinformationsystem.controllers;

import com.example.electionsinformationsystem.models.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.Objects;

public class Controller {

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
    @FXML
    private TextField politicianSearchResult;
    @FXML
    private ListView<Politician> politicianListView;

    @FXML
    private TableView<Politician> politicianTableView;
    @FXML
    private TableColumn<Politician, String> nameColumn;
    @FXML
    private TableColumn<Politician, String> dobColumn;
    @FXML
    private TableColumn<Politician, String> partyColumn;
    @FXML
    private TableColumn<Politician, String> countyColumn;
    @FXML
    private TableColumn<Politician, String> photoColumn;

    @FXML
    private TableView<Election> politicianElectionTableView;
    @FXML
    private TableColumn<Election, String> electionTypeColumn;
    @FXML
    private TableColumn<Election, String> electionLocationColumn;
    @FXML
    private TableColumn<Election, String> electionDateColumn;
    @FXML
    private TableColumn<Election, Integer> electionNumWinnersColumn;

    @FXML
    private ComboBox<String> themePicker;

    // election tab
    @FXML
    private ListView<Election> electionListView;
    @FXML
    private TextField electionSearchResult;
    @FXML
    private ListView<Candidate> candidateListView;

    @FXML
    private TableView<Candidate> candidateTableView;
    @FXML
    private TableColumn<Candidate, String> candidatePoliticianColumn;
    @FXML
    private TableColumn<Candidate, String> candidatePartyColumn;
    @FXML
    private TableColumn<Candidate, Integer> candidateVotesColumn;


    @FXML
    private TableView<Politician> politicianDetailsTableView;
    @FXML
    private TableColumn<Politician, String> dobColumn2;
    @FXML
    private TableColumn<Politician, String> partyColumn2;
    @FXML
    private TableColumn<Politician, String> countyColumn2;
    @FXML
    private TableColumn<Politician, String> photoColumn2;

    // search fields
    private String selectedSearchFilterPolitician = "All (Name, Party, County)";
    private final String selectedSortOptionPolitician = "Name (A-Z)";

    private String selectedSearchFilterElection = "All (Type, Date)";
    private String selectedSortOptionElection = "Type (A-Z)";

    @FXML
    public void initialize() {

        // set up table columns
        // politician details table on politician tab
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("politicianName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        partyColumn.setCellValueFactory(new PropertyValueFactory<>("politicalParty"));
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("homeCounty"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));

        // election details table on politician tab
        electionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("electionType"));
        electionLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        electionDateColumn.setCellValueFactory(new PropertyValueFactory<>("electionDate"));
        electionNumWinnersColumn.setCellValueFactory(new PropertyValueFactory<>("numWinners"));

        // candidate details table on election tab
        candidatePoliticianColumn.setCellValueFactory(new PropertyValueFactory<>("politicianName"));
        candidatePartyColumn.setCellValueFactory(new PropertyValueFactory<>("electionParty"));
        candidateVotesColumn.setCellValueFactory(new PropertyValueFactory<>("votes"));

        // politician details table on election tab
        dobColumn2.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        partyColumn2.setCellValueFactory(new PropertyValueFactory<>("politicalParty"));
        countyColumn2.setCellValueFactory(new PropertyValueFactory<>("homeCounty"));
        photoColumn2.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));


        // allow columns to resize
        politicianTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        politicianElectionTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        candidateTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        politicianDetailsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);


        politicianListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Politician item, boolean empty) {
                super.updateItem(item, empty);

                setText((empty || item == null) ? null : item.getPoliticianName());
            }

        });

        candidateListView.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Candidate item, boolean empty) {
                super.updateItem(item, empty);

                setText((empty || item == null) ? null : item.getPolitician().getPoliticianName());
            }
        });

        // listener to update table view when a politician is selected
        politicianListView.getSelectionModel().selectedItemProperty().addListener((_, _, newPolitician) -> {

            // clear previous politician details
            politicianTableView.getItems().clear();
            politicianElectionTableView.getItems().clear();

            if (newPolitician != null) {

                // show politician personal details
                politicianTableView.getItems().add(newPolitician);

                // show all elections for this politician
                for (Election election : newPolitician.getElectionRecord()) {
                    politicianElectionTableView.getItems().add(election);
                }
            }
        });

        // listener to load candidates for the selected election
        electionListView.getSelectionModel().selectedItemProperty().addListener((_, _, newElection) -> {

            // clear previous candidate data
            candidateListView.getItems().clear();
            candidateTableView.getItems().clear();

            // if an election is selected, show only its candidates
            if (newElection != null) {
                for (Candidate candidate : newElection.getCandidates()) {
                    candidateListView.getItems().add(candidate);
                }
            }

        });

        // listener to show candidate details from selected candidate
        candidateListView.getSelectionModel().selectedItemProperty().addListener((_, _, newCandidate) -> {

            // clear previous candidate details
            candidateTableView.getItems().clear();
            politicianDetailsTableView.getItems().clear();

            // if a candidate is selected, show their details
            if (newCandidate != null) {
                candidateTableView.getItems().add(newCandidate);
                politicianDetailsTableView.getItems().add(newCandidate.getPolitician());
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

    // TODO NEED TO ADD SORTING FOR SEARCH IDK

    // politician buttons

    @FXML
    public void onSearchOptionsPolitician() {
        // filter dialog
        String selectedFilter = politicianFilterDialog(selectedSearchFilterPolitician);
        if (selectedFilter == null) return;

        // sort dialog
        String selectedSort = politicianSortDialog(selectedSortOptionPolitician);
        if (selectedSort == null) return;

        // save the selections
        selectedSearchFilterPolitician = selectedFilter;
        selectedSortOptionElection = selectedSort;
    }
    
    @FXML
    public void onSearchPolitician() {
        // 1. uses fast hash lookups first for identical matches (fast)
        // 2. next uses linked list iteration for partial matches (slow)
        // 3. sorts matches

        // clear old list view
        politicianListView.getItems().clear();
        
        
        String query = politicianSearchResult.getText().trim().toLowerCase();

        // if search bar is empty, add all back to list
        if (query.isEmpty()) {
            for (Politician politician : politicianLinkedList) {
                politicianListView.getItems().add(politician);
            }

            // TODO add sort
            return;
        }

        LinkedList<Politician> matches = new LinkedList<>();

        // 1. exact search : hash lookup

        // name
        if (selectedSearchFilterPolitician.equals("All (Name, Party, County)") || selectedSearchFilterPolitician.equals("Name Only")) {
            Politician nameMatch = nameHashTable.get(query);
            if (nameMatch != null && notAlreadyAdded(matches, nameMatch)) matches.add(nameMatch);
        }

        // party
        if (selectedSearchFilterPolitician.equals("All (Name, Party, County)") || selectedSearchFilterPolitician.equals("Party Only")) {
            LinkedList<Politician> partyMatchList = partyHashTable.get(query);
            if (partyMatchList != null) {
                for (Politician partyMatch : partyMatchList) {
                    if (notAlreadyAdded(matches, partyMatch)) matches.add(partyMatch);
                }
            }
        }

        // county
        if (selectedSearchFilterPolitician.equals("All (Name, Party, County)") || selectedSearchFilterPolitician.equals("County Only")) {
            LinkedList<Politician> countyMatchList = countyHashTable.get(query);
            if (countyMatchList != null) {
                for (Politician countyMatch : countyMatchList) {
                    if (notAlreadyAdded(matches, countyMatch)) matches.add(countyMatch);
                }
            }
        }

        // 2. partial search : iterate through linked list

        for (Politician partialMatch : politicianLinkedList) {
            boolean matchesName = partialMatch.getPoliticianName().toLowerCase().contains(query);
            boolean matchesParty = partialMatch.getPoliticalParty().toLowerCase().contains(query);
            boolean matchesCounty = partialMatch.getHomeCounty().toLowerCase().contains(query);

            boolean shouldAdd = switch (selectedSearchFilterPolitician) {
                case "All (Name, Party, County)" -> matchesName || matchesParty || matchesCounty;
                case "Name Only" -> matchesName;
                case "Party Only" -> matchesParty;
                case "County Only" -> matchesCounty;
                default -> false;
            };

            if (shouldAdd && notAlreadyAdded(matches, partialMatch)) matches.add(partialMatch);
        }

        // 3. sort matches

        // TODO sort matches
        sortPoliticianSearch(matches, selectedSortOptionPolitician);

        // add to display
        for (Politician politician : matches) {
            politicianListView.getItems().add(politician);
        }
    }

    @FXML
    private void sortPoliticianSearch(LinkedList<Politician> matches, String selectedSortOption) {
        //TODO MAKE SORT LOGIC
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
    public void onSearchOptionsElection() {
        // filter dialog
        String selectedFilter = electionFilterDialog(selectedSearchFilterElection);
        if (selectedFilter == null) return;

        // sort dialog
        String selectedSort = electionSortDialog(selectedSortOptionElection);
        if (selectedSort == null) return;

        // save the choices
        selectedSearchFilterElection = selectedFilter;
        selectedSortOptionElection = selectedSort;
    }

    @FXML
    public void onSearchElection() {
        // 1. uses fast hash lookups first for identical matches (fast)
        // 2. next uses linked list iteration for partial matches (slow)
        // 3. sorts matches

        // clear old list view
        electionListView.getItems().clear();

        String query = electionSearchResult.getText().trim().toLowerCase();

        // if search bar is empty, add all back to list
        if (query.isEmpty()) {
            for (Election election : electionLinkedList) {
                electionListView.getItems().add(election);
            }

            // TODO add sort
            return;
        }
        
        LinkedList<Election> matches = new LinkedList<>();

        // 1. exact search : hash lookup
        
        // type
        if (selectedSearchFilterElection.equals("All (Type, Date)") || selectedSearchFilterElection.equals("Election Type Only")) {
            LinkedList<Election> typeMatchList = electionTypeHashTable.get(query);

            if (typeMatchList != null) {
                for (Election typeMatch : typeMatchList) {
                    if (notAlreadyAdded(matches, typeMatch)) matches.add(typeMatch);
                }
            }
        }

        // Date
        if (selectedSearchFilterElection.equals("All (Type, Date)") || selectedSearchFilterElection.equals("Date of Election Only")) {
            LinkedList<Election> dateMatchList = electionDateHashTable.get(query);

            if (dateMatchList != null) {
                for (Election dateMatch : dateMatchList) {
                    if (notAlreadyAdded(matches, dateMatch)) matches.add(dateMatch);
                }
            }
        }

        // 2. partial search : iterate through linked list

        for (Election partialMatch : electionLinkedList) {
            boolean matchesType = partialMatch.getElectionType().toLowerCase().contains(query);
            boolean matchesDate = partialMatch.getElectionDate().toLowerCase().contains(query);

            boolean shouldAdd = switch (selectedSearchFilterElection) {
                case "All (Type, Date)" -> matchesType || matchesDate;
                case "Election Type Only" -> matchesType;
                case "Date of Election Only" -> matchesDate;
                default -> false;
            };

            if (shouldAdd && notAlreadyAdded(matches, partialMatch)) matches.add(partialMatch);
        }

        // 3. sort matches

        // TODO sort matches
        sortElectionSearch(matches, selectedSortOptionElection);

        // add to display
        for (Election election : matches) {
            electionListView.getItems().add(election);
        }
        
        
    }

    @FXML
    private void sortElectionSearch(LinkedList<Election> matches, String selectedSortOption) {
        //TODO MAKE SORT LOGIC
    }

    @FXML
    public void onAddElection() {
        // open the election dialog to create a new election
        Election newElection = electionDialog(null);
        if (newElection == null) return; // if user cancels dialog, do nothing

        // get the linked list of elections for this type from the hash table
        LinkedList<Election> electionTypeList = electionTypeHashTable.get(newElection.getElectionType());

        // if no list exists for this type, create one and store it
        if (electionTypeList == null) {
            electionTypeList = new LinkedList<>();
            electionTypeHashTable.put(newElection.getElectionType(), electionTypeList);
        }

        // add new election to linked list
        electionTypeList.add(newElection);

        // get the linked list of elections for this date from the hash table
        LinkedList<Election> electionDateList = electionDateHashTable.get(newElection.getElectionDate());

        // If no list exists for this date, create one and store it
        if (electionDateList == null) {
            electionDateList = new LinkedList<>();
            electionDateHashTable.put(newElection.getElectionDate(), electionDateList);
        }

        // add new election to linked list
        electionDateList.add(newElection);

        // add the new election to the linked list
        electionLinkedList.add(newElection);

        // add the new election to the list view
        electionListView.getItems().add(newElection);

        // debug
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
        // get the currently selected election from the list view
        Election selected = electionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // store the original election type and date
        // these are used to update the hash table
        String oldElectionType = selected.getElectionType();
        String oldElectionDate = selected.getElectionDate();

        // open the election dialog to make an updated election
        Election edited = electionDialog(selected);
        if (edited == null) return;

        // if user changed election type, update hash table
        if (!oldElectionType.equals(edited.getElectionType())) {

            // remove from old list
            LinkedList<Election> oldElectionTypeList = electionTypeHashTable.get(oldElectionType);
            oldElectionTypeList.remove(selected);

            // gets new list
            LinkedList<Election> newElectionTypeList = electionTypeHashTable.get(edited.getElectionType());

            // if no list for that type exists, make a new one and store it
            if (newElectionTypeList == null) {
                newElectionTypeList = new LinkedList<>();
                electionTypeHashTable.put(edited.getElectionType(), newElectionTypeList);
            }
            newElectionTypeList.add(edited);
        }

        // if user changed election date, update hash table
        if (!oldElectionDate.equals(edited.getElectionDate())) {

            // remove from old list
            LinkedList<Election> oldElectionDateList = electionDateHashTable.get(oldElectionDate);
            oldElectionDateList.remove(selected);

            // gets new list
            LinkedList<Election> newElectionDateList = electionDateHashTable.get(edited.getElectionDate());

            // if no list for that date exists, make a new one and store it
            if (newElectionDateList == null) {
                newElectionDateList = new LinkedList<>();
                electionDateHashTable.put(edited.getElectionDate(), newElectionDateList);
            }
            newElectionDateList.add(edited);
        }

        // refresh list view to show changes
        electionListView.refresh();

        // debug
        System.out.println("Number of elections: " + electionLinkedList.size());
        System.out.println(electionLinkedList.display());
    }

    @FXML
    public void onRemoveElection() {
        // get the currently selected election from the list view
        Election selected = electionListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // remove the election from the hash table based on election type
        LinkedList<Election> electionTypeList = electionTypeHashTable.get(selected.getElectionType());
        if (electionTypeList != null) electionTypeList.remove(selected);

        // remove the election from the hash table based on election date
        LinkedList<Election> electionDateList = electionDateHashTable.get(selected.getElectionDate());
        if (electionDateList != null) electionDateList.remove(selected);

        // remove the election from the linked list
        electionLinkedList.remove(selected);

        // remove the election from the list view
        electionListView.getItems().remove(selected);

        // debug
        System.out.println("Number of elections: " + electionLinkedList.size());
        System.out.println(electionLinkedList.display());
    }

    // candidate buttons

    @FXML
    public void onAddCandidate() {

        // get the currently selected election from the list view
        Election selectedElection = electionListView.getSelectionModel().getSelectedItem();
        if (selectedElection == null) return;

        // open the candidate dialog to create a new candidate
        Candidate newCandidate = candidateDialog(null);
        if (newCandidate == null) return;

        // add the new candidate to the selected election
        selectedElection.addCandidate(newCandidate);

        // add candidate to list view
        candidateListView.getItems().add(newCandidate);

        // adds election to politicians record
        newCandidate.getPolitician().addElection(selectedElection);

        // refresh election table for currently selected politician
        Politician selectedPolitician = politicianListView.getSelectionModel().getSelectedItem();
        refreshPoliticianElectionTable(selectedPolitician);

        // debug
        System.out.println("Number of candidates: " + selectedElection.getCandidates().size());
        System.out.println(selectedElection.getCandidates().display());
    }

    @FXML
    public void onEditCandidate() {
        // get the currently selected candidate
        Candidate selected = candidateListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // create new "edited" candidate
        Candidate edited = candidateDialog(selected);
        if (edited == null) return;

        // candidateDialog already updated selected candidate, hence just refresh
        candidateListView.refresh();
    }

    @FXML
    public void onRemoveCandidate() {

        // get the currently selected candidate
        Candidate toRemove = candidateListView.getSelectionModel().getSelectedItem();
        if (toRemove == null) return;

        // get the currently selected election
        Election selectedElection = electionListView.getSelectionModel().getSelectedItem();
        if (selectedElection == null) return;

        // remove the candidate from the election
        selectedElection.getCandidates().remove(toRemove);

        // remove the election from the list view
        candidateListView.getItems().remove(toRemove);

        // adds election to politicians record
        toRemove.getPolitician().removeElection(selectedElection);

        // refresh election table for currently selected politician
        Politician selectedPolitician = politicianListView.getSelectionModel().getSelectedItem();
        refreshPoliticianElectionTable(selectedPolitician);

        // debug
        System.out.println("Number of candidates: " + selectedElection.getCandidates().size());
    }

    @FXML
    private void refreshPoliticianElectionTable(Politician politician) {
        politicianElectionTableView.getItems().clear();
        if (politician == null) return;

        for (Election e : politician.getElectionRecord()) {
            politicianElectionTableView.getItems().add(e);
        }
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

        // name
        String politicianName = textInputDialog(prefillName, dialogTitle, "Enter Politician Name");
        if (politicianName.isEmpty()) return null;

        // date of birth
        String dateOfBirth = calenderDialog(prefillDob, dialogTitle, "Enter Date of Birth:");
        if (dateOfBirth == null || dateOfBirth.isEmpty()) return null;

        // political party
        String politicalParty = textInputDialog(prefillPoliticalParty, dialogTitle, "Enter Political Party");
        if (politicalParty.isEmpty()) return null;

        // home county
        String homeCounty = countyDialog(prefillCounty, dialogTitle);
        if (homeCounty == null || homeCounty.isEmpty()) return null;

        // photo URL
        String photoUrl = textInputDialog(prefillPhotoUrl, dialogTitle, "Enter Photo URL");
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
        int prefillNumWinners = existing != null ? existing.getNumWinners() : 1;

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
            Integer userInput = numberDialog(prefillNumWinners > 0 ? prefillNumWinners : 1, dialogTitle);
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
    private String textInputDialog(String prefill, String title, String header) {
        TextInputDialog dialog = new TextInputDialog(prefill);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setGraphic(null);
        applyStylesheet(dialog.getDialogPane());

        return dialog.showAndWait().orElse("").trim();
    }

    @FXML
    private String showComboDialog(ComboBox<String> comboBox, Dialog<String> dialog, String prefill) {

        if (prefill != null && !prefill.isEmpty()) {
            comboBox.setValue(prefill);
        }

        dialog.getDialogPane().setContent(comboBox);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().setAll(ok, cancel);

        dialog.setResultConverter(button ->
                button == ok ? comboBox.getValue() : null
        );

        return dialog.showAndWait().orElse(null);
    }

    @FXML
    private Candidate candidateDialog(Candidate existing) {
        // prefill values if editing
        Politician prefillPolitician = existing != null ? existing.getPolitician() : null;
        String prefillParty = existing != null ? existing.getElectionParty() : "";
        int prefillVotes = existing != null ? existing.getVotes() : 0;

        String dialogTitle = (existing != null) ? "Edit Candidate" : "Add Candidate";

        Election election = electionListView.getSelectionModel().getSelectedItem();
        if (election == null) return null;

        // politician
        Politician politician = politicianElectionDialog(prefillPolitician, dialogTitle, election);
        if (politician == null) return null;

        // party during election
        String party = textInputDialog(prefillParty, dialogTitle, "Enter Party for this Election");
        if (party.isEmpty()) return null;

        // num votes
        Integer numVotes = null;

        // keeps asking till valid input/user cancelled dialog
        while (numVotes == null) {

            String numVotesStr = textInputDialog(String.valueOf(prefillVotes), dialogTitle, "Enter Number of Votes");
            if (numVotesStr.isEmpty()) return null; // user cancelled dialog

            try {
                int parsedInt = Integer.parseInt(numVotesStr);
                if (parsedInt < 0) throw new NumberFormatException();
                numVotes = parsedInt;
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Input must be Non-negative Integer");
                alert.setContentText("Please enter a valid input: ");
                alert.showAndWait();
            }


        }

        // if making new candidate, make new candidate
        if (existing == null) return new Candidate(politician, party, numVotes);

        // else update existing candidate
        existing.setPolitician(politician);
        existing.setElectionParty(party);
        existing.setVotes(numVotes);

        return existing;
    }

    @FXML
    private Politician politicianElectionDialog(Politician prefillPolitician, String dialogTitle, Election election) {

        // create dialog for selecting a politician
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText("Select Politician");
        dialog.setGraphic(null);
        applyStylesheet(dialog.getDialogPane());

        // combo box to display available politicians
        ComboBox<String> politicianCombo = new ComboBox<>();

        // loop through all politicians
        for (Politician politician : politicianLinkedList) {

            boolean alreadyCandidate = false;

            // check if this politician is already a candidate in the selected election
            for (Candidate candidate : election.getCandidates()) {
                if (candidate.getPolitician().getPoliticianName().equalsIgnoreCase(politician.getPoliticianName())) {
                    alreadyCandidate = true;
                    break;
                }
            }

            // only add politician if they are NOT already a candidate
            if (!alreadyCandidate) {
                politicianCombo.getItems().add(politician.getPoliticianName());
            }
        }


        // prefill politician name if editing an existing candidate
        String prefillPolName = (prefillPolitician != null) ? prefillPolitician.getPoliticianName() : null;

        // show dialog and get selected politician name
        String selected = showComboDialog(politicianCombo, dialog, prefillPolName);
        if (selected == null || selected.isEmpty()) return null;

        // return politician from the hash table
        return nameHashTable.get(selected.toLowerCase().trim());
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

    @SuppressWarnings("SpellCheckingInspection")
    @FXML
    private String countyDialog(String prefillCounty, String dialogTitle) {
        Dialog<String> countyDialog = new Dialog<>();
        countyDialog.setTitle(dialogTitle);
        countyDialog.setHeaderText("Select County");
        countyDialog.setGraphic(null);
        applyStylesheet(countyDialog.getDialogPane());

        ComboBox<String> countyCombo = new ComboBox<>();
        countyCombo.getItems().addAll(
                "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin",
                "Galway", "Kerry", "Kildare", "Kilkenny", "Laois", "Leitrim",
                "Limerick", "Longford", "Louth", "Mayo", "Meath", "Monaghan",
                "Offaly", "Roscommon", "Sligo", "Tipperary", "Waterford",
                "Westmeath", "Wexford", "Wicklow"
        );

        return showComboDialog(countyCombo, countyDialog, prefillCounty);
    }

    @SuppressWarnings("SpellCheckingInspection")
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

        return showComboDialog(constituencyCombo, generalDialog, prefill);
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

        return showComboDialog(euroCombo, dialog, prefill);
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

        return showComboDialog(electionTypeCombo, electionTypeDialog, prefillElectionType);
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
    private Integer numberDialog(Integer prefill, String dialogTitle) {

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText("Enter Number of Winners:");
        applyStylesheet(dialog.getDialogPane());

        Spinner<Integer> spinner = new Spinner<>(1, 100, prefill != null ? prefill : 1);
        spinner.setEditable(false);

        dialog.getDialogPane().setContent(spinner);

        ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ok, cancel);

        dialog.setResultConverter(button -> button == ok ? spinner.getValue() : null);

        return dialog.showAndWait().orElse(null);
    }

    @FXML
    private String politicianFilterDialog(String prefillFilter) {
        //searchFilterSortDialog(prefillFilter, "Search Filters","Select Filters for Search")
        Dialog<String> filterDialog = new Dialog<>();
        filterDialog.setTitle("Search Filters");
        filterDialog.setHeaderText("Select Filters for Search");
        filterDialog.setGraphic(null);
        applyStylesheet(filterDialog.getDialogPane());
        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll(
                "All (Name, Party, County)",
                "Name Only",
                "Party Only",
                "County Only"
        );

        return showComboDialog(filterCombo, filterDialog, prefillFilter);
    }

    @FXML
    private String politicianSortDialog(String prefillSort) {
        Dialog<String> sortDialog = new Dialog<>();
        sortDialog.setTitle("Sort Options");
        sortDialog.setHeaderText("Select Sort Method");
        sortDialog.setGraphic(null);
        applyStylesheet(sortDialog.getDialogPane());

        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll(
                "Name (A-Z)",
                "Name (Z-A)",
                "Party (A-Z)",
                "County (A-Z)",
                "Date of Birth (Oldest first)",
                "Date of Birth (Youngest first)"
        );

        return showComboDialog(sortCombo, sortDialog, prefillSort);
    }

    @FXML
    private String electionFilterDialog(String prefillFilter) {
        Dialog<String> filterDialog = new Dialog<>();
        filterDialog.setTitle("Search Filters");
        filterDialog.setHeaderText("Select Filters for Search");
        filterDialog.setGraphic(null);
        applyStylesheet(filterDialog.getDialogPane());

        ComboBox<String> filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll(
                "All (Type, Date)",
                "Election Type Only",
                "Date of Election Only"
        );

        return showComboDialog(filterCombo, filterDialog, prefillFilter);
    }

    @FXML
    private String electionSortDialog(String prefillSort) {
        Dialog<String> sortDialog = new Dialog<>();
        sortDialog.setTitle("Sort Options");
        sortDialog.setHeaderText("Select Sort Method");
        sortDialog.setGraphic(null);
        applyStylesheet(sortDialog.getDialogPane());

        ComboBox<String> sortCombo = new ComboBox<>();
        sortCombo.getItems().addAll(
                "Type (A-Z)",
                "Type (Z-A)",
                "Date (Oldest first)",
                "Date (Newest first)"
        );

        return showComboDialog(sortCombo, sortDialog, prefillSort);
    }

   /* @FXML
    private String searchFilterSortDialog(String prefill, String title, String header) {
        Dialog<String> filterDialog = new Dialog<>();
        filterDialog.setTitle(title);
        filterDialog.setHeaderText(header);
        filterDialog.setGraphic(null);
        applyStylesheet(filterDialog.getDialogPane());
    }*/


    private <T> boolean notAlreadyAdded(LinkedList<T> list, T item) {
        for (T existing : list) {
            if (existing == item) {
                return false;
            }
        }
        return true;
    }


}

package com.example.electionsinformationsystem;

import com.example.electionsinformationsystem.models.Election;
import com.example.electionsinformationsystem.models.LinkedList;
import com.example.electionsinformationsystem.models.Politician;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.util.Objects;

public class HelloController {
    private final LinkedList<Politician> politicianLinkedList = new LinkedList<>();

    @FXML
    private ListView<Politician> politicianListView;

    @FXML
    public TableView<Politician> politicianTableView;

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
    public TableView<Election> politicianElectionTableView;

    @FXML
    private TableColumn<Election, String> electionPartyColumn;
    @FXML
    private TableColumn<Election, Integer> numVotesColumn;

    @FXML
    private ComboBox<String> themePicker;

    @FXML
    private TextField politicianSearhResult;




    @FXML @SuppressWarnings("deprecation")
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
        themePicker.getSelectionModel().select("Light Mode");
    }

    @FXML
    public void onChangeTheme() {
        Scene scene = themePicker.getScene();
        if (scene == null) return;

        applyStylesheet(scene.getRoot());
    }

    // TODO NEED TO ADD FILTERING AND SORTING FOR SEARCH PROB
    @FXML
    public void onSearchPolitician() {
        String query = politicianSearhResult.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            politicianListView.getItems().clear();
            for (Politician p : politicianLinkedList) {
                politicianListView.getItems().add(p);
            }
            return;
        }

        LinkedList<Politician> matches = new LinkedList<>();
        for (Politician politician : politicianLinkedList) {
            boolean matchesName = politician.getPoliticianName().toLowerCase().contains(query);
            boolean matchesParty = politician.getPoliticalParty().toLowerCase().contains(query);
            boolean matchesCounty = politician.getHomeCounty().toLowerCase().contains(query);

            if (matchesName || matchesParty || matchesCounty) {
                matches.add(politician);
            }
        }

        politicianListView.getItems().clear();
        for (Politician p : matches) {
            politicianListView.getItems().add(p);
        }
    }

    @FXML
    public void onAddPolitician() {
        Politician newPolitician = politicianDialog(null);
        if (newPolitician == null) return;

        politicianLinkedList.add(newPolitician);
        politicianListView.getItems().add(newPolitician);

        System.out.println("Number of politicians: " + politicianLinkedList.size());
        System.out.println(politicianLinkedList.display());
    }

    @FXML
    public void onEditPolitician() {
        Politician toEdit = politicianDialog(politicianListView.getSelectionModel().getSelectedItem());
        if (toEdit == null) return;

        politicianListView.refresh();

        System.out.println("Number of politicians: " + politicianLinkedList.size());
        System.out.println(politicianLinkedList.display());
    }

    @FXML
    public void onRemovePolitician() {
        Politician selected = politicianListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        politicianLinkedList.remove(selected);
        politicianListView.getItems().remove(selected);

        System.out.println("Number of politicians: " + politicianLinkedList.size());
        System.out.println(politicianLinkedList.display());
    }




    // HELPER METHODS

    @FXML
    private void applyStylesheet(Parent root) {
        if (root == null) return;

        root.getStylesheets().clear();

        String selectedTheme = themePicker.getValue();
        if (selectedTheme == null) return;

        String cssFile = switch (selectedTheme) {
            case "Dark Mode" -> "darkStyle.css";
            case "Cotton Candy" -> "pinkStyle.css";
            default -> "lightStyle.css";
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
        String dateOfBirth = calenderDialog(prefillDob, dialogTitle);
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
    private String calenderDialog(String prefillDob, String dialogTitle) {
        Dialog<String> calenderDialog = new Dialog<>();
        calenderDialog.setTitle(dialogTitle);
        calenderDialog.setHeaderText("Select Date of Birth");
        calenderDialog.setGraphic(null);
        applyStylesheet(calenderDialog.getDialogPane());

        DatePicker datePicker = new DatePicker();

        // prefill if editing
        if (prefillDob != null && !prefillDob.isEmpty()) {
            try {
                datePicker.setValue(java.time.LocalDate.parse(prefillDob));
            } catch (Exception ignored) {}
        }

        calenderDialog.getDialogPane().setContent(datePicker);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        calenderDialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        calenderDialog.setResultConverter(button -> {
            if (button == okButton) return (datePicker.getValue() != null) ? datePicker.getValue().toString() : "";
            return null;
        });

        return calenderDialog.showAndWait().orElse(null);
    }

    @FXML
    private String countyDialog(String prefillCounty, String dialogTitle) {
        Dialog<String> countyDialog = new Dialog<>();
        countyDialog.setTitle(dialogTitle);
        countyDialog.setHeaderText("Select Home County");
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

        countyCombo.setEditable(false);

        // prefill if editing
        if (prefillCounty != null && !prefillCounty.isEmpty()) {
            countyCombo.setValue(prefillCounty);
        }

        countyDialog.getDialogPane().setContent(countyCombo);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        countyDialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        countyDialog.setResultConverter(button -> {
            if (button == okButton) return countyCombo.getValue();
            return null;
        });

        return countyDialog.showAndWait().orElse(null);
    }
}

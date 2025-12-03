package com.example.electionsinformationsystem;

import com.example.electionsinformationsystem.models.Politician;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Objects;

public class HelloController {
    @FXML
    private ListView<Politician> politicianListView;

    @FXML
    public TableView<Politician> politicianTableView;

    private Politician selectedPolitician;


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

    private boolean darkMode = false;

    @FXML @SuppressWarnings("deprecation")
    public void initialize() {
        // set up table columns to pull their values from Politician fields
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("politicianName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        partyColumn.setCellValueFactory(new PropertyValueFactory<>("politicalParty"));
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("homeCounty"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoUrl"));

        politicianTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        politicianListView.getItems().addAll(
                new Politician("Alice", "1990-Jan-01", "Party A", "Dublin", "alice.png"),
                new Politician("Bob", "1985-Feb-12", "Party B", "Cork", "bob.png")
        );

        politicianListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Politician item, boolean empty) {
                super.updateItem(item, empty);

                setText((empty || item == null) ? null : item.getPoliticianName());
            }

        });

        // listener to update TableView when a Politician is selected
        politicianListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            politicianTableView.getItems().clear();
            if (newVal != null) {
                politicianTableView.getItems().add(newVal);
            }
        });
    }

    /*@FXML
    private void toggleTheme(ActionEvent event) {
        darkMode = !darkMode;

        Scene scene = .getScene(); // or whatever your root node is
        scene.getStylesheets().clear();

        String stylesheet = darkMode
                ? "darkStyle.css"
                : "lightStyle.css";

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(stylesheet)).toExternalForm()
        );
    }*/

    @FXML
    public void onToggleTheme() {
        darkMode = !darkMode;

        Scene scene = politicianListView.getScene();
        if (scene != null) {
            applyStylesheet(scene.getRoot());
        }
    }

    @FXML
    public void onAddPolitician() {
        Politician newPolitician = politicianDialog(null);
        if (newPolitician == null) return;

        politicianListView.getItems().add(newPolitician);
    }

    @FXML
    public void onEditPolitician() {
        Politician toEdit = politicianDialog(politicianListView.getSelectionModel().getSelectedItem());
        if (toEdit == null) return;

        politicianListView.refresh();
    }

    @FXML
    public void onRemovePolitician() {
    }




    // HELPER METHODS

    @FXML
    private void applyStylesheet(Parent root) {
        if (root == null) return;

        root.getStylesheets().clear();

        String stylesheet = darkMode ? "darkStyle.css" : "lightStyle.css";

        root.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(stylesheet)).toExternalForm()
        );
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

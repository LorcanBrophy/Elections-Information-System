module com.example.electionsinformationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires xstream;

    opens com.example.electionsinformationsystem to javafx.fxml;
    opens com.example.electionsinformationsystem.models to javafx.fxml, xstream;
    opens com.example.electionsinformationsystem.controllers to javafx.fxml;
    opens com.example.electionsinformationsystem.main to javafx.fxml;
    exports com.example.electionsinformationsystem;
    exports com.example.electionsinformationsystem.models;
    exports com.example.electionsinformationsystem.controllers;
    exports com.example.electionsinformationsystem.main;
}

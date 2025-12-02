module com.example.electionsinformationsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.electionsinformationsystem to javafx.fxml;
    exports com.example.electionsinformationsystem;
}
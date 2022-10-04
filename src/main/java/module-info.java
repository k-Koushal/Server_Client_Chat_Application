module com.messengerapp.messenger_application {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.messengerapp.messenger_application to javafx.fxml;
    exports com.messengerapp.messenger_application;
}
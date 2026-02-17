module gestionautos {
    requires javafx.controls;
    requires javafx.fxml;

    opens gestionauto.gestionautos to javafx.fxml;
    exports gestionauto.gestionautos;
}

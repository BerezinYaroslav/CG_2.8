module com.company.kg_task2_8 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.cs.vsu.berezin_y_a to javafx.fxml;
    exports ru.cs.vsu.berezin_y_a;
}
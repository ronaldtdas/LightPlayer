module com.ronaldtdas.lightplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.ronaldtdas.lightplayer to javafx.fxml;
    exports com.ronaldtdas.lightplayer;
}
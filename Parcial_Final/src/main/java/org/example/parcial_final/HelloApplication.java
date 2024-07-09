package org.example.parcial_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/parcial_final/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("Sistema de Gestión");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void showTarjetaSaldo(int idCliente) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/parcial_final/tarjeta-saldo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        TarjetaSaldo controller = fxmlLoader.getController();
        controller.setIdCliente(idCliente);
        Stage stage = new Stage();
        stage.setTitle("Establecer Saldo de Tarjeta");
        stage.setScene(scene);
        stage.show();
    }




}

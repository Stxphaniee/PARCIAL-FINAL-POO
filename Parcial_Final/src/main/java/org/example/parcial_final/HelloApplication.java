package org.example.parcial_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException { // 00095322 Método principal que arranca la aplicación
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml")); // 00095322 Cargar el archivo FXML
        Scene scene = new Scene(fxmlLoader.load(), 400, 300); // 00095322 Crear una nueva escena con el contenido del FXML
        stage.setTitle("Añadir Cliente"); // 00095322 Ponerle título a la ventana
        stage.setScene(scene); // 00095322 Establecer la escena en la ventana
        stage.show(); // 00095322 Mostrar la ventana
    }

    public static void main(String[] args) { // 00095322 Método principal que lanza la aplicación
        launch(); // 00095322 Iniciar la aplicación JavaFX
    }
}

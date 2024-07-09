package org.example.parcial_final;

import javafx.application.Application; // 00095322 Importa la clase Application de JavaFX
import javafx.fxml.FXMLLoader; // 00095322 Importa FXMLLoader para cargar archivos FXML
import javafx.scene.Scene; // 00095322 Importa Scene para crear una nueva escena
import javafx.stage.Stage; // 00095322 Importa Stage para representar la ventana principal

import java.io.IOException; // 00095322 Importa IOException para manejar excepciones de entrada/salida

public class HelloApplication extends Application { // 00095322 Define la clase HelloApplication que extiende Application
    @Override
    public void start(Stage stage) throws IOException { // 00095322 Método start que se ejecuta al iniciar la aplicación
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/parcial_final/main-view.fxml")); // 00095322 Carga el archivo FXML de la vista principal
        Scene scene = new Scene(fxmlLoader.load(), 900, 500); // 00095322 Crea una nueva escena con las dimensiones especificadas
        stage.setTitle("Sistema de Gestión"); // 00095322 Establece el título de la ventana
        stage.setScene(scene); // 00095322 Establece la escena en la ventana
        stage.show(); // 00095322 Muestra la ventana
    }

    public static void main(String[] args) { // 00095322 Método main que lanza la aplicación
        launch(); // 00095322 Llama al método launch para iniciar la aplicación
    }

    public void showTarjetaSaldo(int idCliente) throws IOException { // 00095322 Método para mostrar la ventana de establecer saldo de tarjeta
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/parcial_final/tarjeta-saldo-view.fxml")); // 00095322 Carga el archivo FXML de la vista de saldo de tarjeta
        Scene scene = new Scene(fxmlLoader.load(), 600, 500); // 00095322 Crea una nueva escena con las dimensiones especificadas
        TarjetaSaldo controller = fxmlLoader.getController(); // 00095322 Obtiene el controlador de la vista
        controller.setIdCliente(idCliente); // 00095322 Establece el ID del cliente en el controlador
        Stage stage = new Stage(); // 00095322 Crea una nueva ventana
        stage.setTitle("Establecer Saldo de Tarjeta"); // 00095322 Establece el título de la ventana
        stage.setScene(scene); // 00095322 Establece la escena en la ventana
        stage.show(); // 00095322 Muestra la ventana
    }
}

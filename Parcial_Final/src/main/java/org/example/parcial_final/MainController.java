package org.example.parcial_final;

import javafx.fxml.FXML; // 00095322 Importa las anotaciones FXML para el control de la vista
import javafx.fxml.FXMLLoader; // 00095322 Importa FXMLLoader para cargar archivos FXML
import javafx.scene.Parent; // 00095322 Importa Parent, el nodo raíz para la escena
import javafx.scene.Scene; // 00095322 Importa Scene para crear una nueva escena
import javafx.scene.control.Alert; // 00095322 Importa Alert para mostrar mensajes emergentes
import javafx.scene.control.Button; // 00095322 Importa Button para manejar los botones
import javafx.stage.Stage; // 00095322 Importa Stage para representar la ventana principal

import java.io.IOException; // 00095322 Importa IOException para manejar excepciones de entrada/salida

public class MainController {

    @FXML
    public Button btnCliente; // 00095322 Declara el botón para registrar cliente

    @FXML
    public Button btnServicio; // 00095322 Declara el botón para abrir la ventana de servicio

    @FXML
    public Button btnRegresar; // 00095322 Declara el botón para regresar

    @FXML
    private void initialize() { // 00095322 Método de inicialización
        btnCliente.setOnAction(event -> { // 00095322 Asigna la acción del botón cliente
            try {
                showCliente(); // 00095322 Llama al método showCliente al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Cliente."); // 00095322 Muestra una alerta de error
            }
        });

        btnServicio.setOnAction(event -> { // 00095322 Asigna la acción del botón servicio
            try {
                showServicio(); // 00095322 Llama al método showServicio al presionar el botón
            } catch (IOException e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Servicio."); // 00095322 Muestra una alerta de error
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00095322 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00095322 Crea una nueva alerta
        alert.setTitle(title); // 00095322 Establece el título de la alerta
        alert.setContentText(message); // 00095322 Establece el mensaje de la alerta
        alert.setHeaderText(null); // 00095322 Elimina el encabezado de la alerta
        alert.showAndWait(); // 00095322 Muestra la alerta y espera a que el usuario la cierre
    }

    public void showCliente() throws IOException { // 00095322 Método para mostrar la ventana de cliente
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/cliente-view.fxml")); // 00095322 Carga el archivo FXML de cliente
        Parent root = fxmlLoader.load(); // 00095322 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnCliente.getScene().getWindow(); // 00095322 Obtiene la ventana actual
        stage.setScene(new Scene(root, 900, 500)); // 00095322 Establece la nueva escena
        stage.setTitle("Registrar Cliente"); // 00095322 Establece el título de la ventana
    }

    public void showServicio() throws IOException { // 00095322 Método para mostrar la ventana de servicio
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/servicio-view.fxml")); // 00095322 Carga el archivo FXML de servicio
        Parent root = fxmlLoader.load(); // 00095322 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnServicio.getScene().getWindow(); // 00095322 Obtiene la ventana actual
        stage.setScene(new Scene(root, 1050, 600)); // 00095322 Establece la nueva escena
        stage.setTitle("Servicio"); // 00095322 Establece el título de la ventana
    }

}

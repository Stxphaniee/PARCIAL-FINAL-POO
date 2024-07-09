package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class ReporteAController {

    @FXML
    public Button btnGenerar; // 00197822 Botón para generar el reporte
    @FXML
    public Button btnRegresar; // 00197822 Botón para regresar al menú anterior
    @FXML
    private TextField clienteIdField; // 00197822 Campo de texto para ingresar el ID del cliente
    @FXML
    private DatePicker inicioField; // 00197822 Campo para seleccionar la fecha de inicio
    @FXML
    private DatePicker finField; // 00197822 Campo para seleccionar la fecha de fin


    @FXML
    private void initialize() { // 00197822 Método que se ejecuta al inicializar la vista
        btnGenerar.setOnAction(event -> { // 00197822 Acción al presionar el botón de generar
            String clienteIdStr = clienteIdField.getText(); // 00197822 Obtener el ID del cliente
            LocalDate inicio = inicioField.getValue(); // 00197822 Obtener la fecha de inicio
            LocalDate fin = finField.getValue(); // 00197822 Obtener la fecha de fin

            if (clienteIdStr.isEmpty() || inicio == null || fin == null) { // 00197822 Validar que los campos no estén vacíos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios."); // 00197822 Mostrar alerta si algún campo está vacío
                return;
            }

            try {
                int clienteId = Integer.parseInt(clienteIdStr); // 00197822 Convertir el ID del cliente a entero
                ReporteService reporteService = new ReporteService(); // 00197822 Crear una instancia de ReporteService
                reporteService.guardarReporteA(clienteId, Date.valueOf(inicio), Date.valueOf(fin)); // 00197822 Generar y guardar el reporte
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reporte generado exitosamente."); // 00197822 Mostrar alerta de éxito
            } catch (NumberFormatException e) { // 00197822 Manejar error si el ID del cliente no es un número
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El ID del Cliente debe ser un número."); // 00197822 Mostrar alerta de error de formato
            } catch (Exception e) { // 00197822 Manejar cualquier otro error
                e.printStackTrace(); // 00197822 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al generar el reporte."); // 00197822 Mostrar alerta de error general
            }
        });

        btnRegresar.setOnAction(event -> { // 00197822 Acción al presionar el botón de regresar
            try {
                backServicio(); // 00197822 Llamar al método para regresar al menú anterior
            } catch (IOException e) { // 00197822 Manejar error si ocurre al regresar
                e.printStackTrace(); // 00197822 Imprimir el stack trace del error
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Servicio."); // 00197822 Mostrar alerta de error
            }
        });


    }


    private void showAlert(Alert.AlertType alertType, String title, String message) { // 00197822 Método para mostrar alertas
        Alert alert = new Alert(alertType); // 00197822 Crear una nueva alerta
        alert.setTitle(title); // 00197822 Establecer el título de la alerta
        alert.setContentText(message); // 00197822 Establecer el mensaje de la alerta
        alert.setHeaderText(null); // 00197822 Eliminar el encabezado de la alerta
        alert.showAndWait(); // 00197822 Mostrar la alerta y esperar a que el usuario la cierre
    }

    public void backServicio() throws IOException { // 00197822 Método para regresar al menú de servicio
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/servicio-view.fxml")); // 00197822 Cargar el archivo FXML del menú de servicio
        Parent root = fxmlLoader.load(); // 00197822 Cargar el contenido del archivo FXML
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00197822 Obtener la ventana actual
        stage.setScene(new Scene(root, 1050, 600)); // 00197822 Establecer la nueva escena en la ventana
        stage.setTitle("Servicio"); // 00197822 Establecer el título de la ventana
    }
}

package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ReporteBController {

    @FXML
    public Button btnGenerarReporte; // 00197822 Botón para generar el reporte
    @FXML
    public Button btnRegresar; // 00197822 Botón para regresar al menú anterior
    @FXML
    private TextField clienteIdField; // 00197822 Campo para ingresar el ID del cliente
    @FXML
    private TextField mesField; // 00197822 Campo para ingresar el mes
    @FXML
    private TextField anioField; // 00197822 Campo para ingresar el año

    @FXML
    private void initialize() { // 00197822 Método que se ejecuta al inicializar la vista
        btnGenerarReporte.setOnAction(event -> { // 00197822 Acción al presionar el botón de generar reporte
            try {
                int clienteId = Integer.parseInt(clienteIdField.getText()); // 00197822 Convertir el ID del cliente a entero
                int mes = Integer.parseInt(mesField.getText()); // 00197822 Convertir el mes a entero
                int anio = Integer.parseInt(anioField.getText()); // 00197822 Convertir el año a entero

                if (mes < 1 || mes > 12 || anio < 1) { // 00197822 Validar que el mes y año sean válidos
                    showAlert(Alert.AlertType.ERROR, "Error", "Por favor ingrese un mes y año válidos."); // 00197822 Mostrar alerta si los valores no son válidos
                    return;
                }

                ReporteService reporteService = new ReporteService(); // 00197822 Crear una instancia de ReporteService
                reporteService.guardarReporteB(clienteId, mes, anio); // 00197822 Generar y guardar el reporte B

                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reporte B generado exitosamente."); // 00197822 Mostrar alerta de éxito
            } catch (NumberFormatException e) { // 00197822 Manejar error si los valores ingresados no son números
                showAlert(Alert.AlertType.ERROR, "Error", "Por favor ingrese valores válidos para el ID del cliente, mes y año."); // 00197822 Mostrar alerta de error de formato
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
        alert.setHeaderText(null); // 00197822 Eliminar el encabezado de la alerta
        alert.setContentText(message); // 00197822 Establecer el mensaje de la alerta
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

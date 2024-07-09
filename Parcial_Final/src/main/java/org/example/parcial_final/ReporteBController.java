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
    public Button btnGenerarReporte;
    @FXML
    public Button btnRegresar;
    @FXML
    private TextField clienteIdField;
    @FXML
    private TextField mesField;
    @FXML
    private TextField anioField;

    @FXML
    private void initialize() {
        btnGenerarReporte.setOnAction(event -> {
            try {
                int clienteId = Integer.parseInt(clienteIdField.getText());
                int mes = Integer.parseInt(mesField.getText());
                int anio = Integer.parseInt(anioField.getText());

                if (mes < 1 || mes > 12 || anio < 1) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Por favor ingrese un mes y año válidos.");
                    return;
                }

                ReporteService reporteService = new ReporteService();
                reporteService.guardarReporteB(clienteId, mes, anio);

                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reporte B generado exitosamente.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Por favor ingrese valores válidos para el ID del cliente, mes y año.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al generar el reporte.");
            }
        });

        btnRegresar.setOnAction(event -> {
            try {
                backServicio();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Servicio.");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void backServicio() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/servicio-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.setScene(new Scene(root, 1050, 600));
        stage.setTitle("Servicio");
    }
}

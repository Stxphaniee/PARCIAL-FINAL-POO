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

public class ReporteCController {

    @FXML
    public Button btnGenerar;
    @FXML
    public Button btnRegresar;
    @FXML
    private TextField clienteIdField;

    @FXML
    private void initialize () {
        btnGenerar.setOnAction(event -> {
            String clienteIdStr = clienteIdField.getText();

            if (clienteIdStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El ID del Cliente es obligatorio.");
                return;
            }

            try {
                int clienteId = Integer.parseInt(clienteIdStr);
                ReporteService reporteService = new ReporteService();
                reporteService.guardarReporteC(clienteId);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Reporte generado exitosamente.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El ID del Cliente debe ser un número.");
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
        alert.setContentText(message);
        alert.setHeaderText(null);
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

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
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class TarjetaController {

    @FXML
    public Button btnAgregar;

    @FXML
    public Button btnRegresar;
    @FXML
    private TextField clienteIdField;
    @FXML
    private TextField numeroTarjetaField;
    @FXML
    private TextField fechaExpiracionField;
    @FXML
    private TextField tipoTarjetaField;
    @FXML
    private TextField facilitadorField;

    @FXML
    private void initialize () {
        btnAgregar.setOnAction(event -> {
            String clienteIdStr = clienteIdField.getText();
            String numeroTarjeta = numeroTarjetaField.getText().replaceAll(" ", "");
            String fechaExpiracion = fechaExpiracionField.getText();
            String tipoTarjeta = tipoTarjetaField.getText();
            String facilitador = facilitadorField.getText();

            if (clienteIdStr.isEmpty() || numeroTarjeta.isEmpty() || fechaExpiracion.isEmpty() || tipoTarjeta.isEmpty() || facilitador.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
                return;
            }
            try {
                int clienteId = Integer.parseInt(clienteIdStr);

                Connection conn = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true");
                String sql = "INSERT INTO Tarjetas (NumeroTarjeta, ID_Cliente, FechaExpiracion, TipoTarjeta, Facilitador) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, numeroTarjeta);
                pstmt.setInt(2, clienteId);
                pstmt.setDate(3, Date.valueOf(fechaExpiracion));
                pstmt.setString(4, tipoTarjeta);
                pstmt.setString(5, facilitador);

                int rowsInserted = pstmt.executeUpdate();

                if (rowsInserted > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Tarjeta añadida exitosamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo añadir la tarjeta.");
                }

                clienteIdField.clear();
                numeroTarjetaField.clear();
                fechaExpiracionField.clear();
                tipoTarjetaField.clear();
                facilitadorField.clear();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al añadir la tarjeta.");
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

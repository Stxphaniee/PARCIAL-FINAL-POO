package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioController {

    @FXML
    public Button btnRegresar;

    @FXML
    public Button btnBuscar;
    @FXML
    private TextField idField;
    @FXML
    private Label infoLabel;

    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    @FXML
    private void initialize () {
        btnBuscar.setOnAction(event -> {

            String idClienteStr = idField.getText();
            if (idClienteStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El campo ID del Cliente es obligatorio.");
                return;
            }
            int idCliente = 0;

            try {
                idCliente = Integer.parseInt(idClienteStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El ID del Cliente debe ser un número válido.");
            }

            Connection conn = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(DB_URL);

                String sql = "SELECT c.NombreCompleto, c.Direccion, c.NumeroTelefono, t.NumeroTarjeta, t.Saldo " +
                        "FROM Clientes c " +
                        "LEFT JOIN Tarjetas t ON c.ID = t.ID_Cliente " +
                        "WHERE c.ID = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, idCliente);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String info = "Nombre: " + rs.getString("NombreCompleto") + "\n" +
                            "Dirección: " + rs.getString("Direccion") + "\n" +
                            "Teléfono: " + rs.getString("NumeroTelefono") + "\n" +
                            "Tarjeta: " + (rs.getString("NumeroTarjeta") != null ? rs.getString("NumeroTarjeta") : "No tiene tarjeta asociada") + "\n" +
                            "Saldo: $" + rs.getDouble("Saldo");
                    infoLabel.setText(info);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se encontró información para el usuario.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al buscar la información del usuario: " + e.getMessage());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnRegresar.setOnAction(event -> {
            try {
                backCliente();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Cliente");
            }
        });
    }


    public void backCliente() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/cliente-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Registrar Cliente");
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

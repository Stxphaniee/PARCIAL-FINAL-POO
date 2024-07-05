package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class HelloController {
    @FXML
    private TextField nombreField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;

    @FXML
    protected void onAddButtonClick() {
        String nombre = nombreField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            // Mostrar una alerta si los campos obligatorios están vacíos
            showAlert(Alert.AlertType.ERROR, "Error de Validación", "Nombre y Teléfono son obligatorios.");
            return;
        }

        Connection conn = null;
        try {
            // Registrar el controlador JDBC
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Establecer la conexión con la base de datos
            String url = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
            conn = DriverManager.getConnection(url);

            // Crear el PreparedStatement para insertar el cliente
            String sql = "INSERT INTO Clientes (NombreCompleto, Direccion, NumeroTelefono) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombre);
            pstmt.setString(2, direccion);
            pstmt.setString(3, telefono);

            // Ejecutar la inserción
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                // Mostrar una alerta de éxito si la inserción fue exitosa
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente añadido exitosamente.");
            } else {
                // Mostrar una alerta de error si la inserción falló
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo añadir el cliente.");
            }

            // Limpiar los campos después de la inserción
            nombreField.clear();
            direccionField.clear();
            telefonoField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            // Mostrar una alerta de error si ocurre una excepción
            showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al añadir el cliente.");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

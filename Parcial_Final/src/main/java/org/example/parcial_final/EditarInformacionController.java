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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditarInformacionController {

    @FXML
    public Button btnRegresar;
    @FXML
    public Button btnBuscar;
    @FXML
    public Button btnActualizar;
    @FXML
    public Button btnEliminar;
    @FXML
    private TextField idClienteField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField fechaExpiracionField;
    @FXML
    private TextField saldoField;

    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    @FXML
    private void initialize () {
        btnBuscar.setOnAction(event -> {
            String idCliente = idClienteField.getText();

            if (idCliente.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El campo ID del Cliente es obligatorio.");
                return;
            }

            Connection conn = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(DB_URL);

                String sql = "SELECT c.NombreCompleto, c.Apellidos, c.Direccion, c.NumeroTelefono, t.FechaExpiracion, t.Saldo " +
                        "FROM Clientes c " +
                        "LEFT JOIN Tarjetas t ON c.ID = t.ID_Cliente " +
                        "WHERE c.ID = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(idCliente));
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    nombreField.setText(rs.getString("NombreCompleto"));
                    apellidoField.setText(rs.getString("Apellidos"));
                    telefonoField.setText(rs.getString("NumeroTelefono"));
                    direccionField.setText(rs.getString("Direccion"));
                    fechaExpiracionField.setText(rs.getString("FechaExpiracion"));
                    saldoField.setText(String.valueOf(rs.getDouble("Saldo")));
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

        btnActualizar.setOnAction(event -> {
            String idCliente = idClienteField.getText();
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String telefono = telefonoField.getText();
            String direccion = direccionField.getText();
            String fechaExpiracion = fechaExpiracionField.getText();
            String saldoStr = saldoField.getText();

            if (idCliente.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() ||
                    direccion.isEmpty() || saldoStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
                return;
            }

            double saldo;
            try {
                saldo = Double.parseDouble(saldoStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El saldo debe ser un número válido.");
                return;
            }

            Connection conn = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(DB_URL);

                // Actualizar información del cliente
                String sqlUpdateCliente = "UPDATE Clientes SET NombreCompleto = ?, Apellidos = ?, Direccion = ?, NumeroTelefono = ? WHERE ID = ?";
                PreparedStatement pstmtUpdateCliente = conn.prepareStatement(sqlUpdateCliente);
                pstmtUpdateCliente.setString(1, nombre);
                pstmtUpdateCliente.setString(2, apellido);
                pstmtUpdateCliente.setString(3, direccion);
                pstmtUpdateCliente.setString(4, telefono);
                pstmtUpdateCliente.setInt(5, Integer.parseInt(idCliente));

                int rowsUpdatedCliente = pstmtUpdateCliente.executeUpdate();

                // Actualizar información de la tarjeta (si existe fecha de expiración)
                if (!fechaExpiracion.isEmpty()) {
                    String sqlUpdateTarjeta = "UPDATE Tarjetas SET FechaExpiracion = ?, Saldo = ? WHERE ID_Cliente = ?";
                    PreparedStatement pstmtUpdateTarjeta = conn.prepareStatement(sqlUpdateTarjeta);
                    pstmtUpdateTarjeta.setString(1, fechaExpiracion);
                    pstmtUpdateTarjeta.setDouble(2, saldo);
                    pstmtUpdateTarjeta.setInt(3, Integer.parseInt(idCliente));

                    int rowsUpdatedTarjeta = pstmtUpdateTarjeta.executeUpdate();

                    if (rowsUpdatedCliente > 0 && rowsUpdatedTarjeta > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Información actualizada exitosamente.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la información.");
                    }
                } else {
                    // Si no hay fecha de expiración, actualizar solo los datos del cliente
                    if (rowsUpdatedCliente > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Información actualizada exitosamente.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la información.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al actualizar la información: " + e.getMessage());
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
                backServicio();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Servicio.");
            }
        });

        btnEliminar.setOnAction(event -> {
            String idClienteStr = idClienteField.getText();

            if (idClienteStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El ID del cliente es obligatorio.");
                return;
            }

            int idCliente;
            try {
                idCliente = Integer.parseInt(idClienteStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El ID del cliente debe ser un número entero.");
                return;
            }

            Connection conn = null;

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(DB_URL);

                String deleteComprasSQL = "DELETE FROM Compras WHERE ID_Cliente = ?";
                String deleteTarjetasSQL = "DELETE FROM Tarjetas WHERE ID_Cliente = ?";
                String deleteClienteSQL = "DELETE FROM Clientes WHERE ID = ?";

                try (PreparedStatement deleteComprasStmt = conn.prepareStatement(deleteComprasSQL);
                     PreparedStatement deleteTarjetasStmt = conn.prepareStatement(deleteTarjetasSQL);
                     PreparedStatement deleteClienteStmt = conn.prepareStatement(deleteClienteSQL)) {

                    deleteComprasStmt.setInt(1, idCliente);
                    deleteTarjetasStmt.setInt(1, idCliente);
                    deleteClienteStmt.setInt(1, idCliente);

                    conn.setAutoCommit(false); // Iniciar transacción

                    deleteComprasStmt.executeUpdate();
                    deleteTarjetasStmt.executeUpdate();
                    deleteClienteStmt.executeUpdate();

                    conn.commit(); // Confirmar transacción

                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado exitosamente.");
                } catch (Exception e) {
                    if (conn != null) {
                        conn.rollback(); // Revertir transacción en caso de error
                    }
                    throw e;
                }

                idClienteField.clear();
                nombreField.clear();
                apellidoField.clear();
                telefonoField.clear();
                direccionField.clear();
                fechaExpiracionField.clear();
                saldoField.clear();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al eliminar el cliente: " + e.getMessage());
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

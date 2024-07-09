package org.example.parcial_final;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CompraController {

    @FXML
    public Button btnRegresar;
    @FXML
    public Button btnCalcular;
    @FXML
    public Button btnRealizarCompra;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidoField;
    @FXML
    private TextField productoField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField cantidadField;
    @FXML
    private DatePicker fechaCompraPicker;
    @FXML
    private ChoiceBox<String> tipoTarjetaChoiceBox;
    @FXML
    private Label totalLabel;

    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    private double totalPagar;

    @FXML
    public void initialize() {
        tipoTarjetaChoiceBox.getItems().addAll("Crédito", "Débito");

        btnRegresar.setOnAction(event -> {
            try {
                backCliente();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Cliente.");
            }
        });
        btnCalcular.setOnAction(event -> {
            String precioStr = precioField.getText();
            String cantidadStr = cantidadField.getText();

            if (precioStr.isEmpty() || cantidadStr.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Precio y Cantidad son obligatorios.");
                return;
            }

            double precio;
            int cantidad;
            try {
                precio = Double.parseDouble(precioStr);
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "Precio debe ser un número y Cantidad un entero.");
                return;
            }

            totalPagar = precio * cantidad;
            totalLabel.setText("Total a pagar: $" + totalPagar);
        });

        btnRealizarCompra.setOnAction(event -> {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String producto = productoField.getText();
            String precioStr = precioField.getText();
            String cantidadStr = cantidadField.getText();
            LocalDate fechaCompra = fechaCompraPicker.getValue();
            String tipoTarjeta = tipoTarjetaChoiceBox.getValue();

            if (nombre.isEmpty() || apellido.isEmpty() || producto.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty() || fechaCompra == null || tipoTarjeta == null) {
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios.");
                return;
            }

            double precio;
            int cantidad;
            try {
                precio = Double.parseDouble(precioStr);
                cantidad = Integer.parseInt(cantidadStr);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "Precio debe ser un número y Cantidad un entero.");
                return;
            }

            Connection conn = null;
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(DB_URL);

                String sqlCliente = "SELECT ID FROM Clientes WHERE NombreCompleto = ? AND Apellidos = ?";
                PreparedStatement pstmtCliente = conn.prepareStatement(sqlCliente);
                pstmtCliente.setString(1, nombre);
                pstmtCliente.setString(2, apellido);
                ResultSet rsCliente = pstmtCliente.executeQuery();

                if (rsCliente.next()) {
                    int idCliente = rsCliente.getInt("ID");

                    String sqlTarjeta = "SELECT NumeroTarjeta, Saldo FROM Tarjetas WHERE ID_Cliente = ? AND TipoTarjeta = ?";
                    PreparedStatement pstmtTarjeta = conn.prepareStatement(sqlTarjeta);
                    pstmtTarjeta.setInt(1, idCliente);
                    pstmtTarjeta.setString(2, tipoTarjeta);
                    ResultSet rsTarjeta = pstmtTarjeta.executeQuery();

                    if (rsTarjeta.next()) {
                        String numeroTarjeta = rsTarjeta.getString("NumeroTarjeta");
                        double saldo = rsTarjeta.getDouble("Saldo");

                        if (saldo >= totalPagar) {
                            saldo -= totalPagar;

                            String sqlUpdateTarjeta = "UPDATE Tarjetas SET Saldo = ? WHERE NumeroTarjeta = ?";
                            PreparedStatement pstmtUpdateTarjeta = conn.prepareStatement(sqlUpdateTarjeta);
                            pstmtUpdateTarjeta.setDouble(1, saldo);
                            pstmtUpdateTarjeta.setString(2, numeroTarjeta);
                            pstmtUpdateTarjeta.executeUpdate();

                            String sqlInsertCompra = "INSERT INTO Compras (ID_Cliente, NumeroTarjeta, FechaCompra, MontoTotal, DescripcionCompra) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement pstmtInsertCompra = conn.prepareStatement(sqlInsertCompra);
                            pstmtInsertCompra.setInt(1, idCliente);
                            pstmtInsertCompra.setString(2, numeroTarjeta);
                            pstmtInsertCompra.setString(3, fechaCompra.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                            pstmtInsertCompra.setDouble(4, totalPagar);
                            pstmtInsertCompra.setString(5, producto + " x" + cantidad);
                            pstmtInsertCompra.executeUpdate();

                            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Compra realizada exitosamente.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Fondos insuficientes para realizar la compra.");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se encontró una tarjeta asociada al cliente.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se encontró un cliente con ese nombre.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al realizar la compra: " + e.getMessage());
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

    public void backCliente() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/cliente-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle("Registrar Cliente");
    }
}

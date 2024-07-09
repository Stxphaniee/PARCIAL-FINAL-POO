package org.example.parcial_final; // 00095322 Indica el paquete al que pertenece esta clase

import javafx.fxml.FXML; // 00095322 Importa la anotación FXML para inyección de elementos del FXML
import javafx.fxml.FXMLLoader; // 00095322 Importa FXMLLoader para cargar archivos FXML
import javafx.scene.Parent; // 00095322 Importa la clase Parent para definir un nodo raíz
import javafx.scene.Scene; // 00095322 Importa la clase Scene para definir una escena en la aplicación
import javafx.scene.control.Alert; // 00095322 Importa la clase Alert para mostrar cuadros de diálogo
import javafx.scene.control.Button; // 00095322 Importa la clase Button para botones en la interfaz
import javafx.scene.control.TextField; // 00095322 Importa la clase TextField para campos de texto
import javafx.stage.Stage; // 00095322 Importa la clase Stage para manejar ventanas en la aplicación

import java.io.IOException; // 00095322 Importa IOException para manejar errores de entrada/salida
import java.sql.Connection; // 00095322 Importa Connection para manejar conexiones a la base de datos
import java.sql.DriverManager; // 00095322 Importa DriverManager para manejar drivers de bases de datos
import java.sql.PreparedStatement; // 00095322 Importa PreparedStatement para ejecutar sentencias SQL precompiladas
import java.sql.ResultSet; // 00095322 Importa ResultSet para manejar resultados de consultas SQL

public class EditarInformacionController { // 00095322 Define la clase EditarInformacionController

    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    public Button btnRegresar; // 00095322 Campo para el botón de regresar
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    public Button btnBuscar; // 00095322 Campo para el botón de buscar
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    public Button btnActualizar; // 00095322 Campo para el botón de actualizar
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    public Button btnEliminar; // 00095322 Campo para el botón de eliminar
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField idClienteField; // 00095322 Campo para el TextField del ID del cliente
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField nombreField; // 00095322 Campo para el TextField del nombre del cliente
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField apellidoField; // 00095322 Campo para el TextField del apellido del cliente
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField telefonoField; // 00095322 Campo para el TextField del teléfono del cliente
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField direccionField; // 00095322 Campo para el TextField de la dirección del cliente
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField fechaExpiracionField; // 00095322 Campo para el TextField de la fecha de expiración de la tarjeta
    @FXML // 00095322 Anotación para inyectar el elemento desde el archivo FXML
    private TextField saldoField; // 00095322 Campo para el TextField del saldo de la tarjeta

    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true"; // 00095322 URL de conexión a la base de datos

    @FXML // 00095322 Método que se llama al inicializar el controlador
    private void initialize () {
        btnBuscar.setOnAction(event -> { // 00095322 Asigna la acción del botón buscar
            String idCliente = idClienteField.getText(); // 00095322 Obtiene el texto del campo ID del cliente

            if (idCliente.isEmpty()) { // 00095322 Verifica si el campo ID del cliente está vacío
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El campo ID del Cliente es obligatorio."); // 00095322 Muestra una alerta de error de validación
                return; // 00095322 Sale del método si hay errores de validación
            }

            Connection conn = null; // 00095322 Declara la variable de conexión
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00095322 Carga el driver de SQL Server
                conn = DriverManager.getConnection(DB_URL); // 00095322 Establece la conexión a la base de datos

                String sql = "SELECT c.NombreCompleto, c.Apellidos, c.Direccion, c.NumeroTelefono, t.FechaExpiracion, t.Saldo " +
                        "FROM Clientes c " +
                        "LEFT JOIN Tarjetas t ON c.ID = t.ID_Cliente " +
                        "WHERE c.ID = ?"; // 00095322 Define la consulta SQL
                PreparedStatement pstmt = conn.prepareStatement(sql); // 00095322 Prepara la consulta SQL
                pstmt.setInt(1, Integer.parseInt(idCliente)); // 00095322 Establece el parámetro ID del cliente
                ResultSet rs = pstmt.executeQuery(); // 00095322 Ejecuta la consulta y obtiene los resultados

                if (rs.next()) { // 00095322 Si hay resultados
                    nombreField.setText(rs.getString("NombreCompleto")); // 00095322 Establece el nombre del cliente en el campo de texto
                    apellidoField.setText(rs.getString("Apellidos")); // 00095322 Establece el apellido del cliente en el campo de texto
                    telefonoField.setText(rs.getString("NumeroTelefono")); // 00095322 Establece el teléfono del cliente en el campo de texto
                    direccionField.setText(rs.getString("Direccion")); // 00095322 Establece la dirección del cliente en el campo de texto
                    fechaExpiracionField.setText(rs.getString("FechaExpiracion")); // 00095322 Establece la fecha de expiración en el campo de texto
                    saldoField.setText(String.valueOf(rs.getDouble("Saldo"))); // 00095322 Establece el saldo en el campo de texto
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No se encontró información para el usuario."); // 00095322 Muestra una alerta si no se encuentra el cliente
                }
            } catch (Exception e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al buscar la información del usuario: " + e.getMessage()); // 00095322 Muestra una alerta de error
            } finally {
                if (conn != null) { // 00095322 Si la conexión no es nula
                    try {
                        conn.close(); // 00095322 Cierra la conexión a la base de datos
                    } catch (Exception e) {
                        e.printStackTrace(); // 00095322 Imprime el error en la consola
                    }
                }
            }
        });

        btnActualizar.setOnAction(event -> { // 00095322 Asigna la acción del botón actualizar
            String idCliente = idClienteField.getText(); // 00095322 Obtiene el texto del campo ID del cliente
            String nombre = nombreField.getText(); // 00095322 Obtiene el texto del campo nombre
            String apellido = apellidoField.getText(); // 00095322 Obtiene el texto del campo apellido
            String telefono = telefonoField.getText(); // 00095322 Obtiene el texto del campo teléfono
            String direccion = direccionField.getText(); // 00095322 Obtiene el texto del campo dirección
            String fechaExpiracion = fechaExpiracionField.getText(); // 00095322 Obtiene el texto del campo fecha de expiración
            String saldoStr = saldoField.getText(); // 00095322 Obtiene el texto del campo saldo

            if (idCliente.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() ||
                    direccion.isEmpty() || saldoStr.isEmpty()) { // 00095322 Verifica si los campos están vacíos
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos son obligatorios."); // 00095322 Muestra una alerta de error de validación
                return; // 00095322 Sale del método si hay errores de validación
            }

            double saldo; // 00095322 Declara una variable para el saldo
            try {
                saldo = Double.parseDouble(saldoStr); // 00095322 Intenta convertir el texto del saldo a un número
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El saldo debe ser un número válido."); // 00095322 Muestra una alerta si el saldo no es un número válido
                return; // 00095322 Sale del método si hay un error de formato
            }

            Connection conn = null; // 00095322 Declara la variable de conexión
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00095322 Carga el driver de SQL Server
                conn = DriverManager.getConnection(DB_URL); // 00095322 Establece la conexión a la base de datos

                // Actualizar información del cliente
                String sqlUpdateCliente = "UPDATE Clientes SET NombreCompleto = ?, Apellidos = ?, Direccion = ?, NumeroTelefono = ? WHERE ID = ?"; // 00095322 Define la consulta SQL para actualizar el cliente
                PreparedStatement pstmtUpdateCliente = conn.prepareStatement(sqlUpdateCliente); // 00095322 Prepara la consulta SQL
                pstmtUpdateCliente.setString(1, nombre); // 00095322 Establece el nombre del cliente
                pstmtUpdateCliente.setString(2, apellido); // 00095322 Establece el apellido del cliente
                pstmtUpdateCliente.setString(3, direccion); // 00095322 Establece la dirección del cliente
                pstmtUpdateCliente.setString(4, telefono); // 00095322 Establece el teléfono del cliente
                pstmtUpdateCliente.setInt(5, Integer.parseInt(idCliente)); // 00095322 Establece el ID del cliente

                int rowsUpdatedCliente = pstmtUpdateCliente.executeUpdate(); // 00095322 Ejecuta la actualización y obtiene el número de filas actualizadas

                // Actualizar información de la tarjeta (si existe fecha de expiración)
                if (!fechaExpiracion.isEmpty()) { // 00095322 Si la fecha de expiración no está vacía
                    String sqlUpdateTarjeta = "UPDATE Tarjetas SET FechaExpiracion = ?, Saldo = ? WHERE ID_Cliente = ?"; // 00095322 Define la consulta SQL para actualizar la tarjeta
                    PreparedStatement pstmtUpdateTarjeta = conn.prepareStatement(sqlUpdateTarjeta); // 00095322 Prepara la consulta SQL
                    pstmtUpdateTarjeta.setString(1, fechaExpiracion); // 00095322 Establece la fecha de expiración
                    pstmtUpdateTarjeta.setDouble(2, saldo); // 00095322 Establece el saldo
                    pstmtUpdateTarjeta.setInt(3, Integer.parseInt(idCliente)); // 00095322 Establece el ID del cliente

                    int rowsUpdatedTarjeta = pstmtUpdateTarjeta.executeUpdate(); // 00095322 Ejecuta la actualización y obtiene el número de filas actualizadas

                    if (rowsUpdatedCliente > 0 && rowsUpdatedTarjeta > 0) { // 00095322 Si se actualizaron filas tanto del cliente como de la tarjeta
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Información actualizada exitosamente."); // 00095322 Muestra una alerta de éxito
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la información."); // 00095322 Muestra una alerta de error
                    }
                } else { // 00095322 Si no hay fecha de expiración, actualizar solo los datos del cliente
                    if (rowsUpdatedCliente > 0) { // 00095322 Si se actualizaron filas del cliente
                        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Información actualizada exitosamente."); // 00095322 Muestra una alerta de éxito
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar la información."); // 00095322 Muestra una alerta de error
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al actualizar la información: " + e.getMessage()); // 00095322 Muestra una alerta de error
            } finally {
                if (conn != null) { // 00095322 Si la conexión no es nula
                    try {
                        conn.close(); // 00095322 Cierra la conexión a la base de datos
                    } catch (Exception e) {
                        e.printStackTrace(); // 00095322 Imprime el error en la consola
                    }
                }
            }
        });

        btnRegresar.setOnAction(event -> { // 00095322 Asigna la acción del botón regresar
            try {
                backServicio(); // 00095322 Llama al método backServicio
            } catch (IOException e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al abrir la ventana de Servicio."); // 00095322 Muestra una alerta de error
            }
        });

        btnEliminar.setOnAction(event -> { // 00095322 Asigna la acción del botón eliminar
            String idClienteStr = idClienteField.getText(); // 00095322 Obtiene el texto del campo ID del cliente

            if (idClienteStr.isEmpty()) { // 00095322 Verifica si el campo ID del cliente está vacío
                showAlert(Alert.AlertType.ERROR, "Error de Validación", "El ID del cliente es obligatorio."); // 00095322 Muestra una alerta de error de validación
                return; // 00095322 Sale del método si hay errores de validación
            }

            int idCliente; // 00095322 Declara una variable para el ID del cliente
            try {
                idCliente = Integer.parseInt(idClienteStr); // 00095322 Intenta convertir el texto del ID del cliente a un número entero
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error de Formato", "El ID del cliente debe ser un número entero."); // 00095322 Muestra una alerta si el ID del cliente no es un número entero
                return; // 00095322 Sale del método si hay un error de formato
            }

            Connection conn = null; // 00095322 Declara la variable de conexión

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00095322 Carga el driver de SQL Server
                conn = DriverManager.getConnection(DB_URL); // 00095322 Establece la conexión a la base de datos

                String deleteComprasSQL = "DELETE FROM Compras WHERE ID_Cliente = ?"; // 00095322 Define la consulta SQL para eliminar compras
                String deleteTarjetasSQL = "DELETE FROM Tarjetas WHERE ID_Cliente = ?"; // 00095322 Define la consulta SQL para eliminar tarjetas
                String deleteClienteSQL = "DELETE FROM Clientes WHERE ID = ?"; // 00095322 Define la consulta SQL para eliminar el cliente

                try (PreparedStatement deleteComprasStmt = conn.prepareStatement(deleteComprasSQL);
                     PreparedStatement deleteTarjetasStmt = conn.prepareStatement(deleteTarjetasSQL);
                     PreparedStatement deleteClienteStmt = conn.prepareStatement(deleteClienteSQL)) { // 00095322 Prepara las consultas SQL

                    deleteComprasStmt.setInt(1, idCliente); // 00095322 Establece el ID del cliente para eliminar compras
                    deleteTarjetasStmt.setInt(1, idCliente); // 00095322 Establece el ID del cliente para eliminar tarjetas
                    deleteClienteStmt.setInt(1, idCliente); // 00095322 Establece el ID del cliente para eliminar el cliente

                    conn.setAutoCommit(false); // 00095322 Inicia una transacción

                    deleteComprasStmt.executeUpdate(); // 00095322 Ejecuta la eliminación de compras
                    deleteTarjetasStmt.executeUpdate(); // 00095322 Ejecuta la eliminación de tarjetas
                    deleteClienteStmt.executeUpdate(); // 00095322 Ejecuta la eliminación del cliente

                    conn.commit(); // 00095322 Confirma la transacción

                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cliente eliminado exitosamente."); // 00095322 Muestra una alerta de éxito
                } catch (Exception e) {
                    if (conn != null) {
                        conn.rollback(); // 00095322 Revierte la transacción en caso de error
                    }
                    throw e; // 00095322 Lanza la excepción para manejarla más arriba
                }

                idClienteField.clear(); // 00095322 Limpia el campo ID del cliente
                nombreField.clear(); // 00095322 Limpia el campo nombre del cliente
                apellidoField.clear(); // 00095322 Limpia el campo apellido del cliente
                telefonoField.clear(); // 00095322 Limpia el campo teléfono del cliente
                direccionField.clear(); // 00095322 Limpia el campo dirección del cliente
                fechaExpiracionField.clear(); // 00095322 Limpia el campo fecha de expiración
                saldoField.clear(); // 00095322 Limpia el campo saldo

            } catch (Exception e) {
                e.printStackTrace(); // 00095322 Imprime el error en la consola
                showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error al eliminar el cliente: " + e.getMessage()); // 00095322 Muestra una alerta de error
            } finally {
                if (conn != null) { // 00095322 Si la conexión no es nula
                    try {
                        conn.close(); // 00095322 Cierra la conexión a la base de datos
                    } catch (Exception e) {
                        e.printStackTrace(); // 00095322 Imprime el error en la consola
                    }
                }
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

    public void backServicio() throws IOException { // 00095322 Método para regresar a la vista de servicio
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/parcial_final/servicio-view.fxml")); // 00095322 Carga el archivo FXML de servicio
        Parent root = fxmlLoader.load(); // 00095322 Carga el nodo raíz del archivo FXML
        Stage stage = (Stage) btnRegresar.getScene().getWindow(); // 00095322 Obtiene la ventana actual
        stage.setScene(new Scene(root, 1050, 600)); // 00095322 Establece la nueva escena
        stage.setTitle("Servicio"); // 00095322 Establece el título de la ventana
    }
}

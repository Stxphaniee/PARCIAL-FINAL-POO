package org.example.parcial_final;

import java.sql.Connection; // 00227722 Importa la clase Connection para manejar conexiones a la base de datos
import java.sql.DriverManager; // 00227722 Importa la clase DriverManager para gestionar controladores JDBC
import java.sql.PreparedStatement; // 00227722 Importa la clase PreparedStatement para manejar sentencias SQL preparadas
import java.sql.ResultSet; // 00227722 Importa la clase ResultSet para manejar los resultados de las consultas SQL
import java.util.Random; // 00227722 Importa la clase Random para generar números aleatorios

public class Tarjeta {
    private String tipoTarjeta; // 00227722 Tipo de tarjeta (Crédito/Débito)
    private double saldo; // 00227722 Saldo de la tarjeta
    private String numeroTarjeta; // 00227722 Número de la tarjeta
    private int idCliente; // 00227722 ID del cliente al que pertenece la tarjeta
    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true"; // 00227722 URL de la base de datos

    public Tarjeta(int idCliente) { // 00227722 Constructor que inicializa el ID del cliente
        this.idCliente = idCliente;
    }

    public void seleccionarTarjeta(String tipoTarjeta, double saldoInicial) { // 00227722 Seleccionar el tipo de tarjeta y el saldo inicial
        this.tipoTarjeta = tipoTarjeta;
        this.saldo = saldoInicial;
        this.numeroTarjeta = generarNumeroTarjeta(); // 00227722 Generar el número de la tarjeta
        guardarEnBaseDeDatos(); // 00227722 Guardar la tarjeta en la base de datos
    }

    private String generarNumeroTarjeta() { // 00227722 Generar un número de tarjeta aleatorio
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) { // 00227722 Generar 16 dígitos
            sb.append(random.nextInt(10)); // 00227722 Añadir un dígito aleatorio
            if ((i + 1) % 4 == 0 && i < 15) { // 00227722 Añadir un espacio cada 4 dígitos
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public double getSaldo() { // 00227722 Obtener el saldo de la tarjeta
        return saldo;
    }

    public void setSaldo(double saldo) { // 00227722 Establecer un nuevo saldo para la tarjeta
        this.saldo = saldo;
        actualizarSaldoEnBaseDeDatos(saldo); // 00227722 Actualizar el saldo en la base de datos
    }

    private void guardarEnBaseDeDatos() { // 00227722 Guardar la tarjeta en la base de datos
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00227722 Registrar el controlador JDBC
            conn = DriverManager.getConnection(DB_URL); // 00227722 Establecer la conexión con la base de datos

            String sqlInsert = "INSERT INTO Tarjetas (NumeroTarjeta, ID_Cliente, FechaExpiracion, TipoTarjeta, Facilitador, Saldo) VALUES (?, ?, GETDATE(), ?, 'Visa', ?)"; // 00227722 Consulta para insertar la tarjeta
            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert); // 00227722 Preparar la consulta
            pstmtInsert.setString(1, numeroTarjeta); // 00227722 Establecer el número de la tarjeta
            pstmtInsert.setInt(2, idCliente); // 00227722 Establecer el ID del cliente
            pstmtInsert.setString(3, tipoTarjeta); // 00227722 Establecer el tipo de tarjeta
            pstmtInsert.setDouble(4, saldo); // 00227722 Establecer el saldo de la tarjeta

            int rowsInserted = pstmtInsert.executeUpdate(); // 00227722 Ejecutar la inserción
            if (rowsInserted > 0) { // 00227722 Verificar si la inserción fue exitosa
                System.out.println("Tarjeta añadida exitosamente."); // 00227722 Mensaje de éxito
            } else {
                System.out.println("No se pudo añadir la tarjeta."); // 00227722 Mensaje de error
            }

        } catch (Exception e) {
            e.printStackTrace(); // 00227722 Imprimir el stack trace del error
        } finally {
            if (conn != null) { // 00227722 Verificar si la conexión no es nula
                try {
                    conn.close(); // 00227722 Cerrar la conexión con la base de datos
                } catch (Exception e) {
                    e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                }
            }
        }
    }

    private void actualizarSaldoEnBaseDeDatos(double nuevoSaldo) { // 00227722 Actualizar el saldo de la tarjeta en la base de datos
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00227722 Registrar el controlador JDBC
            conn = DriverManager.getConnection(DB_URL); // 00227722 Establecer la conexión con la base de datos

            String sqlUpdate = "UPDATE Tarjetas SET Saldo = ? WHERE NumeroTarjeta = ?"; // 00227722 Consulta para actualizar el saldo de la tarjeta
            PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate); // 00227722 Preparar la consulta
            pstmtUpdate.setDouble(1, nuevoSaldo); // 00227722 Establecer el nuevo saldo
            pstmtUpdate.setString(2, numeroTarjeta); // 00227722 Establecer el número de la tarjeta

            int rowsUpdated = pstmtUpdate.executeUpdate(); // 00227722 Ejecutar la actualización
            if (rowsUpdated > 0) { // 00227722 Verificar si la actualización fue exitosa
                System.out.println("Saldo actualizado exitosamente."); // 00227722 Mensaje de éxito
            } else {
                System.out.println("No se pudo actualizar el saldo."); // 00227722 Mensaje de error
            }

        } catch (Exception e) {
            e.printStackTrace(); // 00227722 Imprimir el stack trace del error
        } finally {
            if (conn != null) { // 00227722 Verificar si la conexión no es nula
                try {
                    conn.close(); // 00227722 Cerrar la conexión con la base de datos
                } catch (Exception e) {
                    e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                }
            }
        }
    }

    public String getNumeroTarjeta() { // 00227722 Obtener el número de la tarjeta
        return numeroTarjeta;
    }

    public void cargarSaldoDesdeBaseDeDatos() { // 00227722 Cargar el saldo de la tarjeta desde la base de datos
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00227722 Registrar el controlador JDBC
            conn = DriverManager.getConnection(DB_URL); // 00227722 Establecer la conexión con la base de datos

            String sqlSelect = "SELECT Saldo FROM Tarjetas WHERE NumeroTarjeta = ?"; // 00227722 Consulta para obtener el saldo de la tarjeta
            PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect); // 00227722 Preparar la consulta
            pstmtSelect.setString(1, numeroTarjeta); // 00227722 Establecer el número de la tarjeta
            ResultSet rs = pstmtSelect.executeQuery(); // 00227722 Ejecutar la consulta

            if (rs.next()) { // 00227722 Verificar si se encontró el saldo
                saldo = rs.getDouble("Saldo"); // 00227722 Asignar el saldo obtenido
            }

        } catch (Exception e) {
            e.printStackTrace(); // 00227722 Imprimir el stack trace del error
        } finally {
            if (conn != null) { // 00227722 Verificar si la conexión no es nula
                try {
                    conn.close(); // 00227722 Cerrar la conexión con la base de datos
                } catch (Exception e) {
                    e.printStackTrace(); // 00227722 Imprimir el stack trace del error
                }
            }
        }
    }
}

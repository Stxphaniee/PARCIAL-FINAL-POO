package org.example.parcial_final;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class Tarjeta {
    private String tipoTarjeta;
    private double saldo;
    private String numeroTarjeta;
    private int idCliente;
    private static final String DB_URL = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    public Tarjeta(int idCliente) {
        this.idCliente = idCliente;
    }

    public void seleccionarTarjeta(String tipoTarjeta, double saldoInicial) {
        this.tipoTarjeta = tipoTarjeta;
        this.saldo = saldoInicial;
        this.numeroTarjeta = generarNumeroTarjeta();
        guardarEnBaseDeDatos();
    }

    private String generarNumeroTarjeta() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
            if ((i + 1) % 4 == 0 && i < 15) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
        actualizarSaldoEnBaseDeDatos(saldo);
    }

    private void guardarEnBaseDeDatos() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(DB_URL);

            String sqlInsert = "INSERT INTO Tarjetas (NumeroTarjeta, ID_Cliente, FechaExpiracion, TipoTarjeta, Facilitador, Saldo) VALUES (?, ?, GETDATE(), ?, 'Visa', ?)";
            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setString(1, numeroTarjeta);
            pstmtInsert.setInt(2, idCliente);
            pstmtInsert.setString(3, tipoTarjeta);
            pstmtInsert.setDouble(4, saldo);

            int rowsInserted = pstmtInsert.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Tarjeta añadida exitosamente.");
            } else {
                System.out.println("No se pudo añadir la tarjeta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    private void actualizarSaldoEnBaseDeDatos(double nuevoSaldo) {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(DB_URL);

            String sqlUpdate = "UPDATE Tarjetas SET Saldo = ? WHERE NumeroTarjeta = ?";
            PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
            pstmtUpdate.setDouble(1, nuevoSaldo);
            pstmtUpdate.setString(2, numeroTarjeta);

            int rowsUpdated = pstmtUpdate.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Saldo actualizado exitosamente.");
            } else {
                System.out.println("No se pudo actualizar el saldo.");
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void cargarSaldoDesdeBaseDeDatos() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(DB_URL);

            String sqlSelect = "SELECT Saldo FROM Tarjetas WHERE NumeroTarjeta = ?";
            PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
            pstmtSelect.setString(1, numeroTarjeta);
            ResultSet rs = pstmtSelect.executeQuery();

            if (rs.next()) {
                saldo = rs.getDouble("Saldo");
            }

        } catch (Exception e) {
            e.printStackTrace();
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
}

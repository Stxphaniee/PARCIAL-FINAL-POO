package org.example.parcial_final;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {

    private Connection getConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://DESKTOP-RHFQOU4:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
        return DriverManager.getConnection(url);
    }

    public List<Compra> getComprasPorClienteYPeriodo(int clienteId, Date inicio, Date fin) throws Exception {
        // Ajustar las fechas para incluir todo el día
        LocalDateTime inicioDateTime = inicio.toLocalDate().atStartOfDay();
        LocalDateTime finDateTime = fin.toLocalDate().atTime(23, 59, 59);

        String sql = "SELECT * FROM Compras WHERE ID_Cliente = ? AND FechaCompra BETWEEN ? AND ?";
        List<Compra> compras = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setTimestamp(2, Timestamp.valueOf(inicioDateTime));
            pstmt.setTimestamp(3, Timestamp.valueOf(finDateTime));
            System.out.println("Consulta SQL: " + pstmt.toString());
            System.out.println("Parámetros: clienteId=" + clienteId + ", inicio=" + inicioDateTime + ", fin=" + finDateTime);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Compra compra = new Compra(
                        rs.getInt("ID"),
                        rs.getInt("ID_Cliente"),
                        rs.getString("NumeroTarjeta"),
                        rs.getTimestamp("FechaCompra").toLocalDateTime(),
                        rs.getBigDecimal("MontoTotal"),
                        rs.getString("DescripcionCompra")
                );
                compras.add(compra);
            }
        }
        return compras;
    }
    private List<ClienteCompra> getClienteComprasPorFacilitador(String facilitador) throws Exception {
        String sql = "SELECT c.NombreCompleto, COUNT(co.ID) AS CantidadCompras, SUM(co.MontoTotal) AS TotalGastado " +
                "FROM Clientes c " +
                "JOIN Tarjetas t ON c.ID = t.ID_Cliente " +
                "JOIN Compras co ON t.NumeroTarjeta = co.NumeroTarjeta " +
                "WHERE t.Facilitador = ? " +
                "GROUP BY c.NombreCompleto";
        List<ClienteCompra> clienteCompras = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, facilitador);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ClienteCompra clienteCompra = new ClienteCompra(
                        rs.getString("NombreCompleto"),
                        rs.getInt("CantidadCompras"),
                        rs.getBigDecimal("TotalGastado")
                );
                clienteCompras.add(clienteCompra);
            }
        }
        return clienteCompras;
    }

    public void guardarReporteA(int clienteId, Date inicio, Date fin) throws Exception {
        System.out.println("Generando reporte para cliente " + clienteId + " desde " + inicio + " hasta " + fin);
        List<Compra> compras = getComprasPorClienteYPeriodo(clienteId, inicio, fin);

        System.out.println("Compras recuperadas: " + compras.size());
        for (Compra compra : compras) {
            System.out.println("Compra: " + compra);
        }

        String directoryName = "Reportes";
        Files.createDirectories(Paths.get(directoryName));
        String fileName = directoryName + "/Reporte_A_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) {
            writer.write("Reporte A - Compras realizadas por el cliente " + clienteId + " desde " + inicio + " hasta " + fin + "\n");
            writer.write("===============================================================\n");
            if (compras.isEmpty()) {
                writer.write("No se encontraron compras en el periodo especificado.");
            } else {
                for (Compra compra : compras) {
                    writer.write("Fecha: " + compra.getFechaCompra() + ", Monto: $" + compra.getMontoTotal() + ", Descripción: " + compra.getDescripcionCompra() + "\n");
                }
            }
        }
    }


    public List<Tarjeta> getTarjetasPorCliente(int clienteId) throws Exception {
        String sql = "SELECT * FROM Tarjetas WHERE ID_Cliente = ?";
        List<Tarjeta> tarjetas = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Tarjeta tarjeta = new Tarjeta(
                        rs.getString("NumeroTarjeta"),
                        rs.getInt("ID_Cliente"),
                        rs.getDate("FechaExpiracion"),
                        rs.getString("TipoTarjeta"),
                        rs.getString("Facilitador")
                );
                tarjetas.add(tarjeta);
            }
        }
        return tarjetas;
    }

    public BigDecimal getTotalGastadoPorClienteEnMes(int clienteId, int mes, int anio) throws Exception {
        String sql = "SELECT SUM(MontoTotal) as TotalGastado FROM Compras WHERE ID_Cliente = ? AND MONTH(FechaCompra) = ? AND YEAR(FechaCompra) = ?";
        BigDecimal totalGastado = BigDecimal.ZERO;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setInt(2, mes);
            pstmt.setInt(3, anio);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalGastado = rs.getBigDecimal("TotalGastado");
            }
        }
        return totalGastado;
    }

    public void guardarReporteB(int clienteId, int mes, int anio) throws Exception {
        BigDecimal totalGastado = getTotalGastadoPorClienteEnMes(clienteId, mes, anio);

        String directoryName = "Reportes";
        Files.createDirectories(Paths.get(directoryName));
        String fileName = directoryName + "/Reporte_B_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) {
            writer.write("Reporte B - Total gastado por el cliente " + clienteId + " en " + mes + "/" + anio + "\n");
            writer.write("===============================================================\n");
            writer.write("Total gastado: $" + totalGastado + "\n");
        }
    }
    public void guardarReporteC(int clienteId) throws Exception {
        List<Tarjeta> tarjetas = getTarjetasPorCliente(clienteId);
        String directoryName = "Reportes";
        Files.createDirectories(Paths.get(directoryName));
        String fileName = directoryName + "/Reporte_C_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) {
            writer.write("Tarjetas de crédito:\n");
            boolean hasCredito = false;
            for (Tarjeta tarjeta : tarjetas) {
                if (tarjeta.getTipoTarjeta().equalsIgnoreCase("Crédito")) {
                    writer.write(censurarNumeroTarjeta(tarjeta.getNumeroTarjeta()) + "\n");
                    hasCredito = true;
                }
            }
            if (!hasCredito) {
                writer.write("N/A\n");
            }

            writer.write("Tarjetas de Débito:\n");
            boolean hasDebito = false;
            for (Tarjeta tarjeta : tarjetas) {
                if (tarjeta.getTipoTarjeta().equalsIgnoreCase("Débito")) {
                    writer.write(censurarNumeroTarjeta(tarjeta.getNumeroTarjeta()) + "\n");
                    hasDebito = true;
                }
            }
            if (!hasDebito) {
                writer.write("N/A\n");
            }
        }
    }

    public void guardarReporteD(String facilitador) throws Exception {
        System.out.println("Generando reporte para facilitador " + facilitador);
        List<ClienteCompra> clienteCompras = getClienteComprasPorFacilitador(facilitador);

        System.out.println("Clientes recuperados: " + clienteCompras.size());
        for (ClienteCompra clienteCompra : clienteCompras) {
            System.out.println("ClienteCompra: " + clienteCompra);
        }

        String directoryName = "Reportes";
        Files.createDirectories(Paths.get(directoryName));
        String fileName = directoryName + "/Reporte_D_" + System.currentTimeMillis() + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) {
            writer.write("Reporte D - Clientes que han realizado compras con el facilitador " + facilitador + "\n");
            writer.write("==================================================================\n");
            if (clienteCompras.isEmpty()) {
                writer.write("No se encontraron clientes que hayan realizado compras con el facilitador especificado.");
            } else {
                for (ClienteCompra clienteCompra : clienteCompras) {
                    writer.write("Cliente: " + clienteCompra.getNombreCompleto() + ", Compras: " + clienteCompra.getCantidadCompras() + ", Total Gastado: $" + clienteCompra.getTotalGastado() + "\n");
                }
            }
        }
    }

    private String censurarNumeroTarjeta(String numeroTarjeta) {
        return "XXXX XXXX XXXX " + numeroTarjeta.substring(12);
    }

    // Clase interna Tarjeta
    public static class Tarjeta {
        private String numeroTarjeta;
        private int idCliente;
        private Date fechaExpiracion;
        private String tipoTarjeta;
        private String facilitador;

        public Tarjeta(String numeroTarjeta, int idCliente, Date fechaExpiracion, String tipoTarjeta, String facilitador) {
            this.numeroTarjeta = numeroTarjeta;
            this.idCliente = idCliente;
            this.fechaExpiracion = fechaExpiracion;
            this.tipoTarjeta = tipoTarjeta;
            this.facilitador = facilitador;
        }

        public String getNumeroTarjeta() {
            return numeroTarjeta;
        }

        public int getIdCliente() {
            return idCliente;
        }

        public Date getFechaExpiracion() {
            return fechaExpiracion;
        }

        public String getTipoTarjeta() {
            return tipoTarjeta;
        }

        public String getFacilitador() {
            return facilitador;
        }
    }

    // Clase interna ClienteCompra
    public static class ClienteCompra {
        private String nombreCompleto;
        private int cantidadCompras;
        private BigDecimal totalGastado;

        public ClienteCompra(String nombreCompleto, int cantidadCompras, BigDecimal totalGastado) {
            this.nombreCompleto = nombreCompleto;
            this.cantidadCompras = cantidadCompras;
            this.totalGastado = totalGastado;
        }

        public String getNombreCompleto() {
            return nombreCompleto;
        }

        public int getCantidadCompras() {
            return cantidadCompras;
        }

        public BigDecimal getTotalGastado() {
            return totalGastado;
        }

        @Override
        public String toString() {
            return "ClienteCompra{" +
                    "nombreCompleto='" + nombreCompleto + '\'' +
                    ", cantidadCompras=" + cantidadCompras +
                    ", totalGastado=" + totalGastado +
                    '}';
        }
    }

    // Clase interna Compra
    public static class Compra {
        private int id;
        private int idCliente;
        private String numeroTarjeta;
        private LocalDateTime fechaCompra;
        private BigDecimal montoTotal;
        private String descripcionCompra;

        public Compra(int id, int idCliente, String numeroTarjeta, LocalDateTime fechaCompra, BigDecimal montoTotal, String descripcionCompra) {
            this.id = id;
            this.idCliente = idCliente;
            this.numeroTarjeta = numeroTarjeta;
            this.fechaCompra = fechaCompra;
            this.montoTotal = montoTotal;
            this.descripcionCompra = descripcionCompra;
        }

        public int getId() {
            return id;
        }

        public int getIdCliente() {
            return idCliente;
        }

        public String getNumeroTarjeta() {
            return numeroTarjeta;
        }

        public LocalDateTime getFechaCompra() {
            return fechaCompra;
        }

        public BigDecimal getMontoTotal() {
            return montoTotal;
        }

        public String getDescripcionCompra() {
            return descripcionCompra;
        }

        @Override
        public String toString() {
            return "Compra{" +
                    "id=" + id +
                    ", idCliente=" + idCliente +
                    ", numeroTarjeta='" + numeroTarjeta + '\'' +
                    ", fechaCompra=" + fechaCompra +
                    ", montoTotal=" + montoTotal +
                    ", descripcionCompra='" + descripcionCompra + '\'' +
                    '}';
        }
    }
}

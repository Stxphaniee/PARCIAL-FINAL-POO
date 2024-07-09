package org.example.parcial_final;

import java.io.BufferedWriter; // 00100121 Importa la clase BufferedWriter para escribir archivos
import java.io.IOException; // 00100121 Importa la clase IOException para manejar excepciones de E/S
import java.math.BigDecimal; // 00100121 Importa la clase BigDecimal para manejar valores monetarios
import java.nio.file.Files; // 00100121 Importa la clase Files para manipular archivos
import java.nio.file.Paths; // 00100121 Importa la clase Paths para trabajar con rutas de archivos
import java.nio.file.StandardOpenOption; // 00100121 Importa la clase StandardOpenOption para opciones de apertura de archivos
import java.sql.Connection; // 00100121 Importa la clase Connection para manejar la conexión a la base de datos
import java.sql.Date; // 00100121 Importa la clase Date para trabajar con fechas
import java.sql.DriverManager; // 00100121 Importa la clase DriverManager para obtener la conexión a la base de datos
import java.sql.PreparedStatement; // 00100121 Importa la clase PreparedStatement para ejecutar consultas SQL
import java.sql.ResultSet; // 00100121 Importa la clase ResultSet para manejar los resultados de consultas SQL
import java.sql.Timestamp; // 00100121 Importa la clase Timestamp para manejar marcas de tiempo
import java.time.LocalDateTime; // 00100121 Importa la clase LocalDateTime para manejar fechas y horas
import java.util.ArrayList; // 00100121 Importa la clase ArrayList para manejar listas dinámicas
import java.util.List; // 00100121 Importa la clase List para manejar listas

public class ReporteService {

    private Connection getConnection() throws Exception { // 00100121 Método para obtener la conexión a la base de datos
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 00100121 Registra el controlador JDBC
        String url = "jdbc:sqlserver://DESKTOP-3PV9K0O:1433;databaseName=Parcial_Final_POO;integratedSecurity=true;encrypt=true;trustServerCertificate=true"; // 00100121 URL de conexión a la base de datos
        return DriverManager.getConnection(url); // 00100121 Obtiene la conexión a la base de datos
    }

    public List<Compra> getComprasPorClienteYPeriodo(int clienteId, Date inicio, Date fin) throws Exception {
        // 00100121 Método para obtener las compras de un cliente en un periodo específico
        LocalDateTime inicioDateTime = inicio.toLocalDate().atStartOfDay(); // 00100121 Convierte la fecha de inicio a LocalDateTime
        LocalDateTime finDateTime = fin.toLocalDate().atTime(23, 59, 59); // 00100121 Convierte la fecha de fin a LocalDateTime

        String sql = "SELECT * FROM Compras WHERE ID_Cliente = ? AND FechaCompra BETWEEN ? AND ?"; // 00100121 Consulta SQL para obtener las compras
        List<Compra> compras = new ArrayList<>(); // 00100121 Lista para almacenar las compras
        try (Connection conn = getConnection(); // 00100121 Obtiene la conexión a la base de datos
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 00100121 Prepara la consulta SQL
            pstmt.setInt(1, clienteId); // 00100121 Establece el ID del cliente en la consulta
            pstmt.setTimestamp(2, Timestamp.valueOf(inicioDateTime)); // 00100121 Establece la fecha de inicio en la consulta
            pstmt.setTimestamp(3, Timestamp.valueOf(finDateTime)); // 00100121 Establece la fecha de fin en la consulta
            System.out.println("Consulta SQL: " + pstmt.toString()); // 00100121 Imprime la consulta SQL para depuración
            System.out.println("Parámetros: clienteId=" + clienteId + ", inicio=" + inicioDateTime + ", fin=" + finDateTime); // 00100121 Imprime los parámetros de la consulta
            ResultSet rs = pstmt.executeQuery(); // 00100121 Ejecuta la consulta y obtiene el resultado
            while (rs.next()) { // 00100121 Recorre los resultados de la consulta
                Compra compra = new Compra( // 00100121 Crea un objeto Compra con los datos del resultado
                        rs.getInt("ID"), // 00100121 Obtiene el ID de la compra
                        rs.getInt("ID_Cliente"), // 00100121 Obtiene el ID del cliente
                        rs.getString("NumeroTarjeta"), // 00100121 Obtiene el número de tarjeta
                        rs.getTimestamp("FechaCompra").toLocalDateTime(), // 00100121 Obtiene la fecha de compra
                        rs.getBigDecimal("MontoTotal"), // 00100121 Obtiene el monto total de la compra
                        rs.getString("DescripcionCompra") // 00100121 Obtiene la descripción de la compra
                );
                compras.add(compra); // 00100121 Añade la compra a la lista
            }
        }
        return compras; // 00100121 Retorna la lista de compras
    }

    private List<ClienteCompra> getClienteComprasPorFacilitador(String facilitador) throws Exception {
        // 00100121 Método para obtener las compras de los clientes por facilitador
        String sql = "SELECT c.NombreCompleto, COUNT(co.ID) AS CantidadCompras, SUM(co.MontoTotal) AS TotalGastado " +
                "FROM Clientes c " +
                "JOIN Tarjetas t ON c.ID = t.ID_Cliente " +
                "JOIN Compras co ON t.NumeroTarjeta = co.NumeroTarjeta " +
                "WHERE t.Facilitador = ? " +
                "GROUP BY c.NombreCompleto"; // 00100121 Consulta SQL para obtener los datos de los clientes por facilitador
        List<ClienteCompra> clienteCompras = new ArrayList<>(); // 00100121 Lista para almacenar los datos de los clientes
        try (Connection conn = getConnection(); // 00100121 Obtiene la conexión a la base de datos
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 00100121 Prepara la consulta SQL
            pstmt.setString(1, facilitador); // 00100121 Establece el facilitador en la consulta
            ResultSet rs = pstmt.executeQuery(); // 00100121 Ejecuta la consulta y obtiene el resultado
            while (rs.next()) { // 00100121 Recorre los resultados de la consulta
                ClienteCompra clienteCompra = new ClienteCompra( // 00100121 Crea un objeto ClienteCompra con los datos del resultado
                        rs.getString("NombreCompleto"), // 00100121 Obtiene el nombre completo del cliente
                        rs.getInt("CantidadCompras"), // 00100121 Obtiene la cantidad de compras del cliente
                        rs.getBigDecimal("TotalGastado") // 00100121 Obtiene el total gastado por el cliente
                );
                clienteCompras.add(clienteCompra); // 00100121 Añade los datos del cliente a la lista
            }
        }
        return clienteCompras; // 00100121 Retorna la lista de clientes y sus datos
    }

    public void guardarReporteA(int clienteId, Date inicio, Date fin) throws Exception {
        // 00100121 Método para guardar el reporte A
        System.out.println("Generando reporte para cliente " + clienteId + " desde " + inicio + " hasta " + fin); // 00100121 Imprime mensaje de inicio de generación de reporte
        List<Compra> compras = getComprasPorClienteYPeriodo(clienteId, inicio, fin); // 00100121 Obtiene las compras del cliente en el periodo especificado

        System.out.println("Compras recuperadas: " + compras.size()); // 00100121 Imprime la cantidad de compras recuperadas
        for (Compra compra : compras) { // 00100121 Recorre las compras recuperadas
            System.out.println("Compra: " + compra); // 00100121 Imprime los datos de cada compra
        }

        String directoryName = "Reportes"; // 00100121 Nombre del directorio donde se guardará el reporte
        Files.createDirectories(Paths.get(directoryName)); // 00100121 Crea el directorio si no existe
        String fileName = directoryName + "/Reporte_A_" + System.currentTimeMillis() + ".txt"; // 00100121 Nombre del archivo de reporte
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) { // 00100121 Crea el archivo de reporte para escritura
            writer.write("Reporte A - Compras realizadas por el cliente " + clienteId + " desde " + inicio + " hasta " + fin + "\n"); // 00100121 Escribe la cabecera del reporte
            writer.write("===============================================================\n"); // 00100121 Escribe una línea separadora
            if (compras.isEmpty()) { // 00100121 Verifica si no hay compras
                writer.write("No se encontraron compras en el periodo especificado."); // 00100121 Escribe mensaje de que no hay compras
            } else {
                for (Compra compra : compras) { // 00100121 Recorre las compras y escribe sus datos en el reporte
                    writer.write("Fecha: " + compra.getFechaCompra() + ", Monto: $" + compra.getMontoTotal() + ", Descripción: " + compra.getDescripcionCompra() + "\n");
                    // 00100121 Escribe la fecha, monto y descripción de la compra
                }
            }
        }
    }

    public List<Tarjeta> getTarjetasPorCliente(int clienteId) throws Exception {
        // 00100121 Método para obtener las tarjetas de un cliente
        String sql = "SELECT * FROM Tarjetas WHERE ID_Cliente = ?"; // 00100121 Consulta SQL para obtener las tarjetas de un cliente
        List<Tarjeta> tarjetas = new ArrayList<>(); // 00100121 Lista para almacenar las tarjetas del cliente
        try (Connection conn = getConnection(); // 00100121 Obtiene la conexión a la base de datos
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 00100121 Prepara la consulta SQL
            pstmt.setInt(1, clienteId); // 00100121 Establece el ID del cliente en la consulta
            ResultSet rs = pstmt.executeQuery(); // 00100121 Ejecuta la consulta y obtiene el resultado
            while (rs.next()) { // 00100121 Recorre los resultados de la consulta
                Tarjeta tarjeta = new Tarjeta( // 00100121 Crea un objeto Tarjeta con los datos del resultado
                        rs.getString("NumeroTarjeta"), // 00100121 Obtiene el número de la tarjeta
                        rs.getInt("ID_Cliente"), // 00100121 Obtiene el ID del cliente
                        rs.getDate("FechaExpiracion"), // 00100121 Obtiene la fecha de expiración de la tarjeta
                        rs.getString("TipoTarjeta"), // 00100121 Obtiene el tipo de tarjeta
                        rs.getString("Facilitador") // 00100121 Obtiene el facilitador de la tarjeta
                );
                tarjetas.add(tarjeta); // 00100121 Añade la tarjeta a la lista
            }
        }
        return tarjetas; // 00100121 Retorna la lista de tarjetas del cliente
    }

    public BigDecimal getTotalGastadoPorClienteEnMes(int clienteId, int mes, int anio) throws Exception {
        // 00100121 Método para obtener el total gastado por un cliente en un mes específico
        String sql = "SELECT SUM(MontoTotal) as TotalGastado FROM Compras WHERE ID_Cliente = ? AND MONTH(FechaCompra) = ? AND YEAR(FechaCompra) = ?";
        // 00100121 Consulta SQL para obtener el total gastado por el cliente
        BigDecimal totalGastado = BigDecimal.ZERO; // 00100121 Inicializa el total gastado en cero

        try (Connection conn = getConnection(); // 00100121 Obtiene la conexión a la base de datos
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // 00100121 Prepara la consulta SQL
            pstmt.setInt(1, clienteId); // 00100121 Establece el ID del cliente en la consulta
            pstmt.setInt(2, mes); // 00100121 Establece el mes en la consulta
            pstmt.setInt(3, anio); // 00100121 Establece el año en la consulta
            ResultSet rs = pstmt.executeQuery(); // 00100121 Ejecuta la consulta y obtiene el resultado
            if (rs.next()) { // 00100121 Verifica si hay resultado
                totalGastado = rs.getBigDecimal("TotalGastado"); // 00100121 Obtiene el total gastado del resultado
            }
        }
        return totalGastado; // 00100121 Retorna el total gastado por el cliente
    }

    public void guardarReporteB(int clienteId, int mes, int anio) throws Exception {
        // 00100121 Método para guardar el reporte B
        BigDecimal totalGastado = getTotalGastadoPorClienteEnMes(clienteId, mes, anio); // 00100121 Obtiene el total gastado por el cliente en el mes y año especificados

        String directoryName = "Reportes"; // 00100121 Nombre del directorio donde se guardará el reporte
        Files.createDirectories(Paths.get(directoryName)); // 00100121 Crea el directorio si no existe
        String fileName = directoryName + "/Reporte_B_" + System.currentTimeMillis() + ".txt"; // 00100121 Nombre del archivo de reporte
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) { // 00100121 Crea el archivo de reporte para escritura
            writer.write("Reporte B - Total gastado por el cliente " + clienteId + " en " + mes + "/" + anio + "\n"); // 00100121 Escribe la cabecera del reporte
            writer.write("===============================================================\n"); // 00100121 Escribe una línea separadora
            writer.write("Total gastado: $" + totalGastado + "\n"); // 00100121 Escribe el total gastado en el reporte
        }
    }

    public void guardarReporteC(int clienteId) throws Exception {
        // 00100121 Método para guardar el reporte C
        List<Tarjeta> tarjetas = getTarjetasPorCliente(clienteId); // 00100121 Obtiene las tarjetas del cliente
        String directoryName = "Reportes"; // 00100121 Nombre del directorio donde se guardará el reporte
        Files.createDirectories(Paths.get(directoryName)); // 00100121 Crea el directorio si no existe
        String fileName = directoryName + "/Reporte_C_" + System.currentTimeMillis() + ".txt"; // 00100121 Nombre del archivo de reporte
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) { // 00100121 Crea el archivo de reporte para escritura
            writer.write("Tarjetas de crédito:\n"); // 00100121 Escribe la cabecera para tarjetas de crédito
            boolean hasCredito = false; // 00100121 Inicializa la bandera de tarjetas de crédito en falso
            for (Tarjeta tarjeta : tarjetas) { // 00100121 Recorre las tarjetas del cliente
                if (tarjeta.getTipoTarjeta().equalsIgnoreCase("Crédito")) { // 00100121 Verifica si la tarjeta es de crédito
                    writer.write(censurarNumeroTarjeta(tarjeta.getNumeroTarjeta()) + "\n"); // 00100121 Escribe el número de la tarjeta censurado
                    hasCredito = true; // 00100121 Establece la bandera de tarjetas de crédito en verdadero
                }
            }
            if (!hasCredito) { // 00100121 Verifica si no hay tarjetas de crédito
                writer.write("N/A\n"); // 00100121 Escribe N/A si no hay tarjetas de crédito
            }

            writer.write("Tarjetas de Débito:\n"); // 00100121 Escribe la cabecera para tarjetas de débito
            boolean hasDebito = false; // 00100121 Inicializa la bandera de tarjetas de débito en falso
            for (Tarjeta tarjeta : tarjetas) { // 00100121 Recorre las tarjetas del cliente
                if (tarjeta.getTipoTarjeta().equalsIgnoreCase("Débito")) { // 00100121 Verifica si la tarjeta es de débito
                    writer.write(censurarNumeroTarjeta(tarjeta.getNumeroTarjeta()) + "\n"); // 00100121 Escribe el número de la tarjeta censurado
                    hasDebito = true; // 00100121 Establece la bandera de tarjetas de débito en verdadero
                }
            }
            if (!hasDebito) { // 00100121 Verifica si no hay tarjetas de débito
                writer.write("N/A\n"); // 00100121 Escribe N/A si no hay tarjetas de débito
            }
        }
    }

    public void guardarReporteD(String facilitador) throws Exception {
        // 00100121 Método para guardar el reporte D
        System.out.println("Generando reporte para facilitador " + facilitador); // 00100121 Imprime mensaje de inicio de generación de reporte
        List<ClienteCompra> clienteCompras = getClienteComprasPorFacilitador(facilitador); // 00100121 Obtiene los clientes y sus compras por facilitador

        System.out.println("Clientes recuperados: " + clienteCompras.size()); // 00100121 Imprime la cantidad de clientes recuperados
        for (ClienteCompra clienteCompra : clienteCompras) { // 00100121 Recorre los clientes recuperados
            System.out.println("ClienteCompra: " + clienteCompra); // 00100121 Imprime los datos de cada cliente
        }

        String directoryName = "Reportes"; // 00100121 Nombre del directorio donde se guardará el reporte
        Files.createDirectories(Paths.get(directoryName)); // 00100121 Crea el directorio si no existe
        String fileName = directoryName + "/Reporte_D_" + System.currentTimeMillis() + ".txt"; // 00100121 Nombre del archivo de reporte
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE)) { // 00100121 Crea el archivo de reporte para escritura
            writer.write("Reporte D - Clientes que han realizado compras con el facilitador " + facilitador + "\n"); // 00100121 Escribe la cabecera del reporte
            writer.write("==================================================================\n"); // 00100121 Escribe una línea separadora
            if (clienteCompras.isEmpty()) { // 00100121 Verifica si no hay clientes
                writer.write("No se encontraron clientes que hayan realizado compras con el facilitador especificado.");
                // 00100121 Escribe mensaje de que no hay clientes
            } else {
                for (ClienteCompra clienteCompra : clienteCompras) { // 00100121 Recorre los clientes y escribe sus datos en el reporte
                    writer.write("Cliente: " + clienteCompra.getNombreCompleto() + ", Compras: " + clienteCompra.getCantidadCompras() + ", Total Gastado: $" + clienteCompra.getTotalGastado() + "\n");
                    // 00100121 Escribe el nombre, cantidad de compras y total gastado del cliente
                }
            }
        }
    }

    private String censurarNumeroTarjeta(String numeroTarjeta) { // 00100121 Método para censurar el número de la tarjeta
        return "XXXX XXXX XXXX " + numeroTarjeta.substring(12); // 00100121 Retorna el número de la tarjeta censurado
    }

    public static class Tarjeta { // 00100121 Clase interna para manejar las tarjetas
        private String numeroTarjeta; // 00100121 Atributo para almacenar el número de la tarjeta
        private int idCliente; // 00100121 Atributo para almacenar el ID del cliente
        private Date fechaExpiracion; // 00100121 Atributo para almacenar la fecha de expiración de la tarjeta
        private String tipoTarjeta; // 00100121 Atributo para almacenar el tipo de tarjeta
        private String facilitador; // 00100121 Atributo para almacenar el facilitador de la tarjeta

        public Tarjeta(String numeroTarjeta, int idCliente, Date fechaExpiracion, String tipoTarjeta, String facilitador) {
            // 00100121 Constructor de la clase Tarjeta
            this.numeroTarjeta = numeroTarjeta; // 00100121 Asigna el número de la tarjeta
            this.idCliente = idCliente; // 00100121 Asigna el ID del cliente
            this.fechaExpiracion = fechaExpiracion; // 00100121 Asigna la fecha de expiración de la tarjeta
            this.tipoTarjeta = tipoTarjeta; // 00100121 Asigna el tipo de tarjeta
            this.facilitador = facilitador; // 00100121 Asigna el facilitador de la tarjeta
        }

        public String getNumeroTarjeta() { // 00100121 Método para obtener el número de la tarjeta
            return numeroTarjeta; // 00100121 Retorna el número de la tarjeta
        }

        public int getIdCliente() { // 00100121 Método para obtener el ID del cliente
            return idCliente; // 00100121 Retorna el ID del cliente
        }

        public Date getFechaExpiracion() { // 00100121 Método para obtener la fecha de expiración de la tarjeta
            return fechaExpiracion; // 00100121 Retorna la fecha de expiración de la tarjeta
        }

        public String getTipoTarjeta() { // 00100121 Método para obtener el tipo de tarjeta
            return tipoTarjeta; // 00100121 Retorna el tipo de tarjeta
        }

        public String getFacilitador() { // 00100121 Método para obtener el facilitador de la tarjeta
            return facilitador; // 00100121 Retorna el facilitador de la tarjeta
        }
    }

    public static class ClienteCompra { // 00100121 Clase interna para manejar las compras de los clientes
        private String nombreCompleto; // 00100121 Atributo para almacenar el nombre completo del cliente
        private int cantidadCompras; // 00100121 Atributo para almacenar la cantidad de compras del cliente
        private BigDecimal totalGastado; // 00100121 Atributo para almacenar el total gastado por el cliente

        public ClienteCompra(String nombreCompleto, int cantidadCompras, BigDecimal totalGastado) {
            // 00100121 Constructor de la clase ClienteCompra
            this.nombreCompleto = nombreCompleto; // 00100121 Asigna el nombre completo del cliente
            this.cantidadCompras = cantidadCompras; // 00100121 Asigna la cantidad de compras del cliente
            this.totalGastado = totalGastado; // 00100121 Asigna el total gastado por el cliente
        }

        public String getNombreCompleto() { // 00100121 Método para obtener el nombre completo del cliente
            return nombreCompleto; // 00100121 Retorna el nombre completo del cliente
        }

        public int getCantidadCompras() { // 00100121 Método para obtener la cantidad de compras del cliente
            return cantidadCompras; // 00100121 Retorna la cantidad de compras del cliente
        }

        public BigDecimal getTotalGastado() { // 00100121 Método para obtener el total gastado por el cliente
            return totalGastado; // 00100121 Retorna el total gastado por el cliente
        }

        @Override
        public String toString() { // 00100121 Método para obtener la representación en cadena del objeto
            return "ClienteCompra{" + // 00100121 Comienza la representación en cadena del objeto
                    "nombreCompleto='" + nombreCompleto + '\'' + // 00100121 Añade el nombre completo del cliente
                    ", cantidadCompras=" + cantidadCompras + // 00100121 Añade la cantidad de compras del cliente
                    ", totalGastado=" + totalGastado + // 00100121 Añade el total gastado por el cliente
                    '}'; // 00100121 Cierra la representación en cadena del objeto
        }
    }

    public static class Compra { // 00100121 Clase interna para manejar las compras
        private int id; // 00100121 Atributo para almacenar el ID de la compra
        private int idCliente; // 00100121 Atributo para almacenar el ID del cliente
        private String numeroTarjeta; // 00100121 Atributo para almacenar el número de la tarjeta
        private LocalDateTime fechaCompra; // 00100121 Atributo para almacenar la fecha de la compra
        private BigDecimal montoTotal; // 00100121 Atributo para almacenar el monto total de la compra
        private String descripcionCompra; // 00100121 Atributo para almacenar la descripción de la compra

        public Compra(int id, int idCliente, String numeroTarjeta, LocalDateTime fechaCompra, BigDecimal montoTotal, String descripcionCompra) {
            // 00100121 Constructor de la clase Compra
            this.id = id; // 00100121 Asigna el ID de la compra
            this.idCliente = idCliente; // 00100121 Asigna el ID del cliente
            this.numeroTarjeta = numeroTarjeta; // 00100121 Asigna el número de la tarjeta
            this.fechaCompra = fechaCompra; // 00100121 Asigna la fecha de la compra
            this.montoTotal = montoTotal; // 00100121 Asigna el monto total de la compra
            this.descripcionCompra = descripcionCompra; // 00100121 Asigna la descripción de la compra
        }

        public int getId() { // 00100121 Método para obtener el ID de la compra
            return id; // 00100121 Retorna el ID de la compra
        }

        public int getIdCliente() { // 00100121 Método para obtener el ID del cliente
            return idCliente; // 00100121 Retorna el ID del cliente
        }

        public String getNumeroTarjeta() { // 00100121 Método para obtener el número de la tarjeta
            return numeroTarjeta; // 00100121 Retorna el número de la tarjeta
        }

        public LocalDateTime getFechaCompra() { // 00100121 Método para obtener la fecha de la compra
            return fechaCompra; // 00100121 Retorna la fecha de la compra
        }

        public BigDecimal getMontoTotal() { // 00100121 Método para obtener el monto total de la compra
            return montoTotal; // 00100121 Retorna el monto total de la compra
        }

        public String getDescripcionCompra() { // 00100121 Método para obtener la descripción de la compra
            return descripcionCompra; // 00100121 Retorna la descripción de la compra
        }

        @Override
        public String toString() { // 00100121 Método para obtener la representación en cadena del objeto
            return "Compra{" + // 00100121 Comienza la representación en cadena del objeto
                    "id=" + id + // 00100121 Añade el ID de la compra
                    ", idCliente=" + idCliente + // 00100121 Añade el ID del cliente
                    ", numeroTarjeta='" + numeroTarjeta + '\'' + // 00100121 Añade el número de la tarjeta
                    ", fechaCompra=" + fechaCompra + // 00100121 Añade la fecha de la compra
                    ", montoTotal=" + montoTotal + // 00100121 Añade el monto total de la compra
                    ", descripcionCompra='" + descripcionCompra + '\'' + // 00100121 Añade la descripción de la compra
                    '}'; // 00100121 Cierra la representación en cadena del objeto
        }
    }
}

package org.example; //00197822 Define el paquete donde se encuentra la clase

import java.util.ArrayList; //00197822 Importa la clase ArrayList
import java.util.List; // 00197822 Importa la clase List
import java.util.Scanner; //00197822 Importa la clase Scanner
import java.time.LocalDate; //00197822 Importa la clase LocalDate

public class Main {  //00197822 Define la clase publica Main que es el punto de entrada del programa
    static List<Cliente> clientes = new ArrayList<>(); //00197822 Lista de clientes
    static List<Tarjeta> tarjetas = new ArrayList<>(); //00197822 Lista de tarjetas
    static List<Transaccion> transacciones = new ArrayList<>(); // 00197822 Lista de transacciones

    public static void main(String[] args) { //00197822 Este metodo principal es el que inicia la ejecución del programa
        Scanner scanner = new Scanner(System.in); //00197822 Es para crear un objeto Scanner para la entrada del usuario


        agregarDatosEjemplo();   //00197822 Agrega datos como ejemplo


        while (true) {  //00197822 Bucle para el menu de opciones
            System.out.println("Seleccione una opción:");  //00197822 Imprime el mensaje para que el usurio pueda seleccionar una opcion
            System.out.println("1. Generar Reporte A");  //00197822 Imprime la opción 1 para generar el reporte A
            System.out.println("2. Generar Reporte B"); //00197822 Imprime la opción 2 para generar el reporte B
            System.out.println("3. Generar Reporte C"); //00197822 Imprime la opción 3 para generar el reporte C
            System.out.println("4. Generar Reporte D");  //00197822 Imprime la opción 4 para generar el reporte D
            System.out.println("5. Salir"); //00197822 Imprime la opción 5 para salir del programa
            int opcion = scanner.nextInt(); //00197822 Lee la opción seleccionada por el usuario
            scanner.nextLine();  //00197822 Para limpiar el buffer

            switch (opcion) { //00197822 Maneja la opcion seleccionada
                case 1:
                    System.out.println("Ingrese ID de cliente:"); //00197822 Pide el ID del cliente
                    int clienteIdA = scanner.nextInt(); //00197822 Lee el ID del cliente
                    System.out.println("Ingrese fecha de inicio (yyyy-MM-dd):");  //00197822 Pide la fecha de inicio
                    LocalDate fechaInicio = LocalDate.parse(scanner.next()); //00197822 Lee y convierte la fecha de inicio
                    System.out.println("Ingrese fecha de fin (yyyy-MM-dd):"); //00197822 Pide la fecha de fin
                    LocalDate fechaFin = LocalDate.parse(scanner.next()); //00197822 Lee y convierte la fecha de fin
                    generarReporteA(clienteIdA, fechaInicio, fechaFin); //00197822 Genera el reporte A
                    break;
                case 2:
                    System.out.println("Ingrese ID de cliente:"); //00197822 Pide el ID del cliente
                    int clienteIdB = scanner.nextInt(); //00197822 Lee el ID del cliente
                    System.out.println("Ingrese mes (1-12):"); //00197822 Pide el mes
                    int mes = scanner.nextInt(); //00197822 Pide el mes
                    System.out.println("Ingrese año:"); //00197822 Pide el año
                    int anio = scanner.nextInt(); //00197822 Lee el año
                    generarReporteB(clienteIdB, mes, anio); //00197822 Genera el reporte B
                    break;
                case 3:
                    System.out.println("Ingrese ID de cliente:"); //00197822 Pide el ID del cliente
                    int clienteIdC = scanner.nextInt();  //00197822 Lee el ID del cliente
                    generarReporteC(clienteIdC); //0019782 Genera el reporte C
                    break;
                case 4:
                    System.out.println("Ingrese facilitador de tarjeta (Visa, MasterCard,American Express, etc.):"); // Pide el facilitador de la tarjeta
                    String facilitador = scanner.next(); //00197822 Lee el facilitador
                    generarReporteD(facilitador); //00197822 Genera el reporte D
                    break;
                case 5:
                    System.out.println("Bye..."); //00197822 Le dice al usuario  que se está saliendo
                    scanner.close(); //00197822 Cierra el scanner
                    return; //00197822 Sale del bucle
                default:
                    System.out.println("Opción no válida."); //00197822 Indica que la opción no es válida
            }
        }
    }

    public static void agregarDatosEjemplo() {  //00197822 Esta funcion agrega datos de ejemplo a las listas
        Cliente cliente1 = new Cliente(1, "Ximena Sandoval", "123 Calle El Pedregal", "7190-1234"); // Crea un cliente de ejemplo
        clientes.add(cliente1); //00197822 Agrega el cliente a la lista

        Tarjeta tarjeta1 = new Tarjeta(1, "4256880059786201", LocalDate.of(2025, 12, 31), "Credito", "Visa", 1); //00197822 Crea una tarjeta como ejemplo
        tarjetas.add(tarjeta1); //00197822 Agrega la tarjeta a la lista

        Transaccion transaccion1 = new Transaccion(1, LocalDate.of(2023, 7, 1), 100.50, "Compra en tienda", 1);//00197822 Crea una transacción de ejemplo
        transacciones.add(transaccion1); //00197822 Agrega la transacción a la lista
    }

    public static void generarReporteA(int clienteId, LocalDate fechaInicio, LocalDate fechaFin) { //00197822 Esta funcion genera el reporte A
        System.out.println("Reporte A - Compras realizadas por cliente " + clienteId + " desde " + fechaInicio + " hasta " + fechaFin);  // Imprime el encabezado del reporte
        for (Transaccion transaccion : transacciones) {  //00197822 Recorre las transacciones
            Tarjeta tarjeta = obtenerTarjetaPorId(transaccion.tarjetaId); //00197822 Obtiene la tarjeta asociada
            if (tarjeta != null && tarjeta.clienteId == clienteId && (transaccion.fechaTransaccion.isEqual(fechaInicio) || transaccion.fechaTransaccion.isAfter(fechaInicio)) && (transaccion.fechaTransaccion.isEqual(fechaFin) || transaccion.fechaTransaccion.isBefore(fechaFin))) { //00197822 Verifica si la transacción cumple con los criterios
                System.out.println("Fecha: " + transaccion.fechaTransaccion + ", Monto: " + transaccion.monto + ", Descripción: " + transaccion.descripcion); // Imprime la transacción si cumple con las condiciones
            }
        }
    }

    public static void generarReporteB(int clienteId, int mes, int anio) { //00197822 Esta funcion genera el reporte B
        System.out.println("Reporte B - Total gastado por cliente " + clienteId + " en " + mes + "/" + anio);
        double totalGastado = 0; //00197822 Inicializa el total gastado
        for (Transaccion transaccion : transacciones) {  //00197822 Recorre las transacciones
            Tarjeta tarjeta = obtenerTarjetaPorId(transaccion.tarjetaId);  //00197822 Obtiene la tarjeta asociada
            if (tarjeta != null && tarjeta.clienteId == clienteId && transaccion.fechaTransaccion.getMonthValue() == mes && transaccion.fechaTransaccion.getYear() == anio) {  //00197822 Verifica si la tarjeta no es nula y si pertenece al cliente y si la transaccion ocurrio en el mes y año especificados
                totalGastado += transaccion.monto; //00197822 Suma el monto si cumple con las condiciones
            }
        }
        System.out.println("Total gastado: " + totalGastado); //00197822 Imprime el total gastado
    }

    public static void generarReporteC(int clienteId) { //00197822 Esta funcion genera el reporte C
        System.out.println("Reporte C - Tarjetas asociadas al cliente " + clienteId);
        List<Tarjeta> tarjetasCredito = new ArrayList<>(); //00197822 Lista para tarjetas de credito
        List<Tarjeta> tarjetasDebito = new ArrayList<>();  //00197822 Lista para tarjetas de debito
        for (Tarjeta tarjeta : tarjetas) {  //00197822 Recorre las tarjetas
            if (tarjeta.clienteId == clienteId) { //00197822 Verifica si la tarjeta le pertenece al cliente
                if (tarjeta.tipoTarjeta.equals("Credito")) { //00197822 Verifica si la tarjeta es de credito
                    tarjetasCredito.add(tarjeta); //00197822 Agrega a la lista de crédito si cumple con la condicion
                } else {
                    tarjetasDebito.add(tarjeta);//00197822 Agrega a la lista de débito si cumple con la condicion
                }
            }
        }

        System.out.println("Tarjetas de crédito:");  //00197822 Imprime el titulo para tarjetas de credito
        for (Tarjeta tarjeta : tarjetasCredito) { //00197822 Recorre las tarjetas de credito
            System.out.println("XXXX XXXX XXXX " + tarjeta.numeroTarjeta.substring(12));//00197822 Imprime el numero de tarjeta censuraado
        }

        System.out.println("Tarjetas de Débito:");  //00197822 Imprime el titulo para tarjetas de debito
        if (tarjetasDebito.isEmpty()) {  //00197822 Verifica si no hay tarjetas de debito
            System.out.println("N/A"); //00197822 Imprime N/A (NO APLICA) si no hay tarjetas de debito
        } else {
            for (Tarjeta tarjeta : tarjetasDebito) { //00197822 Recorre cada tarjeta en la lista de tarjetas de debito
                System.out.println("XXXX XXXX XXXX " + tarjeta.numeroTarjeta.substring(12));  //00197822 Imprime el numero de tarjeta censurado
            }
        }
    }

    public static void generarReporteD(String facilitador) {  //00197822 Esta funcion genera el reporte D
        System.out.println("Reporte D - Clientes que han realizado compras con facilitador " + facilitador); //00197822 Imprime el encabezado del reporte
        List<Cliente> clientesReporte = new ArrayList<>(); //00197822 Lista de clientes que cumplen con la condicion
        for (Transaccion transaccion : transacciones) {  //00197822 Recorre las transacciones
            Tarjeta tarjeta = obtenerTarjetaPorId(transaccion.tarjetaId); //00197822 Obtiene la tarjeta asociada
            if (tarjeta != null && tarjeta.facilitador.equals(facilitador)) { //00197822 Verifica si la transacción cumple con las condiciones
                Cliente cliente = obtenerClientePorId(tarjeta.clienteId); //00197822 Obtiene el cliente asociado
                if (cliente != null && !clientesReporte.contains(cliente)) { //00197822 Verifica si el cliente no está en la lista
                    clientesReporte.add(cliente); //00197822 Agrega el cliente a la lista si cumple con la condicion
                }
            }
        }

        for (Cliente cliente : clientesReporte) {  //00197822 Recorre los clientes que cumplen con la condicion
            System.out.println("Cliente: " + cliente.nombre);  //00197822 Imprime el nombre del cliente
        }
    }

    public static Tarjeta obtenerTarjetaPorId(int id) {  //00197822 Esta funcion obtiene una tarjeta por ID
        for (Tarjeta tarjeta : tarjetas) {  //00197822 Recorre las tarjetas
            if (tarjeta.id == id) { //00197822 Verifica si la tarjeta coincide con el ID
                return tarjeta; //00197822 Retorna la tarjeta si coincide el ID
            }
        }
        return null;  //00197822 Retorna null si no encuentra la tarjeta
    }

   public static Cliente obtenerClientePorId(int id) {  //00197822 Esta funcion obtiene un cliente por ID
        for (Cliente cliente : clientes) { //00197822 Recorre los clientes
            if (cliente.id == id) { //00197822 Verifica si el cliente coincide con el ID
                return cliente; //00197822 Retorna el cliente si coincide el ID
            }
        }
        return null; //00197822 Retorna null si no encuentra el cliente
    }
}

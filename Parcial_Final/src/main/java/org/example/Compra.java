package org.example; // 00227722 Define el paquete de la clase
import java.io.File; // 00227722 Maneja archivos en el sistema
import java.io.FileWriter; // 00227722 Necesario para escribir en archivos
import java.io.IOException; // 00227722 Maneja excepciones de entrada/salida
import java.util.ArrayList; // 00227722 Maneja listas dinamicas
import java.util.Scanner; // 00227722 Lee la entrada del usuario

public class Compra { // 00227722 Clase para gestionar las compras de los usuarios
    private ArrayList<String> productosComprados; // 00227722 Almacena los productos comprados
    private ArrayList<Double> preciosComprados; // 00227722 Almacena los precios de los productos comprados
    private String nombres; // 00227722 Almacena los nombres del usuario
    private static final String BASE_PATH = "C:/Users/orlan/OneDrive/Desktop/ParcialFinal/ParFinal/src/main/resources/txt/"; // 00227722 Ruta para guardar los archivos de texto

    public Compra(String nombres) { // 00227722 Constructor que recibe los nombres del usuario
        this.nombres = nombres; // 00227722 Asignando los nombres del usuario a la variable local
        productosComprados = new ArrayList<>(); // 00227722 Inicializa la lista de productos comprados
        preciosComprados = new ArrayList<>(); // 00227722 Inicializa la lista de precios de los productos comprados
    }

    public void comprar(Tarjeta tarjeta) { // 00227722 Metodo para realizar una compra
        Scanner scanner = new Scanner(System.in); // 00227722 Utilizado para leer la entrada del usuario

        System.out.print("Ingrese el nombre del producto: "); // 00227722 Solicita el nombre del producto al usuario
        String producto = scanner.nextLine(); // 00227722 Lee el nombre del producto

        System.out.print("Ingrese el precio del producto: "); // 00227722 Solicita el precio del producto al usuario
        double precio = scanner.nextDouble(); // 00227722 Lee el precio del producto
        scanner.nextLine();  // 00227722 Consume la nueva linea

        if (tarjeta.getSaldo() >= precio) { // 00227722 Verifica si el saldo de la tarjeta es suficiente
            tarjeta.setSaldo(tarjeta.getSaldo() - precio); // 00227722 Resta el precio del producto del saldo de la tarjeta
            productosComprados.add(producto); // 00227722 Agrega el producto a la lista de productos comprados
            preciosComprados.add(precio); // 00227722 Agrega el precio del producto a la lista de precios comprados
            System.out.println("Ha comprado " + producto + " por $" + precio); // 00227722 Informa al usuario sobre la compra
            System.out.println("Saldo restante: $" + tarjeta.getSaldo()); // 00227722 Muestra el saldo restante

            guardarEnArchivo("Compra: " + producto + ", Precio: $" + precio + "\n"); // 00227722 Guarda los detalles de la compra en un archivo
        } else { // 00227722 Si el saldo no es suficiente
            System.out.println("Fondos insuficientes para realizar la compra."); // 00227722 Informa al usuario que no tiene suficiente saldo
        }
    }

    private void guardarEnArchivo(String texto) { // 00227722 Metodo para guardar datos en un archivo
        try { // 00227722 Intenta ejecutar el codigo
            File archivo = new File(BASE_PATH + nombres + ".txt"); // 00227722 Crea un objeto File con la ruta del archivo del usuario
            if (!archivo.exists()) { // 00227722 Verifica si el archivo no existe
                archivo.getParentFile().mkdirs();  // 00227722 Crea los directorios necesarios
                archivo.createNewFile(); // 00227722 Crea un nuevo archivo
            }
            try (FileWriter writer = new FileWriter(archivo, true)) { // 00227722 Utiliza FileWriter para escribir en el archivo
                writer.write(texto); // 00227722 Escribe el texto en el archivo
            }
        } catch (IOException e) { // 00227722 Captura posibles excepciones de escritura
            System.out.println("Ocurrio un error al guardar en el archivo: " + e.getMessage()); // 00227722 Muestra un mensaje de error
        }
    }
}

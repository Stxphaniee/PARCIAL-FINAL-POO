package org.example; // 00227722 Define el paquete de la clase
import java.io.File; // 00227722 Maneja archivos en el sistema

public class Main { // 00227722 Clase principal de la aplicacion
    public static void main(String[] args) { // 00227722 Metodo principal
        crearCarpeta(); // 00227722 Llama al metodo para crear la carpeta si no existe

        Cliente cliente = new Cliente(); // 00227722 Crea una instancia de la clase Cliente
        cliente.iniciar(); // 00227722 Llama al metodo iniciar de la clase Cliente
    }

    private static void crearCarpeta() { // 00227722 Crea la carpeta si no existe
        File carpeta = new File("C:/Users/orlan/OneDrive/Desktop/ParcialFinal/ParFinal/src/main/resources/txt/"); // 00227722 Ruta de la carpeta
        if (!carpeta.exists()) { // 00227722 Verifica si la carpeta no existe
            carpeta.mkdirs(); // 00227722 Crea la carpeta y los directorios necesarios
        }
    }
}

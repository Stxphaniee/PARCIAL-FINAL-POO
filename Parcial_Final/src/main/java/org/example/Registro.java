package org.example; // 00227722 Define el paquete de la clase
import java.io.File; // 00227722 Maneja archivos en el sistema
import java.io.FileWriter; // 00227722 Necesario para escribir en archivos
import java.io.IOException; // 00227722 Maneja excepciones de entrada/salida
import java.util.Scanner; // 00227722 Lee la entrada del usuario

public class Registro { // 00227722 Clase para gestionar el registro de usuarios
    private String nombres; // 00227722 Almacena los nombres del usuario
    private String apellidos; // 00227722 Almacena los apellidos del usuario
    private String telefono; // 00227722 Almacena el telefono del usuario
    private String direccion; // 00227722 Almacena la direccion del usuario
    private static final String BASE_PATH = "C:/Users/orlan/OneDrive/Desktop/ParcialFinal/ParFinal/src/main/resources/txt/"; // 00227722 Ruta para guardar los archivos de texto

    public void registrar() { // 00227722 Metodo para registrar un nuevo usuario
        Scanner scanner = new Scanner(System.in); // 00227722 Lee la entrada del usuario
        System.out.print("Ingrese sus nombres: "); // 00227722 Solicita los nombres del usuario
        nombres = scanner.nextLine(); // 00227722 Lee los nombres ingresados
        System.out.print("Ingrese sus apellidos: "); // 00227722 Solicita los apellidos del usuario
        apellidos = scanner.nextLine(); // 00227722 Lee los apellidos ingresados
        System.out.print("Ingrese su telefono: "); // 00227722 Solicita el telefono del usuario
        telefono = scanner.nextLine(); // 00227722 Lee el telefono ingresado
        System.out.print("Ingrese su direccion: "); // 00227722 Solicita la dirección del usuario
        direccion = scanner.nextLine(); // 00227722 Lee la dirección ingresada
        System.out.println("Registro completado. Bienvenido, " + nombres + " " + apellidos); // 00227722 Confirma el registro al usuario
        guardarEnArchivo("Nombres: " + nombres + "\nApellidos: " + apellidos + "\nTelefono: " + telefono + "\nDireccion: " + direccion + "\n"); // 00227722 Guarda los datos en un archivo
    }

    public boolean iniciarSesion() { // 00227722 Metodo para iniciar sesion con los nombres del usuario
        Scanner scanner = new Scanner(System.in); // 00227722 Lee la entrada del usuario
        System.out.print("Ingrese sus nombres: "); // 00227722 Solicita los nombres del usuario
        nombres = scanner.nextLine(); // 00227722 Lee los nombres ingresados

        File archivo = new File(BASE_PATH + nombres + ".txt"); // 00227722 Crea un objeto File con la ruta del archivo del usuario
        if (archivo.exists()) { // 00227722 Verifica si el archivo del usuario existe
            System.out.println("Bienvenido de nuevo, " + leerDato(archivo, "Nombres") + " " + leerDato(archivo, "Apellidos")); // 00227722 Muestra un mensaje de bienvenida con los datos del usuario
            System.out.println("Su saldo actual es: $" + leerDato(archivo, "Saldo")); // 00227722 Muestra el saldo actual del usuario
            return true; // 00227722 Retorna verdadero si el archivo existe
        } else { // 00227722 Si el archivo no existe
            System.out.println("Error: No se encontro un usuario con esos nombres"); // 00227722 Muestra un mensaje de error
            return false; // 00227722 Retorna falso si el archivo no existe
        }
    }

    private String leerDato(File archivo, String campo) { // 00227722 Metodo para leer un campo especifico del archivo
        try (Scanner scanner = new Scanner(archivo)) { // 00227722 Utilizamos Scanner para leer el archivo
            while (scanner.hasNextLine()) { // 00227722 Itera por cada linea del archivo
                String linea = scanner.nextLine(); // 00227722 Lee una linea del archivo
                if (linea.startsWith(campo)) { // 00227722 Verifica si la linea comienza con el campo buscado
                    return linea.split(": ")[1]; // 00227722 Retorna el valor del campo
                }
            }
        } catch (IOException e) { // 00227722 Captura posibles excepciones de lectura
            System.out.println("Ocurrio un error al leer el archivo: " + e.getMessage()); // 00227722 Muestra un mensaje de error
        }
        return ""; // 00227722 Retorna una cadena vacia si no encuentra el campo
    }

    private void guardarEnArchivo(String texto) { // 00227722 Metodo para guardar texto en un archivo
        try { // 00227722 Intenta ejecutar el bloque de código
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

    public String getNombres() { // 00227722 Retorna los nombres del usuario
        return nombres; // 00227722 Devuelve los nombres
    }

    public String getApellidos() { // 00227722 Retorna los apellidos del usuario
        return apellidos; // 00227722 Devuelve los apellidos
    }
}

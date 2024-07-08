package org.example; // 00227722 Define el paquete de la clase
import java.io.File; // 00227722 Maneja archivos en el sistema
import java.io.FileWriter; // 00227722 Necesario para escribir en archivos
import java.io.IOException; // 00227722 Maneja excepciones de entrada/salida
import java.util.Random; // 00227722 Genera numeros aleatorios
import java.util.Scanner; // 00227722 Lee la entrada del usuario

public class Tarjeta { // 00227722 Clase para gestionar las tarjetas de los usuarios
    private String tipoTarjeta; // 00227722 Almacena el tipo de tarjeta
    private double saldo; // 00227722 Almacena el saldo de la tarjeta
    private String numeroTarjeta; // 00227722 Almacena el nunero de la tarjeta
    private String nombres; // 00227722 Almacena los nombres del usuario
    private static final String BASE_PATH = "C:/Users/orlan/OneDrive/Desktop/ParcialFinal/ParFinal/src/main/resources/txt/"; // 00227722 Ruta para guardar los archivos de texto

    public Tarjeta(String nombres) { // 00227722 Constructor que recibe los nombres del usuario
        this.nombres = nombres; // 00227722 Asigna los nombres del usuario a la variable local
    }

    public void seleccionarTarjeta() { // 00227722 Metodo para seleccionar el tipo de tarjeta
        Scanner scanner = new Scanner(System.in); // 00227722 Lee la entrada del usuario
        System.out.println("Seleccione el tipo de tarjeta:"); // 00227722 Muestra opciones de tipo de tarjeta
        System.out.println("1. Credito"); // 00227722 Opcion de tarjeta de credito
        System.out.println("2. Debito"); // 00227722 Opcion de tarjeta de debito
        int opcion = scanner.nextInt(); // 00227722 Lee la opcion ingresada
        scanner.nextLine();  // 00227722 Consume la nueva linea

        if (opcion == 1) { // 00227722 Si la opcion es credito
            tipoTarjeta = "credito"; // 00227722 Asigna el tipo de tarjeta como credito
            saldo = 500.0; // 00227722 Asigna un saldo inicial de $500
            numeroTarjeta = generarNumeroTarjeta(); // 00227722 Genera un numero de tarjeta aleatorio
            System.out.println("Ha optado por una tarjeta de credito con un monto de $500"); // 00227722 Informa al usuario sobre su saldo
            System.out.println("N° de Tarjeta: " + numeroTarjeta); // 00227722 Muestra el numero de tarjeta
            guardarEnArchivo("Tarjeta: credito\nSaldo: $500\nN° de Tarjeta: " + numeroTarjeta + "\n"); // 00227722 Guarda los detalles en un archivo
        } else if (opcion == 2) { // 00227722 Si la opcion es debito
            tipoTarjeta = "debito"; // 00227722 Asigna el tipo de tarjeta como debito
            System.out.print("¿Cuanto desea agregar a su nueva cuenta de debito? "); // 00227722 Solicita al usuario el saldo inicial
            saldo = scanner.nextDouble(); // 00227722 Lee el saldo ingresado
            scanner.nextLine();  // 00227722 Consume la nueva linea
            numeroTarjeta = generarNumeroTarjeta(); // 00227722 Genera un numero de tarjeta aleatorio
            System.out.println("Ha optado por una tarjeta de debito con un saldo de $" + saldo); // 00227722 Informa al usuario sobre su saldo
            System.out.println("N° de Tarjeta: " + numeroTarjeta); // 00227722 Muestra el numero de tarjeta
            guardarEnArchivo("Tarjeta: debito\nSaldo: $" + saldo + "\nN° de Tarjeta: " + numeroTarjeta + "\n"); // 00227722 Guarda los detalles en un archivo
        } else { // 00227722 Si la opcion no es valida
            System.out.println("Opcion no valida"); // 00227722 Informa al usuario que la opcion es invalida
        }
    }

    private String generarNumeroTarjeta() { // 00227722 Metodo para generar un numero de tarjeta aleatorio
        Random random = new Random(); // 00227722 Crea un objeto Random
        StringBuilder sb = new StringBuilder(); // 00227722 Crea un StringBuilder para construir el numero de tarjeta
        for (int i = 0; i < 16; i++) { // 00227722 Itera 16 veces para generar los digitos
            sb.append(random.nextInt(10)); // 00227722 Agrega un digito aleatorio al numero de tarjeta
            if ((i + 1) % 4 == 0 && i < 15) { // 00227722 Agrega un espacio cada 4 digitos
                sb.append(" "); // 00227722 Agrega el espacio
            }
        }
        return sb.toString(); // 00227722 Devuelve el numero de tarjeta generado
    }

    public double getSaldo() { // 00227722 Metodo para obtener el saldo de la tarjeta
        return saldo; // 00227722 Devuelve el saldo actual
    }

    public void setSaldo(double saldo) { // 00227722 Metodo para establecer el saldo de la tarjeta
        this.saldo = saldo; // 00227722 Asigna el nuevo saldo
        actualizarSaldoEnArchivo(saldo); // 00227722 Actualiza el saldo en el archivo
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

    private void actualizarSaldoEnArchivo(double nuevoSaldo) { // 00227722 Metodo para actualizar el saldo en el archivo
        try { // 00227722 Intenta ejecutar el codigo
            File archivo = new File(BASE_PATH + nombres + ".txt"); // 00227722 Crea un objeto File con la ruta del archivo del usuario
            StringBuilder contenido = new StringBuilder(); // 00227722 Utilizado para reconstruir el contenido del archivo
            try (Scanner scanner = new Scanner(archivo)) { // 00227722 Utiliza Scanner para leer el archivo
                while (scanner.hasNextLine()) { // 00227722 Itera por cada linea del archivo
                    String linea = scanner.nextLine(); // 00227722 Lee una linea del archivo
                    if (linea.startsWith("Saldo:")) { // 00227722 Si la linea empieza con "Saldo:"
                        contenido.append("Saldo: $").append(nuevoSaldo).append("\n"); // 00227722 Actualiza el saldo
                    } else { // 00227722 Si no empieza con "Saldo:"
                        contenido.append(linea).append("\n"); // 00227722 Agrega la linea sin cambios
                    }
                }
            }
            try (FileWriter writer = new FileWriter(archivo)) { // 00227722 Utiliza FileWriter para escribir en el archivo
                writer.write(contenido.toString()); // 00227722 Escribe el contenido actualizado en el archivo
            }
        } catch (IOException e) { // 00227722 Captura posibles excepciones de escritura
            System.out.println("Ocurrio un error al actualizar el saldo en el archivo: " + e.getMessage()); // 00227722 Muestra un mensaje de error
        }
    }

    public String getNumeroTarjeta() { // 00227722 Metodo para obtener el numero de tarjeta
        return numeroTarjeta; // 00227722 Devuelve el numero de tarjeta
    }

    public void cargarSaldoDesdeArchivo() { // 00227722 Metodo para cargar el saldo desde el archivo
        try { // 00227722 Intenta ejecutar el codigo
            File archivo = new File(BASE_PATH + nombres + ".txt"); // 00227722 Crea un objeto File con la ruta del archivo del usuario
            try (Scanner scanner = new Scanner(archivo)) { // 00227722 Utiliza Scanner para leer el archivo
                while (scanner.hasNextLine()) { // 00227722 Itera por cada linea del archivo
                    String linea = scanner.nextLine(); // 00227722 Lee una linea del archivo
                    if (linea.startsWith("Saldo:")) { // 00227722 Si la linea empieza con "Saldo:"
                        saldo = Double.parseDouble(linea.split(": \\$")[1]); // 00227722 Asigna el saldo leido
                    }
                }
            }
        } catch (IOException e) { // 00227722 Captura posibles excepciones de lectura
            System.out.println("Ocurrio un error al cargar el saldo desde el archivo: " + e.getMessage()); // 00227722 Muestra un mensaje de error
        }
    }
}

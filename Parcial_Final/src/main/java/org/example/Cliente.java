package org.example; // 00227722 Define el paquete de la clase
import java.util.Scanner; // 00227722 Lee la entrada del usuario

public class Cliente { // 00227722 Clase principal para gestionar clientes
    private Registro registro; // 00227722 Instancia de la clase Registro
    private Tarjeta tarjeta; // 00227722 Instancia de la clase Tarjeta
    private Compra compra; // 00227722 Instancia de la clase Compra

    public Cliente() { // 00227722 Constructor de la clase Cliente
        registro = new Registro(); // 00227722 Inicializa la instancia de Registro
    }

    public void iniciar() { // 00227722 Metodo para iniciar la aplicacion
        Scanner scanner = new Scanner(System.in); // 00227722 Utilizado para leer la entrada del usuario
        System.out.println("Seleccione una opcion:"); // 00227722 Muestra el menu de opciones
        System.out.println("1. Registrar"); // 00227722 Opcion para registrar un nuevo usuario
        System.out.println("2. Iniciar sesion"); // 00227722 Opcion para iniciar sesion
        int opcion = scanner.nextInt(); // 00227722 Lee la opcion ingresada
        scanner.nextLine();  // 00227722 Consume la nueva linea

        if (opcion == 1) { // 00227722 Si la opcion es registrar
            registro.registrar(); // 00227722 Llama al metodo registrar de la clase Registro
            tarjeta = new Tarjeta(registro.getNombres()); // 00227722 Inicializa la instancia de Tarjeta con los nombres del usuario
            tarjeta.seleccionarTarjeta(); // 00227722 Llama al metodo seleccionarTarjeta de la clase Tarjeta
            compra = new Compra(registro.getNombres()); // 00227722 Inicializa la instancia de Compra con los nombres del usuario

            System.out.print("¿Desea realizar una compra? (si/no): "); // 00227722 Pregunta si desea realizar una compra
            String respuesta = scanner.nextLine().toLowerCase(); // 00227722 Lee la respuesta y la convierte a minusculas

            if (respuesta.equals("si")) { // 00227722 Si la respuesta es si
                realizarCompras(); // 00227722 Llama al metodo realizarCompras
            } else { // 00227722 Si la respuesta es no
                System.out.println("Gracias por usar nuestro servicio."); // 00227722 Muestra un mensaje de agradecimiento
            }
        } else if (opcion == 2) { // 00227722 Si la opcion es iniciar sesion
            if (registro.iniciarSesion()) { // 00227722 Si el inicio de sesion es exitoso
                tarjeta = new Tarjeta(registro.getNombres()); // 00227722 Inicializa la instancia de Tarjeta con los nombres del usuario
                tarjeta.cargarSaldoDesdeArchivo(); // 00227722 Caraga el saldo desde el archivo
                compra = new Compra(registro.getNombres()); // 00227722 Inicializa la instancia de Compra con los nombres del usuario

                System.out.print("¿Desea realizar una compra? (si/no): "); // 00227722 Pregunta si desea realizar una compra
                String respuesta = scanner.nextLine().toLowerCase(); // 00227722 Lee la respuesta y la convierte a minusculas

                if (respuesta.equals("si")) { // 00227722 Si la respuesta es si
                    realizarCompras(); // 00227722 Llama al metodo realizarCompras
                } else { // 00227722 Si la respuesta es no
                    System.out.println("Gracias por usar nuestro servicio."); // 00227722 Muestra un mensaje de agradecimiento
                }
            } else { // 00227722 Si el inicio de sesion falla
                return; // 00227722 Termina la ejecucion del metodo
            }
        } else { // 00227722 Si la opcion no es valida
            System.out.println("Opcion no valida."); // 00227722 Muestra un mensaje de error
            return; // 00227722 Termina la ejecucion del metodo
        }
    }

    private void realizarCompras() { // 00227722 Metodo para gestionar la compra de productos
        Scanner scanner = new Scanner(System.in); // 00227722 Utilizado para leer la entrada del usuario
        boolean seguirComprando = true; // 00227722 Bandera para controlar el ciclo de compras

        while (seguirComprando) { // 00227722 Ciclo para realizar multiples compras
            compra.comprar(tarjeta); // 00227722 Llama al metodo comprar de la clase Compra
            System.out.print("¿Desea realizar otra compra? (si/no): "); // 00227722 Pregunta si desea realizar otra compra
            String respuesta = scanner.nextLine().toLowerCase(); // 00227722 Lee la respuesta y la convierte a minusculas
            if (!respuesta.equals("si")) { // 00227722 Si la respuesta no es si
                seguirComprando = false; // 00227722 Cambia la bandera para terminar el ciclo
            }
        }
        System.out.println("Gracias por usar nuestro servicio."); // 00227722 Muestra un mensaje de agradecimiento
    }
}

package com.itson.componentecomunicacion;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Prueba del componente: mini chat por consola. Se corre dos veces y lo que se
 * escribe en una ventana aparece en la otra.
 */
public class ComponenteComunicacion {

    public static void main(String[] args) throws IOException {
        Scanner teclado = new Scanner(System.in);
        String miIp = InetAddress.getLocalHost().getHostAddress();

        // Datos de conexion (Enter = valor por defecto)
        int miPuerto = Integer.parseInt(preguntar(teclado, "Mi puerto", "5001"));
        String ipOtro = preguntar(teclado, "IP del otro", "localhost");
        int puertoOtro = Integer.parseInt(preguntar(teclado, "Puerto del otro", miPuerto == 5001 ? "5002" : "5001"));

        // La fabrica arma el componente, solo usamos sus interfaces
        DispatcherFactory fabrica = new DispatcherFactory();
        IDispatcher dispatcher = fabrica.crearDispatcher();
        // Cada mensaje que llegue se imprime
        fabrica.crearReceptor(miPuerto, m -> System.out.println("<< " + m.getIpOrigen() + ":" + m.getPuertoOrigen() + " dice: " + m.getContenido()));

        System.out.println("Escuchando en " + miIp + ":" + miPuerto + ". Escribe y presiona Enter (salir = terminar)");

        // Cada linea escrita se envia al otro
        String texto;
        while (!(texto = teclado.nextLine()).equalsIgnoreCase("salir")) {
            Mensaje mensaje = new Mensaje();
            mensaje.setTipo("CHAT");
            mensaje.setContenido(texto);
            mensaje.setIpOrigen(miIp);
            mensaje.setPuertoOrigen(miPuerto);
            mensaje.setIpDestino(ipOtro);
            mensaje.setPuertoDestino(puertoOtro);
            dispatcher.enviar(mensaje);
        }
    }

    // Muestra la pregunta y regresa lo escrito, o el valor por defecto si se deja vacio
    private static String preguntar(Scanner teclado, String pregunta, String porDefecto) {
        System.out.print(pregunta + " [" + porDefecto + "]: ");
        String linea = teclado.nextLine().trim();
        return linea.isEmpty() ? porDefecto : linea;
    }
}

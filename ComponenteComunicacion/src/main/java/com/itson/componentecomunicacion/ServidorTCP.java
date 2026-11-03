package com.itson.componentecomunicacion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Escucha en un puerto. Conexion intermitente: por cada conexion lee todos los
 * bytes hasta que el otro lado cierra y se los pasa al Receptor.
 */
public class ServidorTCP {

    private int puerto;
    private Receptor receptor;

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    /**
     * Abre el puerto aqui mismo para que un error, por ejemplo un puerto ocupado
     * se detecte al arrancar, y luego acepta conexiones en otro hilo.
     */
    public void iniciar() throws IOException {
        if (receptor == null) {
            throw new IllegalStateException("No se ha configurado el receptor. Use setReceptor() primero.");
        }
        ServerSocket servidor = new ServerSocket(puerto);
        Thread hilo = new Thread(() -> {
            while (!servidor.isClosed()) {
                try (Socket conexion = servidor.accept()) {
                    byte[] datos = conexion.getInputStream().readAllBytes();
                    receptor.encolar(datos, conexion.getInetAddress().getHostAddress());
                } catch (IOException e) {
                    System.err.println("Error al recibir en el puerto " + puerto + ": " + e.getMessage());
                }
            }
        }, "ServidorTCP-" + puerto);
        hilo.setDaemon(true);
        hilo.start();
    }
}

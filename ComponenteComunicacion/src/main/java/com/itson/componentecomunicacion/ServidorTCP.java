package com.itson.componentecomunicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Escucha en un puerto. Conexion intermitente
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
                try (Socket conexion = servidor.accept();
                        ObjectInputStream entrada = new ObjectInputStream(conexion.getInputStream())) {
                    receptor.encolar((Mensaje) entrada.readObject());
                } catch (IOException | ClassNotFoundException | ClassCastException e) {
                    System.err.println("Mensaje invalido en el puerto " + puerto + ": " + e.getMessage());
                }
            }
        }, "ServidorTCP-" + puerto);
        hilo.setDaemon(true);
        hilo.start();
    }
}

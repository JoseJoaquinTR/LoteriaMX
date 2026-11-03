package com.itson.componentecomunicacion;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Sujeto del envio. enviar() solo encola y regresa de inmediato; un hilo aparte
 * saca cada mensaje y se lo notifica al observador (ClienteTCP).
 */
public class Dispatcher implements IDispatcher {

    // Lo que espera en la cola: los bytes con su destino. Solo se usa aqui dentro.
    private static class Mensaje {

        private final byte[] datos;
        private final String ip;
        private final int puerto;

        private Mensaje(byte[] datos, String ip, int puerto) {
            this.datos = datos;
            this.ip = ip;
            this.puerto = puerto;
        }
    }

    private final BlockingQueue<Mensaje> cola = new LinkedBlockingQueue<>();
    private IObservadorEnvio observadorEnvio;

    public void setObservadorEnvio(IObservadorEnvio observador) {
        this.observadorEnvio = observador;
    }

    @Override
    public void enviar(byte[] datos, String ip, int puerto) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }
        cola.offer(new Mensaje(datos, ip, puerto));
    }

    public void iniciar() {
        if (observadorEnvio == null) {
            throw new IllegalStateException("No se ha configurado el observador de envio. Use setObservadorEnvio() primero.");
        }
        Thread hilo = new Thread(() -> {
            try {
                while (true) {
                    Mensaje mensaje = cola.take();
                    try {
                        observadorEnvio.enviar(mensaje.datos, mensaje.ip, mensaje.puerto);
                    } catch (IOException e) {
                        // Si el destino no responde se descarta ese mensaje y se sigue con los demas
                        System.err.println("No se pudo enviar a " + mensaje.ip + ":" + mensaje.puerto + ": " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Dispatcher");
        hilo.setDaemon(true);
        hilo.start();
    }
}

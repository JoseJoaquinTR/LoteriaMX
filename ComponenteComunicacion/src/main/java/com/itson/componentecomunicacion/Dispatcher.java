package com.itson.componentecomunicacion;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Cola de salida. enviar() solo encola y regresa de inmediato. Un hilo aparte
 * saca cada mensaje y lo manda con ClienteTCP.
 */
public class Dispatcher implements IDispatcher {

    private final BlockingQueue<Mensaje> cola = new LinkedBlockingQueue<>();
    private ClienteTCP clienteTCP;

    public void setClienteTCP(ClienteTCP clienteTCP) {
        this.clienteTCP = clienteTCP;
    }

    @Override
    public void enviar(Mensaje mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo");
        }
        cola.offer(mensaje);
    }

    public void iniciar() {
        if (clienteTCP == null) {
            throw new IllegalStateException("No se ha configurado el cliente TCP. Use setClienteTCP() primero.");
        }
        Thread hilo = new Thread(() -> {
            try {
                while (true) {
                    Mensaje mensaje = cola.take();
                    try {
                        clienteTCP.enviar(mensaje);
                    } catch (IOException e) {
                        // Si un destino no responde se descarta ese mensaje y se sigue con los demas
                        System.err.println("No se pudo enviar " + mensaje + ": " + e.getMessage());
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

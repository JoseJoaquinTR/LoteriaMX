package com.itson.componentecomunicacion;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Sujeto del envio. enviar() solo encola los bytes y regresa de inmediato; un
 * hilo aparte los saca y se los notifica al observador (ClienteTCP) junto con
 * el destino fijo de este Dispatcher.
 */
public class Dispatcher implements IDispatcher {

    private final BlockingQueue<byte[]> cola = new LinkedBlockingQueue<>();
    private IObservadorEnvio observadorEnvio;
    // volatile: se asignan desde la aplicacion y se leen en el hilo de envio
    private volatile String ip;
    private volatile int puerto;

    public void setObservadorEnvio(IObservadorEnvio observador) {
        this.observadorEnvio = observador;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public void enviar(byte[] datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos");
        }
        cola.offer(datos);
    }

    public void iniciar() {
        if (observadorEnvio == null) {
            throw new IllegalStateException("No se ha configurado el observador de envio. Use setObservadorEnvio() primero.");
        }
        Thread hilo = new Thread(() -> {
            try {
                while (true) {
                    byte[] datos = cola.take();
                    try {
                        observadorEnvio.enviar(datos, ip, puerto);
                    } catch (IOException e) {
                        // Si el destino no responde se descartan esos bytes y se sigue con los demas
                        System.err.println("No se pudo enviar a " + ip + ":" + puerto + ": " + e.getMessage());
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

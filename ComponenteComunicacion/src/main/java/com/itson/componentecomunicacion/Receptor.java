package com.itson.componentecomunicacion;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Sujeto de la recepcion. ServidorTCP encola los bytes que recibe y un hilo
 * aparte se los notifica al observador (IReceptorExterno) uno por uno.
 */
public class Receptor {

    private final BlockingQueue<byte[]> cola = new LinkedBlockingQueue<>();
    private IReceptorExterno receptorExterno;

    public void setReceptorExterno(IReceptorExterno receptorExterno) {
        this.receptorExterno = receptorExterno;
    }

    public void encolar(byte[] datos) {
        cola.offer(datos);
    }

    /**
     * Arranca el hilo que notifica al observador.
     */
    public void iniciar() {
        if (receptorExterno == null) {
            throw new IllegalStateException("No se ha configurado el receptor externo. Use setReceptorExterno() primero.");
        }
        Thread hilo = new Thread(() -> {
            try {
                while (true) {
                    byte[] datos = cola.take();
                    try {
                        receptorExterno.recibir(datos);
                    } catch (RuntimeException e) {
                        // Un error de la aplicacion no debe detener la entrega de los demas
                        System.err.println("Error al entregar datos: " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Receptor");
        hilo.setDaemon(true);
        hilo.start();
    }
}

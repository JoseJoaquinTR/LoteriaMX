package com.itson.componentecomunicacion;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Cola de entrada. ServidorTCP encola lo que recibe y un hilo aparte se lo
 * entrega a la aplicacion uno por uno.
 */
public class Receptor {

    private final BlockingQueue<Mensaje> cola = new LinkedBlockingQueue<>();
    private IReceptorExterno receptorExterno;

    public void setReceptorExterno(IReceptorExterno receptorExterno) {
        this.receptorExterno = receptorExterno;
    }

    public void encolar(Mensaje mensaje) {
        cola.offer(mensaje);
    }

    /**
     * Arranca el hilo que entrega los mensajes. 
     */
    public void iniciar() {
        if (receptorExterno == null) {
            throw new IllegalStateException("No se ha configurado el receptor externo. Use setReceptorExterno() primero.");
        }
        Thread hilo = new Thread(() -> {
            try {
                while (true) {
                    Mensaje mensaje = cola.take();
                    try {
                        receptorExterno.recibir(mensaje);
                    } catch (RuntimeException e) {
                        // Un error de la aplicacion no debe detener la entrega de los demas mensajes
                        System.err.println("Error al entregar " + mensaje + ": " + e.getMessage());
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

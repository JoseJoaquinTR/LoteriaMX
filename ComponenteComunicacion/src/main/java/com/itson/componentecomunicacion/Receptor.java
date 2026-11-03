package com.itson.componentecomunicacion;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Sujeto de la recepcion. ServidorTCP encola lo que llega y un hilo aparte se
 * lo notifica al observador (IReceptorExterno) uno por uno.
 */
public class Receptor {

    // Lo que espera en la cola: los bytes con la IP de quien los envio. Solo se usa aqui dentro.
    private static class Mensaje {

        private final byte[] datos;
        private final String ipOrigen;

        private Mensaje(byte[] datos, String ipOrigen) {
            this.datos = datos;
            this.ipOrigen = ipOrigen;
        }
    }

    private final BlockingQueue<Mensaje> cola = new LinkedBlockingQueue<>();
    private IReceptorExterno receptorExterno;

    public void setReceptorExterno(IReceptorExterno receptorExterno) {
        this.receptorExterno = receptorExterno;
    }

    public void encolar(byte[] datos, String ipOrigen) {
        cola.offer(new Mensaje(datos, ipOrigen));
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
                    Mensaje mensaje = cola.take();
                    try {
                        receptorExterno.recibir(mensaje.datos, mensaje.ipOrigen);
                    } catch (RuntimeException e) {
                        // Un error de la aplicacion no debe detener la entrega de los demas
                        System.err.println("Error al entregar datos de " + mensaje.ipOrigen + ": " + e.getMessage());
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

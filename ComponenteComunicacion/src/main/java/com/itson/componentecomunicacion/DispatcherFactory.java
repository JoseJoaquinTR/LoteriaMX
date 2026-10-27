package com.itson.componentecomunicacion;

import java.io.IOException;

/**
 * Ensamblador del componente: crea las clases concretas, las conecta por
 * setters y las arranca.
 */
public class DispatcherFactory {

    public IDispatcher crearDispatcher() {
        ClienteTCP clienteTCP = new ClienteTCP();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setClienteTCP(clienteTCP);
        dispatcher.iniciar();
        return dispatcher;
    }

    /**
     * Empieza a escuchar en el puerto y entrega cada mensaje recibido a externo.
     */
    public Receptor crearReceptor(int puerto, IReceptorExterno externo) throws IOException {
        Receptor receptor = new Receptor();
        receptor.setReceptorExterno(externo);
        receptor.iniciar();

        ServidorTCP servidorTCP = new ServidorTCP();
        servidorTCP.setPuerto(puerto);
        servidorTCP.setReceptor(receptor);
        servidorTCP.iniciar();
        return receptor;
    }
}

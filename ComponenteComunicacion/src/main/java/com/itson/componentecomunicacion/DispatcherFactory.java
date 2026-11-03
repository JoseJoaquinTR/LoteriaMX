package com.itson.componentecomunicacion;

/**
 * Crea el Dispatcher, le inyecta por setter la unica instancia de ClienteTCP
 * como observador y lo arranca. La aplicacion solo recibe IDispatcher.
 */
public class DispatcherFactory {

    public IDispatcher crearDispatcher() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setObservadorEnvio(ClienteTCP.getInstance());
        dispatcher.iniciar();
        return dispatcher;
    }
}

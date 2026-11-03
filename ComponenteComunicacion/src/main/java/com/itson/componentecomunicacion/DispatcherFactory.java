package com.itson.componentecomunicacion;

/**
 * Crea el Dispatcher, le inyecta por setter el observador que recibe (por
 * ejemplo, un ClienteTCP creado por la aplicacion) y lo arranca. La aplicacion
 * solo recibe IDispatcher.
 */
public class DispatcherFactory {

    public IDispatcher crearDispatcher(IObservadorEnvio observador) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setObservadorEnvio(observador);
        dispatcher.iniciar();
        return dispatcher;
    }
}

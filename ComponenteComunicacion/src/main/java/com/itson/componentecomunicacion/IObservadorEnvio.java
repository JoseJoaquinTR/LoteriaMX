package com.itson.componentecomunicacion;

import java.io.IOException;

/**
 * Observador del Dispatcher. Se le avisa cada vez que hay bytes para enviar.
 */
public interface IObservadorEnvio {

    public void enviar(byte[] datos, String ip, int puerto) throws IOException;
}

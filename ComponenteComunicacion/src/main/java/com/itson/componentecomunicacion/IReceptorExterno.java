package com.itson.componentecomunicacion;

/**
 * Observador del Receptor. Lo implementa la aplicacion que usa el componente
 * y se le avisa cada vez que llegan bytes.
 */
public interface IReceptorExterno {

    public void recibir(byte[] datos);
}

package com.itson.componentecomunicacion;

/**
 * Observador del Receptor. Lo implementa la aplicacion que usa el componente
 * y se le avisa cada vez que llegan bytes, junto con la IP de quien los envio.
 */
public interface IReceptorExterno {

    public void recibir(byte[] datos, String ipOrigen);
}

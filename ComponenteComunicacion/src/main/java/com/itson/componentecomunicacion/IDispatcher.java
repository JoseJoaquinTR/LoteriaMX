package com.itson.componentecomunicacion;

/**
 * Lo usa la aplicacion para mandar bytes a una IP y puerto.
 */
public interface IDispatcher {

    public void enviar(byte[] datos, String ip, int puerto);
}

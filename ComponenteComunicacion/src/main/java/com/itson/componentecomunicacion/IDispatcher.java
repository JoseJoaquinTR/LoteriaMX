package com.itson.componentecomunicacion;

/**
 * Lo usa la aplicacion para mandar bytes. Cada Dispatcher tiene un destino
 * fijo que se configura con setIp() y setPuerto().
 */
public interface IDispatcher {

    public void enviar(byte[] datos);

    public void setIp(String ip);

    public void setPuerto(int puerto);
}

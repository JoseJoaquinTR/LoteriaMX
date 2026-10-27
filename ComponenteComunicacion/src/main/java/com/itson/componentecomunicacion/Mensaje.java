package com.itson.componentecomunicacion;

import java.io.Serializable;

/**
 * Mensaje que viaja por la red.
 * no conoce las clases del juego, el contenido va como texto y lo interpreta quien lo recibe.
 */
public class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tipo;
    private String contenido;
    // Como la conexion se cierra al enviar, el receptor necesita saber a donde responder
    private String ipOrigen;
    private int puertoOrigen;
    // El destino viaja con el mensaje porque espera en la cola del Dispatcher
    private String ipDestino;
    private int puertoDestino;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    public int getPuertoOrigen() {
        return puertoOrigen;
    }

    public void setPuertoOrigen(int puertoOrigen) {
        this.puertoOrigen = puertoOrigen;
    }

    public String getIpDestino() {
        return ipDestino;
    }

    public void setIpDestino(String ipDestino) {
        this.ipDestino = ipDestino;
    }

    public int getPuertoDestino() {
        return puertoDestino;
    }

    public void setPuertoDestino(int puertoDestino) {
        this.puertoDestino = puertoDestino;
    }

    @Override
    public String toString() {
        return tipo + " " + ipOrigen + ":" + puertoOrigen + " -> " + ipDestino + ":" + puertoDestino + " [" + contenido + "]";
    }
}

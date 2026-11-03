package com.itson.componentecomunicacion;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Observador del Dispatcher: envia los bytes con una conexion intermitente
 * (abre, escribe y cierra). Es singleton: como no guarda ninguna conexion
 * abierta, una sola instancia sirve para todos los Dispatcher.
 */
public class ClienteTCP implements IObservadorEnvio {

    private static final ClienteTCP instancia = new ClienteTCP();

    private ClienteTCP() {
    }

    public static ClienteTCP getInstance() {
        return instancia;
    }

    @Override
    public void enviar(byte[] datos, String ip, int puerto) throws IOException {
        try (Socket socket = new Socket(ip, puerto)) {
            OutputStream salida = socket.getOutputStream();
            salida.write(datos);
            salida.flush();
        }
    }
}

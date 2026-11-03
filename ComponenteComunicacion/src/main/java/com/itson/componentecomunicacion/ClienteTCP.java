package com.itson.componentecomunicacion;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Observador del Dispatcher: envia los bytes con una conexion intermitente
 * (abre, escribe y cierra).
 */
public class ClienteTCP implements IObservadorEnvio {

    @Override
    public void enviar(byte[] datos, String ip, int puerto) throws IOException {
        try (Socket socket = new Socket(ip, puerto)) {
            OutputStream salida = socket.getOutputStream();
            salida.write(datos);
            salida.flush();
        }
    }
}

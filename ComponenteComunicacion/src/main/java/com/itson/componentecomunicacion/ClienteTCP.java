package com.itson.componentecomunicacion;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Envia un mensaje con una conexion intermitente.
 */
public class ClienteTCP {

    public void enviar(Mensaje mensaje) throws IOException {
        try (Socket socket = new Socket(mensaje.getIpDestino(), mensaje.getPuertoDestino());
                ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream())) {
            salida.writeObject(mensaje);
            salida.flush();
        }
    }
}

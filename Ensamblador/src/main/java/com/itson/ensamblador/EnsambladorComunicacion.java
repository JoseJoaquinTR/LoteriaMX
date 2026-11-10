package com.itson.ensamblador;

import com.itson.componentecomunicacion.DispatcherFactory;
import com.itson.componentecomunicacion.IDispatcher;
import com.itson.componentecomunicacion.IReceptorExterno;
import com.itson.componentecomunicacion.Receptor;
import com.itson.componentecomunicacion.ServidorTCP;
import java.io.IOException;

/**
 * Arma una instancia del componente de comunicacion. Quien lo usa solo conoce
 * IDispatcher (para enviar) e IReceptorExterno (para recibir).
 */
public class EnsambladorComunicacion {

    public static IDispatcher ensamblar(int puertoLocal, IReceptorExterno receptorExterno) throws IOException {
        Receptor receptor = new Receptor();
        receptor.setReceptorExterno(receptorExterno);
        receptor.iniciar();

        ServidorTCP servidorTCP = new ServidorTCP();
        servidorTCP.setPuerto(puertoLocal);
        servidorTCP.setReceptor(receptor);
        servidorTCP.iniciar();

        return new DispatcherFactory().crearDispatcher();
    }
}

package com.itson.pruebacomunicacion;

import com.itson.componentecomunicacion.IDispatcher;
import com.itson.ensamblador.EnsambladorComunicacion;
import java.io.IOException;
import java.util.Scanner;

/**
 * Escucha en 5001 y envia a MainB (5002) cada linea escrita.
 */
public class MainA {

    public static void main(String[] args) throws IOException {
        IDispatcher dispatcher = EnsambladorComunicacion.ensamblar(5001,
                (datos, ipOrigen) -> System.out.println("Recibido de " + ipOrigen + ": " + new String(datos)));

        Scanner teclado = new Scanner(System.in);
        while (teclado.hasNextLine()) {
            dispatcher.enviar(teclado.nextLine().getBytes(), "localhost", 5002);
        }
    }
}

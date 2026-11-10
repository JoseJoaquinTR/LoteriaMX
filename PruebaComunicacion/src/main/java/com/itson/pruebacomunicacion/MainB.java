package com.itson.pruebacomunicacion;

import com.itson.componentecomunicacion.IDispatcher;
import com.itson.ensamblador.EnsambladorComunicacion;
import java.io.IOException;
import java.util.Scanner;

/**
 * Escucha en 5002 y envia a MainA (5001) cada linea escrita.
 */
public class MainB {

    public static void main(String[] args) throws IOException {
        IDispatcher dispatcher = EnsambladorComunicacion.ensamblar(5002,
                (datos, ipOrigen) -> System.out.println("Recibido de " + ipOrigen + ": " + new String(datos)));

        Scanner teclado = new Scanner(System.in);
        while (teclado.hasNextLine()) {
            dispatcher.enviar(teclado.nextLine().getBytes(), "localhost", 5001);
        }
    }
}

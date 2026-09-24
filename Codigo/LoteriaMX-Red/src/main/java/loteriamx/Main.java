package loteriamx;

import loteriamx.dominio.*;
import loteriamx.modelo.ModeloPartida;
import loteriamx.red.ClienteGriton;


public class Main {

    public static void main(String[] args) throws Exception {
       
        Casilla[] casillas = new Casilla[16];
        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                int idx = f * 4 + c;
                casillas[idx] = new Casilla(f, c, new Carta(idx + 1, "Carta" + (idx + 1), null));
            }
        }
        Tablero tablero = new Tablero(casillas);
        
        Puntaje puntaje = new Puntaje();
        ModeloPartida modelo = new ModeloPartida(new DominioLoteria(), 0, tablero, puntaje);

        modelo.agregarObserver(m -> {
            if (m.getCarta() != null) {
                System.out.println("[CARTA] " + m.getCarta().getNombre());
            }
            if (m.getCasillaMarcada() != null) {
                System.out.println("[CASILLA] jugador " + m.getIdJugadorAfectado()+ " marco fila " + m.getCasillaMarcada().getFila() + " col " + m.getCasillaMarcada().getColumna());
            }
            if (m.getJugada() != null && m.getResultadoJugada() == ResultadoJugada.MARCADA) {
                System.out.println("[JUGADA] jugador " + m.getIdJugadorAfectado()+ " reclamo " + m.getJugada());
            }
            if (m.getMensaje() != null) {
                System.out.println("[MENSAJE] " + m.getMensaje());
            }
        });

        try {
            ClienteGriton griton = new ClienteGriton(modelo);
            griton.conectar("localhost", 5000);
            
            System.out.println("Conectado al Griton en localhost:5000");
            
        } catch (java.io.IOException e) {
            System.out.println("No se pudo conectar al Griton: " + e.getMessage());
        }
        System.out.println("Escuchando. Presiona ENTER para salir");
        System.in.read();
    }
}

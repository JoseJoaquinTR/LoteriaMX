package loteriamx;

import griton.dominio.*;
import loteriamx.control.Controlador;
import loteriamx.modelo.ModeloPartida;
import loteriamx.red.ClienteGriton;

import java.io.IOException;

public class Main {

    private static final String HOST_GRITON = "localhost";
    private static final int PUERTO_GRITON = 5000;
    private static final int ID_JUGADOR_LOCAL = 0;

    public static void main(String[] args) {
        Tablero tablero = crearTablero();
        Puntaje puntaje = new Puntaje();
        ModeloPartida modelo = new ModeloPartida(new DominioLoteria(), ID_JUGADOR_LOCAL, tablero, puntaje);
        Controlador controlador = new Controlador(modelo);

        conectarGriton(modelo);
    }

    private static Tablero crearTablero() {
        Casilla[][] casillas = new Casilla[4][4];
        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                int numeroCarta = (f * 4 + c) + 1;
                casillas[f][c] = new Casilla(f, c, new Carta(numeroCarta, "Carta" + numeroCarta, null));
            }
        }
        return new Tablero(casillas);
    }

    private static void conectarGriton(ModeloPartida modelo) {
        try {
            /*SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    FrmPartida frm = new FrmPartida(modelo, controlador);
                    frm.setVisible(true);
                    conectarGriton(modelo);
                }
            });*/
            ClienteGriton griton = new ClienteGriton(modelo);
            griton.conectar(HOST_GRITON, PUERTO_GRITON);
            System.out.println("Conectado al Griton en " + HOST_GRITON + ":" + PUERTO_GRITON);
        } catch (IOException e) {
            System.out.println("No se pudo conectar al Griton: " + e.getMessage());
        }
    }
}
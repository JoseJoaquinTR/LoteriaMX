package loteriamx;

import griton.Mazo;
import griton.dominio.*;
import loteriamx.control.Controlador;
import loteriamx.modelo.ModeloPartida;
import loteriamx.red.ClienteGriton;
import loteriamx.vista.FrmPartida;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;

public class Main {

    private static final String HOST_GRITON = "localhost";
    private static final int PUERTO_GRITON = 5000;
    private static final int ID_JUGADOR_LOCAL = 0;
    private static final Nivel NIVEL = Nivel.BASICO;

    // Datos de prueba: entre 1 y 3 jugadores remotos (vista de 2, 3 o 4 jugadores)
    private static final int JUGADORES_REMOTOS_PRUEBA = 3;

    public static void main(String[] args) {
        Jugador jugadorLocal = new Jugador(ID_JUGADOR_LOCAL, "Jugador1", "/avatars/image 1.png", crearTablero(), new Puntaje());
        ModeloPartida modelo = new ModeloPartida(new DominioLoteria(), jugadorLocal, NIVEL);
        agregarJugadoresPrueba(modelo);
        Controlador controlador = new Controlador(modelo);

        SwingUtilities.invokeLater(() -> {
            FrmPartida frm = new FrmPartida(modelo, controlador);
            modelo.agregarObserver(frm);
            frm.setVisible(true);
            conectarGriton(modelo);
        });
    }

    private static void agregarJugadoresPrueba(ModeloPartida modelo) {
        for (int i = 1; i <= JUGADORES_REMOTOS_PRUEBA; i++) {
            int numero = i + 1;
            modelo.agregarJugadorRemoto(new Jugador(i, "Jugador " + numero, "/avatars/image " + numero + ".png", crearTablero(), new Puntaje()));
        }
    }

    // 16 cartas distintas al azar de las 54 del mazo
    private static Tablero crearTablero() {
        List<Integer> numeros = new ArrayList<>();
        for (int n = 1; n <= Mazo.NOMBRES_CARTAS.length; n++) {
            numeros.add(n);
        }
        Collections.shuffle(numeros);

        Casilla[][] casillas = new Casilla[4][4];
        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                int numeroCarta = numeros.get(f * 4 + c);
                casillas[f][c] = new Casilla(f, c, new Carta(numeroCarta, Mazo.NOMBRES_CARTAS[numeroCarta - 1], null));
            }
        }
        return new Tablero(casillas);
    }

    private static void conectarGriton(ModeloPartida modelo) {
        try {
            ClienteGriton griton = new ClienteGriton(modelo);
            griton.conectar(HOST_GRITON, PUERTO_GRITON);
            System.out.println("Conectado al Griton en " + HOST_GRITON + ":" + PUERTO_GRITON);
        } catch (IOException e) {
            System.out.println("No se pudo conectar al Griton: " + e.getMessage());
        }
    }
}

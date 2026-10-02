package griton.dominio;

import java.io.Serializable;

/**
 *
 * Jugadaor
 */
public class Jugador implements Serializable {

    private final int id;
    private final Tablero tablero;
    private final Puntaje puntaje;

    public Jugador(int id, Tablero tablero, Puntaje puntaje) {
        this.id = id;
        this.tablero = tablero;
        this.puntaje = puntaje;
    }

    public void marcarCasilla(int fila, int columna) {
        tablero.obtenerCasilla(fila, columna).marcar();
    }

    public void marcarJugada(TipoJugada tipo) {
        puntaje.marcar(tipo);
    }

    public int getId() {
        return id;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public Puntaje getPuntaje() {
        return puntaje;
    }
}

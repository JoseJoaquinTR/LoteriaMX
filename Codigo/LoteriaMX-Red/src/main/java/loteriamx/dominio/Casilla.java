package loteriamx.dominio;

import java.io.Serializable;

public class Casilla implements Serializable {

    private final int fila;
    private final int columna;
    private final Carta carta;
    private boolean marcada;

    public Casilla(int fila, int columna, Carta carta) {
        this.fila = fila;
        this.columna = columna;
        this.carta = carta;
        this.marcada = false;
    }

    public boolean estaMarcada() {
        return marcada;
    }

    public void marcar() {
        this.marcada = true;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public Carta getCarta() {
        return carta;
    }
}

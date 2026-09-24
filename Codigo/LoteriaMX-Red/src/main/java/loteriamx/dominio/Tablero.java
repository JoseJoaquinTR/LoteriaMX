package loteriamx.dominio;

import java.io.Serializable;

public class Tablero implements Serializable {

    private static final int FILAS = 4;
    private static final int COLUMNAS = 4;

    //matriz bidimensional
    private final Casilla[][] casillas; 

    public Tablero(Casilla[][] casillas) {
        if (casillas.length != FILAS || casillas[0].length != COLUMNAS) {
            throw new IllegalArgumentException("El tablero debe ser una matriz de " + FILAS + "x" + COLUMNAS);
        }
        this.casillas = casillas;
    }

    public Casilla obtenerCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }
}


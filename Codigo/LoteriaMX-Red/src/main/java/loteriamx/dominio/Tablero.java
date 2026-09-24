package loteriamx.dominio;

import java.io.Serializable;

public class Tablero implements Serializable {

    private static final int FILAS = 4;
    private static final int COLUMNAS = 4;

    private final Casilla[] casillas; 

    public Tablero(Casilla[] casillas) {
        if (casillas.length != FILAS * COLUMNAS) {
            throw new IllegalArgumentException("El tablero debe tener 16 casillas");
        }
        this.casillas = casillas;
    }

    public Casilla obtenerCasilla(int fila, int columna) {
        return casillas[fila * COLUMNAS + columna];
    }

    public Casilla[] getCasillas() {
        return casillas;
    }
}

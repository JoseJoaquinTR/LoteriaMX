package griton.dominio;

import java.io.Serializable;

public class Puntaje implements Serializable {

    private boolean llena;
    private boolean chorro;
    private boolean cuatroEsquinas;
    private boolean centro;

    public boolean estaDisponible(TipoJugada tipo) {
        switch (tipo) {
            case LLENA:
                return !llena;
            case CHORRO:
                return !chorro;
            case CUATRO_ESQUINAS:
                return !cuatroEsquinas;
            case CENTRO:
                return !centro;
            default:
                return false;
        }
    }

    public void marcar(TipoJugada tipo) {
        switch (tipo) {
            case LLENA ->
                llena = true;
            case CHORRO ->
                chorro = true;
            case CUATRO_ESQUINAS ->
                cuatroEsquinas = true;
            case CENTRO ->
                centro = true;
        }
    }

    public boolean esValida(TipoJugada tipo, Tablero tablero) {
        return switch (tipo) {
            case LLENA ->
                todasMarcadas(tablero, todasLasCasillas());
            case CHORRO ->
                hayChorro(tablero);
            case CUATRO_ESQUINAS ->
                todasMarcadas(tablero, new int[][]{{0, 0}, {0, 3}, {3, 0}, {3, 3}});
            case CENTRO ->
                todasMarcadas(tablero, new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}});
            default ->
                false;
        };
    }

    // Chorro: una fila, una columna o una diagonal completa
    private boolean hayChorro(Tablero tablero) {
        int[][] diagonal = new int[4][2];
        int[][] diagonalInversa = new int[4][2];
        for (int i = 0; i < 4; i++) {
            int[][] fila = new int[4][2];
            int[][] columna = new int[4][2];
            for (int j = 0; j < 4; j++) {
                fila[j] = new int[]{i, j};
                columna[j] = new int[]{j, i};
            }
            if (todasMarcadas(tablero, fila) || todasMarcadas(tablero, columna)) {
                return true;
            }
            diagonal[i] = new int[]{i, i};
            diagonalInversa[i] = new int[]{i, 3 - i};
        }
        return todasMarcadas(tablero, diagonal) || todasMarcadas(tablero, diagonalInversa);
    }

    private int[][] todasLasCasillas() {
        int[][] pos = new int[16][2];
        int i = 0;
        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                pos[i][0] = f;
                pos[i][1] = c;
                i++;
            }
        }
        return pos;
    }

    private boolean todasMarcadas(Tablero tablero, int[][] posiciones) {
        for (int[] p : posiciones) {
            if (!tablero.obtenerCasilla(p[0], p[1]).estaMarcada()) {
                return false;
            }
        }
        return true;
    }

    public boolean isLlena() {
        return llena;
    }

    public boolean isChorro() {
        return chorro;
    }

    public boolean isCuatroEsquinas() {
        return cuatroEsquinas;
    }

    public boolean isCentro() {
        return centro;
    }
}

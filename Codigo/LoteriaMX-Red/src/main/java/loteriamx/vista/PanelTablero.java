package loteriamx.vista;

import griton.dominio.Casilla;
import griton.dominio.Tablero;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.JPanel;

/**
 * PanelTablero: cuadricula de 16 cartas de un jugador. 
 */
class PanelTablero extends JPanel {

    private static final int TAMANO = 4;

    private final Tablero tablero;
    private final int anchoCelda;
    private final int altoCelda;
    private final int separacion;

    PanelTablero(Tablero tablero, int anchoCelda, int altoCelda, int separacion, Consumer<Casilla> alSeleccionar) {
        this.tablero = tablero;
        this.anchoCelda = anchoCelda;
        this.altoCelda = altoCelda;
        this.separacion = separacion;
        setOpaque(false);
        setPreferredSize(new Dimension(
                TAMANO * anchoCelda + (TAMANO - 1) * separacion,
                TAMANO * altoCelda + (TAMANO - 1) * separacion));
        setMaximumSize(getPreferredSize());

        if (alSeleccionar != null) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int columna = e.getX() / (anchoCelda + separacion);
                    int fila = e.getY() / (altoCelda + separacion);
                    if (fila < TAMANO && columna < TAMANO) {
                        alSeleccionar.accept(tablero.obtenerCasilla(fila, columna));
                    }
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = FrmPartida.suavizar(g);
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                Casilla casilla = tablero.obtenerCasilla(fila, columna);
                int x = columna * (anchoCelda + separacion);
                int y = fila * (altoCelda + separacion);
                FrmPartida.pintarCarta(g2, casilla.getCarta(), x, y, anchoCelda, altoCelda);
                if (casilla.estaMarcada()) {
                    FrmPartida.pintarFrijol(g2, x, y, anchoCelda, altoCelda);
                }
            }
        }
        g2.dispose();
    }
}

package loteriamx.vista;

import griton.dominio.Carta;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * PanelHistorial: ultimas cartas ya gritadas
 * La cantidad depende del nivel de la partida.
 */
class PanelHistorial extends JPanel {

    private static final int SEPARACION = 20;

    private final int cantidad;
    private final int anchoCarta;
    private final int altoCarta;
    private List<Carta> historial = new ArrayList<>();

    PanelHistorial(int cantidad, int anchoCarta, int altoCarta) {
        this.cantidad = cantidad;
        this.anchoCarta = anchoCarta;
        this.altoCarta = altoCarta;
        setOpaque(false);
        int alto = cantidad == 0 ? 0 : cantidad * altoCarta + (cantidad - 1) * SEPARACION;
        setPreferredSize(new Dimension(cantidad == 0 ? 0 : anchoCarta, alto));
    }

    void setHistorial(List<Carta> historial) {
        this.historial = historial;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = FrmPartida.suavizar(g);
        // La ultima del historial es la carta actual (se muestra en PanelCarta)
        int y = 0;
        for (int i = historial.size() - 2; i >= 0 && i >= historial.size() - 1 - cantidad; i--) {
            FrmPartida.pintarCarta(g2, historial.get(i), 0, y, anchoCarta, altoCarta);
            y += altoCarta + SEPARACION;
        }
        g2.dispose();
    }
}

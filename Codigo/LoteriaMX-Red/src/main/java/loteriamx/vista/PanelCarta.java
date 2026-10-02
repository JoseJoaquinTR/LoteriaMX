package loteriamx.vista;

import griton.dominio.Carta;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * PanelCarta: ultima carta gritada.
 */
class PanelCarta extends JPanel {

    private Carta carta;

    PanelCarta(int ancho, int alto) {
        setOpaque(false);
        setPreferredSize(new Dimension(ancho, alto));
    }

    void setCarta(Carta carta) {
        this.carta = carta;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (carta == null) {
            return;
        }
        Graphics2D g2 = FrmPartida.suavizar(g);
        FrmPartida.pintarCarta(g2, carta, 0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}

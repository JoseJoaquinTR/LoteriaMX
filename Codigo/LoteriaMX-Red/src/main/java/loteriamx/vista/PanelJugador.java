package loteriamx.vista;

import griton.dominio.Jugador;
import griton.dominio.Puntaje;
import griton.dominio.TipoJugada;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;

/**
 * PanelJugador: avatar, nombre y lista de puntajes de un jugador. 
 */
class PanelJugador extends JPanel {

    private static final TipoJugada[] ORDEN_LOCAL = {
        TipoJugada.LLENA, TipoJugada.CHORRO, TipoJugada.CUATRO_ESQUINAS, TipoJugada.CENTRO};
    private static final TipoJugada[] ORDEN_REMOTO = {
        TipoJugada.CHORRO, TipoJugada.CUATRO_ESQUINAS, TipoJugada.CENTRO, TipoJugada.LLENA};

    private final Jugador jugador;
    private final Map<TipoJugada, JRadioButton> opciones = new EnumMap<>(TipoJugada.class);

    PanelJugador(Jugador jugador, Consumer<TipoJugada> alMarcar) {
        this.jugador = jugador;
        boolean local = alMarcar != null;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int diametroAvatar = local ? 64 : 34;
        int tamanoNombre = local ? 30 : 14;
        int tamanoOpcion = local ? 20 : 13;

        JPanel encabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, local ? 18 : 8, 0));
        encabezado.setOpaque(false);
        encabezado.add(new Avatar(diametroAvatar));
        JLabel nombre = new JLabel(jugador.getNombre());
        nombre.setFont(FrmPartida.fuente(tamanoNombre));
        nombre.setForeground(local ? FrmPartida.NARANJA_NOMBRE : FrmPartida.CAFE_NOMBRE);
        encabezado.add(nombre);
        encabezado.setAlignmentX(LEFT_ALIGNMENT);
        encabezado.setMaximumSize(encabezado.getPreferredSize());
        add(encabezado);

        if (local) {
            add(Box.createVerticalStrut(14));
            JLabel titulo = new JLabel("Puntajes");
            titulo.setFont(FrmPartida.fuente(26));
            titulo.setForeground(FrmPartida.CAFE_PUNTAJES);
            titulo.setBorder(BorderFactory.createEmptyBorder(0, diametroAvatar + 18, 0, 0));
            titulo.setAlignmentX(LEFT_ALIGNMENT);
            add(titulo);
        }
        add(Box.createVerticalStrut(local ? 12 : 8));

        // Sin ButtonGroup: un jugador puede tener varias jugadas marcadas
        for (TipoJugada tipo : local ? ORDEN_LOCAL : ORDEN_REMOTO) {
            JRadioButton opcion = new JRadioButton(texto(tipo));
            opcion.setOpaque(false);
            opcion.setFocusPainted(false);
            opcion.setFont(FrmPartida.fuente(tamanoOpcion));
            opcion.setForeground(FrmPartida.NARANJA_OPCION);
            opcion.setIconTextGap(local ? 26 : 14);
            opcion.setBorder(BorderFactory.createEmptyBorder(local ? 3 : 2, local ? 40 : 14, local ? 3 : 2, 6));
            opcion.setAlignmentX(LEFT_ALIGNMENT);
            if (local) {
                opcion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                opcion.setFocusable(false);
            }
            // El clic cambia el radio por si solo; despues se deja como diga el Puntaje
            opcion.addActionListener(e -> {
                if (local) {
                    alMarcar.accept(tipo);
                }
                actualizar();
            });
            opciones.put(tipo, opcion);
            add(opcion);
        }
        actualizar();
    }

    /**
     * Marca los radios segun las jugadas que el jugador ya tiene en su
     * Puntaje.
     */
    void actualizar() {
        Puntaje puntaje = jugador.getPuntaje();
        opciones.forEach((tipo, opcion) -> opcion.setSelected(estaMarcada(puntaje, tipo)));
    }

    private static String texto(TipoJugada tipo) {
        return switch (tipo) {
            case LLENA -> "Llena";
            case CHORRO -> "Chorro";
            case CUATRO_ESQUINAS -> "4 esquinas";
            case CENTRO -> "Centro";
        };
    }

    private static boolean estaMarcada(Puntaje puntaje, TipoJugada tipo) {
        return switch (tipo) {
            case LLENA -> puntaje.isLlena();
            case CHORRO -> puntaje.isChorro();
            case CUATRO_ESQUINAS -> puntaje.isCuatroEsquinas();
            case CENTRO -> puntaje.isCentro();
        };
    }

    private class Avatar extends JComponent {

        private final int diametro;

        Avatar(int diametro) {
            this.diametro = diametro;
            setPreferredSize(new Dimension(diametro, diametro));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = FrmPartida.suavizar(g);
            FrmPartida.pintarAvatar(g2, jugador.getAvatar(), jugador.getNombre(), 0, 0, diametro);
            g2.dispose();
        }
    }
}

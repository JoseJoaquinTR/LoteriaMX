package loteriamx.vista;

import griton.dominio.Carta;
import griton.dominio.Jugador;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import loteriamx.control.Controlador;
import loteriamx.modelo.IModelo;
import loteriamx.modelo.IObservador;


public class FrmPartida extends JFrame implements IObservador {

    //colores
    static final Color MARCO = new Color(0x1E1E1E);
    static final Color FONDO = new Color(0xFDEAE6);
    static final Color NARANJA_OPCION = new Color(0xF5A31A);
    static final Color NARANJA_NOMBRE = new Color(0xF7931E);
    static final Color CAFE_PUNTAJES = new Color(0xA67C62);
    static final Color CAFE_NOMBRE = new Color(0x7B3F2E);
    static final Color ROJO_SALIR = new Color(0xC8101E);
    static final Color FONDO_AVATAR = new Color(0x9FD3E6);
    static final Color FONDO_CARTA = new Color(0xFFF8E7);
    static final Color FRIJOL = new Color(0x6B3A1E);
    private static final String FUENTE = "Comic Sans MS";
    private static final int ANCHO_MINIMO = 1100;

    // Imagenes
    private static final Map<String, BufferedImage> imagenes = new HashMap<>();

    private final IModelo modelo;
    private final Controlador controlador;

    private final PanelCarta panelCarta;
    private final PanelHistorial panelHistorial;
    private final PanelTablero panelTablero;
    private final PanelJugador panelJugador;
    private final JPanel panelRemotos;
    private final Map<Integer, JPanel> remotos = new HashMap<>();
    private final Map<Integer, PanelJugador> datosRemotos = new HashMap<>();

    public FrmPartida(IModelo modelo, Controlador controlador) {
        super("Lotería Mexicana");
        this.modelo = modelo;
        this.controlador = controlador;

        Jugador local = modelo.getJugadorLocal();
        panelCarta = new PanelCarta(160, 260);
        panelHistorial = new PanelHistorial(cartasHistorial(), 56, 84);
        panelTablero = new PanelTablero(local.getTablero(), 62, 92, 3, controlador::marcarCasilla);
        panelJugador = new PanelJugador(local, controlador::marcarJugada);

        panelRemotos = new JPanel(new FlowLayout(FlowLayout.CENTER, 36, 0));
        panelRemotos.setOpaque(false);
        for (Jugador remoto : modelo.getJugadoresRemotos()) {
            agregarRemoto(remoto);
        }

        JPanel fondo = new FondoRedondeado();
        fondo.setLayout(new BorderLayout(0, 20));
        fondo.setBorder(BorderFactory.createEmptyBorder(14, 20, 12, 14));
        fondo.add(panelRemotos, BorderLayout.NORTH);
        fondo.add(crearPanelInferior(), BorderLayout.CENTER);

        JPanel marco = new JPanel(new BorderLayout());
        marco.setBackground(MARCO);
        marco.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        marco.add(fondo);
        setContentPane(marco);

        panelCarta.setCarta(modelo.getCarta());
        panelHistorial.setHistorial(new ArrayList<>(modelo.getHistorial()));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        // Mismo ancho para la vista de 2, 3 o 4 jugadores
        setSize(Math.max(getWidth(), ANCHO_MINIMO), getHeight());
        setLocationRelativeTo(null);
    }

    private int cartasHistorial() {
        return switch (modelo.getNivel()) {
            case BASICO -> 3;
            case INTERMEDIO -> 2;
            case EXPERTO -> 0;
        };
    }

    private JPanel crearPanelInferior() {
        JPanel inferior = new JPanel(new GridBagLayout());
        inferior.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 30, 0, 40);
        inferior.add(panelHistorial, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 80);
        inferior.add(panelCarta, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 30);
        inferior.add(panelTablero, gbc);

        JPanel columnaLocal = new JPanel(new BorderLayout());
        columnaLocal.setOpaque(false);
        columnaLocal.add(panelJugador, BorderLayout.NORTH);
        JPanel filaSalir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        filaSalir.setOpaque(false);
        filaSalir.add(crearBotonSalir());
        columnaLocal.add(filaSalir, BorderLayout.SOUTH);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        inferior.add(columnaLocal, gbc);
        return inferior;
    }

    private JButton crearBotonSalir() {
        JButton salir = new JButton("Salir") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = suavizar(g);
                g2.setColor(ROJO_SALIR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        salir.setFont(fuente(24));
        salir.setForeground(Color.WHITE);
        salir.setContentAreaFilled(false);
        salir.setBorderPainted(false);
        salir.setFocusPainted(false);
        salir.setBorder(BorderFactory.createEmptyBorder(2, 14, 2, 14));
        salir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        salir.addActionListener(e -> dispose());
        return salir;
    }

    private void agregarRemoto(Jugador jugador) {
        JPanel bloque = new JPanel();
        bloque.setOpaque(false);
        bloque.setLayout(new BoxLayout(bloque, BoxLayout.X_AXIS));
        PanelJugador datos = new PanelJugador(jugador, null);
        PanelTablero tablero = new PanelTablero(jugador.getTablero(), 36, 52, 2, null);
        datos.setAlignmentY(TOP_ALIGNMENT);
        tablero.setAlignmentY(TOP_ALIGNMENT);
        bloque.add(datos);
        bloque.add(Box.createHorizontalStrut(10));
        bloque.add(tablero);
        remotos.put(jugador.getId(), bloque);
        datosRemotos.put(jugador.getId(), datos);
        panelRemotos.add(bloque);
    }

    // Las notificaciones pueden llegar desde los hilos de red: se toman los
    // datos del modelo aqui y se pintan en el hilo de Swing.
    @Override
    public void updateCarta(IModelo modelo) {
        Carta carta = modelo.getCarta();
        SwingUtilities.invokeLater(() -> panelCarta.setCarta(carta));
    }

    @Override
    public void updateHistorial(IModelo modelo) {
        List<Carta> historial = new ArrayList<>(modelo.getHistorial());
        SwingUtilities.invokeLater(() -> panelHistorial.setHistorial(historial));
    }

    @Override
    public void updateCasilla(IModelo modelo) {
        SwingUtilities.invokeLater(panelTablero::repaint);
    }

    @Override
    public void updateJugada(IModelo modelo) {
        SwingUtilities.invokeLater(panelJugador::actualizar);
    }

    @Override
    public void updateTableroJugador(IModelo modelo) {
        int id = modelo.getIdJugadorAfectado();
        Jugador jugador = modelo.getJugadorRemoto(id);
        SwingUtilities.invokeLater(() -> {
            if (jugador == null) {
                return;
            }
            JPanel bloque = remotos.get(id);
            if (bloque == null) {
                agregarRemoto(jugador);
                panelRemotos.revalidate();
                panelRemotos.repaint();
            } else {
                datosRemotos.get(id).actualizar();
                bloque.repaint();
            }
        });
    }

    @Override
    public void updateMensaje(IModelo modelo) {
        String mensaje = modelo.getMensaje();
        if (mensaje == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, mensaje));
    }

    
    static Font fuente(int tamano) {
        return new Font(FUENTE, Font.BOLD, tamano);
    }

    static Graphics2D suavizar(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        return g2;
    }

    private static BufferedImage cargarImagen(String ruta) {
        if (!imagenes.containsKey(ruta)) {
            BufferedImage original = null;
            URL url = FrmPartida.class.getResource(ruta);
            if (url != null) {
                try {
                    original = ImageIO.read(url);
                } catch (IOException e) {
                    original = null;
                }
            }
            imagenes.put(ruta, original);
        }
        return imagenes.get(ruta);
    }

    //escala la imagen una sola vez por tamaño. Se hace al tamaño real en pixeles
    //de la pantalla (escala de Windows, ej. 125%) para que no se vea pixeleada
    private static BufferedImage cargarImagen(Graphics2D g, String ruta, int w, int h) {
        double escala = g.getTransform().getScaleX();
        int pw = (int) Math.ceil(w * escala);
        int ph = (int) Math.ceil(h * escala);
        String llave = ruta + "@" + pw + "x" + ph;
        if (!imagenes.containsKey(llave)) {
            BufferedImage original = cargarImagen(ruta);
            imagenes.put(llave, original == null ? null : reducir(original, pw, ph));
        }
        return imagenes.get(llave);
    }

    //reduce a la mitad varias veces y luego al tamaño final; reducir de golpe
    //(ej. 1292x2048 a 62x92) pierde casi todos los pixeles y se ve escalonado
    private static BufferedImage reducir(BufferedImage imagen, int w, int h) {
        BufferedImage actual = imagen;
        int cw = imagen.getWidth();
        int ch = imagen.getHeight();
        do {
            cw = Math.max(w, cw / 2);
            ch = Math.max(h, ch / 2);
            BufferedImage paso = new BufferedImage(cw, ch, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = paso.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(actual, 0, 0, cw, ch, null);
            g2.dispose();
            actual = paso;
        } while (cw != w || ch != h);
        return actual;
    }

    //dibuja la carta, si no la encuentra pone el numero
    static void pintarCarta(Graphics2D g, Carta carta, int x, int y, int w, int h) {
        String ruta = carta.getImagen() != null ? carta.getImagen() : "/cartas/" + carta.getNumero() + ".jpg";
        BufferedImage imagen = cargarImagen(g, ruta, w, h);
        if (imagen != null) {
            g.drawImage(imagen, x, y, w, h, null);
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.clipRect(x, y, w, h);
        g2.setColor(FONDO_CARTA);
        g2.fillRoundRect(x, y, w - 1, h - 1, 8, 8);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(x, y, w - 1, h - 1, 8, 8);

        int tamano = Math.max(8, w / 7);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, tamano));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(String.valueOf(carta.getNumero()), x + 4, y + fm.getAscent() + 2);
        String nombre = carta.getNombre();
        g2.drawString(nombre, x + (w - fm.stringWidth(nombre)) / 2, y + h - fm.getDescent() - 3);
        g2.dispose();
    }

    //frijol para marcar casilla
    static void pintarFrijol(Graphics2D g, int x, int y, int w, int h) {
        int ancho = (int) (Math.min(w, h) * 0.55);
        int alto = (int) (ancho * 0.7);
        int fx = x + (w - ancho) / 2;
        int fy = y + (h - alto) / 2;
        g.setColor(FRIJOL);
        g.fillOval(fx, fy, ancho, alto);
        g.setColor(FRIJOL.darker());
        g.drawOval(fx, fy, ancho, alto);
        g.setColor(new Color(255, 255, 255, 110));
        g.fillOval(fx + ancho / 5, fy + alto / 5, ancho / 3, alto / 4);
    }

    //avatar
    static void pintarAvatar(Graphics2D g, String ruta, String nombre, int x, int y, int d) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(FONDO_AVATAR);
        g2.fillOval(x, y, d, d);
        BufferedImage imagen = ruta == null ? null : cargarImagen(g2, ruta, d, d);
        if (imagen != null) {
            g2.setClip(new Ellipse2D.Float(x, y, d, d));
            g2.drawImage(imagen, x, y, d, d, null);
        } else if (nombre != null && !nombre.isEmpty()) {
            g2.setColor(Color.WHITE);
            g2.setFont(fuente(d / 2));
            FontMetrics fm = g2.getFontMetrics();
            String inicial = nombre.substring(0, 1).toUpperCase();
            g2.drawString(inicial, x + (d - fm.stringWidth(inicial)) / 2, y + (d - fm.getHeight()) / 2 + fm.getAscent());
        }
        g2.dispose();
    }

    
    private static class FondoRedondeado extends JPanel {

        FondoRedondeado() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = suavizar(g);
            g2.setColor(FONDO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.dispose();
        }
    }
}

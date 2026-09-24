package loteriamx.modelo;

import loteriamx.dominio.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ModeloPartida.
 */
public class ModeloPartida {

    private final IDominio dominio;
    private IObservador observador;
    private IEmisorRed emisorRed; 

    private final int idJugadorLocal;
    private final Tablero tablero; 
    private final Puntaje puntaje;

    private final List<Carta> historialCartas = new ArrayList<>();
    private Carta cartaActual;
    private Casilla casillaMarcada;
    private TipoJugada jugadaActual;
    private ResultadoJugada resultadoJugada;
    private int idJugadorAfectado;
    private String mensaje;

    public ModeloPartida(IDominio dominio, int idJugadorLocal, Tablero tablero, Puntaje puntaje) {
        this.dominio = dominio;
        this.idJugadorLocal = idJugadorLocal;
        this.tablero = tablero;
        this.puntaje = puntaje;
    }

    /**
     *  FrmPartida se suscribe al crearse.
     * @param observador
     */
    public void agregarObserver(IObservador observador) {
        this.observador = observador;
    }

    /**
     * Conecta este modelo con la red entre jugadores ManejadorSocket la
     * implementa.
     * @param emisorRed
     */
    public void setEmisorRed(IEmisorRed emisorRed) {
        this.emisorRed = emisorRed;
    }

    private void update() {
        if (observador != null) {
            observador.update(this);
        }
    }

    // Flujo cartaGritada(TCP) lo que le llega a CADA jugador desde el Griton 
    public void updateCarta(Carta cartaRecibida) {
        mensaje = null;
        if (cartaRecibida == null) {
            mensaje = "Ya no quedan cartas en el mazo";
            cartaActual = null;
            update();
            return;
        }
        dominio.guardarCartaHistorial(cartaRecibida);
        cartaActual = cartaRecibida;
        historialCartas.add(cartaRecibida);
        update();
    }

    // Flujo seleccionar casilla 
    public void marcarCasilla(Casilla casillaSeleccionada) {
        mensaje = null;
        boolean ok = dominio.marcarCasilla(casillaSeleccionada, cartaActual);
        if (ok) {
            casillaMarcada = casillaSeleccionada;
            idJugadorAfectado = idJugadorLocal;
            if (emisorRed != null) {
                try {
                    emisorRed.enviarCasillaMarcada(casillaSeleccionada);
                } catch (java.io.IOException e) {
                    mensaje = "No se pudo avisar a los demas jugadores (revisa la conexion)";
                }
            }
        } else {
            casillaMarcada = null;
            mensaje = "Esa casilla no corresponde a la carta actual, o ya esta marcada";
        }
        update();
    }

    // Flujo marcar jugada 
    public void marcarJugada(TipoJugada tipo) {
        mensaje = null;
        jugadaActual = tipo;
        resultadoJugada = dominio.marcarJugada(tablero, puntaje, tipo);
        idJugadorAfectado = idJugadorLocal;
        if (null != resultadoJugada)
            switch (resultadoJugada) {
                case MARCADA -> {
                    if (emisorRed != null) {
                        try {
                            emisorRed.enviarJugada(tipo);
                        } catch (java.io.IOException e) {
                            mensaje = "No se pudo avisar a los demas jugadores (revisa la conexion)";
                        }
                    }
                }
                case NO_VALIDA ->
                    mensaje = "Ese puntaje no esta realmente completo";
                case NO_DISPONIBLE ->
                    mensaje = "Ese puntaje ya fue reclamado antes";
                default -> {
                }
            }
        update();
    }

    // Flujo jugada de otro jugador 
    public void updateCasilla(int idJugadorRemoto, Casilla casillaRemota) {
        casillaMarcada = casillaRemota;
        idJugadorAfectado = idJugadorRemoto;
        mensaje = null;
        update();
    }

    public void updateJugada(int idJugadorRemoto, TipoJugada tipoRemoto) {
        jugadaActual = tipoRemoto;
        resultadoJugada = ResultadoJugada.MARCADA;
        idJugadorAfectado = idJugadorRemoto;
        mensaje = null;
        update();
    }


    //Getters que la vista jala despues de cada update() 
    public Carta getCarta() {
        return cartaActual;
    }

    public List<Carta> getHistorial() {
        return historialCartas;
    }

    public Casilla getCasillaMarcada() {
        return casillaMarcada;
    }

    public TipoJugada getJugada() {
        return jugadaActual;
    }

    public ResultadoJugada getResultadoJugada() {
        return resultadoJugada;
    }

    public int getIdJugadorAfectado() {
        return idJugadorAfectado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public Puntaje getPuntaje() {
        return puntaje;
    }
}

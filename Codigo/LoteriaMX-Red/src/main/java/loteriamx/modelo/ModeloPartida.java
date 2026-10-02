package loteriamx.modelo;

import griton.dominio.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ModeloPartida.
 */
public class ModeloPartida implements IModelo {

    private final IDominio dominio;
    private IObservador observador;
    private IEmisorRed emisorRed; 

    private final Jugador jugadorLocal;
    private final Nivel nivel;

    private final Map<Integer, Jugador> jugadoresRemotos = new LinkedHashMap<>();
    private final List<Carta> historialCartas = new ArrayList<>();
    private Carta cartaActual;
    private Casilla casillaMarcada;
    private TipoJugada jugadaActual;
    private ResultadoJugada resultadoJugada;
    private int idJugadorAfectado;
    private String mensaje;

    public ModeloPartida(IDominio dominio, Jugador jugadorLocal, Nivel nivel) {
        this.dominio = dominio;
        this.jugadorLocal = jugadorLocal;
        this.nivel = nivel;
    }

    /**
     * Main suscribe a FrmPartida al crearla.
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

    public void agregarJugadorRemoto(Jugador jugador) {
        jugadoresRemotos.put(jugador.getId(), jugador);
        idJugadorAfectado = jugador.getId();
        notificarTableroJugador();
    }

    private void notificarCarta() {
        if (observador != null) {
            observador.updateCarta(this);
        }
    }

    private void notificarHistorial() {
        if (observador != null) {
            observador.updateHistorial(this);
        }
    }

    private void notificarCasilla() {
        if (observador != null) {
            observador.updateCasilla(this);
        }
    }

    private void notificarJugada() {
        if (observador != null) {
            observador.updateJugada(this);
        }
    }

    private void notificarTableroJugador() {
        if (observador != null) {
            observador.updateTableroJugador(this);
        }
    }

    private void notificarMensaje() {
        if (observador != null) {
            observador.updateMensaje(this);
        }
    }

    // Flujo cartaGritada(TCP) lo que le llega a CADA jugador desde el Griton 
    public void updateCarta(Carta cartaRecibida) {
        mensaje = null;
        if (cartaRecibida == null) {
            mensaje = "Ya no quedan cartas en el mazo";
            cartaActual = null;
            notificarMensaje();
            return;
        }
        dominio.guardarCartaHistorial(cartaRecibida);
        cartaActual = cartaRecibida;
        historialCartas.add(cartaRecibida);
        notificarCarta();
        notificarHistorial();
    }

    // Flujo seleccionar casilla 
    public void marcarCasilla(Casilla casillaSeleccionada) {
        mensaje = null;
        boolean ok = dominio.marcarCasilla(casillaSeleccionada, cartaActual);
        if (ok) {
            casillaMarcada = casillaSeleccionada;
            idJugadorAfectado = jugadorLocal.getId();
            notificarCasilla();
            if (emisorRed != null) {
                try {
                    emisorRed.enviarCasillaMarcada(casillaSeleccionada);
                } catch (java.io.IOException e) {
                    mensaje = "No se pudo avisar a los demas jugadores (revisa la conexion)";
                    notificarMensaje();
                }
            }
        } else {
            casillaMarcada = null;
            mensaje = "Esa casilla no corresponde a la carta actual, o ya esta marcada";
            notificarMensaje();
        }
    }

    // Flujo marcar jugada 
    public void marcarJugada(TipoJugada tipo) {
        mensaje = null;
        jugadaActual = tipo;
        resultadoJugada = dominio.marcarJugada(jugadorLocal.getTablero(), jugadorLocal.getPuntaje(), tipo);
        idJugadorAfectado = jugadorLocal.getId();
        if (null != resultadoJugada)
            switch (resultadoJugada) {
                case MARCADA -> {
                    notificarJugada();
                    if (emisorRed != null) {
                        try {
                            emisorRed.enviarJugada(tipo);
                        } catch (java.io.IOException e) {
                            mensaje = "No se pudo avisar a los demas jugadores (revisa la conexion)";
                            notificarMensaje();
                        }
                    }
                }
                case NO_VALIDA -> {
                    mensaje = "Ese puntaje no esta realmente completo";
                    notificarMensaje();
                }
                case NO_DISPONIBLE -> {
                    mensaje = "Ese puntaje ya fue reclamado antes";
                    notificarMensaje();
                }
                default -> {
                }
            }
    }

    // Flujo jugada de otro jugador 
    public void updateCasilla(int idJugadorRemoto, Casilla casillaRemota) {
        Jugador jugador = jugadoresRemotos.get(idJugadorRemoto);
        if (jugador == null) {
            return;
        }
        jugador.marcarCasilla(casillaRemota.getFila(), casillaRemota.getColumna());
        idJugadorAfectado = idJugadorRemoto;
        mensaje = null;
        notificarTableroJugador();
    }

    public void updateJugada(int idJugadorRemoto, TipoJugada tipoRemoto) {
        Jugador jugador = jugadoresRemotos.get(idJugadorRemoto);
        if (jugador == null) {
            return;
        }
        jugador.marcarJugada(tipoRemoto);
        idJugadorAfectado = idJugadorRemoto;
        mensaje = null;
        notificarTableroJugador();
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

    public Jugador getJugadorLocal() {
        return jugadorLocal;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public Tablero getTablero() {
        return jugadorLocal.getTablero();
    }

    public Puntaje getPuntaje() {
        return jugadorLocal.getPuntaje();
    }

    public Jugador getJugadorRemoto(int idJugador) {
        return jugadoresRemotos.get(idJugador);
    }

    public Collection<Jugador> getJugadoresRemotos() {
        return jugadoresRemotos.values();
    }
}
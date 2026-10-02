package loteriamx.modelo;

import griton.dominio.Carta;
import griton.dominio.Jugador;
import griton.dominio.Nivel;
import java.util.Collection;
import java.util.List;

/**
 * Interfaz segregada de ModeloPartida: solo lo que la vista necesita
 */
public interface IModelo {

    Carta getCarta();

    List<Carta> getHistorial();

    Nivel getNivel();

    Jugador getJugadorLocal();

    Collection<Jugador> getJugadoresRemotos();

    Jugador getJugadorRemoto(int idJugador);

    int getIdJugadorAfectado();

    String getMensaje();
}

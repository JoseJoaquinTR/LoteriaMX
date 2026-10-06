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

    public Carta getCarta();

    public List<Carta> getHistorial();

    public Nivel getNivel();

    public Jugador getJugadorLocal();

    public Collection<Jugador> getJugadoresRemotos();

    public Jugador getJugadorRemoto(int idJugador);

    public int getIdJugadorAfectado();

    public String getMensaje();
}

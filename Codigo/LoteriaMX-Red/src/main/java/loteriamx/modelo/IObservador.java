package loteriamx.modelo;

/**
 * Interfaz IObservador 
 */
public interface IObservador {

    void updateCarta(ModeloPartida modelo);

    void updateHistorial(ModeloPartida modelo);

    void updateCasilla(ModeloPartida modelo);

    void updateJugada(ModeloPartida modelo);

    void updateTableroJugador(ModeloPartida modelo);

    void updateMensaje(ModeloPartida modelo);
}

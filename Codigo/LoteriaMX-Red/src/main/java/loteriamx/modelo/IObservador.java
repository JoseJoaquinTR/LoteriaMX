package loteriamx.modelo;

/**
 * Interfaz IObservador. 
 */
public interface IObservador {

    void updateCarta(IModelo modelo);

    void updateHistorial(IModelo modelo);

    void updateCasilla(IModelo modelo);

    void updateJugada(IModelo modelo);

    void updateTableroJugador(IModelo modelo);

    void updateMensaje(IModelo modelo);
}

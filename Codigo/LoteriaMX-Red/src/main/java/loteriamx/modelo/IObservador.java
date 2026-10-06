package loteriamx.modelo;

/**
 * Interfaz IObservador. 
 */
public interface IObservador {

    public void updateCarta(IModelo modelo);

    public void updateHistorial(IModelo modelo);

    public void updateCasilla(IModelo modelo);

    public void updateJugada(IModelo modelo);

    public void updateTableroJugador(IModelo modelo);

    public void updateMensaje(IModelo modelo);
}

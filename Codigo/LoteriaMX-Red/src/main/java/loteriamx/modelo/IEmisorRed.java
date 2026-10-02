package loteriamx.modelo;

import griton.dominio.Casilla;
import griton.dominio.TipoJugada;
import java.io.IOException;

/**
 * Puerto de salida hacia la red ENTRE JUGADORES lo implementa
 * ManejadorSocket. 
 */
public interface IEmisorRed {
    void enviarCasillaMarcada(Casilla casilla) throws IOException;
    void enviarJugada(TipoJugada tipo) throws IOException;
}

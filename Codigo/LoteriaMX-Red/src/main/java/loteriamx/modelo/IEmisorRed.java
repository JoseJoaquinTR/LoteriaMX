package loteriamx.modelo;

import loteriamx.dominio.Casilla;
import loteriamx.dominio.TipoJugada;
import java.io.IOException;

/**
 * Puerto de salida hacia la red ENTRE JUGADORES lo implementa
 * ManejadorSocket. 
 */
public interface IEmisorRed {
    void enviarCasillaMarcada(Casilla casilla) throws IOException;
    void enviarJugada(TipoJugada tipo) throws IOException;
}

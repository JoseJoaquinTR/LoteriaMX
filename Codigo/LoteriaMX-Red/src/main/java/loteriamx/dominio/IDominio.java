package loteriamx.dominio;


public interface IDominio {

    void guardarCartaHistorial(Carta carta);

    /**
     *  jugador selecciono una casilla en su tablero.
     * @param casillaSeleccionada
     * @param cartaActual
     * @return 
     */
    public boolean marcarCasilla(Casilla casillaSeleccionada, Carta cartaActual);

    public ResultadoJugada marcarJugada(Tablero tablero, Puntaje puntaje, TipoJugada tipo);
}

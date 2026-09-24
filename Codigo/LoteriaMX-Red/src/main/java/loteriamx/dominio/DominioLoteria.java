package loteriamx.dominio;

public class DominioLoteria implements IDominio {

    @Override
    public void guardarCartaHistorial(Carta carta) {

    }

    @Override
    public boolean marcarCasilla(Casilla casillaSeleccionada, Carta cartaActual) {

        boolean correcta = verificarCasillaCorrecta(casillaSeleccionada, cartaActual);
        boolean disponible = verificarCasillaDisponible(casillaSeleccionada);

        if (!correcta || !disponible) {
            return false; // casillaInvalida
        }
        casillaSeleccionada.marcar();
        return true; // casillaMarcada
    }

    private boolean verificarCasillaCorrecta(Casilla casilla, Carta cartaActual) {
        if (cartaActual == null) {
            return false; // todavia no han gritado carta
        }
        return casilla.getCarta().getNumero() == cartaActual.getNumero();
    }

    private boolean verificarCasillaDisponible(Casilla casilla) {
        return !casilla.estaMarcada();
    }

    @Override
    public ResultadoJugada marcarJugada(Tablero tablero, Puntaje puntaje, TipoJugada tipo) {
        if (!validarDisponibilidadJugada(puntaje, tipo)) {
            return ResultadoJugada.NO_DISPONIBLE;
        }
        if (!validarJugada(tablero, puntaje, tipo)) {
            return ResultadoJugada.NO_VALIDA;
        }
        puntaje.marcar(tipo);
        return ResultadoJugada.MARCADA;
    }

    private boolean validarDisponibilidadJugada(Puntaje puntaje, TipoJugada tipo) {
        return puntaje.estaDisponible(tipo);
    }

    private boolean validarJugada(Tablero tablero, Puntaje puntaje, TipoJugada tipo) {
        return puntaje.esValida(tipo, tablero);
    }
}

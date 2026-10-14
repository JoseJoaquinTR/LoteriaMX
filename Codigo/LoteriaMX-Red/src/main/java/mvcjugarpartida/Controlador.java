/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mvcjugarpartida;

import griton.dominio.Casilla;
import griton.dominio.TipoJugada;

/**
 *
 * Controlador
 */
public class Controlador {

    private final ModeloPartida modelo;

    public Controlador(ModeloPartida modelo) {
        this.modelo = modelo;
    }

    public void marcarCasilla(Casilla casilla) {
        modelo.marcarCasilla(casilla);
    }

    public void marcarJugada(TipoJugada tipo) {
        modelo.marcarJugada(tipo);
    }
}

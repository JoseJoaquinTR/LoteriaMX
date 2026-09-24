package loteriamx.dominio;

import java.io.Serializable;

public class Carta implements Serializable {

    private final int numero;
    private final String nombre;
    private final String imagen; 
    private boolean disponible;

    public Carta(int numero, String nombre, String imagen) {
        this.numero = numero;
        this.nombre = nombre;
        this.imagen = imagen;
        this.disponible = true;
    }

    public void cantar() {
        this.disponible = false;
    }

    public int getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

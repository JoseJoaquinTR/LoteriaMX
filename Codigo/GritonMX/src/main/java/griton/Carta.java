package griton;

/**
 * Cartas del mazo 
 */
public class Carta {

    private final int numero;
    private final String nombre;
    private final String imagen;

    public Carta(int numero, String nombre, String imagen) {
        this.numero = numero;
        this.nombre = nombre;
        this.imagen = imagen;
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

    @Override
    public String toString() {
        return nombre;
    }
}

package griton;

import griton.dominio.Carta;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {

    // Las 54 cartas; la posicion + 1 es el numero de la carta (y de su imagen en imgs/cartas)
    public static final String[] NOMBRES_CARTAS = {
        "El Gallo", "El Diablito", "La Dama", "El Catrin", "El Paraguas", "La Sirena",
        "La Escalera", "La Botella", "El Barril", "El Arbol", "El Melon", "El Valiente",
        "El Gorrito", "La Muerte", "La Pera", "La Bandera", "El Bandolon", "El Violoncello",
        "La Garza", "El Pajaro", "La Mano", "La Bota", "La Luna", "El Cotorro",
        "El Borracho", "El Negrito", "El Corazon", "La Sandia", "El Tambor", "El Camaron",
        "Las Jaras", "El Musico", "La Arana", "El Soldado", "La Estrella", "El Cazo",
        "El Mundo", "El Apache", "El Nopal", "El Alacran", "La Rosa", "La Calavera",
        "La Campana", "El Cantarito", "El Venado", "El Sol", "La Corona", "La Chalupa",
        "El Pino", "El Pescado", "La Palma", "La Maceta", "El Arpa", "La Rana"};

    private final List<Carta> carta;
    private int indice = 0;

    public Mazo(String[] nombres) {
        List<Carta> mazo=  new ArrayList<>();    
        for (int i = 0; i < nombres.length; i++) {
            mazo.add(new Carta(i + 1, nombres[i], null));
        }
        Collections.shuffle(mazo);
        this.carta = mazo;
    }
        
    public boolean hayDisponibles() {
        return indice < carta.size();
    }

    public Carta siguienteCarta() {
        return hayDisponibles() ? carta.get(indice++) : null;
    }
}

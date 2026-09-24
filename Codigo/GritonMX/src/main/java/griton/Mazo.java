package griton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {

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

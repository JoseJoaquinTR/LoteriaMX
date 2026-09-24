package griton;

import java.io.IOException;
/**
 *  Griton Api
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] nombres = {"El Sol", "La Luna", "El Arbol", "La Dama", "El Catrin","La Bandera", "El Nopal", "La Bota", "El Valiente", "El Pino","La Pera", "La Corona", "El Tambor", "El Corazon", "La Palma", "El Camaron"};
        
        int puerto = 5000;
        
        ServidorGriton servidor = new ServidorGriton(puerto);
        servidor.aceptarJugadores();
        System.out.println("Griton escuchando en el puerto " + puerto);

        Mazo mazo = new Mazo(nombres);
        int intervaloMs = 8000;                                                             //intervalo de tiempo para gritar la carta 

        while (mazo.hayDisponibles()) {
            System.out.println( (intervaloMs / 1000) + " segundos para la siguiente carta ("+ servidor.getNumJugadoresConectados() + " jugadores conectados)");
            Thread.sleep(intervaloMs);
            Carta carta = mazo.siguienteCarta();
            System.out.println("Gritando: " + carta.getNombre());
            servidor.repartirCarta(carta);
        }

        System.out.println("Ya no quedan cartas. Avisando fin de mazo...");
        servidor.avisarFinDeMazo();
    }
}

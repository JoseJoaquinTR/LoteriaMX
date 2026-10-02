package griton;

import griton.dominio.Carta;
import java.io.IOException;
/**
 *  Griton Api
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] nombres = Mazo.NOMBRES_CARTAS;

        int puerto = 5000;
        
        ServidorGriton servidor = new ServidorGriton(puerto);
        servidor.aceptarJugadores();
        System.out.println("Griton escuchando en el puerto " + puerto);

        // no gritar hasta que haya al menos 1 jugador
        System.out.println("Esperando jugadores...");
        while (servidor.getNumJugadoresConectados() < 1) {
            Thread.sleep(500);
        }

        Mazo mazo = new Mazo(nombres);
        int intervaloMs = 4000; 

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

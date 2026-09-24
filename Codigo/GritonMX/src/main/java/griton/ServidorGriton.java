package griton;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Servidor TCP 
 */
public class ServidorGriton {

    private final ServerSocket servidor;
    private final List<PrintWriter> jugadores = new CopyOnWriteArrayList<>();//es una lista de cada uno de los jugadores conectados, 
                                                                            // cada q sale una carta se recorre la lista y se manda a todos.

    public ServidorGriton(int puerto) throws IOException {
        servidor = new ServerSocket(puerto);
    }

    //hilo para aceptar los jugadores que vayan entrando. 
    public void aceptarJugadores() {
        Thread hilo = new Thread(() -> {
            while (true) {
                try {
                    Socket socket = servidor.accept();
                    PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);//es para mandar a la red cada print al instante. 
                    jugadores.add(salida);
                    System.out.println("Jugador conectado. Total: " + jugadores.size());
                } catch (IOException e) {
                    break; 
                }
            }
        });
        hilo.setDaemon(true);
        hilo.start();
    }

    public void repartirCarta(Carta carta) {
        String imagen = "";
        if (carta.getImagen() != null) {
            imagen = carta.getImagen();
        }
        
        String linea = "CARTA|" + carta.getNumero() + "|" + carta.getNombre() + "|" + imagen;

        for (PrintWriter jugador : jugadores) {
            jugador.println(linea);
        }
    }

    public void avisarFinDeMazo() {
        for (PrintWriter jugador : jugadores) {
            jugador.println("FIN");
        }
    }

    public int getNumJugadoresConectados() {
        return jugadores.size();
    }
}

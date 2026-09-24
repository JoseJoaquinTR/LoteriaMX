package loteriamx.red;

import loteriamx.modelo.ModeloPartida;
import loteriamx.modelo.IEmisorRed;
import loteriamx.dominio.Casilla;
import loteriamx.dominio.TipoJugada;

import java.io.*;
import java.net.Socket;

/**
 * ManejadorSocket la conexion TCP por aqui pasan las casillas marcadas y jugadas. Las
 * cartas no pasan por aqui esas llegan a cada jugador directo del
 * software del Griton.
 */
public class ManejadorSocket implements IEmisorRed {

    private final ModeloPartida modelo;
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    public ManejadorSocket(ModeloPartida modelo) {
        this.modelo = modelo;
    }

    public void conectar(String host, int puerto) throws IOException {
        socket = new Socket(host, puerto);
        salida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());
        escucharSegundoPlano();
    }

    private void escucharSegundoPlano() {
        Thread hiloEscucha = new Thread(() -> {
            try {
                while (true) {
                    Object mensaje = entrada.readObject();

                    switch (mensaje) {                                   //TODO
                        case Casilla casilla ->
                            modelo.updateCasilla(0, casilla);
                        //TODO
                        case TipoJugada tipo ->
                            modelo.updateJugada(0, tipo);
                        //TODO
                        default -> {
                        }
                    }
                    //TODO
                }
            } catch (IOException | ClassNotFoundException e) {

            }
        });
        hiloEscucha.setDaemon(true);
        hiloEscucha.start();
    }

    @Override
    public void enviarCasillaMarcada(Casilla casilla) throws IOException {
        salida.writeObject(casilla);
        salida.flush();
    }

    @Override
    public void enviarJugada(TipoJugada tipo) throws IOException {
        salida.writeObject(tipo);
        salida.flush();
    }
}

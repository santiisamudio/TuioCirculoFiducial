package javaapplication5;

/**
 *
 * @author santi
 */
import TUIO.*;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
    
    public class ClienteTuio {
    private TuioClient _cliente;
    private Pane _contenedor;

    public ClienteTuio(int puerto,Pane contenedor) {
        this._cliente = new TuioClient(puerto);
        this._contenedor = contenedor;
    }

    public void start(){
        
        ListenerTuio listener = new ListenerTuio(_contenedor);//se crea el listener que manejara los eventos de los simbolos fiduciales y se le pasa el contenedor pane
        this._cliente.addTuioListener(listener);//se agrega al cliente el tuiolistener       
        this._cliente.connect();//se conecta al servidor 
        System.out.println("conectado ");
        
    }
    
    public void stop(){
        this._cliente.disconnect();//se desconecta del servidor
    }
}

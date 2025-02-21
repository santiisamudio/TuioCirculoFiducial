package javaapplication5;

/**
 *
 * @author santi
 */
import TUIO.*;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
    
    public class TuioClientExample {
    private TuioClient client;
    private Pane pane;

    public TuioClientExample(int port,Pane pane) {
        this.client = new TuioClient(port);
        this.pane = pane;
    }

    public void start(){
        
        TuioListenerExample listener = new TuioListenerExample(pane);
        client.addTuioListener(listener);
        System.out.println("conectando");
        client.connect();
        System.out.println("Esperando eventos TUIO...");
        
    }
    
    public void stop(){
        client.disconnect();
    }
}

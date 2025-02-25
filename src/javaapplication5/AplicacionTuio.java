/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication5;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 *
 * @author santi
 */
public class AplicacionTuio extends Application{
    
    public void start(Stage primaryStage) {
        Pane _contenedor = new Pane();//el contenedor se va a encargar de mostrar la UI 
        
        ClienteTuio cliente = new ClienteTuio(3333,_contenedor);//se configura el puerto 3333 al contenedor pane y se crea una instancia de cliente
        cliente.start();//se inicia conexion con el servidor TUIO
        

        Scene escena = new Scene(_contenedor, 800, 600);//se crea la ventana y establecen sus dimensiones
        primaryStage.setScene(escena);
        primaryStage.setTitle("TUIO");
        primaryStage.show();//se muestra la ventana
        
    }
    
    public static void main(String[] args) {
        
        
        launch(args);//inicia el programa en launch y se llama al metodo start
    }
}

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
public class TuioTestApp extends Application{
    
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        
        TuioClientExample clientApp = new TuioClientExample(3333,root);
        clientApp.start();
        

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TUIO Fiducial Tracking");
        primaryStage.show();
        
    }
    
    public static void main(String[] args) {
        /*
        TuioClientExample clientApp = new TuioClientExample(3333);
        
        
        clientApp.start(); 
        */
        
        launch(args);
    }
}

package com.messengerapp.messenger_application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;
/*
implements Initializables:
 FXMLLoader will now automatically call any suitably annotated
 no-arg initialize() method defined by the controller
 */
public class messengerController  implements Initializable {
    @FXML
    private Button sendButton;

    @FXML
    private TextField TF_area;

    @FXML
    private ScrollPane sp_Main;

    @FXML
    private VBox vboxMessage;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            try{
                server= new Server(new ServerSocket(1234));

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(" Error Creating the Server");
            }
            // adjusting the ScrollPane's height according to the Vbox's height;
            vboxMessage.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                    sp_Main.setVvalue((Double) newValue);
                }
            });

            // inorder to receive message from a client and show it on the server, we need to append the messege to Vbox;

            server.receiveMessageFromClient(vboxMessage);

            sendButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String messageToSend= TF_area.getText();

                    if(!messageToSend.isEmpty()){
                        // add the message to our VBox as separate H-boxes;
                        HBox hbox=new HBox();// messages will be shown in this H-box;
                        hbox.setAlignment(Pos.CENTER_RIGHT);
                        hbox.setPadding(new Insets(5 ,5,5, 10));

                        // creating the text and textFlow

                        Text text= new Text(messageToSend);
                        TextFlow textFlow= new TextFlow(text); // TextFlow has wrapping property.

                        //adding style to out TextFow:
                        textFlow.setStyle ( " - fx - color : rgb ( 239 , 242 , 255)"+
                                            " -fx - background - color: rgb ( 15,125,242 )  "+
                                            "-fx - background - radius : 20px " ) ;
                        textFlow.setPadding ( new Insets (  5 ,  10 ,  5 ,  10 ) ) ;
                        text . setFill ( Color.color (  0.934 ,  0.945 , 0.996 ) ) ;

                        // now add the textFlow to HBox;
                        hbox.getChildren().add(textFlow);
                        vboxMessage.getChildren().add(hbox);

                        // now send the text to the client:
                        server.sendMessageToClient(messageToSend);

                        // empty the TextField:
                        TF_area.clear();


                    }
                }
            });
    }

    // Method to Append H-box to our GUI:
    public static void addLabel(String messageFromClient, VBox vbox){
        HBox hbox= new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setPadding(new Insets(5 ,5,5, 10));

        // creating the text and textFlow

        Text text= new Text(messageFromClient);
        TextFlow textFlow= new TextFlow(text); // TextFlow has wrapping property.

        //adding style to out TextFow:
        textFlow.setStyle (" -fx - background - color:rgb ( 233 , 233 , 235)  "+
                "-fx - background - radius : 20px " ) ;
        textFlow.setPadding ( new Insets (  5 ,  10 ,  5 ,  10 ) ) ;
       // text . setFill ( Color.color (  0.934 ,  0.945 , 0.996 ) ) ;

        // now add the textFlow to HBox;
        hbox.getChildren().add(textFlow);
        //NOTE:
        // in JavaFx, we cannot update the GUI from a thread other than the application thread.
        // but to update the GUI,here we need to call this current method from the server class .
        // therefore, we use Platform.runLater();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hbox);
            }
        });
    }

}
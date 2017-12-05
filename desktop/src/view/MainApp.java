package view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import control.Sessao;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author IFControl
 */
public class MainApp extends Application {
    
    private static Stage stage;
    protected static Sessao sessao;
    protected static Alert alert;
    private boolean sessaoAberta = true;
    protected static boolean running = true;
    
    @Override
    public void start(Stage primaryStage) {
        
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        
        sessao = new Sessao();
        if (sessao.iniciarSessao() == 0) {
            sessaoAberta = false;
            alert.setHeaderText(null);
            alert.setTitle("Erro de conexão");
            alert.setContentText("IFControl não conseguiu se conectar ao servidor");
            alert.showAndWait();
            System.exit(0);
        }

        // Estagio
        this.stage = primaryStage;
        stage.setTitle("IFControl");
        stage.setHeight(700);
        stage.setWidth(1000);
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                if (sessaoAberta) {
                    sessao.encerrarSessao();
                }
                running = false;
            }
        });
        showLogin();
        stage.show();
    }
    
    public static void showLogin() {
        try {
            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(MainApp.class.getResource("Login.fxml"));
            AnchorPane login = (AnchorPane) fxml.load();
            stage.setScene(new Scene(login));
        } catch (IOException ioe) {
            System.out.println("Erro ao carregar login: " + ioe.getMessage());
        }
    }
    
    public static void showCadastro() {
        try {
            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(MainApp.class.getResource("Cadastro.fxml"));
            AnchorPane cadastro = (AnchorPane) fxml.load();
            stage.setScene(new Scene(cadastro));
        } catch (IOException ioe) {
            System.out.println("Erro ao carregar cadastro: " + ioe.getMessage());
        }
    }
    
    public static void showSalas() {
        try {
            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(MainApp.class.getResource("Salas.fxml"));
            AnchorPane sala = (AnchorPane) fxml.load();
            stage.setScene(new Scene(sala));
        } catch (IOException ioe) {
            System.out.println("Erro ao carregar sala: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}

package view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author IFControl
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txfLogin;
    @FXML
    private PasswordField pwfSenha;
    @FXML
    private Button bttEntrar;
    @FXML
    private Button bttCadastrar;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @FXML
    private void entrar(ActionEvent event) {
        if ("".equals(txfLogin.getText())) {
            MainApp.alert.setAlertType(Alert.AlertType.ERROR);
            MainApp.alert.setTitle("Login");
            MainApp.alert.setContentText("Insira um login");
            MainApp.alert.showAndWait();
        } else if ("".equals(pwfSenha.getText())) {
            MainApp.alert.setAlertType(Alert.AlertType.ERROR);
            MainApp.alert.setTitle("Senha");
            MainApp.alert.setContentText("Insira uma senha");
            MainApp.alert.showAndWait();
        } else {
            String retorno = MainApp.sessao.login(txfLogin.getText(), pwfSenha.getText());
            if (retorno.contains("LOGIN_")) {
                if ("LOGIN_OK".equals(retorno)) {
                    MainApp.showSalas();
                } else {
                    MainApp.alert.setAlertType(Alert.AlertType.ERROR);
                    MainApp.alert.setTitle("Login");
                    MainApp.alert.setContentText("Login ou senha inválidos!");
                    MainApp.alert.showAndWait();
                }
            }
        }
    }

    @FXML
    private void cadastrar(ActionEvent event) {
        MainApp.showCadastro();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}

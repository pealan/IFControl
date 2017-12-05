package view;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import static java.awt.SystemColor.text;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

/**
 * FXML Controller class
 *
 * @author IFControl
 */
public class CadastroController implements Initializable {

    @FXML
    private TextField txfNome;
    @FXML
    private TextField txfLogin;
    @FXML
    private Button bttCadastrar;
    @FXML
    private TextField txfSIAP;
    @FXML
    private PasswordField pwfSenha;
    @FXML
    private Button bttCancelar;
    @FXML
    private PasswordField pwfSenha1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txfSIAP.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("\\d*")) {
                    try{
                        int value = Integer.parseInt(newValue);
                    } catch ( NumberFormatException e ) {
                        
                    }
                } else {
                    txfSIAP.setText(oldValue);
                }
            }
        });

    }

    @FXML
    private void cadastrar(ActionEvent event) {

        if ("".equals(txfLogin.getText())) {
            System.out.println("loginvazio");
        } else {

            if (!pwfSenha.getText().equals(pwfSenha1.getText())) {
                MainApp.alert.setAlertType(Alert.AlertType.ERROR);
                MainApp.alert.setTitle("Senha");
                MainApp.alert.setContentText("Senhas não coincidem!");
                MainApp.alert.showAndWait();
            }

            User user = new User();
            user.setLogin(txfLogin.getText());
            user.setNome(txfNome.getText());
            user.setSenha(pwfSenha.getText());
            user.setSiap(Long.parseLong(txfSIAP.getText()));
            String retorno = MainApp.sessao.cadastrar(user);
            if ("CAD_OK".equals(retorno)) {
                MainApp.alert.setAlertType(Alert.AlertType.INFORMATION);
                MainApp.alert.setTitle("Cadastro");
                MainApp.alert.setContentText("Cadastro efetuado com sucesso.");
                MainApp.alert.showAndWait();
                MainApp.showLogin();
            } else if ("INVALID_SIAP".equals(retorno)) {
                MainApp.alert.setAlertType(Alert.AlertType.ERROR);
                MainApp.alert.setTitle("Cadastro");
                MainApp.alert.setContentText("Siap inválido.");
                MainApp.alert.showAndWait();
                txfSIAP.requestFocus();
            } else if ("INVALID_LOGIN".equals(retorno)) {
                MainApp.alert.setAlertType(Alert.AlertType.ERROR);
                MainApp.alert.setTitle("Cadastro");
                MainApp.alert.setContentText("Login inválido.");
                MainApp.alert.showAndWait();
                txfLogin.requestFocus();
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        MainApp.showLogin();
    }

}

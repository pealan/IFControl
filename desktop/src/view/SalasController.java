/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Acao;
import model.Sala;

/**
 * FXML Controller class
 *
 * @author IFControl
 */
public class SalasController implements Initializable {

    @FXML
    private ImageView imgLampada1;
    @FXML
    private ImageView imgFloco1;
    @FXML
    private ImageView imgPresenca1;
    @FXML
    private Button bttLamp1;
    @FXML
    private Slider sliderLamp1;
    @FXML
    private Button bttFloco1;
    @FXML
    private Slider sliderFloco1;
    @FXML
    private AnchorPane paneSalas;
    @FXML
    private Button bttLogout;
    @FXML
    private ListView<String> listLogs;
    @FXML
    private ImageView imgLampada3;
    @FXML
    private ImageView imgFloco3;
    @FXML
    private ImageView imgPresenca3;
    @FXML
    private Button bttLamp3;
    @FXML
    private Slider sliderLamp3;
    @FXML
    private Button bttFloco3;
    @FXML
    private ComboBox<Integer> cbTemp3;
    @FXML
    private Label lUmidade3;
    @FXML
    private Label lTemp3;
    @FXML
    private ComboBox<String> cbDatvM3;
    @FXML
    private ComboBox<String> cbDatvH3;
    @FXML
    private ComboBox<String> cbAtvH3;
    @FXML
    private ComboBox<String> cbAtvM3;
    @FXML
    private Button bttHorario3;
    @FXML
    private ImageView imgLampada2;
    @FXML
    private ImageView imgFloco2;
    @FXML
    private ImageView imgPresenca2;
    @FXML
    private Button bttLamp2;
    @FXML
    private Slider sliderLamp2;
    @FXML
    private Button bttFloco2;
    @FXML
    private Slider sliderFloco2;
    @FXML
    private Slider sliderFloco3;
    @FXML
    private ComboBox<Integer> cbTemp2;
    @FXML
    private Label lUmidade2;
    @FXML
    private Label lTemp2;
    @FXML
    private ComboBox<String> cbDatvM2;
    @FXML
    private ComboBox<String> cbDatvH2;
    @FXML
    private ComboBox<String> cbAtvH2;
    @FXML
    private ComboBox<String> cbAtvM2;
    @FXML
    private Button bttHorario2;
    @FXML
    private ImageView imgLampada4;
    @FXML
    private ImageView imgFloco4;
    @FXML
    private ImageView imgPresenca4;
    @FXML
    private Button bttLamp4;
    @FXML
    private Slider sliderLamp4;
    @FXML
    private Button bttFloco4;
    @FXML
    private Slider sliderFloco4;
    @FXML
    private ComboBox<Integer> cbTemp4;
    @FXML
    private Label lUmidade4;
    @FXML
    private Label lTemp4;
    @FXML
    private ComboBox<String> cbDatvM4;
    @FXML
    private ComboBox<String> cbDatvH4;
    @FXML
    private ComboBox<String> cbAtvH4;
    @FXML
    private ComboBox<String> cbAtvM4;
    @FXML
    private Button bttHorario4;
    @FXML
    private ComboBox<Integer> cbTemp1;
    @FXML
    private ComboBox<String> cbDatvM1;
    @FXML
    private ComboBox<String> cbDatvH1;
    @FXML
    private ComboBox<String> cbAtvH1;
    @FXML
    private ComboBox<String> cbAtvM1;
    @FXML
    private Button bttHorario1;
    @FXML
    private Label lUmidade1;
    @FXML
    private Label lTemp1;

    private final String lampadaON = "C:\\Users\\IFControl\\Dropbox\\IFControl\\IFControlClient\\src\\view\\Lampada on 125.png";
    private final String lampadaOFF = "C:\\Users\\IFControl\\Dropbox\\IFControl\\IFControlClient\\src\\view\\Lampada off 125.png";
    private final String presenca = "C:\\Users\\IFControl\\Dropbox\\IFControl\\IFControlClient\\src\\view\\Presença on 125.png";
    private final String flocoON = "C:\\Users\\IFControl\\Dropbox\\IFControl\\IFControlClient\\src\\view\\Floco on 125.png";
    private final String flocoOFF = "C:\\Users\\IFControl\\Dropbox\\IFControl\\IFControlClient\\src\\view\\Floco off 125.png";
    private final Image luzON = new Image(new File(lampadaON).toURI().toString());
    private final Image luzOFF = new Image(new File(lampadaOFF).toURI().toString());
    private final Image arON = new Image(new File(flocoON).toURI().toString());
    private final Image arOFF = new Image(new File(flocoOFF).toURI().toString());
    private final Image gente = new Image(new File(presenca).toURI().toString());

    private Type tipoListaSala, tipoListaLogs;
    private Gson gson = new Gson();
    private List<Sala> salas;
    private List<Acao> logs;
    private boolean atualizado;
    @FXML
    private Button bttAtualizar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TOD

        bttHorario1.setDisable(true);
        imgPresenca1.setImage(gente);
        imgLampada1.setImage(luzOFF);
        imgPresenca1.setBlendMode(BlendMode.SOFT_LIGHT);
        imgFloco1.setImage(arOFF);

        ObservableList<String> horas
                = FXCollections.observableArrayList();
        ObservableList<String> minutos
                = FXCollections.observableArrayList();

        horas.add("--");
        for (int i = 0; i <= 9; i++) {
            horas.add("0" + i);
        }
        for (int i = 10; i <= 23; i++) {
            horas.add("" + i);
        }

        minutos.addAll("--", "00", "05");
        for (int i = 10; i <= 55; i += 5) {
            minutos.add("" + i);
        }

        cbAtvH1.setItems(horas);
        cbAtvM1.setItems(minutos);
        cbDatvH1.setItems(horas);
        cbDatvM1.setItems(minutos);
        cbAtvH2.setItems(horas);
        cbAtvM2.setItems(minutos);
        cbDatvH2.setItems(horas);
        cbDatvM2.setItems(minutos);
        cbAtvH3.setItems(horas);
        cbAtvM3.setItems(minutos);
        cbDatvH3.setItems(horas);
        cbDatvM3.setItems(minutos);
        cbAtvH4.setItems(horas);
        cbAtvM4.setItems(minutos);
        cbDatvH4.setItems(horas);
        cbDatvM4.setItems(minutos);

        ObservableList<Integer> temp = FXCollections.observableArrayList();
        temp.addAll(25, 24, 23, 22, 21, 20);
        cbTemp1.setItems(temp);
        cbTemp2.setItems(temp);
        cbTemp3.setItems(temp);
        cbTemp4.setItems(temp);
        
        bttHorario1.setDisable(true);
        bttHorario2.setDisable(true);
        bttHorario3.setDisable(true);
        bttHorario4.setDisable(true);

        tipoListaSala = new TypeToken<ArrayList<Sala>>() {
        }.getType();
        tipoListaLogs = new TypeToken<ArrayList<Acao>>() {
        }.getType();
        new Thread(new AtualizaDados()).start();
        MainApp.sessao.atualizar();
        //MainApp.sessao.logs();
    }

    public void interfaceSalas() {

        if (salas.get(0).isEstadoLuzes()) {
            imgLampada1.setImage(luzON);
            sliderLamp1.setValue(1);
        } else {
            imgLampada1.setImage(luzOFF);
            sliderLamp1.setValue(0);
        }
        if (salas.get(0).isPresenca()) {
            imgPresenca1.setBlendMode(BlendMode.HARD_LIGHT);
        } else {
            imgPresenca1.setBlendMode(BlendMode.SOFT_LIGHT);
        }
        if (salas.get(0).isEstadoAr()) {
            imgFloco1.setImage(arON);
            sliderFloco1.setValue(1);
            cbTemp1.setDisable(false);
        } else {
            imgFloco1.setImage(arOFF);
            sliderFloco1.setValue(0);
            cbTemp1.setDisable(true);
        }
        if (bttHorario1.isDisabled()) {
            if (salas.get(0).getHoraAtivacao() != null) {
                cbAtvH1.setValue(salas.get(0).getHoraAtivacao().toString().substring(0, 2));
                cbAtvM1.setValue(salas.get(0).getHoraAtivacao().toString().substring(3, 5));
            } else {
                cbAtvH1.setValue("--");
                cbAtvM1.setValue("--");
            }
            if (salas.get(0).getHoraDesativacao() != null) {
                cbDatvH1.setValue(salas.get(0).getHoraDesativacao().toString().substring(0, 2));
                cbDatvM1.setValue(salas.get(0).getHoraDesativacao().toString().substring(3, 5));
            } else {
                cbDatvH1.setValue("--");
                cbDatvM1.setValue("--");
            }
        }
        lTemp1.setText("Temperatura: " + salas.get(0).getTemperatura() + " °C");
        lUmidade1.setText("Umidade: " + salas.get(0).getUmidade() + "%");
        cbTemp1.setValue(salas.get(0).getTempAr());
        
        if (salas.get(1).isEstadoLuzes()) {
            imgLampada2.setImage(luzON);
            sliderLamp2.setValue(1);
        } else {
            imgLampada2.setImage(luzOFF);
            sliderLamp2.setValue(0);
        }
        if (salas.get(1).isPresenca()) {
            imgPresenca2.setBlendMode(BlendMode.HARD_LIGHT);
        } else {
            imgPresenca2.setBlendMode(BlendMode.SOFT_LIGHT);
        }
        if (salas.get(1).isEstadoAr()) {
            imgFloco2.setImage(arON);
            sliderFloco2.setValue(1);
            cbTemp2.setDisable(false);
        } else {
            imgFloco2.setImage(arOFF);
            sliderFloco2.setValue(0);
            cbTemp2.setDisable(true);
        }
        if (bttHorario2.isDisabled()) {
            if (salas.get(1).getHoraAtivacao() != null) {
                cbAtvH2.setValue(salas.get(1).getHoraAtivacao().toString().substring(0, 2));
                cbAtvM2.setValue(salas.get(1).getHoraAtivacao().toString().substring(3, 5));
            } else {
                cbAtvH2.setValue("--");
                cbAtvM2.setValue("--");
            }
            if (salas.get(1).getHoraDesativacao() != null) {
                cbDatvH2.setValue(salas.get(1).getHoraDesativacao().toString().substring(0, 2));
                cbDatvM2.setValue(salas.get(1).getHoraDesativacao().toString().substring(3, 5));
            } else {
                cbDatvH2.setValue("--");
                cbDatvM2.setValue("--");
            }
        }
        lTemp2.setText("Temperatura: " + salas.get(1).getTemperatura() + " °C");
        lUmidade2.setText("Umidade: " + salas.get(1).getUmidade() + "%");
        cbTemp2.setValue(salas.get(1).getTempAr());
        
        if (salas.get(2).isEstadoLuzes()) {
            imgLampada3.setImage(luzON);
            sliderLamp3.setValue(1);

        } else {
            imgLampada3.setImage(luzOFF);
            sliderLamp3.setValue(0);
        }
        if (salas.get(2).isPresenca()) {
            imgPresenca3.setBlendMode(BlendMode.HARD_LIGHT);
        } else {
            imgPresenca3.setBlendMode(BlendMode.SOFT_LIGHT);
        }
        if (salas.get(2).isEstadoAr()) {
            imgFloco3.setImage(arON);
            sliderFloco3.setValue(1);
            cbTemp3.setDisable(false);
        } else {
            imgFloco3.setImage(arOFF);
            sliderFloco3.setValue(0);
            cbTemp3.setDisable(true);
        }
        if (bttHorario3.isDisabled()) {
            if (salas.get(2).getHoraAtivacao() != null) {
                cbAtvH3.setValue(salas.get(2).getHoraAtivacao().toString().substring(0, 2));
                cbAtvM3.setValue(salas.get(2).getHoraAtivacao().toString().substring(3, 5));
            } else {
                cbAtvH3.setValue("--");
                cbAtvM3.setValue("--");
            }
            if (salas.get(2).getHoraDesativacao() != null) {
                cbDatvH3.setValue(salas.get(2).getHoraDesativacao().toString().substring(0, 2));
                cbDatvM3.setValue(salas.get(2).getHoraDesativacao().toString().substring(3, 5));
            } else {
                cbDatvH3.setValue("--");
                cbDatvM3.setValue("--");
            }
        }
        lTemp3.setText("Temperatura: " + salas.get(2).getTemperatura() + " °C");
        lUmidade3.setText("Umidade: " + salas.get(2).getUmidade() + "%");
        cbTemp3.setValue(salas.get(2).getTempAr());
        
        if (salas.get(3).isEstadoLuzes()) {
            imgLampada4.setImage(luzON);
            sliderLamp4.setValue(1);

        } else {
            imgLampada4.setImage(luzOFF);
            sliderLamp4.setValue(0);
        }
        if (salas.get(3).isPresenca()) {
            imgPresenca4.setBlendMode(BlendMode.HARD_LIGHT);
        } else {
            imgPresenca4.setBlendMode(BlendMode.SOFT_LIGHT);
        }
        if (salas.get(3).isEstadoAr()) {
            imgFloco4.setImage(arON);
            sliderFloco4.setValue(1);
            cbTemp4.setDisable(false);
        } else {
            imgFloco4.setImage(arOFF);
            sliderFloco4.setValue(0);
            cbTemp4.setDisable(true);
        }
        if (bttHorario4.isDisabled()) {
            if (salas.get(3).getHoraAtivacao() != null) {
                cbAtvH4.setValue(salas.get(3).getHoraAtivacao().toString().substring(0, 2));
                cbAtvM4.setValue(salas.get(3).getHoraAtivacao().toString().substring(3, 5));
            } else {
                cbAtvH4.setValue("--");
                cbAtvM4.setValue("--");
            }
            if (salas.get(3).getHoraDesativacao() != null) {
                cbDatvH4.setValue(salas.get(3).getHoraDesativacao().toString().substring(0, 2));
                cbDatvM4.setValue(salas.get(3).getHoraDesativacao().toString().substring(3, 5));
            } else {
                cbDatvH4.setValue("--");
                cbDatvM4.setValue("--");
            }
        }
        lTemp4.setText("Temperatura: " + salas.get(3).getTemperatura() + " °C");
        lUmidade4.setText("Umidade: " + salas.get(3).getUmidade() + "%");
        cbTemp4.setValue(salas.get(3).getTempAr());
        
        paneSalas.setDisable(false);
    }

    public void interfaceLogs() {

        ObservableList<String> options
                = FXCollections.observableArrayList();

        for (int i = logs.size() - 1; i >= 0; i--) {
            Acao acao = logs.get(i);
            String tipoAcao = "";
            if ( acao.isStatus() ) {
                if ( acao.getTipoAcao().contains("ON") ) {
                    tipoAcao += " ligou ";
                } else if ( acao.getTipoAcao().contains("OFF") ) {
                    tipoAcao += " desligou ";
                } else {
                    tipoAcao += " mudou a temperatura ";
                }
            } else {
                if ( acao.getTipoAcao().contains("ON") ) {
                    tipoAcao += " tentou ligar ";
                } else if ( acao.getTipoAcao().contains("OFF") ) {
                    tipoAcao += " tentou desligar ";
                } else {
                    tipoAcao += " tentou mudar a temperatura ";
                }
            }
            if ( acao.getTipoAcao().contains("LUZ") ) {
                tipoAcao += "a luz ";
            } else if ( acao.getTipoAcao().contains("AR") ) {
                if ( tipoAcao.contains("temperatura") ) {
                    tipoAcao += "do ar ";
                } else {
                    tipoAcao += "o ar ";
                }
            }
            if (acao.getTipoAcao().contains("HA")) {
                tipoAcao = " mudou o horario de ativação ";
            } else if (acao.getTipoAcao().contains("HD")) {
                tipoAcao = " mudou o horario de desativação ";
            }
            options.add(acao.getLogin() + tipoAcao + "da sala " + acao.getnSala());
        }

        listLogs.setItems(options);
        paneSalas.setDisable(false);

    }

    @FXML
    private void tratarLuz1(ActionEvent event) {
        if (sliderLamp1.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZON.\",\"nSala\":1}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZOFF.\",\"nSala\":1}");
        }
        paneSalas.setDisable(true);
    }

    @FXML
    private void tratarLuz2(ActionEvent event) {
        if (sliderLamp2.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZON.\",\"nSala\":2}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZOFF.\",\"nSala\":2}");
        }
        paneSalas.setDisable(true);
    }

    @FXML
    private void tratarLuz3(ActionEvent event) {
        if (sliderLamp3.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZON.\",\"nSala\":3}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZOFF.\",\"nSala\":3}");
        }
        paneSalas.setDisable(true);
    }

    @FXML
    private void tratarLuz4(ActionEvent event) {
        if (sliderLamp4.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZON.\",\"nSala\":4}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"LUZOFF.\",\"nSala\":4}");
        }
        paneSalas.setDisable(true);
    }

    @FXML
    private void tratarFloco1(ActionEvent event) {
        if (sliderFloco1.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"ARON.\",\"nSala\":1}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AROFF.\",\"nSala\":1}");
        }
        paneSalas.setDisable(true);
    }

    @FXML
    private void tratarFloco2(ActionEvent event) {
        if (sliderFloco2.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"ARON.\",\"nSala\":2}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AROFF.\",\"nSala\":2}");
        }
    }

    @FXML
    private void tratarFloco3(ActionEvent event) {
        if (sliderFloco3.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"ARON.\",\"nSala\":3}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AROFF.\",\"nSala\":3}");
        }
    }

    @FXML
    private void tratarFloco4(ActionEvent event) {
        if (sliderFloco4.getValue() == 0) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"ARON.\",\"nSala\":4}");
        } else {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AROFF.\",\"nSala\":4}");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        MainApp.showLogin();
    }

    @FXML
    private void tratarTemp1() {
        if (!atualizado) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AR" + cbTemp1.getValue()
                    + ".\",\"nSala\":1}");
            paneSalas.setDisable(true);
        }
    }
    
    @FXML
    private void tratarTemp2() {
        if (!atualizado) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AR" + cbTemp2.getValue()
                    + ".\",\"nSala\":2}");
            paneSalas.setDisable(true);
        }
    }
    
    @FXML
    private void tratarTemp3() {
        if (!atualizado) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AR" + cbTemp3.getValue()
                    + ".\",\"nSala\":3}");
            paneSalas.setDisable(true);
        }
    }
    
    @FXML
    private void tratarTemp4() {
        if (!atualizado) {
            MainApp.sessao.trataAcao("{\"tipoAcao\":\"AR" + cbTemp4.getValue()
                    + ".\",\"nSala\":4}");
            paneSalas.setDisable(true);
        }
    }

    @FXML
    private void tratarHorario1(ActionEvent event) {

        if (!cbAtvH1.isDisabled()) {
            if (!"--".equals(cbAtvH1.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA" + cbAtvH1.getValue() + ":"
                        + cbAtvM1.getValue() + ":" + "00.\",\"nSala\":1}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA99:99:99.\",\"nSala\":1}");
            }
            bttHorario1.setDisable(true);
            cbDatvH1.setDisable(false);
            cbDatvM1.setDisable(false);
        } else {
            if (!"--".equals(cbDatvH1.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD" + cbDatvH1.getValue() + ":"
                        + cbDatvM1.getValue() + ":" + "00.\",\"nSala\":1}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD99:99:99.\",\"nSala\":1}");
            }
            bttHorario1.setDisable(true);
            cbAtvH1.setDisable(false);
            cbAtvM1.setDisable(false);
        }

    }

    @FXML
    private void tratarHorario2(ActionEvent event) {

        if (!cbAtvH2.isDisabled()) {
            if (!"--".equals(cbAtvH2.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA" + cbAtvH2.getValue() + ":"
                        + cbAtvM2.getValue() + ":" + "00.\",\"nSala\":2}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA99:99:99.\",\"nSala\":2}");
            }
            bttHorario2.setDisable(true);
            cbDatvH2.setDisable(false);
            cbDatvM2.setDisable(false);
        } else {
            if (!"--".equals(cbDatvH2.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD" + cbDatvH2.getValue() + ":"
                        + cbDatvM2.getValue() + ":" + "00.\",\"nSala\":2}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD99:99:99.\",\"nSala\":2}");
            }
            bttHorario2.setDisable(true);
            cbAtvH2.setDisable(false);
            cbAtvM2.setDisable(false);
        }

    }
    
    @FXML
    private void tratarHorario3(ActionEvent event) {

        if (!cbAtvH3.isDisabled()) {
            if (!"--".equals(cbAtvH3.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA" + cbAtvH3.getValue() + ":"
                        + cbAtvM3.getValue() + ":" + "00.\",\"nSala\":3}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA99:99:99.\",\"nSala\":3}");
            }
            bttHorario3.setDisable(true);
            cbDatvH3.setDisable(false);
            cbDatvM3.setDisable(false);
        } else {
            if (!"--".equals(cbDatvH3.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD" + cbDatvH3.getValue() + ":"
                        + cbDatvM3.getValue() + ":" + "00.\",\"nSala\":3}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD99:99:99.\",\"nSala\":3}");
            }
            bttHorario3.setDisable(true);
            cbAtvH3.setDisable(false);
            cbAtvM3.setDisable(false);
        }

    }    

    @FXML
    private void tratarHorario4(ActionEvent event) {

        if (!cbAtvH4.isDisabled()) {
            if (!"--".equals(cbAtvH4.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA" + cbAtvH4.getValue() + ":"
                        + cbAtvM4.getValue() + ":" + "00.\",\"nSala\":4}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HA99:99:99.\",\"nSala\":4}");
            }
            bttHorario4.setDisable(true);
            cbDatvH4.setDisable(false);
            cbDatvM4.setDisable(false);
        } else {
            if (!"--".equals(cbDatvH4.getValue())) {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD" + cbDatvH4.getValue() + ":"
                        + cbDatvM4.getValue() + ":" + "00.\",\"nSala\":4}");
            } else {
                MainApp.sessao.trataAcao("{\"tipoAcao\":\"HD99:99:99.\",\"nSala\":4}");
            }
            bttHorario4.setDisable(true);
            cbAtvH4.setDisable(false);
            cbAtvM4.setDisable(false);
        }

    }
    

    private void pedirLogs(ActionEvent event) {
        paneSalas.setDisable(true);
        MainApp.sessao.logs();
    }

    @FXML
    private void atualizarSalas(ActionEvent event) {
        paneSalas.setDisable(true);
        MainApp.sessao.atualizar();
    }

    @FXML
    private void tratarDesatvM1(ActionEvent event) {
        if (!atualizado) {
            bttHorario1.setDisable(false);
            cbAtvH1.setDisable(true);
            cbAtvM1.setDisable(true);
            if (!"--".equals(cbDatvM1.getValue())) {
                if (cbDatvH1.getValue().equals("--")) {
                    cbDatvH1.setValue("00");
                }
            } else {
                cbDatvH1.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvM2(ActionEvent event) {
        if (!atualizado) {
            bttHorario2.setDisable(false);
            cbAtvH2.setDisable(true);
            cbAtvM2.setDisable(true);
            if (!"--".equals(cbDatvM2.getValue())) {
                if (cbDatvH2.getValue().equals("--")) {
                    cbDatvH2.setValue("00");
                }
            } else {
                cbDatvH2.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvM3(ActionEvent event) {
        if (!atualizado) {
            bttHorario3.setDisable(false);
            cbAtvH3.setDisable(true);
            cbAtvM3.setDisable(true);
            if (!"--".equals(cbDatvM3.getValue())) {
                if (cbDatvH3.getValue().equals("--")) {
                    cbDatvH3.setValue("00");
                }
            } else {
                cbDatvH3.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvM4(ActionEvent event) {
        if (!atualizado) {
            bttHorario4.setDisable(false);
            cbAtvH4.setDisable(true);
            cbAtvM4.setDisable(true);
            if (!"--".equals(cbDatvM4.getValue())) {
                if (cbDatvH4.getValue().equals("--")) {
                    cbDatvH4.setValue("00");
                }
            } else {
                cbDatvH4.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvH1(ActionEvent event) {
        if (!atualizado) {
            bttHorario1.setDisable(false);
            cbAtvH1.setDisable(true);
            cbAtvM1.setDisable(true);
            if (!"--".equals(cbDatvH1.getValue())) {
                if (cbDatvM1.getValue().equals("--")) {
                    cbDatvM1.setValue("00");
                }
            } else {
                cbDatvM1.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvH2(ActionEvent event) {
        if (!atualizado) {
            bttHorario2.setDisable(false);
            cbAtvH2.setDisable(true);
            cbAtvM2.setDisable(true);
            if (!"--".equals(cbDatvH2.getValue())) {
                if (cbDatvM2.getValue().equals("--")) {
                    cbDatvM2.setValue("00");
                }
            } else {
                cbDatvM2.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvH3(ActionEvent event) {
        if (!atualizado) {
            bttHorario3.setDisable(false);
            cbAtvH3.setDisable(true);
            cbAtvM3.setDisable(true);
            if (!"--".equals(cbDatvH3.getValue())) {
                if (cbDatvM3.getValue().equals("--")) {
                    cbDatvM3.setValue("00");
                }
            } else {
                cbDatvM3.setValue("--");
            }
        }
    }

    @FXML
    private void tratarDesatvH4(ActionEvent event) {
        if (!atualizado) {
            bttHorario4.setDisable(false);
            cbAtvH4.setDisable(true);
            cbAtvM4.setDisable(true);
            if (!"--".equals(cbDatvH4.getValue())) {
                if (cbDatvM4.getValue().equals("--")) {
                    cbDatvM4.setValue("00");
                }
            } else {
                cbDatvM4.setValue("--");
            }
        }
    }

    @FXML
    private void tratarAtvH1(ActionEvent event) {
        if (!atualizado) {
            bttHorario1.setDisable(false);
            cbDatvH1.setDisable(true);
            cbDatvM1.setDisable(true);
            if (!"--".equals(cbAtvH1.getValue())) {
                if (cbAtvM1.getValue().equals("--")) {
                    cbAtvM1.setValue("00");
                }
            } else {
                cbAtvM1.setValue("--");
            }
        }
    }

    @FXML
    private void tratarAtvH2(ActionEvent event) {
        if (!atualizado) {
            bttHorario2.setDisable(false);
            cbDatvH2.setDisable(true);
            cbDatvM2.setDisable(true);
            if (!"--".equals(cbAtvH2.getValue())) {
                if (cbAtvM2.getValue().equals("--")) {
                    cbAtvM2.setValue("00");
                }
            } else {
                cbAtvM2.setValue("--");
            }
        }
    }

    @FXML
    private void tratarAtvH3(ActionEvent event) {
        if (!atualizado) {
            bttHorario3.setDisable(false);
            cbDatvH3.setDisable(true);
            cbDatvM3.setDisable(true);
            if (!"--".equals(cbAtvH3.getValue())) {
                if (cbAtvM3.getValue().equals("--")) {
                    cbAtvM3.setValue("00");
                }
            } else {
                cbAtvM3.setValue("--");
            }
        }
    }

    @FXML
    private void tratarAtvH4(ActionEvent event) {
        if (!atualizado) {
            bttHorario4.setDisable(false);
            cbDatvH4.setDisable(true);
            cbDatvM4.setDisable(true);
            if (!"--".equals(cbAtvH4.getValue())) {
                if (cbAtvM4.getValue().equals("--")) {
                    cbAtvM4.setValue("00");
                }
            } else {
                cbAtvM4.setValue("--");
            }
        }
    }

    @FXML
    private void tratarAtvM1(ActionEvent event) {

        if (!atualizado) {
            bttHorario1.setDisable(false);
            cbDatvH1.setDisable(true);
            cbDatvM1.setDisable(true);
            if (!"--".equals(cbAtvM1.getValue())) {

                if (cbAtvH1.getValue().equals("--")) {
                    cbAtvH1.setValue("00");
                }
            } else {
                cbAtvH1.setValue("--");
            }
        }

    }

    @FXML
    private void tratarAtvM2(ActionEvent event) {

        if (!atualizado) {
            bttHorario2.setDisable(false);
            cbDatvH2.setDisable(true);
            cbDatvM2.setDisable(true);
            if (!"--".equals(cbAtvM2.getValue())) {

                if (cbAtvH2.getValue().equals("--")) {
                    cbAtvH2.setValue("00");
                }
            } else {
                cbAtvH2.setValue("--");
            }
        }

    }

    @FXML
    private void tratarAtvM3(ActionEvent event) {

        if (!atualizado) {
            bttHorario3.setDisable(false);
            cbDatvH3.setDisable(true);
            cbDatvM3.setDisable(true);
            if (!"--".equals(cbAtvM3.getValue())) {

                if (cbAtvH3.getValue().equals("--")) {
                    cbAtvH3.setValue("00");
                }
            } else {
                cbAtvH3.setValue("--");
            }
        }

    }

    @FXML
    private void tratarAtvM4(ActionEvent event) {

        if (!atualizado) {
            bttHorario4.setDisable(false);
            cbDatvH4.setDisable(true);
            cbDatvM4.setDisable(true);
            if (!"--".equals(cbAtvM4.getValue())) {

                if (cbAtvH4.getValue().equals("--")) {
                    cbAtvH4.setValue("00");
                }
            } else {
                cbAtvH4.setValue("--");
            }
        }

    }

    private class AtualizaDados implements Runnable {

        @Override
        public void run() {
            while (MainApp.running) {
                String resposta = MainApp.sessao.verificarResposta();
                if (resposta.contains("tempAr")) {
                    salas = gson.fromJson(resposta, tipoListaSala);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            atualizado = true;
                            interfaceSalas();
                            MainApp.sessao.logs();
                            atualizado = false;
                        }
                    });
                } else if (resposta.contains("dataAcao")) {
                    logs = gson.fromJson(resposta, tipoListaLogs);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            atualizado = true;
                            interfaceLogs();
                            atualizado = false;
                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("FEZES COM A THREAD");
                }
            }
            System.exit(0);
        }

    }

}

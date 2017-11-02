package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private ObservableList<String> cities = FXCollections.observableArrayList("Kraków", "Rzeszów", "Lublin");
    private final static String[] LOCATION_URL = {
            "http://api.apixu.com/v1/current.json?key=d578c483ed8b494bad8170534161511&q=Cracow",
            "http://api.apixu.com/v1/current.json?key=d578c483ed8b494bad8170534161511&q=Rzeszow",
            "http://api.apixu.com/v1/current.json?key=d578c483ed8b494bad8170534161511&q=Lublin"};
    @FXML
    private Button refreshButton;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label pressureLabel;

    @FXML
    private Label cloudLabel;

    @FXML
    private Label precipLabel;

    @FXML
    private Label windLabel;

    @FXML
    private ChoiceBox chooseCity;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        chooseCity.setItems(cities);
        chooseCity.setValue(cities.get(0));

        try {
            setValues(new URL(LOCATION_URL[0]));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        chooseCity.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    String currentUrl = LOCATION_URL[newValue.intValue()];
                    try {
                        URL url = new URL(currentUrl);
                        setValues(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    @FXML
    public void refresh() {
        refreshButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    setValues(new URL(LOCATION_URL[getIndex(cities, chooseCity.getAccessibleText())]));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setValues(URL url) {
        try {
            JSONObject json = new JSONObject(IOUtils.toString(url));
            JSONObject currentJson = (JSONObject) json.get("current");
            String tempC = currentJson.get("temp_c").toString();
            String cloud = currentJson.get("cloud").toString();
            String precip = currentJson.get("precip_mm").toString();
            String pressure = currentJson.get("pressure_mb").toString();
            String wind = currentJson.get("wind_kph").toString();
            pressureLabel.setText("Ciśnienie: " + pressure + " hPa");
            temperatureLabel.setText("Temperatura: " + tempC + " c");
            cloudLabel.setText("Zachmurzenie: " + cloud + " %");
            precipLabel.setText("Opady: " + precip + " mm");
            windLabel.setText("Prędkość wiatru: " + wind + " km/h");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIndex(ObservableList<String> arr, String value) {
        for (int i = 0; i < arr.size(); i++) {
            if (value.equals(arr.get(i))) {
                return i;
            }
        }
        return 0;
    }
}

package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.models.Donor;
import org.models.dtos.CharityCaseDto;
import org.services.ITeledonService;
import org.services.Observer;
import org.services.ServiceException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TeledonController implements Initializable, Observer {
    private ITeledonService teledonService;
    private final ObservableList<CharityCaseDto> charityCases = FXCollections.observableArrayList();
    private final ObservableList<Donor> filteredDonors = FXCollections.observableArrayList();
    private Stage mainStage;

    @FXML
    public TextField donorNameField;
    @FXML
    public Spinner<Double> amountSpinner;
    @FXML
    private TextField donorEmailField;
    @FXML
    private TextField donorPhoneNumberField;
    @FXML
    private TableView<Donor> donorsTableView;
    @FXML
    private TableColumn<Donor, String> nameColumn;
    @FXML
    private TableColumn<Donor, String> emailColumn;
    @FXML
    private TableColumn<Donor, String> phoneColumn;
    @FXML
    private TableView<CharityCaseDto> charityTableView;
    @FXML
    private TableColumn<CharityCaseDto, Double> totalMoneyColumn;
    @FXML
    private TableColumn<CharityCaseDto, Long> idColumn;
    @FXML
    private TableColumn<CharityCaseDto, String> caseNameColumn;
    @FXML
    private TableColumn<CharityCaseDto, String> descriptionColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.caseNameColumn.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        this.totalMoneyColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmountCollected"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.emailColumn.setCellValueFactory(new PropertyValueFactory<>("mailAddress"));
        this.phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        this.amountSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 9999, 1));

        this.charityTableView.setItems(this.charityCases);
        this.donorsTableView.setItems(this.filteredDonors);
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void setService(ITeledonService teledonService) {
        this.teledonService = teledonService;
        initialiseCharityCases();
    }

    private void initialiseCharityCases() {
        try {
            Iterable<CharityCaseDto> charityCasesDto = teledonService.getAllCharityCases();
            List<CharityCaseDto> cases = new ArrayList<>();
            charityCasesDto.forEach(cases::add);
            this.charityCases.setAll(cases);
        } catch (ServiceException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error in receiving donors");
            alert.show();
        }
    }


    public void onTextChanged(KeyEvent inputMethodEvent) {
        String nameSubstring = donorNameField.getText();
        if (nameSubstring == null || nameSubstring == "") {
            return;
        }
        Iterable<Donor> filteredDonors = teledonService.getDonorsWithNameContaining(nameSubstring);
        System.out.println(nameSubstring);
        List<Donor> donors = new ArrayList<>();
        filteredDonors.forEach(donors::add);
        this.filteredDonors.setAll(donors);
    }

    public void onSelectedDonor(MouseEvent mouseEvent) {
        Donor donor = donorsTableView.getSelectionModel().getSelectedItem();
        if (donor == null) {
            return;
        }
        this.donorNameField.setText(donor.getName());
        this.donorEmailField.setText(donor.getMailAddress());
        this.donorPhoneNumberField.setText(donor.getPhoneNumber());

    }

    public void onAddDonation(ActionEvent actionEvent) {
        CharityCaseDto charityCaseDto = charityTableView.getSelectionModel().getSelectedItem();
        if (charityCaseDto == null) {
            return;
        }
        String donorName = donorNameField.getText();
        String donorEmail = donorEmailField.getText();
        String donorPhone = donorPhoneNumberField.getText();
        Double donationAmount = amountSpinner.getValue();

        if (donorName == null || donorEmail == null || donorPhone == null) {
            return;
        }
        if (donorName.equals("") || donorEmail.equals("") || donorPhone.equals("")) {
            return;
        }
        try {
            this.teledonService.addNewDonation(charityCaseDto.getId(), donorName, donorEmail, donorPhone, donationAmount);
        } catch (ServiceException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in adding donation");
            alert.setContentText(exception.getMessage());

            alert.showAndWait();
        }
    }


    @FXML
    public void onLogOut(ActionEvent actionEvent) {
        this.teledonService.logout();
//        this.mainStage.close();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void update() {
        System.out.println("Planning to update view");
        Platform.runLater(this::initialiseCharityCases);
    }
}

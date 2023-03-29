package com.example.labgui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.dtos.CharityCaseDto;
import org.example.models.Donor;
import org.example.observer.Event;
import org.example.observer.Observer;
import org.example.services.CharityCaseService;
import org.example.services.DonationService;
import org.example.services.DonorService;
import org.example.services.ServiceException;
import org.example.validators.ValidationError;

import java.net.URL;
import java.util.*;

public class TeledonController implements Initializable, Observer<Event> {
    private CharityCaseService charityCaseService;
    private DonationService donationService;
    private DonorService donorService;
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

    public void setCharityCaseService(CharityCaseService charityCaseService) {
        this.charityCaseService = charityCaseService;
        initialiseCharityCases();
    }

    public void setDonationService(DonationService donationService) {
        this.donationService = donationService;
        this.donationService.addObserver(this);
    }

    public void setDonorService(DonorService donorService) {
        this.donorService = donorService;
    }

    private void initialiseCharityCases() {
        Iterable<CharityCaseDto> charityCasesDto = charityCaseService.getAllCharityCases();
        List<CharityCaseDto> cases = new ArrayList<>();
        charityCasesDto.forEach(cases::add);
        this.charityCases.setAll(cases);
    }


    public void onTextChanged(KeyEvent inputMethodEvent) {
        String nameSubstring = donorNameField.getText();
        if (nameSubstring == null || nameSubstring == "") {
            return;
        }
        Iterable<Donor> filteredDonors = donorService.getDonorsWithNameContaining(nameSubstring);
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
            this.donationService.addNewDonation(charityCaseDto.getId(), donorName, donorEmail, donorPhone, donationAmount);
        } catch (ValidationError | ServiceException exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error in adding donation");
            alert.setContentText(exception.getMessage());

            alert.showAndWait();
        }
    }

    @Override
    public void update(Event event) {
        this.initialiseCharityCases();
    }

    @FXML
    public void onLogOut(ActionEvent actionEvent) {
        this.mainStage.close();
    }
}

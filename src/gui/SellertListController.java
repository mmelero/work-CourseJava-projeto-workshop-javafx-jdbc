package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listener.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellertListController implements Initializable, DataChangeListener {
	
	//criar uma depenencia do SellerServicw;
	private SellerService service;
	

	//Criar as referencias para os componentes do SellerList
	@FXML
	private TableView<Seller> tableViewSeller;
	
	//Colunas da table view
	@FXML
	private TableColumn<Seller, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Seller, String> TableColumnName;
	
	@FXML
	private TableColumn<Seller, String> TableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> TableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> TableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;
	
	//Carregar a list de serviços na tableView;
	private ObservableList<Seller> obsList;
	
	
	@FXML
	private Button btNew;
	//Metodo para tratamento do botão
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}
	
	//Injetar dependencia do SellerService / inversão de controle;
	public void setSellerService(SellerService service) {
		this.service = service;
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		//metodo para configurar a tabela
		initializeNodes();

		
	}


	private void initializeNodes() {
		// padrão javaFX para inicializar os padrões de comportamento das colunas;
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		TableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(TableColumnBirthDate, "dd/MM/yyyy");
		TableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(TableColumnBaseSalary, 2);
		
		//Macete para que a tabela (tableView) acompanhe o tamanho da tela;
		//Objetivo: pegar a referencia da janela e dimenciona-la conforme elementos da tabela
		
		
		Stage stage =  (Stage) Main.getMainScen().getWindow();
		
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
		
		
	}
	//metodo para carregar os serviços na obsList e carregar os dados para a tableView
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Srevice was null");
		}
		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller obj, String abusolteName,Stage parentStage) {
		         
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(abusolteName));
			Pane pane = loader.load();
			
			SellerFormController controller = loader.getController(); 
			controller.setSeller(obj);
			controller.updateFormData();
			controller.setDepatamentService(new SellerService());
			controller.subscribeDataChangeListener(this);
			
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Sellere Data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view ", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void dataCheged() {
		 
		updateTableView();
		
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller obj) {
		
		Optional<ButtonType> result =  Alerts.showConfirmation("Confirmation", "Are you sure to delete ?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try{
			service.remove(obj);
			updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Error remove Object", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
		
	}
}

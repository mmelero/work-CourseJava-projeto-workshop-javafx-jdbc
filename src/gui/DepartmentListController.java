package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	//criar uma depenencia do DepartmentServicw;
	private DepartmentService service;
	

	//Criar as referencias para os componentes do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	//Colunas da table view
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> TableColumnName;
	
	//Carregar a list de servi�os na tableView;
	private ObservableList<Department> obsList;
	
	
	@FXML
	private Button btNew;
	//Metodo para tratamento do bot�o
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	//Injetar dependencia do DepartmentService / invers�o de controle;
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		//metodo para configurar a tabela
		initializeNodes();

		
	}


	private void initializeNodes() {
		// padr�o javaFX para inicializar os padr�es de comportamento das colunas;
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Macete para que a tabela (tableView) acompanhe o tamanho da tela;
		//Objetivo: pegar a referencia da janela e dimenciona-la conforme elementos da tabela
		
		
		Stage stage =  (Stage) Main.getMainScen().getWindow();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
		
	}
	//metodo para carregar os servi�os na obsList e carregar os dados para a tableView
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Srevice was null");
		}
		List<Department> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(obsList);
	}

}
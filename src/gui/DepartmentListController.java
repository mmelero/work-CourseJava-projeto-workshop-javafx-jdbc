package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	//Criar as referencias para os componentes do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	//Colunas da table view
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> TableColumnName;
	
	@FXML
	private Button btNew;
	//Metodo para tratamento do botão
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
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
		
		//Macete para que a tabela (tableView) acompanhe o tamanho da tela;
		//Objetivo: pegar a referencia da janela e dimenciona-la conforme elementos da tabela
		
		
		Stage stage =  (Stage) Main.getMainScen().getWindow();
		
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
		
	}

}

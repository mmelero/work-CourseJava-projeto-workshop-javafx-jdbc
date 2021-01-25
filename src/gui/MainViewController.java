package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{

	@FXML
	MenuItem menuItemSeller;
	
	@FXML
	MenuItem menuItemDepartment;

	@FXML
	MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSeller() {
		System.out.println("onMenuItemSeller");
	}
	
	@FXML
	public void onMenuItemDepartment() {
		loadView("/gui/DepartmentList.fxml");
	}

	@FXML
	public void onMenuItemAbout() {
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	//Metodo criado para carregar a view About
	private void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			// pegar a referencia do palco principal
			Scene mainScene = Main.getMainScen();
			
			//variavel criada para guardar a referencia do Vbox da janela princial;
			//Objetivo: Pegar o primeiro elemento da view
			//Efetuar os casting ScroollPane e VBox e pegar o conteudo da view
			VBox mainVBOx = (VBox) ((ScrollPane)mainScene.getRoot()).getContent();
			
			//limpar o conteudo do menu principal do menuBar e incluir o conteudo do menuBar About
			
			// Guardar referecia do menuBar na posicao "zero" da janela principal
			Node mainMenu = mainVBOx.getChildren().get(0);
			
			//Limpar todos os elementos do menuBar
			mainVBOx.getChildren().clear();
			
			//Adicionar MenuBar vazio
			mainVBOx.getChildren().add(mainMenu);
			
			//Adicionar os itens do menu About
			mainVBOx.getChildren().addAll(newVBox);
			
			
			
			
		}
		catch(IOException e) {
		
			Alerts.showAlert("IO Exeception", "Error Loading View", e.getMessage(), AlertType.ERROR);
		}
	}

}

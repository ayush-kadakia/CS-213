/*
 * Names: Allen Estrada(are55), Ayush Kadakia(aak227)
 */
package view;

import java.util.ArrayList;


import app.Song;

import java.util.Collections;
import java.io.*;

import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;



public class ListController {

	@FXML
	ListView<Song> songListView;

	@FXML
	private TextField songTitle;

	@FXML
	private TextField songArtist;

	@FXML
	private TextField songAlbum;

	@FXML
	private TextField songYear;

	@FXML
	private Button editButton;

	@FXML
	private Button deleteButton;

	@FXML
	private TextField newTitle;

	@FXML
	private TextField newArtist;

	@FXML
	private TextField newAlbum;

	@FXML
	private TextField newYear;

	@FXML
	private Button addButton;

	@FXML
	private Button applyButton;
	@FXML
	private Button cancelButton;

	public static ObservableList<Song> obsList;

	private Stage mainStage;

	public void start(Stage mainStage) {
		this.mainStage = mainStage;

		obsList = FXCollections.observableArrayList(importSongs());
		songListView.setItems(obsList);

		songTitle.setDisable(true);
		songArtist.setDisable(true);
		songAlbum.setDisable(true);
		songYear.setDisable(true);
		applyButton.setVisible(false);

		// select the first item
		if (!obsList.isEmpty())
			songListView.getSelectionModel().select(0);
		updateSelection();

		songListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateSelection());

	}

	private ArrayList<Song> importSongs() {
		ArrayList<Song> songs = new ArrayList<Song>();
		try (BufferedReader br = new BufferedReader(new FileReader("songs.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] helper = line.trim().split("\\|", -1);
				songs.add(new Song(helper[0], helper[1], helper[2], helper[3]));
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		Collections.sort(songs);
		return songs;
	}

	private void updateSelection() {
		songTitle.setDisable(true);
		songArtist.setDisable(true);
		songAlbum.setDisable(true);
		songYear.setDisable(true);
		applyButton.setVisible(false);
		cancelButton.setVisible(false);

		int idx = songListView.getSelectionModel().getSelectedIndex();
		if (idx < 0) {
			songTitle.setText("");
			songArtist.setText("");
			songAlbum.setText("");
			songYear.setText("");
			return;
		}
		Song s = songListView.getSelectionModel().getSelectedItem();
		songTitle.setText(s.getTitle());
		songArtist.setText(s.getArtist());
		songAlbum.setText(s.getAlbum());
		songYear.setText(s.getYear());
	}

	private boolean containsSong(String t, String a) {
		return (obsList.stream().filter(o -> o.getTitle().toLowerCase().equals(t.toLowerCase())).findFirst().isPresent()
				&& obsList.stream().filter(o -> o.getArtist().toLowerCase().equals(a.toLowerCase())).findFirst()
						.isPresent());
	}

	@FXML
	void handleAdd(ActionEvent event) {
		String newT = newTitle.getText().trim();
		String newAr = newArtist.getText().trim();
		String newAl = newAlbum.getText().trim();
		String newY = newYear.getText().trim();

		if (newT.length() == 0 || newAr.length() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid Parameter(s).");
			alert.setContentText("Cannot add a song with a blank title or artist.");
			alert.showAndWait();

			return;
		}

		if (containsSong(newT, newAr)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to add.");
			alert.setContentText("Cannot add a song that already exists in the list.");
			alert.showAndWait();
			return;
		}

		if(newT.contains("|") || newAr.contains("|") || newAl.contains("|") || newY.contains("|")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid parameter(s).");
			alert.setContentText("Cannot use the pipe character in any of the fields.");
			alert.showAndWait();
			return;
		}
		
		try {
			if (newY.length() > 0 && Integer.parseInt(newY) < 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(mainStage);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid parameter(s).");
				alert.setContentText("Year must be a positive integer.");
				alert.showAndWait();
				return;
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid parameter(s).");
			alert.setContentText("Year must be a positive integer.");
			alert.showAndWait();
			return;
		}

		Song s = new Song(newT, newAr, newAl, newY);
		obsList.add(s);
		Collections.sort(obsList);

		newTitle.setText("");
		newArtist.setText("");
		newAlbum.setText("");
		newYear.setText("");
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Success");
		alert.setHeaderText("Add was successful.");
		alert.setContentText("Successfully added " + s.toString() + ".");
		alert.showAndWait();
		
	}

	@FXML
	void handleDelete(ActionEvent event) {
		songTitle.setDisable(true);
		songArtist.setDisable(true);
		songAlbum.setDisable(true);
		songYear.setDisable(true);
		applyButton.setVisible(false);

		Song s = songListView.getSelectionModel().getSelectedItem();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Confirm Deletion");
		alert.setHeaderText("Are you sure you want to delete " + s.toString() + "?");
		alert.showAndWait();
		if(alert.getResult().equals(ButtonType.OK))
			obsList.remove(s);

	}

	@FXML
	void handleEdit(ActionEvent event) {
		songTitle.setDisable(false);
		songArtist.setDisable(false);
		songAlbum.setDisable(false);
		songYear.setDisable(false);
		applyButton.setVisible(true);
		cancelButton.setVisible(true);
		// cancel button
	}

	@FXML
	void handleApply(ActionEvent event) {
		Song s = songListView.getSelectionModel().getSelectedItem();
		String newT = songTitle.getText().trim();
		String newAr = songArtist.getText().trim();
		String newAl = songAlbum.getText().trim();
		String newY = songYear.getText().trim();

		if (newT.length() == 0 || newAr.length() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid parameter(s).");
			alert.setContentText("Cannot edit a song to have a blank title or artist.");
			alert.showAndWait();
			return;
		}

		if (containsSong(newT, newAr) && !(s.getTitle().equalsIgnoreCase(newT) && s.getArtist().equalsIgnoreCase(newAr))) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to edit.");
			alert.setContentText("Cannot edit a song to have the same title and artist as another song in the list.");
			alert.showAndWait();
			return;
		}

		if(newT.contains("|") || newAr.contains("|") || newAl.contains("|") || newY.contains("|")) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid parameter(s).");
			alert.setContentText("Cannot use the pipe character in any of the fields.");
			alert.showAndWait();
			return;
		}
		
		try {
			if (newY.length() > 0 && Integer.parseInt(newY) < 1) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initOwner(mainStage);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid parameter(s).");
				alert.setContentText("Year must be a positive integer.");
				alert.showAndWait();
				return;
			}
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Invalid parameter(s).");
			alert.setContentText("Year must be a positive integer.");
			alert.showAndWait();
			return;
		}
		
		if (newT.equalsIgnoreCase(s.getTitle()) && newAr.equalsIgnoreCase(s.getArtist())
				&& newAl.equalsIgnoreCase(s.getAlbum()) && newY.equalsIgnoreCase(s.getYear())) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error");
			alert.setHeaderText("Failed to edit.");
			alert.setContentText("No changes were made to the selected song.");
			alert.showAndWait();
			return;
		}

		if (!newT.equals(s.getTitle()))
			songListView.getSelectionModel().getSelectedItem().setTitle(newT);
		if (!newAr.equals(s.getArtist()))
			songListView.getSelectionModel().getSelectedItem().setArtist(newAr);
		if (!newAl.equals(s.getAlbum()))
			songListView.getSelectionModel().getSelectedItem().setAlbum(newAl);
		if (!newY.equals(s.getYear()))
			songListView.getSelectionModel().getSelectedItem().setYear(newY);

		Collections.sort(obsList);

		songTitle.setDisable(true);
		songArtist.setDisable(true);
		songAlbum.setDisable(true);
		songYear.setDisable(true);

		
		// write to file when done

		applyButton.setVisible(false);
		cancelButton.setVisible(false);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Success");
		alert.setHeaderText("Edit was successful.");
		alert.setContentText("Successfully edited the selected song.");
		alert.showAndWait();
	}

	@FXML
	void handleCancel(ActionEvent event) {
		songTitle.setDisable(true);
		songArtist.setDisable(true);
		songAlbum.setDisable(true);
		songYear.setDisable(true);

		Song s = songListView.getSelectionModel().getSelectedItem();
		songTitle.setText(s.getTitle());
		songArtist.setText(s.getArtist());
		songAlbum.setText(s.getAlbum());
		songYear.setText(s.getYear());

		applyButton.setVisible(false);
		cancelButton.setVisible(false);
	}

}

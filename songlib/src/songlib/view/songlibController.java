// Gabrielle Capulong and Parth Vora - Software Methodology Fall 2020 
// Song Library Project - Assignment 1 Due October 2nd 2020

package songlib.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.scene.control.TextArea;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class songlibController implements Initializable{
	
	// BUTTON FIELDS 
	@FXML Button add; // add button 
	@FXML Button edit; // edit button
	@FXML Button delete; // delete button 
	@FXML Button clear; // clear button
	
	// TEXT FIELDS
	@FXML TextField song; // name of song
	@FXML TextField artist; // name of artist
	@FXML TextField album; // name of album
	@FXML TextField year; // year song was made
	
	// MAIN SONG LIST VIEWER
	@FXML ListView<String> song_list;
	
	// ITERATORS and MAIN SONG STORAGE STRUCTURE
    ObservableList observableList = FXCollections.observableArrayList();
    TreeMap<String, Entry> tm= new TreeMap<String, Entry>((String.CASE_INSENSITIVE_ORDER));
    Set<java.util.Map.Entry<String, Entry>> set = tm.entrySet();
    
    // LISTS OF ALL SONGS IN VIEW with UPDATED POINTER
    List<String> values = new ArrayList<String>();
    List<String> valuesUpdated = new ArrayList<String>();
   
    String currItem; // storage of current item
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { //start on opening of GUI
        
        try {
			addToListView("songlibrarylist.txt"); // start list with song library list file populating 
			
		} catch (FileNotFoundException e) {
	
			e.printStackTrace();
		}
    }
    
	public void alert(ActionEvent e) throws IOException { // called on all four button presses

		Button b = (Button)e.getSource(); // get button
	
		if (b.getId().equals("add")) { // ADD
			
			Alert addAlert = new Alert(AlertType.CONFIRMATION); // confirmation of add 
			addAlert.setTitle("Adding a Song");
			addAlert.setHeaderText("If you would like to proceed and add a song, make sure you first press clear and input new information below under song, artist, etc.");
			addAlert.setContentText("Are you sure you would like add the song? Press ok to proceed.");
			Optional<ButtonType> result = addAlert.showAndWait();
			
			if (result.get() == ButtonType.OK){ // if confirmed, add to list via helper method
			    addAndRefresh("a", null);
			} 
			
		// -------------------------------------------------------------------------	
			
		} else if (b.getId().equals("edit")) { //EDIT
			
			if (values.isEmpty()) { // check if list is empty and run error log
				
				Alert addAlert = new Alert(AlertType.INFORMATION);
		    	addAlert.setTitle("Editing Song Error");
		    	addAlert.setHeaderText("There are no songs to edit. Please add a song first.");
		    	addAlert.showAndWait();
			
			} else {  // if list is  not empty
			
				Alert alert = new Alert(AlertType.CONFIRMATION); // confirmation of edit
				alert.setTitle("Editing a Song");
				alert.setHeaderText("Edit the information of the song selected by changing the fields below. If you made the changes, hit OK. ");
				alert.setContentText("Are you ok with this?");
				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.get() == ButtonType.OK){ // if confirmed, edit list via helper method 
					addAndRefresh("c", tm.get(currItem)); // call helper method and store old item before edit. 	
				} 
			
			}
			
		// -------------------------------------------------------------------------
			
			
		} else if (b.getId().equals("delete")) { //DELETE
			
			if (values.isEmpty()) { // test case to see if list is empty. Cannot delete from empty list.
				
				Alert addAlert = new Alert(AlertType.INFORMATION);
		    	addAlert.setTitle("Deleting Song Error");
		    	addAlert.setHeaderText("There are no songs to delete. Please add a song first.");
		    	addAlert.showAndWait();
		    	
			} else { // if list is not empty
			
				Alert alert = new Alert(AlertType.CONFIRMATION); // confirm delete of item
				alert.setTitle("Deleting a Song");
				alert.setHeaderText("If you do not want this song to be in the song library, please proceed.");
				alert.setContentText("This action cannot be undone, please hit OK to proceed");
				Optional<ButtonType> result = alert.showAndWait();
				
				if (result.get() == ButtonType.OK){ // if deletion is confirmed 
					
					String artistToDelete = tm.get(song_list.getSelectionModel().getSelectedItem()).eArtist;
				    String songToDelete = tm.get(song_list.getSelectionModel().getSelectedItem()).eName;
				    String songArtistToDelete = songToDelete + " by " + artistToDelete; // get key of song to delete
				    
				    if (song_list.getSelectionModel().isEmpty() == false) { // if an item IS selected
				    	
				    	delete(songArtistToDelete); // delete item
				    	
				    } else { // if no items are selected (happens when CLEAR --> DELETE)
				    	
				    	Alert addAlert = new Alert(AlertType.INFORMATION);
				    	addAlert.setTitle("Deleting Song Error");
				    	addAlert.setHeaderText("Please select a song you want to delete.");
				    	addAlert.showAndWait();
				    	
				    }
				    
				    // UPDATE LIST WITH NEW VALUES
				    
				    Iterator i = set.iterator();
				    
				    while(i.hasNext()) { //add to the updated values
				    	
			  	          Map.Entry me = (Map.Entry)i.next();
			  	          valuesUpdated.add(me.getKey().toString());
			  	    }
			  	      	
			    	song_list.setItems(FXCollections.observableList(valuesUpdated));
			    	values = valuesUpdated;
			    	valuesUpdated = new ArrayList<String>();
			    	song_list.refresh();
			    	
			    	// If there are no more values, allow add button otherwise disable it. 
			    	
			    	if (values.size()>0) {
			    		add.setDisable(true);
			    		
			    	} else {
			    		
			    		add.setDisable(false);
			    		
			    	}
			    	
			    	updateList(); 
			    	
				}
			}
			
			if (values.isEmpty()) { // test case to see if list is empty. Cannot delete from empty list.
				
				edit.setDisable(true);
		    	delete.setDisable(true);
		    	clear.setDisable(true);
			}
		
		// -------------------------------------------------------------------------	
			
		} else if (b.getId().equals("clear")) { // CLEAR
			
			// set all values to empty and remove selection field from all objects. 
			artist.setText("");
		    song.setText("");
		    album.setText("");
		    year.setText("");
			song_list.getSelectionModel().clearSelection();
			
			// allow adding button but disable edit and delete.
			add.setDisable(false);
			edit.setDisable(true);
			delete.setDisable(true);
			
		}
		
	} // END OF 4 BUTTON METHODS
	
	public void addToListView(String textfile) throws FileNotFoundException { // auto-populate list on program launch
	
		File file = new File(textfile); // scan file
		Scanner sc = new Scanner(file); // iterate through lines of input
		
			while (sc.hasNextLine()) { // go to next line
				
				String line = sc.nextLine();
				
				Entry e = new Entry(null,null,null,null,null); // create new empty ENTRY object 
				
				for (String a : line.split(",")){ // split by comma
					
					// add properties for the line to ENTRY object
					
					if (e.eName == null) {
						e.eName = a;
					} else if (e.eArtist == null) {
						e.eArtist = a;
					} else if (e.eAlbum == null) {
						if (a.equals("null")) {
							e.eAlbum = "";
						} else {
							e.eAlbum = a;
						}
					} else {
						if (a.equals("null")) {
							e.eYear = "";
						} else {
							e.eYear = a;
						}
						
					}
				
				}
				
				// create property containing name by artist format - will be displayed as key
				
				e.eNameArtist = e.eName + " by " + e.eArtist;
				
				// add to TreeMap
				
				tm.put(e.eNameArtist,e);
				
			} // END OF WHILE LOOP, ALL ENTRIES IN TREE MAP
			
		     
			// add all entries from tree map into ArrayList values
			
			Iterator i = set.iterator();
		      
		    while(i.hasNext()){
		    	
		    	Map.Entry me = (Map.Entry)i.next();
		        values.add(me.getKey().toString());
		    }
			
		    // if there are entries 
		    
		    if (values.isEmpty() == false) {
		    	
				song_list.setItems(FXCollections.observableList(values)); // add all of values of list view
				song_list.getSelectionModel().select(0); //initialize selection to first item
				currItem = tm.firstKey(); // set current item to the first item
				
				// initialize properties to first item's values
				
				artist.setText(tm.get(tm.firstKey()).eArtist); 
			    song.setText(tm.get(tm.firstKey()).eName); 
			    album.setText(tm.get(tm.firstKey()).eAlbum);
			    year.setText(tm.get(tm.firstKey()).eYear);
			    add.setDisable(true);
			    
		    
		    } else {
		    	edit.setDisable(true);
		    	delete.setDisable(true);
		    	clear.setDisable(true);
		    }
		
	}

	public void handle(MouseEvent arg0) { // on click of list view item
		
		if (values.isEmpty() == false){ // as long as there is a song in the list view
			
		// sets text fields to clicked songs information
			
		currItem = song_list.getSelectionModel().getSelectedItem();
	    artist.setText(tm.get(song_list.getSelectionModel().getSelectedItem()).eArtist);
	    song.setText(tm.get(song_list.getSelectionModel().getSelectedItem()).eName);
	    album.setText(tm.get(song_list.getSelectionModel().getSelectedItem()).eAlbum);
	    year.setText(tm.get(song_list.getSelectionModel().getSelectedItem()).eYear);
	    
	    // disable the add button on click, and enable delete/edit buttons
	    
	    add.setDisable(true);
	    delete.setDisable(false);
	    edit.setDisable(false);
	    
		}
	}
	
	public void updateList() throws IOException { //iterating through the tree map and writing to file
		
		FileWriter wr = new FileWriter("songlibrarylist.txt"); // location to be written to
		
		for(Map.Entry<String,Entry> e : tm.entrySet()) { // write line by line of entries in correct format
			
			String str = e.getValue().eName + "," +  e.getValue().eArtist + "," +  e.getValue().eAlbum + "," +  e.getValue().eYear;
			wr.write(str);
			wr.write("\n");
		}
		
		wr.close ();
		
	} // File is updated.
	
	public void addAndRefresh(String choice,Entry e) throws IOException { // method for adding songs and editing entries
		
		if (artist.getText().equals("") || song.getText().equals("")) { // NULL case
			
	    	Alert addAlert = new Alert(AlertType.INFORMATION);
	    	
	    	if (choice.equals("a")){ // if it's the ADD action
		    	addAlert.setTitle("Adding Song Error");
		    	addAlert.setHeaderText("Please enter a song name and artist name before hitting add song.");
		    	addAlert.setContentText("Please note album and year are optional.");
		    	addAlert.showAndWait();
		    	
	    	} else { // the EDIT action
	    		
	    		addAlert.setTitle("Editing Song Error");
		    	addAlert.setHeaderText("Please enter a song name and artist name before hitting edit song.");
		    	addAlert.setContentText("Please note album and year are optional.");
		    	addAlert.showAndWait();
	    	}
	    	
	    // --------------------------------------------------------------------------
	    	
	    } else {
	    	
	    	// creating new ENTRY object for item fields.
	    	
	    	String newSong = song.getText();
	    	String newArtist = artist.getText();
	    	String newAlbum = album.getText();
	    	String newYear = year.getText();
	    	String newSongArtist = newSong + " by " + newArtist;
	    	//System.out.print(newYear);
	    	
	    	if (!( newYear.matches("[0-9]+") || newYear.equals(""))){ // if year has letters
	    		
	    		Alert addAlert = new Alert(AlertType.INFORMATION);
		    	addAlert.setTitle("Song Error");
		    	addAlert.setHeaderText("Please enter a valid year value ( Year must be a positive number)");
		    	addAlert.setContentText("Please note album and year are optional.");
		    	addAlert.showAndWait();	
		    	
	    	} else {
	    			
	    		int yearVal = 0;
	    		
		    		if (!newYear.equals("")) {
				    	yearVal = Integer.parseInt(newYear); // get int of year
		    		} else {
		    			yearVal =1;		
		    		}
				    	if (yearVal<0 ) { // if year is negative
				    		
				    		Alert addAlert = new Alert(AlertType.INFORMATION);
					    	addAlert.setTitle("Song Error");
					    	addAlert.setHeaderText("Please enter a valid year value ( Year must be > 0)");
					    	addAlert.setContentText("Please note album and year are optional.");
					    	addAlert.showAndWait();	
			    		
			    	
		    	} else {
	    		
		    		Entry newEntry = new Entry(song.getText(),artist.getText(),album.getText(),year.getText(),newSongArtist);
		    	
		    		if (choice.equals("a")) { // ADD
		    		
			    		Boolean isContained = false;
				    	
				    	for (Map.Entry<String, Entry> 
			            entry : tm.entrySet())
				    		
				    	if (entry.getKey().toLowerCase().equals(newSongArtist.toLowerCase())) {
				    		
				    		isContained = true;
				    		
				    	}
		           
				    	if (isContained) { // if entry is already in song list
				    		Alert addAlert = new Alert(AlertType.INFORMATION);
					    	addAlert.setTitle("Adding Song Error");
					    	addAlert.setHeaderText("Please enter a NEW song name and artist name before hitting add song.");
					    	addAlert.setContentText("Please note album and year are optional.");
					    	addAlert.showAndWait();	
					    	
				    	} else { // add to tree map and shuffle into correct location.
				    		
				    		song_list.getSelectionModel().getSelectedItems().removeAll(values);
				    		tm.put(newSongArtist,newEntry);
				    		Set<java.util.Map.Entry<String, Entry>> set = tm.entrySet();
				    		Iterator i = set.iterator();
				    		
				    		while(i.hasNext()) {
				    			 
					  	          Map.Entry me = (Map.Entry)i.next();
					  	          valuesUpdated.add(me.getKey().toString());
					  	    }
					  	      	
				    		// update values and push into list view
					    	song_list.setItems(FXCollections.observableList(valuesUpdated));
					    	values = valuesUpdated; //updating the base values to be the updated version
					    	valuesUpdated = new ArrayList<String>();
					    	song_list.refresh();
					    	add.setDisable(true);
					    	updateList(); 
					    	
					    	// set selection focus to item that was just added
					    	song_list.getSelectionModel().select(newSongArtist);
					    	artist.setText(tm.get(newSongArtist).eArtist);
						    song.setText(tm.get(newSongArtist).eName);
						    album.setText(tm.get(newSongArtist).eAlbum);
						    year.setText(tm.get(newSongArtist).eYear);
					    	currItem = newSongArtist;
					    	delete.setDisable(false);
						    edit.setDisable(false);
						    clear.setDisable(false);
				    	} 
			    
		    		} else { //EDIT
		    		
			    		Entry e2 = e; // copy entry in case there is error in editing. Used to re-add data. 
			    		
			    		delete(e.eNameArtist); // delete entry.
			    		
			    		Boolean isContained = false;
			    		
			    		for (Map.Entry<String, Entry> 
			            entry : tm.entrySet())
				    		
				    	if (entry.getKey().toLowerCase().equals(newSongArtist.toLowerCase())) {
				    		
				    		isContained = true;
				    		
				    	}
			    		
			    		if (isContained) { //if the edited entry = an existing one 
			    			
			    			Alert addAlert = new Alert(AlertType.INFORMATION);
					    	addAlert.setTitle("Editing Song Error");
					    	addAlert.setHeaderText("Please enter a NEW song name and artist name before hitting edit song.");
					    	addAlert.setContentText("Please note album and year are optional.");
					    	addAlert.showAndWait();	
					    	
					    	tm.put(e2.eNameArtist,e2); // replace old entry since edit failed
					    	
					    	// sort tree to place old entry back into correct location.
					    	
					    	Set<java.util.Map.Entry<String, Entry>> set = tm.entrySet();
				    		Iterator i = set.iterator();
				    		
				    		while(i.hasNext()) {
				    			 
					  	          Map.Entry me = (Map.Entry)i.next();
					  	          valuesUpdated.add(me.getKey().toString());
					  	    }
					  	    
				    		// update list view and enable/disable correct buttons.
				    		
					    	song_list.setItems(FXCollections.observableList(valuesUpdated));
					    	values = valuesUpdated; //updating the base values to be the updated version
					    	valuesUpdated = new ArrayList<String>();
					    	song_list.refresh();
					    	add.setDisable(true);
					    	delete.setDisable(false);
						    edit.setDisable(false);
						    currItem = e2.eNameArtist;
						    song_list.getSelectionModel().select(e2.eNameArtist);
							artist.setText(tm.get(e2.eNameArtist).eArtist);
						    song.setText(tm.get(e2.eNameArtist).eName);
						    album.setText(tm.get(e2.eNameArtist).eAlbum);
						    year.setText(tm.get(e2.eNameArtist).eYear);
					    	updateList(); 
					    	
					    		
					    } else { // if not present in list view, we do not need to add the old entry back , and simply add the new entry. 
					    	
					    	
			    			tm.put(newSongArtist,newEntry);	// add to tree map
			    			
			    			// shuffle back into list view. 
			    			
			    			Set<java.util.Map.Entry<String, Entry>> set = tm.entrySet();
				    		Iterator i = set.iterator();
				    		
				    		while(i.hasNext()) {
				    			 
					  	          Map.Entry me = (Map.Entry)i.next();
					  	          valuesUpdated.add(me.getKey().toString());
					  	    }
					  	      	
				    		// add to list view and set focus on new edited item.
				    		
					    	song_list.setItems(FXCollections.observableList(valuesUpdated));
					    	values = valuesUpdated;
					    	valuesUpdated = new ArrayList<String>();
					    	song_list.getSelectionModel().select(newSongArtist);
							artist.setText(tm.get(newSongArtist).eArtist);
						    song.setText(tm.get(newSongArtist).eName);
						    album.setText(tm.get(newSongArtist).eAlbum);
						    year.setText(tm.get(newSongArtist).eYear);
						    add.setDisable(true);
						    updateList();
						    delete.setDisable(false);
						    edit.setDisable(false);
						    song_list.refresh();
						    currItem = newSongArtist;
					    }				    		
		    		}  
	    		}
	    	}
	    }
	
	} // END OF EDIT AND ADD HELPER METHOD
	
	public void delete(String songArtist) throws IOException{ // deleting song from list view
		 
		int nextEntry = values.indexOf(songArtist); // get index of entry to be deleted
		
		// remove entry from all data structures
		
		tm.remove(songArtist);
		values.remove(songArtist);
		updateList();
		
		if (values.size()>0 && nextEntry <values.size()){ // if there is a next entry, delete and shift focus to next item
			
			song_list.getSelectionModel().select(nextEntry);
			String nextSong = song_list.getItems().get(nextEntry);
			artist.setText(tm.get(nextSong).eArtist);
		    song.setText(tm.get(nextSong).eName);
		    album.setText(tm.get(nextSong).eAlbum);
		    year.setText(tm.get(nextSong).eYear);
		    currItem = (nextSong);
		    add.setDisable(true);
		    
		} else if (values.size()>0 && nextEntry == values.size()) { // if there is no next entry, shift focus to previous item
			
			song_list.getSelectionModel().select(nextEntry-1);
			String nextSong = song_list.getItems().get(nextEntry-1);
			artist.setText(tm.get(nextSong).eArtist);
		    song.setText(tm.get(nextSong).eName);
		    album.setText(tm.get(nextSong).eAlbum);
		    year.setText(tm.get(nextSong).eYear);
		    currItem = (nextSong);
		    add.setDisable(true);
		    
		} else { // if there is no next or previous item, empty list and set view to empty
			
			artist.setText("");
		    song.setText("");
		    album.setText("");
		    year.setText("");
			song_list.getSelectionModel().clearSelection();
			add.setDisable(false);
		}
	}    	

}
	



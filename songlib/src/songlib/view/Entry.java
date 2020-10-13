// Gabrielle Capulong and Parth Vora - Software Methodology Fall 2020 
// Song Library Project - Assignment 1 Due October 2nd 2020

package songlib.view;

import javafx.application.Application;

//Entry object - holds the following fields which will be used to access and display data for each song in list

public class Entry { 
	String eName;
	String eArtist;
	String eAlbum;
	String eYear;
	String eNameArtist;
	
	//constructor
	
	public Entry(String Name, String Artist, String Album, String Year,String both) {
		eName = Name;
		eArtist = Artist;
		eAlbum = Album;
		eYear = Year;
		eNameArtist = both;
		
	}
}
	
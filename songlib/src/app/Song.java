/*
 * Names: Allen Estrada(are55), Ayush Kadakia(aak227)
 */
package app;

public class Song implements Comparable<Song>{
	
	private String title;
	private String artist;
	private String album;
	private String year;

	public Song(String title, String artist, String album, String year) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public Song(String title, String artist) {
		this.title = title;
		this.artist = artist;
	}

	public String getTitle(){
		return this.title;
	}

	public String getArtist(){
		return this.artist;
	}

	public String getAlbum() {
		return this.album;
	}

	public String getYear() {
		return this.year;
	}
	
	public void setTitle(String title){
		this.title = title;
	}

	public void setArtist(String artist){
		this.artist = artist;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	public String toString() {
		return title + " by " + artist;
	}
	
	public String toCSV(){
		return title + "|" + artist + "|" + album + "|" + year;
	}
	
	public int compareTo(Song other){
		int c = this.getTitle().toLowerCase().compareTo(other.getTitle().toLowerCase());
		if(c == 0) {
			return this.getArtist().toLowerCase().compareTo(other.getArtist().toLowerCase());
		}
		return c;
	}
	
}

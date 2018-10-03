package model;

public class Artist {
    private String id;
    private String artistName;
    private String numberSong;
    private String numberAlbum;
    private int imageArtist;

    public Artist(String id, String artistName, String numberSong, String numberAlbum, int imageArtist) {
        this.id = id;
        this.artistName = artistName;
        this.numberSong = numberSong;
        this.numberAlbum = numberAlbum;
        this.imageArtist = imageArtist;
    }

    public Artist(String id, String artistName, String numberSong, String numberAlbum) {
        this.id = id;
        this.artistName = artistName;
        this.numberSong = numberSong;
        this.numberAlbum = numberAlbum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getNumberSong() {
        return numberSong;
    }

    public void setNumberSong(String numberSong) {
        this.numberSong = numberSong;
    }

    public String getNumberAlbum() {
        return numberAlbum;
    }

    public void setNumberAlbum(String numberAlbum) {
        this.numberAlbum = numberAlbum;
    }

    public int getImageArtist() {
        return imageArtist;
    }

    public void setImageArtist(int imageArtist) {
        this.imageArtist = imageArtist;
    }
}

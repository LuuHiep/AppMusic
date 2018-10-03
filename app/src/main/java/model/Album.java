package model;

public class Album {
    private String id;
    private String albumName;
    private String albumArtistName;
    private String albumImage;

    public Album(String id, String albumName, String albumArtistName, String albumImage) {
        this.id = id;
        this.albumName = albumName;
        this.albumArtistName = albumArtistName;
        this.albumImage = albumImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArtistName() {
        return albumArtistName;
    }

    public void setAlbumArtistName(String albumArtistName) {
        this.albumArtistName = albumArtistName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }
}

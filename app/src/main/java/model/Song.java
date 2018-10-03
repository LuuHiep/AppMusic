package model;

public class Song {
    private String id;
    private String songName;
    private String songArtistName;
    private int imageSong;

    public Song(String id, String songName, String songArtistName) {
        this.id = id;
        this.songName = songName;
        this.songArtistName = songArtistName;
    }

    public Song(String id, String songName, String songArtistName, int imageSong) {
        this.id = id;
        this.songName = songName;
        this.songArtistName = songArtistName;
        this.imageSong = imageSong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtistName() {
        return songArtistName;
    }

    public void setSongArtistName(String songArtistName) {
        this.songArtistName = songArtistName;
    }

    public int getImageSong() {
        return imageSong;
    }

    public void setImageSong(int imageSong) {
        this.imageSong = imageSong;
    }
}

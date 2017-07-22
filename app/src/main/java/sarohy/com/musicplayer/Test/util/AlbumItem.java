package sarohy.com.musicplayer.Test.util;

/**
 * Created by Sarohy on 7/22/2017.
 */

public class AlbumItem {
    int albumID,albumTotalSongs=0;
    String albumSingerName,albumTitle,bitmapPath;

    public int getAlbumID() {
        return albumID;
    }

    public int getAlbumTotalSongs() {
        return albumTotalSongs;
    }

    public String getAlbumSingerName() {
        return albumSingerName;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public String getBitmapPath() {
        return bitmapPath;
    }


    public void setAlbumID(int albumID) {
        this.albumID =  albumID;
    }

    public void setAlbumSingerName(String albumSingerName) {
        this.albumSingerName = albumSingerName;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public void setAlbumTotalSongs(int albumTotalSongs) {
        this.albumTotalSongs = albumTotalSongs;
    }

    public void setBitmapPath(String bitmapPath) {
        this.bitmapPath = bitmapPath;
    }
}

package com.io.tatsuki.toney.Repositories;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.io.tatsuki.toney.Models.Album;
import com.io.tatsuki.toney.Models.Artist;
import com.io.tatsuki.toney.Models.Song;

import java.util.ArrayList;

/**
 * ローカル情報アクセスクラス
 */

public class LocalAccess {

    private static final String TAG = LocalAccess.class.getSimpleName();
    private Context context;
    private ContentResolver contentResolver;

    /**
     * コンストラクタ
     * @param context
     */
    public LocalAccess(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    /**
     * 曲リストを取得（IDにより取得する曲リストを切り替え）
     * @param   albumId     アルバムID
     * @return  songs       曲リスト
     */
    public ArrayList<Song> getSongs(String albumId) {
        ArrayList<Song> songs = new ArrayList<>();

        // 全ての曲を取得するCursor
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                              null,
                                              null,
                                              null,
                                              "ARTIST ASC");
        // アルバム内の曲を取得するCursor
        if (albumId != null) {
            cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                           null,
                                           MediaStore.Audio.Albums._ID + "=?",
                                           new String[] {albumId},
                                           null);
        }

        while (cursor.moveToFirst()) {
            if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) > 5000) {
                Song song = new Song();
                song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                song.setSongName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                song.setSongUri(Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getSongId()));
                song.setSongArtPath(getArtPath(MediaStore.Audio.Media.ALBUM_ID));
                songs.add(song);
                Log.d(TAG, song.getSongName());
            }
        }
        cursor.close();
        return songs;
    }

    /**
     * アルバムリストを取得
     * @param   artistName  アーティスト名
     * @return  albums      アルバムリスト
     */
    public ArrayList<Album> getAlbums(String artistName) {
        ArrayList<Album> albums = new ArrayList<>();
        // 全てのアルバムを取得するCursor
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                              null,
                                              null,
                                              null,
                                              "ARTIST ASC");

        // 同一アーティストのアルバムを取得するCursor
        if (artistName != null) {
            cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                           null,
                                           MediaStore.Audio.Albums.ARTIST + "=?",
                                           new String[] {artistName},
                                           null);
        }

        while (cursor.moveToFirst()) {
            Album album = new Album();
            album.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)));
            album.setAlbumKey(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY)));
            album.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
            album.setAlbumArtPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
            album.setSongs(getSongs(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID))));
            albums.add(album);
            Log.d(TAG, album.getAlbumName());
        }
        cursor.close();
        return albums;
    }

    /**
     * アーティストリストを取得
     * @return  artists     アーティストリスト
     */
    public ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<>();
        // 全てのアーティストを取得するCursor
        Cursor cursor = contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                                              null,
                                              null,
                                              null,
                                              "ARTIST ASC");
        while (cursor.moveToFirst()) {
            Artist artist = new Artist();
            artist.setArtistId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
            artist.setArtistKey(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY)));
            artist.setArtistName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
            artist.setAlbums(getAlbums(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))));
            artists.add(artist);
            Log.d(TAG, artist.getArtistName());
        }
        cursor.close();
        return artists;
    }

    /**
     * アルバムアートワーク取得
     * @param   albumId         アルバムID
     * @return  albumArtPath    アルバムアートワークパス
     */
    public String getArtPath(String albumId) {
        String albumArtPath = null;
        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                              null,
                                              MediaStore.Audio.Albums._ID + "=?",
                                              new String[] {albumId},
                                              null);

        if (cursor.moveToFirst()) {
            int albumArtId = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            albumArtPath = cursor.getString(albumArtId);
        }
        cursor.close();
        return albumArtPath;
    }
}

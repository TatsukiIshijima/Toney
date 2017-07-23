package com.io.tatsuki.toney.Repositories;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
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
    private static final Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    private static final Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
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
     * @param   albumId     アルバムID(Albums._ID)
     * @return  songs       曲リスト
     */
    public ArrayList<Song> getSongs(String albumId, String artistId) {
        ArrayList<Song> songs = new ArrayList<>();

        ContentProviderClient contentProviderClient = contentResolver.acquireContentProviderClient(mediaUri);

        Cursor cursor = null;

        try {
            // 全ての曲を取得するCursor
            cursor = contentProviderClient.query(mediaUri, null, null, null, "ARTIST ASC");

            if (albumId != null && artistId == null) {
                // アルバム内の曲を取得するCursor
                cursor = contentProviderClient.query(mediaUri,
                                                     null,
                                                     MediaStore.Audio.Media.ALBUM_ID + "=?",
                                                     new String[] {albumId},
                                                     null);

            } else if (artistId != null && albumId == null) {
                // 同一アーティストの曲を取得するCursor
                cursor = contentProviderClient.query(mediaUri,
                                                     null,
                                                     MediaStore.Audio.Media.ARTIST_ID + "=?",
                                                     new String[] {artistId},
                                                     null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) > 5000) {
                    Song song = new Song();
                    song.setSongId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    song.setSongName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    // SerializeなのでStringでセット 再生するときにUriに戻すこと
                    song.setSongPath(Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getSongId()).toString());
                    song.setSongArtPath(getArtPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))));
                    song.setSongArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                    song.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    songs.add(song);
                }
            }
            cursor.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentProviderClient.close();
            } else {
                contentProviderClient.release();
            }
        }
        return songs;
    }

    /**
     * アルバムリストを取得
     * @param   artistName  アーティスト名
     * @return  albums      アルバムリスト
     */
    public ArrayList<Album> getAlbums(String artistName) {
        ArrayList<Album> albums = new ArrayList<>();

        ContentProviderClient contentProviderClient = contentResolver.acquireContentProviderClient(albumUri);

        Cursor cursor = null;
        try {
            // 全てのアルバムを取得するCursor
            cursor = contentProviderClient.query(albumUri, null, null, null, "ARTIST ASC");

            // 同一アーティストのアルバムを取得するCursor
            if (artistName != null) {
                cursor = contentProviderClient.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                                     null,
                                                     MediaStore.Audio.Albums.ARTIST + "=?",
                                                     new String[] {artistName},
                                                     null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Album album = new Album();
                album.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
                album.setAlbumKey(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY)));
                album.setAlbumName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
                album.setAlbumArtPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                album.setAlbumArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
                album.setSongs(getSongs(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)), null));
                albums.add(album);
                Log.d(TAG, "Album:" + album.getAlbumName());
            }
            cursor.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentProviderClient.close();
            } else {
                contentProviderClient.release();
            }
        }
        return albums;
    }

    /**
     * アーティストリストを取得
     * @return  artists     アーティストリスト
     */
    public ArrayList<Artist> getArtists() {
        ArrayList<Artist> artists = new ArrayList<>();

        ContentProviderClient contentProviderClient = contentResolver.acquireContentProviderClient(artistUri);

        Cursor cursor = null;
        try {
            // 全てのアーティストを取得するCursor
            cursor = contentProviderClient.query(artistUri, null, null, null, "ARTIST ASC");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Artist artist = new Artist();
                artist.setArtistId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
                artist.setArtistKey(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY)));
                artist.setArtistName(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
                artist.setAlbumNum(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
                artist.setAlbums(getAlbums(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))));
                artist.setSongs(getSongs(null, cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID))));
                artists.add(artist);
                Log.d(TAG, "Artist:" + artist.getArtistName());
            }
            cursor.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentProviderClient.close();
            } else {
                contentProviderClient.release();
            }
        }
        return artists;
    }

    /**
     * アルバムアートワーク取得
     * @param   albumId         アルバムID
     * @return  albumArtPath    アルバムアートワークパス
     */
    private String getArtPath(String albumId) {
        String albumArtPath = null;
        ContentProviderClient contentProviderClient = contentResolver.acquireContentProviderClient(albumUri);
        Cursor cursor = null;
        try {
            cursor = contentProviderClient.query(albumUri,
                                                  null,
                                                  MediaStore.Audio.Albums._ID + "=?",
                                                  new String[] {albumId},
                                                  null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (cursor != null && cursor.moveToFirst()) {
            int albumArtId = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            albumArtPath = cursor.getString(albumArtId);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentProviderClient.close();
            } else {
                contentProviderClient.release();
            }
            cursor.close();
        }
        return albumArtPath;
    }
}

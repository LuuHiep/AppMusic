package fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lau.appmusic.R;

import java.util.ArrayList;

import adapter.SongAdapter;
import model.Song;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;

public class FragmentSong extends Fragment {

    private View view;
    private ArrayList<Song> arrayListSong;
    private RecyclerView rvSong;
    private SongAdapter songAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        initViewSong();
        return view;
    }

    public void initViewSong() {
        RecyclerView recyclerViewSong = view.findViewById(R.id.rvSong);
        recyclerViewSong.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplication(), VERTICAL, false);
        recyclerViewSong.setLayoutManager(layoutManager);
        arrayListSong = new ArrayList<>();
        songAdapter = new SongAdapter(arrayListSong, getContext());
        // get data listSong
        getDataListSong();
        songAdapter.notifyDataSetChanged();
        recyclerViewSong.setAdapter(songAdapter);
    }

    public void getDataListSong(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
        {
            if (songCursor != null && songCursor.moveToFirst()) {
                int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                do {
                    int currenId = songCursor.getInt(songId);
                    String currentTitle = songCursor.getString(songTitle);
                    String currentArtist = songCursor.getString(songArtist);
                    String data = songCursor.getString(songData);
                    arrayListSong.add(new Song(currenId,currentTitle, currentArtist,R.drawable.icon_beats));
                    Log.d("data", data);
                } while (songCursor.moveToNext());
            }
        }
    }
}

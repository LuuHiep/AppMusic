package activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toolbar;

import com.example.lau.appmusic.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Song;
import service.MusicController;
import service.MusicService;

public class PlayMusicActivity extends AppCompatActivity {

    private Toolbar tbSongName;
    private CircleImageView civCDPlay;
    private SeekBar seekBar;
    private ImageView ivPrevious, ivRepeat, ivPlayPause, ivShuffle, ivNext;
    private RecyclerView rvSongName;

    private ObjectAnimator animator;
    private int position = 0;
    private ArrayList<Song> songList;
    //service
    public MusicService musicSrv;
    private Intent playIntent;
    int currentPos;
    //binding
    private boolean musicBound = false;
    //controller
    private MusicController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        initView();
        customIconCDPlay();
        getListSong();
        getPositionSong();
        setNameSongPlay();
        initClick();
    }

    private void initView(){

        tbSongName = (Toolbar) findViewById(R.id.tb_song_name);
        civCDPlay = (CircleImageView) findViewById(R.id.civ_cd_play);
        seekBar = (SeekBar) findViewById(R.id.seebar);
        ivPrevious = (ImageView) findViewById(R.id.iv_previous);
        ivRepeat = (ImageView) findViewById(R.id.iv_repeat);
        ivPlayPause = (ImageView) findViewById(R.id.iv_play_pause);
        ivShuffle = (ImageView) findViewById(R.id.iv_shuffle);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        rvSongName = (RecyclerView) findViewById(R.id.rv_play_song);

        songList = new ArrayList<>();
    }

    // nhận position
    private void getPositionSong() {
        position = getIntent().getIntExtra("position", 1);
        Log.d("position_1", " " + position);
    }

    private void customIconCDPlay(){
        animator = ObjectAnimator.ofFloat(civCDPlay, "rotation", 0f, 360f);
        animator.setDuration(10000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        // animator.start();
    }

    private void getListSong(){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
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
                    songList.add(new Song(currenId,currentTitle, currentArtist,R.drawable.icon_beats));
                    Log.d("data", data);
                } while (songCursor.moveToNext());
            }
        }
    }

    private void setNameSongPlay() {
        tbSongName.setTitle(songList.get(position).getSongName());
    }

    // Connect to the Service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            songPicked();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //start and bind the service when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void songPicked() {
        musicSrv.setSong(position);
        musicSrv.playSong();
    }

    private void initClick(){

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPrev();
            }
        });

        ivRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playRepeat();
                if (musicSrv != null){
                    if (musicSrv.isPlaying()){
                        ivRepeat.setImageResource(R.drawable.repeat_one);
                    } else {
                        ivRepeat.setImageResource(R.drawable.repeat);
                    }
                }
            }
        });

        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playPauseMusic();
                if (musicSrv != null) {
                    if (musicSrv.isPlaying()) {
                        ivPlayPause.setImageResource(R.drawable.pause);
                    } else {
                        ivPlayPause.setImageResource(R.drawable.play);
                    }
                }
            }
        });

        ivShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playShufle();
                if (musicSrv != null){
                    if (musicSrv.isPlaying()){
                        ivShuffle.setImageResource(R.drawable.shullf_one);
                    } else {
                        ivShuffle.setImageResource(R.drawable.shuffle);
                    }
                }
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.playNext();
            }
        });

    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        super.onDestroy();
        unbindService(musicConnection);
    }

}

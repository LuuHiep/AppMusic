package activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.lau.appmusic.R;

import java.util.ArrayList;

import model.Song;
import service.MusicService;

public class MainActivity extends AppCompatActivity {


    //service
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private ArrayList<Song> songList;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView ivIconAlbumMain, ivPlayPauseMain, ivNextMain;
    TextView tvSongNameMain, tvArtistNameMain;
    Toolbar tbMain;
  //  ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkPermissions()) {
            return;
        }

        initView();
    }

    // check permission
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String check : permissions) {
                int status = checkSelfPermission(check);
                if (status == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(permissions, 0);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions()) {
            //initViews();
        } else {
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ivIconAlbumMain = (ImageView) findViewById(R.id.iv_album_main);
        ivPlayPauseMain = (ImageView) findViewById(R.id.iv_play_pause_main);
        ivNextMain = (ImageView) findViewById(R.id.iv_next_main);
        tvSongNameMain = (TextView) findViewById(R.id.tv_song_name_main);
        tvArtistNameMain = (TextView) findViewById(R.id.tv_artist_main);
        tbMain = (Toolbar) findViewById(R.id.tb_main);

        setSupportActionBar(tbMain);

        showTab();
    }

    private void showTab(){
        PagerAdapter adapter = new adapter.PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
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
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playIntent);
        unbindService(musicConnection);
    }

}

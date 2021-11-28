package com.example.pockergame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView imgleft1, imgcenter1, imgright1, imgleft2, imgcenter2, imgright2;
    private String[] cardType = {"c", "s", "d", "h"};
    private int[] cardNumber = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
  //  private int[] cardNumber = { 9, 10, 11, 12, 13};
    private ArrayList<String> arraysCardOpen;
    private EditText name1,name2;
    private TextView score_player1, score_player2;
    private Integer scoreplayer1,scoreplayer2;
    private ArrayList<Integer> arraysCardOpenPlayer1, arraysCardOpenPlayer2;
    //for play sound
    private MediaPlayer mediaPlayer2;
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 5;
    private SoundPool soundPool;
    private AudioManager audioManager;
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private boolean loaded;
    private int soundIdBackground;
    private int soundIdGun;
    private float volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // MediaPlayer mediaPlayer2 =MediaPlayer.create(this,R.raw.jungle);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_newgame:
                playAgain();
                break;
            case R.id.menu_resetscore:
                resetscore();
                break;
            case R.id.menu_quit:
                finish();
                return  true;
                
            case R.id.menu_about:
                   showToast("Written by Phan Nhat Thanh");
                    break;
        }
        return super.onOptionsItemSelected(item);
    }





    public void init() {
        imgleft1 = (ImageView) findViewById(R.id.ImgLeft1);
        imgcenter1 = (ImageView) findViewById(R.id.ImgCenter1);
        imgright1 = (ImageView) findViewById(R.id.ImgRight1);
        imgleft2 = (ImageView) findViewById(R.id.ImgLeft2);
        imgcenter2 = (ImageView) findViewById(R.id.ImgCenter2);
        imgright2 = (ImageView) findViewById(R.id.ImgRight2);
        score_player1 = (TextView) findViewById(R.id.ScoreName1);
        score_player2 = (TextView) findViewById(R.id.ScoreName2);
        name1= (EditText ) findViewById(R.id.EditName1);
        name2= (EditText ) findViewById(R.id.EditName2);
        arraysCardOpen = new ArrayList<>();
        arraysCardOpenPlayer1 = new ArrayList<>();
        arraysCardOpenPlayer2 = new ArrayList<>();
        scoreplayer1=0; scoreplayer2=0;
        //  final MediaPlayer mediaPlayer =MediaPlayer.create(this,R.raw.sound_water);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.jungle);

        mediaPlayer2.start();
        mediaPlayer2.setLooping(true);


        //sound
        // AudioManager audio settings for adjusting the volume
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        this.volume = currentVolumeIndex / maxVolumeIndex;

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttrib;
            audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });

        // Load sound file () into SoundPool.
        this.soundIdBackground = this.soundPool.load(this, R.raw.jungle, 1);

        // Load sound file () into SoundPool.
        this.soundIdGun = this.soundPool.load(this, R.raw.sound_water, 1);


        // playSoundBackGround(); playSoundGun();
        View.OnClickListener imgclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;
                playSoundClick();
                name1.clearFocus();
                name2.clearFocus();

                int randCardType = createRandomNumber(0, 3);
                int randCardNumber = createRandomNumber(0, 12);
                String cardValue = (String) cardType[randCardType] + cardNumber[randCardNumber];
                //Check and add new Card to Array list
                while (arraysCardOpen.contains(cardValue)) {
                    randCardType = createRandomNumber(0, 3);
                    randCardNumber = createRandomNumber(0, 12);
                    cardValue = (String) cardType[randCardType] + cardNumber[randCardNumber];

                }
                arraysCardOpen.add(cardValue);
                switch (imageView.getId()) {
                    case R.id.ImgLeft1:
                    case R.id.ImgCenter1:
                    case R.id.ImgRight1:
                        //add card to  List of Player 1
                        arraysCardOpenPlayer1.add(randCardNumber + 1);
                        break;
                    case R.id.ImgLeft2:
                    case R.id.ImgCenter2:
                    case R.id.ImgRight2:
                        //add card to  List of Player 2
                        arraysCardOpenPlayer2.add(randCardNumber + 1);
                        break;

                }
                show_Score();
                   //get id Image from resource drawable
                int idimg = getResources().getIdentifier(cardValue, "drawable", getPackageName());
                imageView.setClickable(false);
                imageView.setImageResource(idimg);
                //imageView.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),idimg));

            }
        };
        imgleft1.setOnClickListener(imgclick);
        imgcenter1.setOnClickListener(imgclick);
        imgright1.setOnClickListener(imgclick);
        imgleft2.setOnClickListener(imgclick);
        imgcenter2.setOnClickListener(imgclick);
        imgright2.setOnClickListener(imgclick);


    }

    ;

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    ;

    public int createRandomNumber(int from, int to) {
        Random rndGenerator = new Random();
        //return from + rndGenerator.nextInt(to-from+1);
        return rndGenerator.nextInt(to);
    }

    ;

    public void playAgain() {
        arraysCardOpen.clear();
        arraysCardOpenPlayer1.clear();
        arraysCardOpenPlayer2.clear();

        imgleft1.setImageResource(R.drawable.blue_back);
        imgcenter1.setImageResource(R.drawable.blue_back);
        imgright1.setImageResource(R.drawable.blue_back);
        imgleft2.setImageResource(R.drawable.blue_back);
        imgcenter2.setImageResource(R.drawable.blue_back);
        imgright2.setImageResource(R.drawable.blue_back);
        imgleft1.setClickable(true);
        imgcenter1.setClickable(true);
        imgright1.setClickable(true);
        imgleft2.setClickable(true);
        imgcenter2.setClickable(true);
        imgright2.setClickable(true);

    }

    ;
public void resetscore (){
    score_player1.setText("Score: 0");
    score_player2.setText("Score: 0");
};

    // When users click on the ImangeView
    public void playSoundClick() {
        if (loaded) {
            float leftVolumn = volume;
            float rightVolumn = volume;
            // Play sound of gunfire. Returns the ID of the new stream.
            int streamId = this.soundPool.play(this.soundIdGun, leftVolumn, rightVolumn, 2, 0, 1f);
        }
    }

    ;

    // When users click on the button "Destroy"
    public void playSoundBackGround() {
        if (loaded) {
            float leftVolumn = volume;
            float rightVolumn = volume;

            // Play sound objects . Returns the ID of the new stream.
            int streamId2 = this.soundPool.play(this.soundIdBackground, leftVolumn, rightVolumn, 1, 10, 1f);


        }
    }

    ;

    private void show_Score() {
        if (arraysCardOpen.size()==6) {
            int score1 = 0;
            int score2 = 0;

        if (arraysCardOpenPlayer1.size() == 3)  {

            for (int i = 0; i < arraysCardOpenPlayer2.size(); i++) {
                if (arraysCardOpenPlayer2.get(i) > 10) {
                    score1 += 50;
                }
                }
            if (score1 != 150) {
                score1 = 0;
                for (int y = 0; y < arraysCardOpenPlayer1.size(); y++) {
                    if (arraysCardOpenPlayer1.get(y) >= 10) {
                        score1 += 0;
                    } else {
                        score1 += arraysCardOpenPlayer1.get(y);
                    }
                    // showToast(arraysCardOpenPlayer2.get(i)+"");
                }
                ;
                score1 = score1 % 10;
            }

          //  score_player1.setText("Score: " + score1);
        }
        if (arraysCardOpenPlayer2.size() == 3) {

            for (int i = 0; i < arraysCardOpenPlayer2.size(); i++) {
                if (arraysCardOpenPlayer2.get(i) > 10) {
                    score2 += 50;
                }
                // showToast(arraysCardOpenPlayer2.get(i)+"");
            }
            if (score2 != 150) {
                score2 = 0;
                for (int y = 0; y < arraysCardOpenPlayer2.size(); y++) {
                    if (arraysCardOpenPlayer2.get(y) >= 10) {
                        score2 += 0;
                    } else {
                        score2 += arraysCardOpenPlayer2.get(y);
                    }
                    // showToast(arraysCardOpenPlayer2.get(i)+"");
                }
                ;
                score2 = score2 % 10;
            }

           // score_player2.setText("Score: " + score2);
        }
        if (score1>score2){scoreplayer1+=1;};
        if (score2>score1){scoreplayer2+=1;};
            score_player1.setText("Score: " + scoreplayer1);
            score_player2.setText("Score: " + scoreplayer2);
         //   showToast("Name1 :"+ score1+"-"+"Name2 :"+ score2+"");
        //score2=score2 % 10;

        // showToast("Name2 :"+ score2+"");
    }
    }

public int getScoreinCard(Integer cardValue) {
    Integer value =cardValue;
    if (cardValue>=10) value=0;
    return  value;

};

    @Override
    protected void onResume() {
        mediaPlayer2 =MediaPlayer.create(this,R.raw.jungle);

        mediaPlayer2.start();
        mediaPlayer2.setLooping(true);
        super.onResume();


    }

    @Override
    protected void onStop() {
        if(mediaPlayer2 != null && mediaPlayer2.isPlaying())
        {
            mediaPlayer2.stop();

        }
        super.onStop();
    //    mediaPlayer2.stop();

    }
}
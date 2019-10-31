package com.example.cpg;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cpg.helpers.MidiWrapper;
import com.example.cpg.sql.DatabaseHelper;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;

import com.example.cpg.model.User;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final AppCompatActivity activity = MainActivity.this;

    private static final String CLIENT_ID = "4d7413a14a664db8a826bb9735292790";
    private static final String REDIRECT_URI = "com.example.cpg://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private Button mPlayButton;
    private Button mPauseButton;
    private Button mUpdateAccountButton;
    private Button mDeleteAccountButton;
    private Button mUserListButton;

    private MidiWrapper midiWrapper;
    private MediaPlayer player;

    private DatabaseHelper userInfo;
    private User user;

    private AppCompatTextView textViewName;
    private TextInputEditText textInputEditTextEmail;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","ONCREATE TRIGGERED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab the logged in user's email
        String emailFromIntent = getIntent().getStringExtra("EMAIL");

        userInfo = new DatabaseHelper(activity);
        user = new User();

        mPlayButton = (Button) findViewById(R.id.play_button);
        mPauseButton = (Button) findViewById(R.id.pause_button);
        mUpdateAccountButton = (Button) findViewById(R.id.update_account_button);
        mDeleteAccountButton = (Button) findViewById(R.id.delete_account_button);
        mUserListButton = (Button) findViewById(R.id.user_list_button);

        //Right now just creates and plays a midi file with progression F Fm C C
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play a playlist
                //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");


                //Create and play midi file
                midiWrapper = new MidiWrapper();
                midiWrapper.writeProg(getApplicationContext());
                File cacheDir = getCacheDir();
                File midout = new File(cacheDir + "/midout.mid");

                midout.setReadable(true, false);
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(midout);
                    if(fileInputStream.getFD().valid()){
                        player = new MediaPlayer();
                    }
                    //player = MediaPlayer.create(getApplicationContext(), uriFromParse);
                    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    player.setDataSource(fileInputStream.getFD());
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    player.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                player.start();

            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpotifyAppRemote.getPlayerApi().pause();
            }
        });

        mUpdateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setEmail(emailFromIntent);
                user = userInfo.getUserByEmail(emailFromIntent);

                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(context);
                inputAlert.setTitle("Update your account information");
                inputAlert.setMessage("Please enter a new email address");
                final EditText userInput = new EditText(context);
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        user.setEmail(userInputValue);
                        userInfo.updateUser(user);
                        finish();
                    }
                });
                inputAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = inputAlert.create();
                alertDialog.show();
            }
        });

        mDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setEmail(emailFromIntent);
                user = userInfo.getUserByEmail(emailFromIntent);
                userInfo.deleteUser(user);
                finish();
            }
        });

        mUserListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accountsIntent = new Intent(activity, UsersListActivity.class);
                accountsIntent.putExtra("EMAIL", emailFromIntent);
                startActivity(accountsIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        Log.d("MainActivity","ONSTART TRIGGERED");
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        Log.d("MainActivity", "FAILED");
                    }
                });
    }

    private void connected() {
        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }

    @Override
    protected void onStop() {
        Log.d("MainActivity", "ONSTOP TRIGGERED");
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
    }
}

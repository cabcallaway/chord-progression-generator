package com.example.cpg;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cpg.dao.ProgressionDao;
import com.example.cpg.dao.UserDao;
import com.example.cpg.helpers.MIDI.MidiGenerator;
import com.example.cpg.model.Progression;
import com.example.cpg.viewModels.ProgressionViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.types.Track;

import com.example.cpg.model.User;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final AppCompatActivity activity = MainActivity.this;

    private static final String CLIENT_ID = "4d7413a14a664db8a826bb9735292790";
    private static final String REDIRECT_URI = "com.example.cpg://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private Button mPlayButton;
    private Button mPauseButton;
    private Button mSaveButton;
    private Button mLoadButton;
    private Button mUpdateAccountButton;
    private Button mDeleteAccountButton;
    private Button mUserListButton;

    private MidiGenerator midiGenerator;
    private MediaPlayer player;

    private ProgressionDao progressionDao;
    private UserDao userDao;
    private AppDatabase database;
    private User user;

    private AppCompatTextView textViewName;
    private TextInputEditText textInputEditTextEmail;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load The progression view model to gain access to liveData
        ProgressionViewModel pViewModel = ViewModelProviders.of(this).get(ProgressionViewModel.class);
        //Gets the initially loaded progression to check against later
        Progression oldProgression = pViewModel.getProgression().getValue();

        //Load the progression fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ProgressionFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        //Init the database
        database = AppDatabase.getInstance(getApplicationContext());

        //Get currently logged in user
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        //userInfo = new DatabaseHelper(activity);
        userDao = database.getUserDao();
        user = new User();

        progressionDao = database.getProgressionDao();


        //Initialize all buttons
        mPlayButton = findViewById(R.id.play_button);
        mPauseButton = findViewById(R.id.pause_button);
        mSaveButton = findViewById(R.id.save_button);
        mLoadButton = findViewById(R.id.load_button);
        mUpdateAccountButton = findViewById(R.id.update_account_button);
        mDeleteAccountButton = findViewById(R.id.delete_account_button);
        mUserListButton = findViewById(R.id.user_list_button);

        //TODO: Make the chords be loaded to/from view
        //Initial chords loaded into the progression.
        //Will be changed by clickListeners on each bar in the progression

        //Generate midi file and play it given the chords in the progression
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the currently active progression
                Progression progression = pViewModel.getProgression().getValue();
                //If user hits play while already playing an progression, restart if same progression, or rebuild midi if different
                if(player != null && player.isPlaying()){
                        player.stop();
                    }
                    //if(oldProgression != null && progression.sameProg(oldProgression)){
                    //    player.start();
                    //} else {
                        //Write the midi file

                    //Write the midi file
                    File midout = new File(getCacheDir() + "/midout.mid");
                    midiGenerator = new MidiGenerator();
                    midiGenerator.writeProgression(MainActivity.this, progression);
                    //Create the media player
                    player = MediaPlayer.create(getApplicationContext(), Uri.fromFile(midout));
                    //TODO: make loop fluid (look at tempo and adjust or add silent note? - investigate midi file in ableton)
                    player.setLooping(true);
                    player.start();
                    //}
            }
        });

        mPauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                player.pause();

            }

        });

        mSaveButton.setOnClickListener((new View.OnClickListener() {

            //TODO: Get the progression (from Intent?), update database
            @Override
            public void onClick(View v) {}

        }));

        mLoadButton.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Find the User
                user = userDao.getUserByEmail(emailFromIntent);

                //Get all of their Progressions the user has given their ID
                List<String> progressionNames = progressionDao.getAllProgNames(user.getId());

                //List of progressions to display to user
                String[] items = new String[progressionNames.size()];

                for (int i = 0; i < progressionNames.size(); i++) {

                    // Assign each value to String array
                    items[i] = progressionNames.get(i);

                }

                //new prompt
                AlertDialog.Builder loadPrompt = new AlertDialog.Builder(context);

                loadPrompt.setTitle("Please select a Progression: ");

                loadPrompt.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String progName = items[which];

                        //TODO: Set progression to loaded Progression
                        //Progression progression = progressionDao.getProgressionByName(progName);

                        Snackbar.make(findViewById(android.R.id.content), progName, Snackbar.LENGTH_LONG).show();
                    }
                });

                loadPrompt.setCancelable(false).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                    @Override  public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                loadPrompt.create().show();

            }
        }));


        //Update Account Details listeners
        mUpdateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setEmail(emailFromIntent);
                user = userDao.getUserByEmail(emailFromIntent);

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
                        userDao.update(user);
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
                user = userDao.getUserByEmail(emailFromIntent);
                userDao.delete(user);
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

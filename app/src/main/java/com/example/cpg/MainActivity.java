package com.example.cpg;

import android.content.Context;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cpg.dao.ProgressionDao;
import com.example.cpg.dao.UserDao;
import com.example.cpg.helpers.MIDI.MidiGenerator;
import com.example.cpg.model.Chord;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import safety.com.br.android_shake_detector.core.ShakeCallback;
import safety.com.br.android_shake_detector.core.ShakeDetector;
import safety.com.br.android_shake_detector.core.ShakeOptions;

public class MainActivity extends AppCompatActivity {

    private final AppCompatActivity activity = MainActivity.this;

    private static final String CLIENT_ID = "4d7413a14a664db8a826bb9735292790";
    private static final String REDIRECT_URI = "com.example.cpg://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    private Button mPlayButton;
    private Button mPauseButton;
    private Button mSaveButton;
    private Button mLoadButton;

    //ShakeDetector library used to listen for accelerometer changes (Shake to Play!)
    //https://github.com/tbouron/ShakeDetector
    private ShakeDetector shakeDetector;

    private Button mUpdateAccountButton;
    private Button mDeleteAccountButton;
    private Button mGenerateButton;
    private Button mLogoutButton;

    private MidiGenerator midiGenerator;
    public MediaPlayer player;

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
        mGenerateButton = findViewById(R.id.generate_button);
        mUpdateAccountButton = findViewById(R.id.update_account_button);
        mDeleteAccountButton = findViewById(R.id.delete_account_button);
        mLogoutButton = findViewById(R.id.logout_button);


        //Setup Options for Shake Detector (external library)
        ShakeOptions options = new ShakeOptions()
                .background(false)
                .interval(1000)
                .shakeCount(2)
                .sensibility(2.0f);

        this.shakeDetector = new ShakeDetector(options).start(this, new ShakeCallback() {

            //Same Logic as Play Button Listener
            //TODO: Clean up, maybe move into separate function
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onShake() {

                Progression progression = pViewModel.getProgression().getValue();

                //If user hits play while already playing an progression, restart if same progression, or rebuild midi if different
                if(player != null && player.isPlaying()){
                    player.stop();
                }

                Progression randomProgression = new Progression();

                List<String> progressionNames = Arrays.asList("A", "Am", "A7", "Am7", "B", "Bm", "B7", "Bm7", "C", "Cm", "C7", "Cm7", "D", "Dm", "D7", "Dm7", "E", "Em", "E7", "Em7", "F", "Fm", "F7", "Fm7", "G", "Gm", "G7", "Gm7");
                Collections.shuffle(progressionNames);

                String progRandomName = "";
                progRandomName = String.join("", progressionNames.get(0), progressionNames.get(1), progressionNames.get(2), progressionNames.get(3));

                randomProgression.setName(progRandomName);
                randomProgression.addChord(new Chord(progressionNames.get(0), 1));
                randomProgression.addChord(new Chord(progressionNames.get(1), 1));
                randomProgression.addChord(new Chord(progressionNames.get(2), 1));
                randomProgression.addChord(new Chord(progressionNames.get(3), 1));

                pViewModel.setProgression(randomProgression);

                // Pass the chord names to ProgressionFragment so the UI will update
                FragmentManager fm = getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putString("chord1Text", pViewModel.getCurchord(0).getValue());
                arguments.putString("chord2Text", pViewModel.getCurchord(1).getValue());
                arguments.putString("chord3Text", pViewModel.getCurchord(2).getValue());
                arguments.putString("chord4Text", pViewModel.getCurchord(3).getValue());

                ProgressionFragment fragment = new ProgressionFragment();
                fragment.setArguments(arguments);
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                // Make the Generate button shake
                mGenerateButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake));

                //Write the midi file
                File midout = new File(getCacheDir() + "/midout.mid");
                midiGenerator = new MidiGenerator();
                midiGenerator.writeProgression(MainActivity.this, randomProgression);
                //Create the media player
                player = MediaPlayer.create(getApplicationContext(), Uri.fromFile(midout));
                player.start();

                Snackbar.make(findViewById(android.R.id.content), "Progression " + randomProgression.getName() + " Generated!", Snackbar.LENGTH_LONG).show();
            }
        });

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
                if (player != null && player.isPlaying()) {
                    player.pause();
                }
            }

        });

        mSaveButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (player != null && player.isPlaying()) {

                    player.pause();

                }

                Progression progression = pViewModel.getProgression().getValue();

                progression.setUserId(userDao.getUserByEmail(emailFromIntent).getId());

                //When swapping chords, Progression name is not accurate
                List<Chord> chords = progression.getChords();

                StringBuilder sb = new StringBuilder();

                for (Chord chord: chords) {

                    sb.append(chord.getChordName());

                }

                progression.setName(sb.toString());

                if (progressionDao.checkProgression(progression.getName(), progression.getUserId()) == 0) {

                    progressionDao.insert(progression);
                    Snackbar.make(findViewById(android.R.id.content), "Progression " + progression.getName() + " Saved", Snackbar.LENGTH_LONG).show();

                } else {
                    // Snack Bar to show error message that record already exists
                    Snackbar.make(findViewById(android.R.id.content), "Progression " + progression.getName() + " Already Exists", Snackbar.LENGTH_LONG).show();
                }


            }

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

                loadPrompt.setTitle("Please Select a Progression: ");

                loadPrompt.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Progression currentProg = new Progression();
                        List<Chord> chordList;
                        String progName = items[which];

                        pViewModel.setProgression(progressionDao.getProgressionByName(progName, user.getId()));

                        //currentProg = pViewModel.getProgression().getValue();
                        //chordList = currentProg.getChords();

                        //pViewModel.changeChord(0, chordList.get(0).getChordName());
                        //pViewModel.changeChord(1, chordList.get(1).getChordName());
                        //pViewModel.changeChord(2, chordList.get(2).getChordName());
                        //pViewModel.changeChord(3, chordList.get(3).getChordName());

                        // Pass the chord names to ProgressionFragment so the UI will update
                        FragmentManager fm = getSupportFragmentManager();
                        Bundle arguments = new Bundle();
                        arguments.putString("chord1Text", pViewModel.getCurchord(0).getValue());
                        arguments.putString("chord2Text", pViewModel.getCurchord(1).getValue());
                        arguments.putString("chord3Text", pViewModel.getCurchord(2).getValue());
                        arguments.putString("chord4Text", pViewModel.getCurchord(3).getValue());

                        ProgressionFragment fragment = new ProgressionFragment();
                        fragment.setArguments(arguments);
                        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                        if (player != null && player.isPlaying()) {
                            player.stop();
                        }

                        Snackbar.make(findViewById(android.R.id.content), "Progression " + progName + " Loaded", Snackbar.LENGTH_LONG).show();
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

        mGenerateButton.setOnClickListener((new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Progression randomProgression = new Progression();

                List<String> progressionNames = Arrays.asList("A", "Am", "A7", "Am7", "B", "Bm", "B7", "Bm7", "C", "Cm", "C7", "Cm7", "D", "Dm", "D7", "Dm7", "E", "Em", "E7", "Em7", "F", "Fm", "F7", "Fm7", "G", "Gm", "G7", "Gm7");
                Collections.shuffle(progressionNames);

                String progRandomName = "";
                progRandomName = String.join("", progressionNames.get(0), progressionNames.get(1), progressionNames.get(2), progressionNames.get(3));

                randomProgression.setName(progRandomName);
                randomProgression.addChord(new Chord(progressionNames.get(0), 1));
                randomProgression.addChord(new Chord(progressionNames.get(1), 1));
                randomProgression.addChord(new Chord(progressionNames.get(2), 1));
                randomProgression.addChord(new Chord(progressionNames.get(3), 1));

                pViewModel.setProgression(randomProgression);

                //pViewModel.changeChord(0, progressionNames.get(0));
                //pViewModel.changeChord(1, progressionNames.get(1));
                //pViewModel.changeChord(2, progressionNames.get(2));
                //pViewModel.changeChord(3, progressionNames.get(3));

                // Pass the chord names to ProgressionFragment so the UI will update
                FragmentManager fm = getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putString("chord1Text", pViewModel.getCurchord(0).getValue());
                arguments.putString("chord2Text", pViewModel.getCurchord(1).getValue());
                arguments.putString("chord3Text", pViewModel.getCurchord(2).getValue());
                arguments.putString("chord4Text", pViewModel.getCurchord(3).getValue());

                ProgressionFragment fragment = new ProgressionFragment();
                fragment.setArguments(arguments);
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();


                if (player != null && player.isPlaying()) {
                    player.pause();
                }
                // Play a shake animation for the Generate button
                mGenerateButton.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake));

                Snackbar.make(findViewById(android.R.id.content), "Progression " + randomProgression.getName() + " Generated!", Snackbar.LENGTH_LONG).show();
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
                inputAlert.setMessage("Please enter a new email address:");
                final EditText userInput = new EditText(context);
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        user.setEmail(userInputValue);
                        userDao.update(user);
                        finish();
                        Toast.makeText(getApplicationContext(), "Email Updated Successfully", Toast.LENGTH_LONG).show();
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

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
            }
        });

        mDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setEmail(emailFromIntent);
                user = userDao.getUserByEmail(emailFromIntent);

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Delete Account");
                alertDialog.setMessage("Are You Sure?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                userDao.delete(user);
                                finish();
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Account Deleted Successfully", Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
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

        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }
}

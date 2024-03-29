package com.example.cpg;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cpg.helpers.MIDI.MidiGenerator;
import com.example.cpg.viewModels.ProgressionViewModel;
import com.example.cpg.model.Chord;
import com.example.cpg.databinding.ProgressionFragmentBinding;
import com.example.cpg.model.Progression;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ProgressionFragment extends Fragment {

    public MainActivity mainActivity;

    private ProgressionViewModel mViewModel;

    private MidiGenerator midiGenerator;
    private MediaPlayer player;

    private Button mChord1Button, mChord2Button, mChord3Button, mChord4Button;
    private ProgressionFragmentBinding binding;

    private List<String> chordList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();

        //Set up the PROGRESSION VIEW MODEL
        mViewModel = ViewModelProviders.of(getActivity()).get(ProgressionViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.progression_fragment, container, false);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(mViewModel);
        View view = binding.getRoot();

        mViewModel.getProgression().observe(this, new Observer<Progression>() {
            @Override
            public void onChanged(@Nullable Progression progression) {
                //progression.setName(mChord1Button.getText().toString() + mChord2Button.getText().toString() + mChord3Button.getText().toString() + mChord4Button.getText().toString());
                //progression.getChords();
                //System.out.println(progression.getName());
            }
        });

        mChord1Button = binding.CH1;
        mChord2Button = binding.CH2;
        mChord3Button = binding.CH3;
        mChord4Button = binding.CH4;

        chordList = Arrays.asList("A", "Am", "A7", "Am7", "B", "Bm", "B7", "Bm7", "C", "Cm", "C7", "Cm7", "D", "Dm", "D7", "Dm7", "E", "Em", "E7", "Em7", "F", "Fm", "F7", "Fm7", "G", "Gm", "G7", "Gm7");

        // If the load button or generate button (or shake to generate) are pressed, then get the new chord values from MainActivity to update their names
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String chord1Text = bundle.getString("chord1Text");
            String chord2Text = bundle.getString("chord2Text");
            String chord3Text = bundle.getString("chord3Text");
            String chord4Text = bundle.getString("chord4Text");

            mChord1Button.setText(chord1Text);
            mChord2Button.setText(chord2Text);
            mChord3Button.setText(chord3Text);
            mChord4Button.setText(chord4Text);
        }

        mChord1Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //List of progressions to display to user
                String[] items = new String[chordList.size()];
                for (int i = 0; i < chordList.size(); i++) {

                    // Assign each value to String array
                    items[i] = chordList.get(i);

                }

                //new prompt
                AlertDialog.Builder loadPrompt = new AlertDialog.Builder(getActivity());

                loadPrompt.setTitle("Please select a Chord: ");

                loadPrompt.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = items[which];
                        mChord1Button.setText(userInputValue);
                        mViewModel.changeChord(0, userInputValue);
                    }
                });

                loadPrompt.setCancelable(false).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    @Override  public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                loadPrompt.create().show();

                return true;
            }
        });

        mChord1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curChord = "";
                curChord = mViewModel.getCurchord(0).getValue();
                Progression progression = new Progression();
                progression.addChord(new Chord(curChord, 1));
                //Write the midi file
                File midout = new File(getActivity().getCacheDir() + "/midout.mid");
                midiGenerator = new MidiGenerator();
                midiGenerator.writeProgression(getActivity(), progression);
                //Create the media player
                player = MediaPlayer.create(getActivity(), Uri.fromFile(midout));
                player.start();
            }
        });

        mChord2Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //List of progressions to display to user
                String[] items = new String[chordList.size()];
                for (int i = 0; i < chordList.size(); i++) {

                    // Assign each value to String array
                    items[i] = chordList.get(i);

                }

                //new prompt
                AlertDialog.Builder loadPrompt = new AlertDialog.Builder(getActivity());

                loadPrompt.setTitle("Please select a Chord: ");

                loadPrompt.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = items[which];
                        mChord2Button.setText(userInputValue);
                        mViewModel.changeChord(1, userInputValue);
                    }
                });

                loadPrompt.setCancelable(false).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    @Override  public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                loadPrompt.create().show();

                return true;
            }
        });

        mChord2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curChord = "";
                curChord = mViewModel.getCurchord(1).getValue();
                Progression progression = new Progression();
                progression.addChord(new Chord(curChord, 1));
                //Write the midi file
                File midout = new File(getActivity().getCacheDir() + "/midout.mid");
                midiGenerator = new MidiGenerator();
                midiGenerator.writeProgression(getActivity(), progression);
                //Create the media player
                player = MediaPlayer.create(getActivity(), Uri.fromFile(midout));
                player.start();
            }
        });

        mChord3Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //List of progressions to display to user
                String[] items = new String[chordList.size()];
                for (int i = 0; i < chordList.size(); i++) {

                    // Assign each value to String array
                    items[i] = chordList.get(i);

                }

                //new prompt
                AlertDialog.Builder loadPrompt = new AlertDialog.Builder(getActivity());

                loadPrompt.setTitle("Please select a Chord: ");

                loadPrompt.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = items[which];
                        mChord3Button.setText(userInputValue);
                        mViewModel.changeChord(2, userInputValue);
                    }
                });

                loadPrompt.setCancelable(false).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    @Override  public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                loadPrompt.create().show();

                return true;
            }
        });

        mChord3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curChord = "";
                curChord = mViewModel.getCurchord(2).getValue();
                Progression progression = new Progression();
                progression.addChord(new Chord(curChord, 1));
                //Write the midi file
                File midout = new File(getActivity().getCacheDir() + "/midout.mid");
                midiGenerator = new MidiGenerator();
                midiGenerator.writeProgression(getActivity(), progression);
                //Create the media player
                player = MediaPlayer.create(getActivity(), Uri.fromFile(midout));
                player.start();
            }
        });

        mChord4Button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //List of progressions to display to user
                String[] items = new String[chordList.size()];
                for (int i = 0; i < chordList.size(); i++) {

                    // Assign each value to String array
                    items[i] = chordList.get(i);

                }

                //new prompt
                AlertDialog.Builder loadPrompt = new AlertDialog.Builder(getActivity());

                loadPrompt.setTitle("Please select a Chord: ");

                loadPrompt.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = items[which];
                        mChord4Button.setText(userInputValue);
                        mViewModel.changeChord(3, userInputValue);
                    }
                });

                loadPrompt.setCancelable(false).setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    @Override  public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                loadPrompt.create().show();

                return true;
            }
        });

        mChord4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curChord = "";
                curChord = mViewModel.getCurchord(3).getValue();
                Progression progression = new Progression();
                progression.addChord(new Chord(curChord, 1));
                //Write the midi file
                File midout = new File(getActivity().getCacheDir() + "/midout.mid");
                midiGenerator = new MidiGenerator();
                midiGenerator.writeProgression(getActivity(), progression);
                //Create the media player
                player = MediaPlayer.create(getActivity(), Uri.fromFile(midout));
                player.start();
            }
        });

        return view;
    }

    public void setTextChord1(String text) {
        mChord1Button.setText(text);
    }

    public void setTextChord2(String text) {
        mChord2Button.setText(text);
    }

    public void setTextChord3(String text) {
        mChord3Button.setText(text);
    }

    public void setTextChord4(String text) {
        mChord4Button.setText(text);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(ProgressionViewModel.class);
    }

}

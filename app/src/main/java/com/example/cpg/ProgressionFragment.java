package com.example.cpg;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cpg.ViewModels.ProgressionViewModel;
import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgressionFragment extends Fragment {

    private ProgressionViewModel mViewModel;

    private Button mAddChordButton;
    private Button mSubtractChordButton;
    private Button mChord1Button, mChord2Button, mChord3Button, mChord4Button, mChord5Button, mChord6Button;
    // Progression to add chords into for the View Model
    Progression currentProgression = new Progression();

    public static ProgressionFragment newInstance() {
        return new ProgressionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progression_fragment, container, false);


        //Set up the PROGRESSION VIEW MODEL
        mViewModel = ViewModelProviders.of(this).get(ProgressionViewModel.class);

        mViewModel.getCurrentChord1().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String chordName) {
                mChord1Button.setText(chordName);
            }
        });

        mViewModel.getCurrentChord2().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String chordName) {
                mChord2Button.setText(chordName);
            }
        });

        mViewModel.getCurrentChord3().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String chordName) {
                mChord3Button.setText(chordName);
            }
        });

        mViewModel.getCurrentChord4().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String chordName) {
                mChord4Button.setText(chordName);
            }
        });

        mViewModel.getProgression().observe(this, new Observer<Progression>() {
            @Override
            public void onChanged(@Nullable Progression progression) {
                progression.setName(mChord1Button.getText().toString() + mChord1Button.getText().toString() + mChord1Button.getText().toString() + mChord1Button.getText().toString());
                //progression.getChords();
                System.out.println(progression.getName());
            }
        });


        mAddChordButton = view.findViewById(R.id.add_chord);
        mSubtractChordButton = view.findViewById(R.id.subtract_chord);
        mChord1Button = view.findViewById(R.id.CH1);
        mChord2Button = view.findViewById(R.id.CH2);
        mChord3Button = view.findViewById(R.id.CH3);
        mChord4Button = view.findViewById(R.id.CH4);
        mChord5Button = view.findViewById(R.id.CH5);
        mChord6Button = view.findViewById(R.id.CH6);

        // Start with only 4 visible chords
        mChord5Button.setVisibility(View.GONE);
        mChord6Button.setVisibility(View.GONE);

        currentProgression.addChord((new Chord(mChord1Button.getText().toString(), 1)));
        currentProgression.addChord((new Chord(mChord2Button.getText().toString(), 1)));
        currentProgression.addChord((new Chord(mChord3Button.getText().toString(), 1)));
        currentProgression.addChord((new Chord(mChord4Button.getText().toString(), 1)));
        //System.out.println(mChord4Button.getText().toString());
        //mViewModel.getProgression().setValue(currentProgression);
        mViewModel.setProgression(currentProgression);

        mAddChordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChord5Button.getVisibility() == View.GONE) {
                    mChord5Button.setVisibility(View.VISIBLE);
                }
                else if (mChord5Button.getVisibility() == View.VISIBLE && mChord6Button.getVisibility() == View.GONE) {
                    mChord6Button.setVisibility(View.VISIBLE);
                }

            }
        });

        mSubtractChordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChord6Button.getVisibility() == View.VISIBLE) {
                    mChord6Button.setVisibility(View.GONE);
                }
                else if (mChord6Button.getVisibility() == View.GONE && mChord5Button.getVisibility() == View.VISIBLE) {
                    mChord5Button.setVisibility(View.GONE);
                }
            }
        });

        mChord1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
                inputAlert.setTitle("Select Chord:");
                inputAlert.setMessage("A Am A7 Am7 B Bm B7 Bm7 C Cm C7 Cm7 D Dm D7 Dm7 E Em E7 Em7 F Fm F7 Fm7 G Gm G7 Gm7");
                final EditText userInput = new EditText(getActivity());
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        mViewModel.getCurrentChord1().setValue(userInputValue);
                        currentProgression.addChord((new Chord(userInputValue, 1)));
                        mViewModel.getProgression().setValue(currentProgression);
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

        mChord2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
                inputAlert.setTitle("Select Chord:");
                inputAlert.setMessage("A Am A7 Am7 B Bm B7 Bm7 C Cm C7 Cm7 D Dm D7 Dm7 E Em E7 Em7 F Fm F7 Fm7 G Gm G7 Gm7");
                final EditText userInput = new EditText(getActivity());
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        mViewModel.getCurrentChord2().setValue(userInputValue);
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

        mChord3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
                inputAlert.setTitle("Select Chord:");
                inputAlert.setMessage("A Am A7 Am7 B Bm B7 Bm7 C Cm C7 Cm7 D Dm D7 Dm7 E Em E7 Em7 F Fm F7 Fm7 G Gm G7 Gm7");
                final EditText userInput = new EditText(getActivity());
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        mViewModel.getCurrentChord3().setValue(userInputValue);
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

        mChord4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder inputAlert = new AlertDialog.Builder(getActivity());
                inputAlert.setTitle("Select Chord:");
                inputAlert.setMessage("A Am A7 Am7 B Bm B7 Bm7 C Cm C7 Cm7 D Dm D7 Dm7 E Em E7 Em7 F Fm F7 Fm7 G Gm G7 Gm7");
                final EditText userInput = new EditText(getActivity());
                inputAlert.setView(userInput);
                inputAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInputValue = userInput.getText().toString();
                        mViewModel.getCurrentChord4().setValue(userInputValue);
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

        mChord5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mChord6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(ProgressionViewModel.class);
    }

}

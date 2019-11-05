package com.example.cpg;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cpg.ViewModels.ProgressionViewModel;
import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

import java.util.List;

public class ProgressionFragment extends Fragment {

    private ProgressionViewModel mViewModel;

    private Button mAddChordButton;
    private Button mSubtractChordButton;
    private Button mChord1Button, mChord2Button, mChord3Button, mChord4Button, mChord5Button, mChord6Button;

    public static ProgressionFragment newInstance() {
        return new ProgressionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.progression_fragment, container, false);


        //Set up the PROGRESSION VIEW MODEL
        mViewModel = ViewModelProviders.of(this).get(ProgressionViewModel.class);
        mViewModel.getProgression().observe(this, new Observer<Progression>() {
            @Override
            public void onChanged(@Nullable Progression progression) {

            }
        });

        mAddChordButton = view.findViewById(R.id.add_chord);
        mSubtractChordButton = view.findViewById(R.id.subtract_chord);
        mChord2Button = view.findViewById(R.id.CH2);
        mChord1Button = view.findViewById(R.id.CH1);
        mChord3Button = view.findViewById(R.id.CH3);
        mChord4Button = view.findViewById(R.id.CH4);
        mChord5Button = view.findViewById(R.id.CH5);
        mChord6Button = view.findViewById(R.id.CH6);

        // Start with only 4 visible chords
        mChord5Button.setVisibility(View.GONE);
        mChord6Button.setVisibility(View.GONE);

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

            }
        });

        mChord2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mChord3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mChord4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

package com.example.cpg;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        mAddChordButton = (Button)view.findViewById(R.id.add_chord);
        mSubtractChordButton = (Button)view.findViewById(R.id.subtract_chord);
        mChord1Button = (Button)view.findViewById(R.id.CH1);
        mChord2Button = (Button)view.findViewById(R.id.CH2);
        mChord3Button = (Button)view.findViewById(R.id.CH3);
        mChord4Button = (Button)view.findViewById(R.id.CH4);
        mChord5Button = (Button)view.findViewById(R.id.CH5);
        mChord6Button = (Button)view.findViewById(R.id.CH6);

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

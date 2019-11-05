package com.example.cpg.ViewModels;

import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cpg.R;
import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ProgressionViewModel extends ViewModel {

    private MutableLiveData<Progression> prog;
    private MutableLiveData<List<Chord>> chords;

    public LiveData<Progression> getProgression(){
        if(prog == null){
            //Create the initially loaded progression
            prog = new MutableLiveData<>();
            Progression p = new Progression();
            p.setName("FFmCC");
            p.addChord(new Chord("F", 1));
            p.addChord(new Chord("Fm", 1));
            p.addChord(new Chord("C", 1));
            p.addChord(new Chord("C", 1));
            prog.setValue(p);
        }
        return prog;
    }

    public LiveData<List<Chord>> getChords(){
        if(chords == null){
            chords = new MutableLiveData<>();
        }
        return chords;
    }
}

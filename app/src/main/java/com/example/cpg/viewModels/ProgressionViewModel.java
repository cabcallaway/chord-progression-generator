package com.example.cpg.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

import java.util.List;

public class ProgressionViewModel extends ViewModel {

    private MutableLiveData<Progression> prog;
    private MutableLiveData<List<Chord>> chords;
    private MutableLiveData<String> currentChord1, currentChord2, currentChord3, currentChord4;

    public MutableLiveData<String> getCurrentChord1() {
        if (currentChord1 == null) {
            currentChord1 = new MutableLiveData<String>();
        }
        return currentChord1;
    }

    public MutableLiveData<String> getCurrentChord2() {
        if (currentChord2 == null) {
            currentChord2 = new MutableLiveData<String>();
        }
        return currentChord2;
    }

    public MutableLiveData<String> getCurrentChord3() {
        if (currentChord3 == null) {
            currentChord3 = new MutableLiveData<String>();
        }
        return currentChord3;
    }

    public MutableLiveData<String> getCurrentChord4() {
        if (currentChord4 == null) {
            currentChord4 = new MutableLiveData<String>();
        }
        return currentChord4;
    }

    public MutableLiveData<Progression> getProgression(){
        if(prog == null){
            //Create the initially loaded progression
            prog = new MutableLiveData<Progression>();
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

    public void setProgression(Progression progression) {

        prog.setValue(progression);

    }

    public MutableLiveData<List<Chord>> getChords(){
        if(chords == null){
            chords = new MutableLiveData<>();
            //chords.observe();
        }
        return chords;
    }
}
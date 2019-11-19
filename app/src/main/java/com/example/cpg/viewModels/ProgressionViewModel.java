package com.example.cpg.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

//import java.util.ArrayList;
import java.util.List;

public class ProgressionViewModel extends ViewModel {

    private MutableLiveData<Progression> prog;
    private MutableLiveData<List<Chord>> chords;
    private MutableLiveData<String> curchord;

    public LiveData<String> getCurchord(int i){
        MutableLiveData<String> c = new MutableLiveData<>();
        c.setValue(chords.getValue().get(i).getChordName());
        curchord = c;
        return curchord;
    }

    public void setCurchord(int i){
        MutableLiveData<String> c = new MutableLiveData<>();
        c.setValue(chords.getValue().get(i).getChordName());
        curchord = c;
    }

    public void changeChord(int i, String chordName){
        Progression p = prog.getValue();
        p.changeChord(i, chordName);
        prog.setValue(p);
        chords.setValue(p.getChords());
    }

    public LiveData<Progression> getProgression(){
        if(prog == null){
            //Create the initially loaded progression
            prog = new MutableLiveData<>();
            chords = new MutableLiveData<>();

            Progression p = new Progression();
            p.setName("FFmCC");
            p.addChord(new Chord("F", 1));
            p.addChord(new Chord("Fm", 1));
            p.addChord(new Chord("C", 1));
            p.addChord(new Chord("C", 1));
            prog.setValue(p);
            chords.setValue(p.getChords());
        }
        return prog;
    }

    public void setProgression(Progression p) {

        prog.setValue(p);

        chords.setValue(p.getChords());

    }
}
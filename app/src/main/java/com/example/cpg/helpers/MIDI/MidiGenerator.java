package com.example.cpg.helpers.MIDI;

import android.content.Context;

import com.example.cpg.MainActivity;
import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.meta.Tempo;
import com.pdrogfer.mididroid.event.meta.TimeSignature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.cpg.helpers.MIDI.Notes;


//TODO: Write boilerplate method for setting up midi tracks to write to
//TODO: Write methods for writing different kinds of chords to a track


public class MidiGenerator {

    /**
     * method to write chord progression
     *
     * @return Filer
     */
    public File writeTestFile(Context context) {

        Notes notes = new Notes();

        //Create MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack  = new MidiTrack();

        //Add events to tracks.
        //Track 0 is tempo track
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(228);
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        //Track 1 will contain the F Fm C C chord progression
        final int NOTES_PER_CHORD = 4;

        int midiNote = notes.getNote("C4");
        midiNote = notes.getNote("C9");
        midiNote = notes.getNote("C0");
        midiNote = notes.getNote("Csh4");
        midiNote = notes.getNote("Ab4");
        midiNote = notes.getNote("Ab7");
        for(int i = 0; i < NOTES_PER_CHORD; i++){
            int channel = 0;
            int pitch = 50 + i;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            noteTrack.insertNote(channel, pitch, velocity, tick, duration);
        }

        //Create MidiFile
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
        File midout = null;
        //Write midi to file
        File outputDir = context.getCacheDir();
        try {
            //midout = File.createTempFile("midout", ".mid", outputDir);
            midout = new File(outputDir + "/midout.mid");
            midi.writeToFile(midout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return midout;
    }

    public File write_FFmCC(Context context){
        //Create MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack  = new MidiTrack();

        //Add events to tracks.
        //Track 0 is tempo track
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(228);
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        //Track 1 will contain notes
        final int NOTE_COUNT = 40;
        for(int i = 0; i < NOTE_COUNT; i++){
            int channel = 0;
            int pitch = 50 + i;
            int velocity = 100;
            long tick = i * 480;
            long duration = 120;

            noteTrack.insertNote(channel, pitch, velocity, tick, duration);
        }

        //Create MidiFile
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);
        File midout = null;
        //Write midi to file
        File outputDir = context.getCacheDir();
        try {
            //midout = File.createTempFile("midout", ".mid", outputDir);
            midout = new File(outputDir + "/midout.mid");
            midi.writeToFile(midout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return midout;
    }


}

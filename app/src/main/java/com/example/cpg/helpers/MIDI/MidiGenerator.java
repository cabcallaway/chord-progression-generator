package com.example.cpg.helpers.MIDI;

import android.content.Context;

import com.example.cpg.model.Progression;
import com.pdrogfer.mididroid.MidiFile;
import com.pdrogfer.mididroid.MidiTrack;
import com.pdrogfer.mididroid.event.meta.Tempo;
import com.pdrogfer.mididroid.event.meta.TimeSignature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//TODO: Write boilerplate method for setting up midi tracks to write to
//TODO: Write methods for writing different kinds of chords to a track

class Note{
    int channel;
    int pitch;
    int velocity;
    int tick;
    int duration;
    Note(){
        this.channel = 0;
        this.velocity = 100;
        this.duration = 960;
    }
}


public class MidiGenerator {

    /**
     * method to write chord progression
     *
     * @return Filer
     */
    public File writeTestFile(Context context) {

        MidiNotes notes = new MidiNotes();

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

    public File write_FFmCC(Context context){
        //Create MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack  = new MidiTrack();

        //Add events to tracks.
        //Track 0 is tempo track
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo tempo = new Tempo();
        tempo.setBpm(120);
        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(tempo);

        //Track 1 will contain the F Fm C C chord progression
        final int NOTES_PER_CHORD = 4;
        String[] chords = {"F", "Fm", "C", "C"};
        for(int i = 0; i < chords.length; i++){
            String chord = chords[i];
            writeChord(chord, i, noteTrack);
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

    public void writeProgression(Context context, Progression p){

    }

    private void writeChord(String chord, int bar, MidiTrack track){
        Note n = new Note();

    }

    //TODO: Just use chord type patters.
    //TODO:
    // MAJ: 4-3
    // MIN: 3-4
    // MAJ7: 4-3-4
    // MIN7: 3-4-3

    private void writeF(int bar, MidiTrack track){
        String[] notesF= {"F3", "F4", "A4", "C4"};
        Note n = new Note();
        for(String note : notesF){
            n.pitch = MidiNotes.getNote(note);
            //Quarter note = 480, 1 bar = 4 quarter notes
            n.tick = bar * (480 * 4);
            track.insertNote(n.channel, n.pitch, n.velocity, n.tick, n.duration);
        }
    }

    private void writeFm(int bar, MidiTrack track){
        String[] notesF= {"F3", "F4", "Ab4", "C4"};
        Note n = new Note();
        for(String note : notesF){
            n.pitch = MidiNotes.getNote(note);
            //Quarter note = 480, 1 bar = 4 quarter notes
            n.tick = bar * (480 * 4);
            track.insertNote(n.channel, n.pitch, n.velocity, n.tick, n.duration);
        }
    }

    private void writeC(int bar, MidiTrack track){
        String[] notesF= {"C3", "C4", "E4", "G4"};
        Note n = new Note();
        for(String note : notesF){
            n.pitch = MidiNotes.getNote(note);
            //Quarter note = 480, 1 bar = 4 quarter notes
            n.tick = bar * (480 * 4);
            track.insertNote(n.channel, n.pitch, n.velocity, n.tick, n.duration);
        }
    }


}

package com.example.cpg.helpers.MIDI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//MidiNotes Class gives ability to get midi note numbers corresponding to note names
public class MidiNotes {

    //Sub class defining an input note string by the string and octave number
    static class InputString{
        String note;
        int octave;
        InputString(String s){
            //Parses out the octave from given note
            Pattern p = Pattern.compile("\\d");
            Matcher m = p.matcher(s);
            if(m.find()){
                octave = Integer.parseInt(m.group());
                note = s.replace(m.group(), "");
            } else{
                octave = 4;
                note = s;
            }
        }
    }

    //Starting notes enum in octave 4
    enum Note{
        C(60),
        Csh(61),
        Db(61),
        D(62),
        Dsh(63),
        Eb(63),
        E(64),
        F(65),
        Fsh(66),
        Gb(66),
        G(67),
        Gsh(68),
        Ab(68),
        A(69),
        Ash(70),
        Bb(70),
        B(71);

        private int midiNote;

        private Note(int midiNote){
            this.midiNote = midiNote;
        }

        public int getMidiNote(){
            return this.midiNote;
        }
    }

    //Transposes the note to the correct octave
    private static int transpose(int n, int octave){
        int midiNote = n;
        if(octave < 0 || octave > 9){
            throw new IllegalArgumentException("Please use octave between 0 and 9");
        }
        int difference = octave - 4;
        midiNote += (12 * difference);

        return midiNote;
    }

    //Returns the midi note number associated with the given note
    public static int getNote(String note){
        InputString input = new InputString(note);

        if(input.note.contains("#")){
            input.note = input.note.replace("#", "sh");
        }
        Note n = Note.valueOf(input.note);

        int midiNote = n.getMidiNote();
        if(input.octave != 4){
            midiNote = transpose(n.getMidiNote(), input.octave);
        }

        return midiNote;
    }
}

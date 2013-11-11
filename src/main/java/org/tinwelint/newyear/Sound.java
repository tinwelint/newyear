package org.tinwelint.newyear;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound
{
    public interface Listener
    {
        void onSoundEnded();
    }
    
    private final Clip clip;
    private final File soundFile;
    
    public Sound( File soundFile ) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        this.soundFile = soundFile;
        clip = AudioSystem.getClip();
        try ( AudioInputStream audioInput = AudioSystem.getAudioInputStream( soundFile ) )
        {
            clip.open( audioInput );
        }
    }
    
    public void play( final Listener events )
    {
        clip.addLineListener( new LineListener()
        {
            @Override
            public void update( LineEvent event )
            {
                if ( event.getType() == Type.STOP )
                {
                    events.onSoundEnded();
                    clip.removeLineListener( this );
                    clip.setFramePosition( 0 );
                }
            }
        } );
        clip.start();
    }
    
    @Override
    public String toString()
    {
        return "file:" + soundFile;
    }
}

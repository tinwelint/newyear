package org.tinwelint.newyear;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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
        void onProgress( float secondsLeft );
        
        void onSoundEnded();
    }
    
    private final Clip clip;
    private final float frameRate;
    private final File soundFile;
    private final AtomicBoolean playing = new AtomicBoolean();
    
    public Sound( File soundFile ) throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        this.soundFile = soundFile;
        clip = AudioSystem.getClip();
        try ( AudioInputStream audioInput = AudioSystem.getAudioInputStream( soundFile ) )
        {
            clip.open( audioInput );
            frameRate = clip.getFormat().getFrameRate();
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
                    playing.set( false );
                }
            }
        } );
        
        Thread progressThread = new Thread()
        {
            @Override
            public void run()
            {
                while ( playing.get() )
                {
                    int framesLeft = clip.getFrameLength() - clip.getFramePosition();
                    float secondsLeft = framesLeft / frameRate;
                    events.onProgress( secondsLeft );
                    
                    try
                    {
                        Thread.sleep( 100 );
                    }
                    catch ( InterruptedException e )
                    {
                        break;
                    }
                }
            }
        };
        
        playing.set( true );
        clip.start();
        progressThread.start();
    }
    
    @Override
    public String toString()
    {
        return "file:" + soundFile;
    }
}

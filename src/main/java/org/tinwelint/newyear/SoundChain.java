package org.tinwelint.newyear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.tinwelint.newyear.Sound.Listener;

public class SoundChain
{
    private final List<Sound> sounds;
    
    private SoundChain( List<Sound> sounds )
    {
        this.sounds = sounds;
    }
    
    public SoundChain chain( Sound sound )
    {
        List<Sound> newSounds = new ArrayList<>( sounds );
        newSounds.add( sound );
        return new SoundChain( newSounds );
    }

    public void play()
    {
        final Iterator<Sound> soundIterator = sounds.iterator();
        if ( soundIterator.hasNext() )
        {
            soundIterator.next().play( new Listener()
            {
                @Override
                public void onSoundEnded()
                {
                    if ( soundIterator.hasNext() )
                    {
                        soundIterator.next().play( this );
                    }
                }
            } );
        }
    }
    
    @Override
    public String toString()
    {
        return "sound chain" + sounds;
    }
    
    public static SoundChain startChain()
    {
        return new SoundChain( Collections.<Sound>emptyList() );
    }
}

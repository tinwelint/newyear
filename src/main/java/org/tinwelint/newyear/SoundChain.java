package org.tinwelint.newyear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.tinwelint.newyear.Sound.ListenerAdapter;

public class SoundChain
{
    private final List<Sound> sounds;
    private final DynamicConfiguration config;
    
    private SoundChain( List<Sound> sounds, DynamicConfiguration config )
    {
        this.sounds = sounds;
        this.config = config;
    }
    
    public SoundChain add( Sound sound )
    {
        List<Sound> newSounds = new ArrayList<>( sounds );
        newSounds.add( sound );
        return new SoundChain( newSounds, config );
    }

    public void play()
    {
        final Iterator<Sound> soundIterator = sounds.iterator();
        if ( soundIterator.hasNext() )
        {
            soundIterator.next().play( new ChainListener( soundIterator ) );
        }
    }
    
    private class ChainListener extends ListenerAdapter
    {
        private volatile boolean fired;
        private final Iterator<Sound> sounds;
        
        ChainListener( Iterator<Sound> sounds )
        {
            this.sounds = sounds;
        }
        
        @Override
        public void onProgress( float secondsLeft )
        {
            if ( !fired && secondsLeft <= config.getSecondsCrossFade() )
            {
                fired = true;
                if ( sounds.hasNext() )
                {
                    sounds.next().play( new ChainListener( sounds ) );
                }
            }
        }
        
        @Override
        public void onSoundEnded()
        {
            onProgress( 0 );
        }
    }
    
    @Override
    public String toString()
    {
        return "sound chain" + sounds;
    }
    
    public static SoundChain startChain( DynamicConfiguration config )
    {
        return new SoundChain( Collections.<Sound>emptyList(), config );
    }
}

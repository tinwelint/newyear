package org.tinwelint.newyear;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SoundChain
{
    private final List<Sound> sounds = new ArrayList<>();
    private final DynamicConfiguration config;
    
    public SoundChain( DynamicConfiguration config )
    {
        this.config = config;
    }
    
    public void add( Sound sound )
    {
        sounds.add( sound );
    }

    public void play()
    {
        final Iterator<Sound> soundIterator = sounds.iterator();
        if ( soundIterator.hasNext() )
        {
            soundIterator.next().play( new ChainListener( soundIterator ) );
        }
    }
    
    private class ChainListener implements Sound.Listener
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
}

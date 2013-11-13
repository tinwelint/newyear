package org.tinwelint.newyear;

import static java.lang.Math.min;
import static java.util.Collections.shuffle;

import java.util.LinkedList;
import java.util.Random;

public class SoundSet
{
    private final LinkedList<Sound> list = new LinkedList<>();
    private final Random random;
    
    public SoundSet( Random random )
    {
        this.random = random;
    }
    
    public void add( Sound sound )
    {
        list.add( sound );
    }
    
    public void doneAdding()
    {
        shuffle( list );
    }
    
    /**
     * Will return recently returned sounds less likely. The more recent, the less likely.
     */
    public Sound random()
    {
        if ( list.isEmpty() )
        {
            return null;
        }
        
        int soundIndex = min( random.nextInt( list.size() ), random.nextInt( list.size() ) );
        Sound sound = list.remove( soundIndex );
        list.addLast( sound );
        return sound;
    }
}

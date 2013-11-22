package org.tinwelint.newyear.helpers;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.min;
import static java.util.Collections.shuffle;

public class RandomSet<T>
{
    private final LinkedList<T> list = new LinkedList<>();
    private final Random random;
    
    public RandomSet( Random random )
    {
        this.random = random;
    }
    
    public void add( T item )
    {
        list.add( item );
    }
    
    public void doneAdding()
    {
        shuffle( list );
    }
    
    /**
     * Will return recently returned sounds less likely. The more recent, the less likely.
     */
    public T random()
    {
        if ( list.isEmpty() )
        {
            return null;
        }
        
        int soundIndex = min( random.nextInt( list.size() ), random.nextInt( list.size() ) );
        T item = list.remove( soundIndex );
        list.addLast( item );
        return item;
    }
}

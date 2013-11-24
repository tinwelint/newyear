package org.tinwelint.newyear;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownCounter extends Thread
{
    private final List<Listener> listeners = new ArrayList<>();
    private boolean halted;

    public interface Listener
    {
        void timeRemaining( RemainingTime remainingTime );
    }
    
    public DownCounter( Listener listener )
    {
        this.listeners.add( listener );
        start();
    }
    
    @Override
    public void run()
    {
        while ( !halted )
        {
            try
            {
                check();
                waitAWhile();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                System.out.println( "Don't panic, continuing" );
            }
        }
    }
    
    public void addListener( Listener listener )
    {
        this.listeners.add( listener );
    }
    
    private void check()
    {
        RemainingTime remainingTime = new RemainingTime( new Date() );
        for ( Listener listener : listeners )
        {
            listener.timeRemaining( remainingTime );
        }
    }

    private void waitAWhile()
    {
        try
        {
            Thread.sleep( 100 );
        }
        catch ( InterruptedException e )
        {
            Thread.interrupted();
        }
    }

    public void halt()
    {
        this.halted = true;
    }
}

package org.tinwelint.newyear;

import java.util.Date;

public class DownCounter extends Thread
{
    private final Listener listener;
    private boolean halted;

    public interface Listener
    {
        void timeRemaining( RemainingTime remainingTime );
    }
    
    public DownCounter( Listener listener )
    {
        this.listener = listener;
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
    
    private void check()
    {
        RemainingTime remainingTime = RemainingTime.ofDay( new Date() );
        listener.timeRemaining( remainingTime );
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

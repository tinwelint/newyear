package org.tinwelint.newyear;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.tinwelint.newyear.DownCounter.Listener;

public class Main
{
    public static void main( String[] args ) throws IOException
    {
        SoundRepository soundRepository = new SoundRepository( new File( "audio/repository" ) );
        
        Listener timeListener = playSoundByTimeRemaining( soundRepository );
        DownCounter downCounter = new DownCounter( timeListener );
        
        System.out.println( "Press ENTER to exit..." );
        System.in.read();
        
        downCounter.halt();
    }

    private static Listener playSoundByTimeRemaining( final SoundRepository soundRepository )
    {
        return new Listener()
        {
            private final Random random = new Random();
            private int nextMinuteDiff;
            private RemainingTime lastAduibleTime;
            
            @Override
            public void timeRemaining( RemainingTime remainingTime )
            {
                if ( remainingTime.hours() == 0 && remainingTime.minutes() == 0 && remainingTime.seconds() <= 20 )
                {   // final count down
                    throw new UnsupportedOperationException( "TODO" );
                }
                else if ( lastAduibleTime == null || remainingTime.minuteDiff( lastAduibleTime ) == nextMinuteDiff )
                {
                    playSound( remainingTime );
                }
                nextMinuteDiff = random.nextInt( 7 )+1;
                lastAduibleTime = remainingTime;
            }

            private void playSound( RemainingTime remainingTime )
            {
                SoundChain chain = SoundChain.startChain();
                chain = chain.chain( soundRepository.word( "nowitis" ) );
                // hour
                if ( remainingTime.tensOfHours() > 0 )
                {
                    chain = chain.chain( soundRepository.tenBasedSound( remainingTime.tensOfHours() ) );
                }
                if ( remainingTime.restHours() > 0 )
                {
                    chain = chain.chain( soundRepository.oneBasedSound( remainingTime.restHours() ) );
                }
                if ( remainingTime.hours() > 0 )
                {
                    chain = chain.chain( soundRepository.word( "hours" ) );
                }
                
                // minute
                if ( remainingTime.tensOfMinutes() > 0 )
                {
                    chain = chain.chain( soundRepository.tenBasedSound( remainingTime.tensOfMinutes() ) );
                }
                if ( remainingTime.restMinutes() > 0 )
                {
                    chain = chain.chain( soundRepository.oneBasedSound( remainingTime.restMinutes() ) );
                }
                if ( remainingTime.minutes() > 0 )
                {
                    chain = chain.chain( soundRepository.word( "minutes" ) );
                }
                // left to new year...
                chain = chain.chain( soundRepository.word( "left" ) )
                             .chain( soundRepository.word( "tonewyear" ) );
                
                // ... and play
                chain.play();
            }
        };
    }
}

package org.tinwelint.newyear;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.tinwelint.newyear.DownCounter.Listener;
import static org.tinwelint.newyear.SoundChain.startChain;

public class NewYearsEveMain
{
    public static void main( String[] args ) throws IOException
    {
        SoundRepository soundRepository = new SoundRepository( new File( "audio/repository" ) );
        
        AtomicReference<RemainingTime> lastAudibleTime = new AtomicReference<>();
        Listener timeListener = playSoundByTimeRemaining( soundRepository, lastAudibleTime );
        DownCounter downCounter = new DownCounter( timeListener );
        
        LittleWindow window = new LittleWindow( lastAudibleTime, downCounter );
        
        System.out.println( "Type 'exit' and ENTER to exit..." );
        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true )
        {
            String line = reader.readLine();
            System.out.println( line );
            if ( line == null || line.equals( "exit" ) )
            {
                break;
            }
            
            if ( line.length() == 0 || line.equals( "say" ) )
            {
                lastAudibleTime.set( null );
            }
        }
        
        downCounter.halt();
    }

    private static Listener playSoundByTimeRemaining( final SoundRepository soundRepository,
            final AtomicReference<RemainingTime> lastAudibleTime )
    {
        return new Listener()
        {
            private final Random random = new Random();
            private int nextMinuteDiff;
            private Integer lastCountDownSecond;
            
            @Override
            public void timeRemaining( RemainingTime remainingTime )
            {
//                if ( remainingTime.hours() >= 23 )
//                {
//                    return;
//                }
                
                if ( remainingTime.seconds() < 20 )
                {
                    if ( remainingTime.hours() == 0 && remainingTime.minutes() == 0 )
                    {
                        playCountdown( remainingTime );
                    }
                }
                else if ( lastAudibleTime.get() == null ||
                        remainingTime.minuteDiff( lastAudibleTime.get() ) == nextMinuteDiff )
                {
                    playSound( remainingTime );
                    nextMinuteDiff = random.nextInt( 7 )+1;
                    lastAudibleTime.set( remainingTime );
                    lastCountDownSecond = null;
                }
            }

            private void playCountdown( RemainingTime remainingTime )
            {
                if ( lastCountDownSecond == null || lastCountDownSecond.intValue() != remainingTime.seconds() )
                {
                    SoundChain chain = startChain();
                    // TODO play all happynewyear sounds in parallell
                    chain = remainingTime.seconds() == 0 ?
                            chain.add( soundRepository.word( "happynewyear" ) ) :
                            chain.add( soundRepository.oneBasedSound( remainingTime.seconds() ) );
                    chain.play();
                    lastCountDownSecond = remainingTime.seconds();
                }
            }

            private void playSound( RemainingTime remainingTime )
            {
                SoundChain chain = SoundChain.startChain();
                chain = chain.add( soundRepository.word( "nowitis" ) );
                chain = addHours( remainingTime, chain );
                chain = addMinutes( remainingTime, chain );
                chain = chain.add( soundRepository.word( "left" ) )
                             .add( soundRepository.word( "tonewyear" ) );
                
                chain.play();
            }

            private SoundChain addMinutes( RemainingTime remainingTime, SoundChain chain )
            {
                if ( remainingTime.minutes() > 0 && remainingTime.hours() > 0 )
                {
                    chain = chain.add( soundRepository.word( "and" ) );
                }
                if ( remainingTime.tensOfMinutes() > 0 )
                {
                    chain = chain.add( soundRepository.tenBasedSound( remainingTime.tensOfMinutes() ) );
                }
                if ( remainingTime.restMinutes() > 0 )
                {
                    chain = chain.add( soundRepository.oneBasedSound( remainingTime.restMinutes() ) );
                }
                if ( remainingTime.minutes() > 0 )
                {
                    chain = chain.add( soundRepository.word( remainingTime.minutes() == 1 ? "minute" : "minutes" ) );
                }
                return chain;
            }

            private SoundChain addHours( RemainingTime remainingTime, SoundChain chain )
            {
                if ( remainingTime.tensOfHours() > 0 )
                {
                    chain = chain.add( soundRepository.tenBasedSound( remainingTime.tensOfHours() ) );
                }
                if ( remainingTime.restHours() > 0 )
                {
                    chain = chain.add( soundRepository.oneBasedSound( remainingTime.restHours() ) );
                }
                if ( remainingTime.hours() > 0 )
                {
                    chain = chain.add( soundRepository.word( remainingTime.hours() == 1 ? "hour" : "hours" ) );
                }
                return chain;
            }
        };
    }
}

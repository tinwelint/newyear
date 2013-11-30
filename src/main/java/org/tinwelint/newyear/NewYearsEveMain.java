package org.tinwelint.newyear;

import java.io.File;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.tinwelint.newyear.DownCounter.Listener;

public class NewYearsEveMain
{
    public NewYearsEveMain()
    {
        SoundRepository soundRepository = new SoundRepository( new File( "audio/repository" ) );
        
        DynamicConfiguration config = new DynamicConfiguration();
        AtomicReference<RemainingTime> lastAudibleTime = new AtomicReference<>();
        DownCounter downCounter = new DownCounter( soundPlayer( soundRepository, lastAudibleTime, config ) );
        
        new LittleWindow( lastAudibleTime, downCounter, config ).showIt();
    }

    private Listener soundPlayer( final SoundRepository soundRepository,
            final AtomicReference<RemainingTime> lastAudibleTime, final DynamicConfiguration config )
    {
        return new Listener()
        {
            private final Random random = new Random();
            private int nextMinuteDiff;
            private Integer lastCountDownSecond;
            
            @Override
            public void timeRemaining( RemainingTime remainingTime )
            {
                if ( remainingTime.hours() >= 23 )
                {
                    return;
                }
                
                if ( remainingTime.hours() == 0 && remainingTime.minutes() == 0 )
                {
                    if ( remainingTime.seconds() < 20 )
                    {
                        playCountdown( remainingTime );
                    }
                }
                else if ( lastAudibleTime.get() == null ||
                        remainingTime.minuteDiff( lastAudibleTime.get() ) >= nextMinuteDiff )
                {
                    playTimeLeft( remainingTime );
                    nextMinuteDiff = random.nextInt( 7 )+1;
                    lastAudibleTime.set( remainingTime );
                    lastCountDownSecond = null;
                }
            }

            private void playCountdown( RemainingTime remainingTime )
            {
                if ( lastCountDownSecond == null || lastCountDownSecond.intValue() != remainingTime.seconds() )
                {
                    SoundChain chain = new SoundChain( config );
                    chain.add( remainingTime.seconds() == 0 ?
                            soundRepository.word( "happynewyear" ) :
                            soundRepository.oneBasedSound( remainingTime.seconds() ) );
                    chain.play();
                    lastCountDownSecond = remainingTime.seconds();
                }
            }

            private void playTimeLeft( RemainingTime remainingTime )
            {
                SoundChain chain = new SoundChain( config );
                chain.add( soundRepository.word( "nowitis" ) );
                addHours( remainingTime, chain );
                addMinutes( remainingTime, chain );
                chain.add( soundRepository.word( "left" ) );
                chain.add( soundRepository.word( "tonewyear" ) );
                chain.play();
            }

            private void addMinutes( RemainingTime remainingTime, SoundChain chain )
            {
                if ( remainingTime.minutes() > 0 && remainingTime.hours() > 0 )
                {
                    chain.add( soundRepository.word( "and" ) );
                }
                if ( remainingTime.tensOfMinutes() > 0 )
                {
                    chain.add( soundRepository.tenBasedSound( remainingTime.tensOfMinutes() ) );
                }
                if ( remainingTime.restMinutes() > 0 )
                {
                    chain.add( soundRepository.oneBasedSound( remainingTime.restMinutes() ) );
                }
                if ( remainingTime.minutes() > 0 )
                {
                    chain.add( soundRepository.word( remainingTime.minutes() == 1 ? "minute" : "minutes" ) );
                }
            }

            private void addHours( RemainingTime remainingTime, SoundChain chain )
            {
                if ( remainingTime.tensOfHours() > 0 )
                {
                    chain.add( soundRepository.tenBasedSound( remainingTime.tensOfHours() ) );
                }
                if ( remainingTime.restHours() > 0 )
                {
                    chain.add( soundRepository.oneBasedSound( remainingTime.restHours() ) );
                }
                if ( remainingTime.hours() > 0 )
                {
                    chain.add( soundRepository.word( remainingTime.hours() == 1 ? "hour" : "hours" ) );
                }
            }
        };
    }

    public static void main( String[] args )
    {
        new NewYearsEveMain();
    }
}

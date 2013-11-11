package org.tinwelint.newyear.helpers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class LatchUtil
{
    private static final int DEFAULT_WAIT_TIME = 20;
    private static final TimeUnit DEFAULT_WAIT_TIME_UNIT = SECONDS;
    
    public static void awaitLatch( CountDownLatch latch )
    {
        try
        {
            latch.await( DEFAULT_WAIT_TIME, DEFAULT_WAIT_TIME_UNIT );
        }
        catch ( InterruptedException e )
        {
            throw new RuntimeException( e );
        }
    }
}

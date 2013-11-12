package org.tinwelint.newyear;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

public class RemainingTime
{
    private final int hours, minutes, seconds;
    private final Calendar calendar;
    
    public RemainingTime( Date date )
    {
        calendar = Calendar.getInstance();
        calendar.setTime( date );
        int hourOfDay = calendar.get( Calendar.HOUR_OF_DAY );
        int minuteOfHour = calendar.get( Calendar.MINUTE );
        
        int hoursLeft = 23-hourOfDay;
        if ( minuteOfHour == 0 )
        {
            hoursLeft++;
        }
        int minutesLeft = 60-minuteOfHour;
        int secondsLeft = 59-calendar.get( Calendar.SECOND );
        
        // 23:00 ==> 1h, 0m
        // 23:01 ==> 0h, 59m
        // 23:15 ==> 0h, 45m
        this.hours = hoursLeft;
        this.minutes = minutesLeft;
        this.seconds = secondsLeft;
    }
    
    private int tensOf( int number )
    {
        return number < 20 ? 0 : number/10;
    }
    
    private int restOf( int number )
    {
        return number < 20 ? number : number%10;
    }
    
    public int tensOfHours()
    {
        return tensOf( hours );
    }
    
    public int restHours()
    {
        return restOf( hours );
    }
    
    public int tensOfMinutes()
    {
        return tensOf( minutes );
    }
    
    public int restMinutes()
    {
        return restOf( minutes );
    }

    public int hours()
    {
        return hours;
    }

    public int minutes()
    {
        return minutes;
    }
    
    public int seconds()
    {
        return seconds;
    }
    
    public int minuteDiff( RemainingTime time )
    {
        return (int) (abs( time.calendar.getTimeInMillis() - calendar.getTimeInMillis() ) / (1000*60));
    }
    
    @Override
    public String toString()
    {
        return "[" + hours + "h " + minutes + "m " + seconds + "s]";
    }
    
    public static RemainingTime ofDay( Date date )
    {
        return new RemainingTime( date );
    }
}

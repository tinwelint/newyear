package org.tinwelint.newyear;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.tinwelint.newyear.DownCounter.Listener;

public class LittleWindow extends JFrame
{
    private final AtomicReference<RemainingTime> lastAudibleTime;
    private JLabel timeLabel;

    public LittleWindow( AtomicReference<RemainingTime> lastAudibleTime, DownCounter downCounter )
    {
        super( "Snart e det nytt år" );
        this.lastAudibleTime = lastAudibleTime;
        init();
        registerTimeListener( downCounter );
        setVisible( true );
    }

    private void registerTimeListener( DownCounter downCounter )
    {
        downCounter.addListener( new Listener()
        {
            @Override
            public void timeRemaining( RemainingTime remainingTime )
            {
                String newTime = remainingTime.hours() + "h " + remainingTime.minutes() + "m";
                if ( timeLabel.getText() == null || timeLabel.getText().length() == 0 ||
                        !timeLabel.getText().equals( newTime ) )
                {
                    timeLabel.setText( newTime );
                }
            }
        } );
    }

    private void init()
    {
        getContentPane().setLayout( new FlowLayout() );
        
        JButton sayButton = new JButton( "Hur långt kvar?" );
        sayButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                lastAudibleTime.set( null );
            }
        } );
        getContentPane().add( sayButton );
        
        timeLabel = new JLabel();
        timeLabel.setFont( timeLabel.getFont().deriveFont( 50f ) );
        getContentPane().add( timeLabel );
        
        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                System.exit( 0 );
            }
        } );
        this.setBounds( 100, 100, 230, 200 );
    }
}

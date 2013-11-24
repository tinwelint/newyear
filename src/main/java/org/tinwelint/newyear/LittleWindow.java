package org.tinwelint.newyear;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.tinwelint.newyear.DownCounter.Listener;

public class LittleWindow extends JFrame
{
    private final AtomicReference<RemainingTime> lastAudibleTime;
    private JLabel timeLabel;
    private final DynamicConfiguration config;

    public LittleWindow( AtomicReference<RemainingTime> lastAudibleTime, DownCounter downCounter,
            DynamicConfiguration config )
    {
        super( "Snart e det nytt år" );
        this.lastAudibleTime = lastAudibleTime;
        this.config = config;
        init();
        registerTimeListener( downCounter );
    }
    
    public void showIt()
    {
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
        
        getContentPane().add( sayButton() );
        getContentPane().add( timeLabel = timeLabel() );
        getContentPane().add( crossfadeSlider() );
        
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        this.setBounds( 100, 100, 230, 200 );
    }

    private JSlider crossfadeSlider()
    {
        final JSlider crossfadeSlider = new JSlider( 0, 10, (int)(config.getSecondsCrossFade()*20) );
        crossfadeSlider.addChangeListener( new ChangeListener()
        {
            @Override
            public void stateChanged( ChangeEvent e )
            {
                config.setSecondsCrossFade( crossfadeSlider.getValue()/20.0f );
            }
        } );
        return crossfadeSlider;
    }

    private JLabel timeLabel()
    {
        JLabel label = new JLabel();
        label.setFont( label.getFont().deriveFont( 50f ) );
        return label;
    }

    private JButton sayButton()
    {
        JButton sayButton = new JButton( "Säg hur långt det är kvar till nyår" );
        sayButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                lastAudibleTime.set( null );
            }
        } );
        return sayButton;
    }
}

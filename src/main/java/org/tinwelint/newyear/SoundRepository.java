package org.tinwelint.newyear;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundRepository
{
    private static final FileFilter DIRECTORIES = new FileFilter()
    {
        @Override
        public boolean accept( File path )
        {
            return path.isDirectory();
        }
    };
    
    private interface StringConverter<TO>
    {
        TO convert( String string );
    }
    
    private static final StringConverter<Integer> NUMBERS = new StringConverter<Integer>()
    {
        @Override
        public Integer convert( String string )
        {
            return Integer.parseInt( string );
        }
    };
    
    private static final StringConverter<String> WORDS = new StringConverter<String>()
    {
        @Override
        public String convert( String string )
        {
            return string;
        }
    };
    
    private final Random random = new Random();
    private final Map<Integer,SoundSet> tenBasedSounds;
    private final Map<Integer,SoundSet> oneBasedSounds;
    private final Map<String,SoundSet> wordSounds;
    private final SoundSet missingSounds;
    
    public SoundRepository( File baseDirectory )
    {
        try
        {
            oneBasedSounds = loadSoundBank( new File( baseDirectory, "one" ), NUMBERS );
            tenBasedSounds = loadSoundBank( new File( baseDirectory, "ten" ), NUMBERS );
            wordSounds = loadSoundBank( new File( baseDirectory, "word" ), WORDS );
            missingSounds = loadSounds( new File( baseDirectory, "missing" ) );
        }
        catch ( LineUnavailableException | IOException | UnsupportedAudioFileException e )
        {
            throw new RuntimeException( e );
        }
    }
    
    private SoundSet loadSounds( File directory )
            throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        SoundSet sounds = new SoundSet( random );
        for ( File soundFile : directory.listFiles() )
        {
            sounds.add( new Sound( soundFile ) );
        }
        sounds.doneAdding();
        return sounds;
    }

    private <KEY> Map<KEY,SoundSet> loadSoundBank( File directory, StringConverter<KEY> converter )
            throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        Map<KEY,SoundSet> target = new HashMap<>();
        if ( !directory.exists() )
        {
            return target;
        }
        for ( File numberDirectory : directory.listFiles( DIRECTORIES ) )
        {
            KEY key = converter.convert( numberDirectory.getName() );
            target.put( key, loadSounds( numberDirectory ) );
        }
        return target;
    }

    public Sound tenBasedSound( int number ) // e.g. 3 ==> random sound in audio/repository/ten/3
    {
        return randomSound( tenBasedSounds, number, "ten based" );
    }

    public Sound oneBasedSound( int number ) // e.g. 5 ==> random sound in audio/repository/one/5
    {
        return randomSound( oneBasedSounds, number, "one based" );
    }

    public Sound word( String word ) // e.g. "hours" ==> random sound in audio/repository/word/hours/
    {
        return randomSound( wordSounds, word, "word" );
    }

    private <KEY> Sound randomSound( Map<KEY,SoundSet> source, KEY key, String description )
    {
        SoundSet sounds = source.get( key );
        Sound sound = sounds.random();
        if ( sound == null )
        {
            sound = missingSounds.random();
        }
        return sound;
    }
}

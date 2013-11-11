package org.tinwelint.newyear;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tinwelint.newyear.helpers.Function;

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
    
    private static final Function<File,Integer> NUMBERS = new Function<File,Integer>()
    {
        @Override
        public Integer apply( File from )
        {
            return Integer.parseInt( from.getName() );
        }
    };
    
    private static final Function<File,String> WORDS = new Function<File,String>()
    {
        @Override
        public String apply( File from )
        {
            return from.getName();
        }
    };
    
    private final Random random = new Random();
    private final Map<Integer,List<Sound>> tenBasedSounds;
    private final Map<Integer,List<Sound>> oneBasedSounds;
    private final Map<String,List<Sound>> wordSounds;
    private final List<Sound> missingSounds;
    
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
    
    private List<Sound> loadSounds( File directory )
            throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        List<Sound> sounds = new ArrayList<>();
        for ( File soundFile : directory.listFiles() )
        {
            sounds.add( loadSound( soundFile ) );
        }
        return sounds;
    }

    private <KEY> Map<KEY,List<Sound>> loadSoundBank( File directory, Function<File,KEY> converter )
            throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        Map<KEY,List<Sound>> target = new HashMap<>();
        if ( !directory.exists() )
        {
            return target;
        }
        for ( File numberDirectory : directory.listFiles( DIRECTORIES ) )
        {
            KEY key = converter.apply( numberDirectory );
            target.put( key, loadSounds( numberDirectory ) );
        }
        return target;
    }

    private Sound loadSound( File soundFile )
            throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        Sound sound = new Sound( soundFile );
        return sound;
    }

    public Sound tenBasedSound( int number ) // e.g. 3 ==> random sound in audio/repository/ten/3
    {
        return randomSound( tenBasedSounds, number, "ten based" );
    }

    public Sound oneBasedSound( int number ) // e.g. 11 ==> random sound in audio/repository/one/11
    {
        return randomSound( oneBasedSounds, number, "one based" );
    }

    public Sound word( String word ) // e.g. "hours" ==> random sound in audio/repository/word/hours/
    {
        return randomSound( wordSounds, word, "word" );
    }

    private <KEY> Sound randomSound( Map<KEY,List<Sound>> source, KEY key, String description )
    {
        List<Sound> sounds = source.get( key );
        if ( sounds == null || sounds.isEmpty() )
        {
            return randomSound( missingSounds );
        }
        return randomSound( sounds );
    }

    private Sound randomSound( List<Sound> sounds )
    {
        return sounds.get( random.nextInt( sounds.size() ) );
    }
}

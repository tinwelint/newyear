package org.tinwelint.newyear;

public class DynamicConfiguration
{
    private volatile float secondsCrossFade = 0.1f;
    
    public float getSecondsCrossFade()
    {
        return this.secondsCrossFade;
    }
    
    public void setSecondsCrossFade( float value )
    {
        this.secondsCrossFade = value;
    }
}

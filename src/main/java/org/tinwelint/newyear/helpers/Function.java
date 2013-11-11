package org.tinwelint.newyear.helpers;

public interface Function<FROM,TO>
{
    TO apply( FROM from );
}

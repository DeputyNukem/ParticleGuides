package me.monst.particleguides.configuration.values;

import me.monst.particleguides.configuration.ConfigurationValue;
import me.monst.particleguides.configuration.transform.IntegerTransformer;

public class GuideLength extends ConfigurationValue<Integer> {
    
    public GuideLength() {
        super("guide-length", 10, new IntegerTransformer().positive());
    }
    
}

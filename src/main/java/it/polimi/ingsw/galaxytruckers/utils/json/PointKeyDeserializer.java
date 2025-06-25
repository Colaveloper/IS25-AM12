package it.polimi.ingsw.galaxytruckers.utils.json;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        Matcher matcher = Pattern.compile("\\((\\d+),(\\d+)\\)").matcher(key);
        if (matcher.matches()) {
            return new Point(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        throw new IOException("Point key must be in the format '(x,y)' but was: " + key);
    }
}

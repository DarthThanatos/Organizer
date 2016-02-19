package vobis.example.com.organizer;

import java.util.HashMap;

public class ColorManager {

    String[] eventsNames = {"movies", "games", "music", "sport", "TV shows","Custom Event"};
    final String[] colorsValues = {"#AB0000", "#CD1234", "#555555", "#BBBBBB", "#999999","#ABCDEF"};
    final HashMap<String, String> colorsMap = new HashMap<String, String>();

    public ColorManager(){
        for (int i = 0; i < eventsNames.length; i++)
            colorsMap.put(eventsNames[i], colorsValues[i]);
    }

}

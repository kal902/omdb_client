package omdb_sdk;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mili
 */
public interface OMDBNotification {
    public List<OMDBNotificationListener> listeners = new ArrayList<OMDBNotificationListener>();
    public default void addListener(OMDBNotificationListener o){
        listeners.add(o);
    }
    
    public default void notify(String msg){
        for(OMDBNotificationListener l: listeners){
            l.onNotification(msg);
    }
    }
}
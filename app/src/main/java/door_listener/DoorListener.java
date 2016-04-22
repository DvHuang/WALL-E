package door_listener;

/**
 * Created by JTdavy on 2016/1/15.
 */
import java.util.EventListener;
public interface DoorListener extends EventListener
{
    public void doorEvent(DoorEvent event);
}

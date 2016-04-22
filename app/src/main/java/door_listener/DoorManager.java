package door_listener;

/**
 * Created by JTdavy on 2016/1/15.
 * 模拟实现事件源类DoorManager,用于触发事件和广播通知事件;它使用一个Collection类型的对象类存储所有注册进来的事件监听器对象;
 */
import java.util.*;
public class DoorManager
{
    private Collection listeners; //存储所有注册进来的事件监听器对象;
    public void addDoorListener(DoorListener listener) //把一个事件监听器对象注册进来;
    {
        if(this.listeners == null)
        {
            this.listeners = new HashSet();
        }
        this.listeners.add(listener);
    }
    public void removeDoorListener(DoorListener listener) //移除一个已经注册的事件监听器对象;
    {
        if(this.listeners != null)
        {
            this.listeners.remove(listener);
        }
    }
    protected void fireDoorOpened()  //触发开门事件
    {
        if(this.listeners != null)
        {
            DoorEvent event = new DoorEvent(this, "open");
            notifyListeners(event);
        }
    }
    protected void fireDoorClose()  //触发关门事件
    {
        if(this.listeners != null)
        {
            DoorEvent event = new DoorEvent(this, "close");
            notifyListeners(event);
        }
    }
    private void notifyListeners(DoorEvent event)
    {
        Iterator iter = this.listeners.iterator();
        while(iter.hasNext())
        {
            DoorListener listener = (DoorListener)iter.next();
            listener.doorEvent(event);
        }
    }
}

package door_listener;

/**
 * Created by JTdavy on 2016/1/15.
 * 事件状态对象作为单一的参数传递给对应的事件监听器对象的事件处理函数;
 首先,使用类EventObject作为父类来定义自己的事件类;
 然后,使用接口EventListener来实现自己的事件监听器类;
 最后就是注册这些事件以及事件监听器对象;
 */
import java.util.EventObject;
public class DoorEvent extends EventObject
{
    private String doorState = ""; //表示门的状态,取值"开"和"关";
    public DoorEvent(Object source, String doorState)
    {
        super(source);
        this.doorState = doorState;
    }
    public void setDoorState(String doorState)
    {
        this.doorState = doorState;
    }
    public String getDoorState()
    {
        return this.doorState;
    }
}

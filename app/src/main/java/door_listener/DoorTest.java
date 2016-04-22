package door_listener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.android.wali.R;
/**
 * Created by JTdavy on 2016/1/15.
 */
public class DoorTest extends Activity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);

        DoorManager manager = new DoorManager();
        manager.addDoorListener(new DoorListener1()); //给"门1"增加事件监听器;
        manager.addDoorListener(new DoorListener2()); //给"门2"增加事件监听器;
        manager.fireDoorOpened(); //开门;
        //System.out.println("我已经进来了");
        manager.fireDoorClose();  //关门;



    }

    public class DoorListener1 implements DoorListener
    {
        TextView word = (TextView) findViewById(R.id.word);



        public void doorEvent(DoorEvent event)
        {
            if(event.getDoorState() != null && event.getDoorState().equals("open"))
            {
                System.out.println("门1打开");
                word.setText("门1打开");

            }
            else
            {
                System.out.println("门1关闭");
                word.setText("门1关闭");

            }
        }
    }

    public class DoorListener2 implements DoorListener
    {

        TextView word = (TextView) findViewById(R.id.word);
        public void doorEvent(DoorEvent event)
        {
            if(event.getDoorState() != null && event.getDoorState().equals("open"))
            {
                System.out.println("门2打开,同时打开走廊里面的灯");
                word.setText("门2打开,同时打开走廊里面的灯");
            }
            else
            {
                System.out.println("门2关闭,同时关闭走廊里面的灯");
                word.setText("门2关闭,同时关闭走廊里面的灯");
            }
        }
    }

}

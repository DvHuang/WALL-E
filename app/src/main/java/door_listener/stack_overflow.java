package door_listener;

/**
 * Created by JTdavy on 2016/1/15.
 * stack_overflow an example about interface listener from stack over flow
 * 1.register
 * 2.liek_recyle method do something
 * 3.there is liek_recyle interface in the last method
 接口的作用---在开发者与使用者之间建立了一种协议
 开发者通过接口在上一个方法中定义了一个可以让使用者自己实现的回调函数名称，
 使用者想在某件事（就是这个方法）发生时，执行的那个函数，就是这个回调函数

 当拥有很多的监听器时，就需要有一个管理器来管理这些标志位，这时，就像door的例子一样，需要一个manager
 */
public class stack_overflow {

    /*Your Activity does nothing special, just register itself (since the interface is implemented directly in the class) with the Other class that provides the listener.

    public class MyActivity extends Activity implements InternetManager.Listener {

        private TextView mText;
        private InternetManager mInetMgr;

        *//* called just like onCreate at some point in time *//*
        public void onStateChange(boolean state) {
            if (state) {
                mText.setText("on");
            } else {
                mText.setText("off");
            }
        }

        public void onCreate() {
            mInetMgr = new InternetManager();
            mInetMgr.registerListener(this);
            mInetMgr.doYourWork();
        }
    }
    The other class has to do pretty much all the work. Besides that it has to handle the registration of listeners it has to call the onStateChange method once something happend.

    public class InternetManager {
        // all the listener stuff below
        public interface Listener {
            public void onStateChange(boolean state);
        }

        private Listener mListener = null;
        public void registerListener (Listener listener) {
            mListener = listener;
        }

        // -----------------------------
        // the part that this class does

        private boolean isInternetOn = false;
        public void doYourWork() {
            // do things here
            // at some point
            isInternetOn = true;
            // now notify if someone is interested.
            if (mListener != null)
                mListener.onStateChange(isInternetOn);
        }
    }*/
}

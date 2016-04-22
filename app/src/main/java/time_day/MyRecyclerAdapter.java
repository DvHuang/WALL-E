package time_day;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.wali.R;

import java.util.ArrayList;
import java.util.List;


public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<DayInfo> lists;
    private Context context;
    private List<Integer> heights;
    private OnItemClickListener mListener;
    public MyRecyclerAdapter(Context context,List<DayInfo> lists) {
        this.context = context;
        this.lists = lists;
        getRandomHeight(this.lists);
    }
    private void getRandomHeight(List<DayInfo> lists){//�õ����item�ĸ߶�
        heights = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {

            //heights.add((int)(200+Math.random()*400));
            heights.add((int)(300));
        }
    }
    public interface OnItemClickListener{
        void ItemClickListener(View view, int postion);
        void ItemLongClickListener(View view, int postion);
    }
    public void setOnClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_recyle_item,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        getRandomHeight(lists);
        ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//�õ�item��LayoutParams���ֲ���
        params.height = heights.get(position);//������ĸ߶ȸ���item����


        holder.itemView.setLayoutParams(params);//��params���ø�item����

        holder.period.setText(lists.get(position).getPeriod());//Ϊ�ؼ�������
        holder.work.setText(lists.get(position).work);
        holder.tips.setText(lists.get(position).tips);
        holder.Degree.setText(lists.get(position).Degree_of_completion);

        if(mListener!=null){//��������˼�����ô���Ͳ�Ϊ�գ�Ȼ��ص���Ӧ�ķ���
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                int pos = holder.getLayoutPosition();//�õ���ǰ���item��λ��pos
                mListener.ItemClickListener(holder.itemView,pos);//���¼���������ʵ�ֵĽӿ����ﴦ��
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();//�õ���ǰ���item��λ��pos
                    mListener.ItemLongClickListener(holder.itemView,pos);//���¼���������ʵ�ֵĽӿ����ﴦ��
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    TextView period;
    TextView work;
    TextView tips;
    TextView Degree;
    ImageButton Degree_button;

    public MyViewHolder(View itemView) {
        super(itemView);
        period = (TextView) itemView.findViewById(R.id.period);
        work = (TextView) itemView.findViewById(R.id.work);
        tips = (TextView) itemView.findViewById(R.id.tips);
        Degree = (TextView) itemView.findViewById(R.id.Degree);
        Degree_button=(ImageButton) itemView.findViewById(R.id.yes);

        Degree_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("Degree_button", "smile");



            }
        });


    }
}
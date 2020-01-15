package great.sachin.timer_upgraded;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class Adapter_For_ListItems extends ArrayAdapter {
    private List<DataModel> data;
    private final int layoutResource;
    private Context mContext;
    private PopupMenu popup;
    private Activity activity;
    private DatabaseHelperClass database;
    private String task_name;
    private int task_id;
    private interfaceToCallClass interfaceCall;
    public int myPosition;

    public Adapter_For_ListItems(@NonNull Activity activity,
                                 int resource, List<DataModel> data,
                                 interfaceToCallClass interfaceCall) {
        super(activity.getApplicationContext(), R.layout.activity_main4);
        this.layoutResource = resource;
        this.data = data;
        this.mContext = activity.getApplicationContext();
        this.activity = activity;
        this.database = new DatabaseHelperClass(mContext);
        this.interfaceCall = interfaceCall;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        myPosition = position;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Declaring the resources
            convertView = layoutInflater.inflate(R.layout.task_content, parent, false);
            viewHolder.menuImage = convertView.findViewById(R.id.menuImage);
            viewHolder.textView3 = convertView.findViewById(R.id.textView3);
            viewHolder.remainingTimeTask = convertView.findViewById(R.id.remainingTimeTask);
            viewHolder.totalTimeTask = convertView.findViewById(R.id.totalTimeTask);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Populating the views
        DataModel currentApp = data.get(position);
        viewHolder.textView3.setText(currentApp.getTaskName().toUpperCase());//All task in UpperCase
        viewHolder.remainingTimeTask.setText(timerTime(currentApp.getTimeLeft()));
        viewHolder.totalTimeTask.setText(timerTime(currentApp.getTotalTimeInSec()));

        viewHolder.menuImage.setImageResource(R.drawable.ic_delete_sweep_black_24dp);
        viewHolder.menuImage.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(activity, v);
            popup.setOnMenuItemClickListener((item) -> {
                if (item.getItemId() == R.id.deleteTask) {
                    Cursor dataCursor = database.getItemID(currentApp.getTaskName());
                //   Cursor String = database.getData().getString()
                    if(dataCursor !=null) {
                        //update the layout
                        interfaceCall.foo(position);
                        //Enter data into the cursor
                        dataCursor.moveToFirst();
                        //get task Id in column 0.
                        task_id = dataCursor.getInt(0);
                        //get task name from current app
                        task_name = currentApp.getTaskName();
                        //delete from the database
                        database.deleteDataFromDatabase(task_id, task_name);
                    }
                }

                return false;
            });
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.task_menu, popup.getMenu());
            popup.show();
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textView3;
        ImageView menuImage;
        TextView remainingTimeTask;
        TextView totalTimeTask;
    }
    private String timerTime(long counter){
        NumberFormat numberFormat = new DecimalFormat("00");
        long hour = ((counter / 3600) % 24);
        long minutes = ((counter / 60) % 60);
        long seconds = ((counter) % 60);
        return  numberFormat.format(hour) + ":" +
                numberFormat.format(minutes) + ":" +
                numberFormat.format(seconds);
    }
}


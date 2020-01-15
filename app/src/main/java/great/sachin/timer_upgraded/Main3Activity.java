package great.sachin.timer_upgraded;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

public class Main3Activity extends AppCompatActivity implements interfaceToCallClass, PopupMenu.OnMenuItemClickListener {
    private static final String TAG = "Main3Activity";
    private long mLastClickTime = 0;


    enum ActivityStatus {
        WAITING, STARTED, RUNNING, ENDED;
    }

    public ActivityStatus activityStatus = ActivityStatus.WAITING;
    private ImageView buttonAddToList;
    public ListView listViewOfItems;
    private ImageView imageView2;
    private boolean backButtonPressed = false;
    private ImageView authorInfoIcon;
    private ImageView deleteAllIcons;
    Menu deleteAllMenu;
    PopupMenu popUp;

    public List<DataModel> mModelArrayList = DataHolder.getInstance().listOfTasks;
    public Adapter_For_ListItems adapter;
    public DatabaseHelperClass databaseHelperClass;
    private Cursor data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("   Home");
        getSupportActionBar().setIcon(R.drawable.ic_home_black_24dp);

        databaseHelperClass = new DatabaseHelperClass(this);

        buttonAddToList = findViewById(R.id.ButtonAddToList);
        listViewOfItems = findViewById(R.id.listOfItems);
        imageView2 = findViewById(R.id.imageView2);
        imageView2.setImageResource(R.drawable.intersection_4);
        deleteAllIcons = findViewById(R.id.deleteAllIcon);
        authorInfoIcon = findViewById(R.id.AuthorInfoIcon);
        deleteAllIcons.setImageResource(R.drawable.ic_clear_all_black_24dp);

        authorInfoIcon.setOnClickListener(v->{
            Intent myInfo = new Intent(Main3Activity.this,Who_Am_I.class);
            startActivity(myInfo);
        });

        deleteAllIcons.setOnClickListener(v -> {
            showPopup(v);
        });

        /**
         * Getting data from database when process starts
         * */

        Intent intent = getIntent();
        String newTask = intent.getStringExtra("value");

        Main4Activity activity = new Main4Activity();

        activity.setStatus = (Main4Activity.SetMessageStatus) intent.getSerializableExtra("main");
        listViewOfItems.setOnItemClickListener((parent, view, position, id) -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent mainTimerIntent = new Intent(
                    Main3Activity.this, MainActivity.class);

            mainTimerIntent.putExtra("Updating_Timer", mModelArrayList.get(position).getTaskName());

            /**
             * I'm passing it opposite because I have called it like
             * In this the total time is time left
             * And time left is total Time
             */

            mainTimerIntent.putExtra("left_pass", mModelArrayList.get(position).getTimeLeft());
            mainTimerIntent.putExtra("total_pass", mModelArrayList.get(position).getTotalTimeInSec());
            startActivity(mainTimerIntent);
            mModelArrayList.clear();
            finish();
        });

        if ((activity.setStatus != Main4Activity.SetMessageStatus.NOSIGNAL) && (activity.setStatus != null)) {
            addNewTask(newTask);
            if (newTask != null) {
                data = databaseHelperClass.getData();
                data.moveToLast();

                DataModel model = new DataModel(data.getString(1));
                model.setTaskName(data.getString(1));//adding to the generic model
                mModelArrayList.add(model);
            }


        } else {
            if (!backButtonPressed) {
                data = databaseHelperClass.getData();
                showListOfItems();
            }


        }

        adapter = new Adapter_For_ListItems(
                Main3Activity.this, R.layout.task_content, mModelArrayList, this);

        listViewOfItems.setAdapter(adapter);
        addButtonClick();
    }

    @Override
    public void foo(int position) {
        mModelArrayList.remove(mModelArrayList.get(position));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateTimeLive(int position) {

//        adapter.notifyDataSetChanged();
    }


    public void addButtonClick() {
        buttonAddToList.setOnClickListener((v) -> {
            Intent EditTextIntent = new
                    Intent(Main3Activity.this, Main4Activity.class);
            startActivity(EditTextIntent);
            adapter.notifyDataSetChanged();

        });
    }

    /**
     * New Task Entry
     */
    public void addNewTask(String newEntry) {
        boolean insertTask = databaseHelperClass.addData(newEntry, 0, 0);

        if (insertTask) {
            toastMessage("New Task Added");
        } else
            toastMessage("Error 404");
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 1. Show all the data when the program start
     * 2. Show all data once and the added program
     */
    public void showListOfItems() {
        while (data.moveToNext()) {
            DataModel model = new DataModel(data.getString(1));
            model.setTaskName(data.getString(1));//adding to the generic model

            /***
             * In this the total time is time left
             * And time left is total Time
             */

            model.setTimeLeft(data.getLong(3));
            model.setTotalTimeInSec(data.getLong(2));
            mModelArrayList.add(model);
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        mModelArrayList.clear();
        super.onBackPressed();
    }

    public void deleteAllTask() {
        mModelArrayList.clear();
        adapter.notifyDataSetChanged();
    }

    public void showPopup(View v) {
        popUp = new PopupMenu(this, v);
        popUp.setOnMenuItemClickListener(this);
        popUp.inflate(R.menu.delete_all);
        popUp.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllTask:
                databaseHelperClass.deleteAllDataFromDatabase();
                deleteAllTask();
                return true;

            default:
                return false;
        }
    }

}

package great.sachin.timer_upgraded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Main4Activity extends AppCompatActivity {
    public enum SetMessageStatus {
        NOSIGNAL,INCOMING,OUTGOING,SENT,RECIEVED
    }

    SetMessageStatus setStatus = SetMessageStatus.NOSIGNAL;
    private EditText editTextForNewTask;
    private Button buttonToSaveTask;
    public String results;
    DatabaseHelperClass db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        editTextForNewTask = findViewById(R.id.editTextForNewTask);
        buttonToSaveTask = findViewById(R.id.buttonToSaveTask);
        buttonToSaveTaskClick();

        db = new DatabaseHelperClass(Main4Activity.this);
    }

    public void buttonToSaveTaskClick(){
        buttonToSaveTask.setOnClickListener((v) -> {
                results = editTextForNewTask.getText().toString();
            //Avoiding Repetation
            int count = 0;
            while(db.findTask(results.toUpperCase()).moveToNext()){
                count++;
                break;
            }
            if(count!=0){
                Snackbar.make(v, "Name Already Taken", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }else {
                if (!results.isEmpty()) {
                    Intent EditTextIntent = new
                            Intent(Main4Activity.this, Main3Activity.class);
                    setStatus = SetMessageStatus.INCOMING;
                    EditTextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    EditTextIntent.putExtra("main", setStatus);
                    EditTextIntent.putExtra("value", results);
                    startActivity(EditTextIntent);
                    finish();
                } else {
                    Snackbar.make(v, "No! No! You have to write first", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}

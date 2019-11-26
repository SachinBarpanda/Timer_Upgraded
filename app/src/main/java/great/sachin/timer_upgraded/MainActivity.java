package great.sachin.timer_upgraded;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.swifty.animateplaybutton.AnimatePlayButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

//import great.sachin.timer_upgraded.util.TempActivity;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "Main Activity";


    public enum TimerState {
        STOPPED, PAUSED, RUNNING, BUTTONPLRESSED
    }

    public enum ExtraState {
        EXTRAPRESSED, EXTRANOTPRESSED
    }

    /**
     * The time for the counter are all set by the user so its a temporary one for now
     */

    public static CountDownTimer timer;

    public long counter;
    public TimerState timerState = TimerState.STOPPED;
    public ExtraState extraState = ExtraState.EXTRANOTPRESSED;
    private TextView displayTimer;
    Handler handler = new Handler();
    private ProgressBar progress_bar;
    private Button buttonSetTime;
    private int totalTimeForUI;
    private int extraTimeUI;
    private long totalTimeInSeconds;
    private MediaPlayer notificationSound;

    /**
     * Updating the UI for material progressbar
     */
    private void updateUI() {
        progress_bar = findViewById(R.id.progress_countdown);
        if (extraState == ExtraState.EXTRAPRESSED) {
            totalTimeForUI = totalTimeForUI + extraTimeUI;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progress_bar.setMax(totalTimeForUI);
                        progress_bar.setProgress((int) counter);
                    }
                });

            }
        }).start();
    }

//    TempActivity tempActivity = new TempActivity();

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//
    //    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (extraState == ExtraState.EXTRAPRESSED) {
            if (timerState == TimerState.RUNNING) {
                pauseTimer();
                int hours = hourOfDay * 3600;
                int minutes = minute * 60;
                long ExtraTimeInSeconds = (hours + minutes);
                extraTimeUI = (int) ExtraTimeInSeconds;
                counter = counter + ExtraTimeInSeconds;
                updateUI();
                startTimer();
                timerState = TimerState.RUNNING;
                extraState = ExtraState.EXTRANOTPRESSED;
            }
            int hours = hourOfDay * 3600;
            int minutes = minute * 60;
            long ExtraTimeInSeconds = (hours + minutes);
            extraTimeUI = (int) ExtraTimeInSeconds;

            counter = counter + ExtraTimeInSeconds;
            updateUI();
            extraState = ExtraState.EXTRANOTPRESSED;

        } else {
//            if (hourOfDay == 0 && minute == 0) {
//                timer.cancel();
//            } else {
                int hours = hourOfDay * 3600;
                int minutes = minute * 60;

                totalTimeInSeconds = (hours + minutes);
                totalTimeForUI = (int) totalTimeInSeconds;
                counter = totalTimeInSeconds;
//            }
        }
    }

    private void startTimer() {
        if (timerState == TimerState.PAUSED) {
            timer = new CountDownTimer(counter * 1000, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long millisUntilFinished) {
                    counter = millisUntilFinished / 1000;
                    displayTimer = findViewById(R.id.textViewTimer);

                    NumberFormat numberFormat = new DecimalFormat("00");
                    long hour = ((counter / 3600) % 24);
                    long minutes = ((counter / 60) % 60);
                    long seconds = ((counter) % 60);
                    displayTimer.setText(numberFormat.format(hour) + ":" +
                            numberFormat.format(minutes) + ":" +
                            numberFormat.format(seconds));
                    updateUI();
                }

                @Override
                public void onFinish() {
                    counter = 0;
                    displayTimer.setText("00:00:00");
                    //updateUI(counter);

                }
            }.start();

        } else {
            try {
                timer = new CountDownTimer(counter * 1000, 1000) {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {


                        counter = millisUntilFinished / 1000;
                        displayTimer = findViewById(R.id.textViewTimer);

                        NumberFormat numberFormat = new DecimalFormat("00");
                        long hour = ((counter / 3600) % 24);
                        long minutes = ((counter / 60) % 60);
                        long seconds = ((counter) % 60);
                        displayTimer.setText(numberFormat.format(hour) + ":" +
                                numberFormat.format(minutes) + ":" +
                                numberFormat.format(seconds));
                        updateUI();
                    }

                    @Override
                    public void onFinish() {
                        counter = 0;
                        displayTimer.setText("00:00:00");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    notificationSound.start();
                                    Thread.sleep(4000);
                                } catch (InterruptedException e) {
                                    e.getMessage();
                                }
                            }
                        }).start();
                    }

                }.start();
            } catch (NullPointerException e) {
                e.getMessage();
            }
        }
    }

    private void pauseTimer() {
        timerState = TimerState.PAUSED;
        timer.cancel();
    }

    private void stopTimer() {
        timerState = TimerState.STOPPED;
        timer.cancel();
        counter = 0;
        updateUI();
        displayTimer.setText("00:00:00");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("        Timer");
        getSupportActionBar().setIcon(R.drawable.ic_access_time_black_24dp);

        notificationSound = MediaPlayer.create(MainActivity.this, R.raw.notification_up);

        //notificationSound  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /**
         * Button for setting the time
         * */


        buttonSetTime = findViewById(R.id.buttonSetTime);
        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerState == TimerState.STOPPED || timerState ==TimerState.BUTTONPLRESSED) {
                    DialogFragment timePicker = new TimePickerfragment();
                    timePicker.show(getSupportFragmentManager(), "Time-Picker");
                    timerState = TimerState.BUTTONPLRESSED;
                } else {
                    Snackbar.make(v, "Please Stop The Timer First", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        AnimatePlayButton animatePlayButton = findViewById(R.id.playPauseButton);
        animatePlayButton.
                setPlayListener(new AnimatePlayButton.OnButtonsListener() {
                    @Override
                    public boolean onPlayClick(View view) {
                        if (timerState == TimerState.BUTTONPLRESSED
                                && totalTimeInSeconds!=0) {
                            //starting the timer
                            startTimer();
                            //State Changed
                            timerState = TimerState.RUNNING;
                            //Buttons are updated
                            return true;
                        } else {
                            Snackbar.make(view, "Please Enter a time", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return false;
                        }
                    }

                    @Override
                    public boolean onPauseClick(View view) {
                        //TODO: Stop the timer

                        if(totalTimeInSeconds!=0){
                        pauseTimer();
                        return true;
                        }
                        else
                            timer.onFinish();
                        return false;
                    }

                    @Override
                    public boolean onResumeClick(View view) {
                        startTimer();
                        timerState = TimerState.RUNNING;
                        return true;
                    }

                    @Override
                    public boolean onStopClick(View view) {
                        stopTimer();
                        return true;
                    }
                });

        FloatingActionButton fab = findViewById(R.id.fab_Add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerfragment();
                timePicker.show(getSupportFragmentManager(), "Time-Picker");
                extraState = ExtraState.EXTRAPRESSED;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
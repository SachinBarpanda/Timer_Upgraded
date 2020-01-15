package great.sachin.timer_upgraded;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    final List<DataModel> listOfTasks = new ArrayList<>();

    private DataHolder(){

    }
    static  DataHolder getInstance(){
        if(instance==null){
            instance = new DataHolder();
        }
        return instance;
    }
    private static DataHolder instance;
}

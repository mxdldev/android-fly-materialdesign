package com.yesway.calendardemo.control;

import android.os.AsyncTask;
import android.os.SystemClock;

import com.yesway.calendardemo.model.DayEvent;
import com.yesway.calendardemo.model.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * daemon request to get events
 *
 * @author wl
 * @since 2016/08/30 16:11
 */
public class GetDecorsTask extends AsyncTask<Integer, Void, List<DayEvent>> {
    private DecorResult callback;

    public GetDecorsTask(DecorResult callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        if(callback != null)
            callback.onStart();
    }

    @Override
    protected List<DayEvent> doInBackground(Integer... params) {
        int year = params[0];
        ArrayList<DayEvent> yearEvents = new ArrayList<>();
        if(year == 2016) {
            DayEvent dayEvent1 = new DayEvent(year, 3, 14, EventType.EAT, new String[]{"a big turkey", "picnic"});
            DayEvent dayEvent2 = new DayEvent(year, 3, 15, EventType.ENTERTAINMENT, new String[]{"play VR game", "watch movie"});
            DayEvent dayEvent3 = new DayEvent(year, 6, 25, EventType.BEAUTY, new String[]{"Yoga lesson"});
            DayEvent dayEvent4 = new DayEvent(year, 8, 13, EventType.SPORT, new String[]{"swimming match"});
            DayEvent dayEvent5 = new DayEvent(year, 11, 30, EventType.SPORT, new String[]{"play basketball", "i don't like football", "ping pang!"});

            yearEvents.add(dayEvent1);
            yearEvents.add(dayEvent2);
            yearEvents.add(dayEvent3);
            yearEvents.add(dayEvent4);
            yearEvents.add(dayEvent5);
        }

        SystemClock.sleep(1000);
        return yearEvents;
    }

    @Override
    protected void onPostExecute(List<DayEvent> dayEvents) {
        if(callback != null)
            callback.onResult(dayEvents);
    }

    public interface DecorResult {
        void onStart();
        void onResult(List<DayEvent> events);
    }
}

package com.example.abilashr.saikailashooty.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.abilashr.saikailashooty.R;
import com.example.abilashr.saikailashooty.data.DataContract;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReminderFirebaseJobService extends JobService {
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {

        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context context = ReminderFirebaseJobService.this;
                ReminderTasks.executeTask(context);


                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}

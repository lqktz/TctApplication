package com.lq.tct.tctapplication;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private ComponentName jobService;
    private static final String TAG = "MyJobService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jobService = new ComponentName(this, MyJobService.class);
        Intent service = new Intent(this, MyJobService.class);
        startService(service);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick");
                pollServer();
            }
        });
    }

    private void pollServer() {

        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo jobInfo = new JobInfo.Builder(10087, jobService) //任务Id等于123
                .setMinimumLatency(12345)// 任务最少延迟时间
                .setOverrideDeadline(60000)// 任务deadline，当到期没达到指定条件也会开始执行
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)// 网络条件，默认值NETWORK_TYPE_NONE
                .setRequiresCharging(true)// 是否充电
                .setRequiresDeviceIdle(false)// 设备是否空闲
                .setPersisted(true) //设备重启后是否继续执行
                .setBackoffCriteria(3000,JobInfo.BACKOFF_POLICY_LINEAR) //设置退避/重试策略
                .build();

        Log.d(TAG,"scheduler.schedule");
        scheduler.schedule(jobInfo);
    }
}
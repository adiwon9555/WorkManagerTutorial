package com.example.workmanagertutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class WorkManagerStartReceiver : BroadcastReceiver() {
    companion object{
        private const val TAG = "WorkManagerStartReceive"
    }
    var mWorkManager: WorkManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "BOOT COMPLETED", Toast.LENGTH_LONG).show()
            Log.d(TAG, "onReceive: BOOT COMPLETED")
            
        }
        Log.d(TAG, "onReceive outside: BOOT COMPLETED")
        setPeriodicWorkRequest(context!!)
    }
    fun setPeriodicWorkRequest(context: Context){
        val workManager = WorkManager.getInstance(context)
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadRequestWorker::class.java,16,TimeUnit.MINUTES)
                .build()
        workManager.enqueue(periodicWorkRequest)
    }
}
package com.example.workmanagertutorial

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

//WorkManager requires worker to do a particular task
class FilterRequestWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext,workerParams) {

    companion object{
        private const val TAG = "FilterRequestWorker"
    }
    override fun doWork(): Result {
        try {
            for (i in 1 until 600){
                Log.d(TAG, "doWork: "+i)
            }
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }


    }
}
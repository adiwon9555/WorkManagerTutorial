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
class UploadRequestWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext,workerParams) {

    companion object{
        private const val TAG = "UploadRequestWorker"
        const val KEY_WORKER_OUTPUT = "KEY_WORKER_OUTPUT"
    }
    override fun doWork(): Result {
        try {
            val count = inputData.getInt(MainActivity.KEY_DATA_INPUT,600)
            for (i in 1 until count){
                Log.d(TAG, "doWork: "+i)
            }
            val time = SimpleDateFormat("dd/MM/yyyy hh:mm:ss",Locale.ENGLISH)
            val currentDate = time.format(Date())
            val outputData = Data.Builder()
                    .putString(KEY_WORKER_OUTPUT,currentDate)
                    .build()

            return Result.success(outputData)
        }catch (e: Exception){
            return Result.failure()
        }


    }
}
package com.example.workmanagertutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "MainActivity"
        const val KEY_DATA_INPUT = "KEY_DATA_INPUT"
    }
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
    }

    fun startOneTimeWork(view: View) {
        setOneTimeWorkRequest()
    }

    private fun setOneTimeWorkRequest(){
        val workManager = WorkManager.getInstance(applicationContext)
        //Simple schedule of task
//        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadRequestWorker::class.java)
//                .build()


        //Task with constraints
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
//        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadRequestWorker::class.java)
//                .setConstraints(constraints)
//                .build()

        //Send and recieve data
        val data = Data.Builder()
                .putInt(KEY_DATA_INPUT,800)
                .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadRequestWorker::class.java)
                .setConstraints(constraints)
                .setInputData(data)
                .build()


//        workManager.enqueue(oneTimeWorkRequest)

        //Subsequent Chaining of worker
        val filterWorkRequest = OneTimeWorkRequest.Builder(FilterRequestWorker::class.java)
                .setConstraints(constraints)
                .build()

        val compressionWorkRequest = OneTimeWorkRequest.Builder(CompressionRequestWorker::class.java)
                .setConstraints(constraints)
                .build()

//        workManager.beginWith(filterWorkRequest)
//                .then(compressionWorkRequest)
//                .then(oneTimeWorkRequest)
//                .enqueue()

        //parallel chaining of worker
        val downloadWorkRequest = OneTimeWorkRequest.Builder(DownloadRequestWorker::class.java)
                .setConstraints(constraints)
                .build()

        val parallelWorks = mutableListOf(
                filterWorkRequest,
                downloadWorkRequest
        )

        workManager.beginWith(parallelWorks)
                .then(compressionWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue()


        //Get Status
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                .observe(this, Observer {
                    //There are for states -> Blocked, Enqueued, Running and Succceded -> For state
                    textView.text = it.state.name

                    //For Output Data
                    if(it.state.isFinished){
                        val outputData = it.outputData.getString(UploadRequestWorker.KEY_WORKER_OUTPUT)
                        Toast.makeText(this,outputData,Toast.LENGTH_SHORT).show()
                    }
                })
    }
    private fun setPeriodicWorkRequest(){
        val workManager = WorkManager.getInstance(applicationContext)
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadRequestWorker::class.java,16,TimeUnit.MINUTES)
                .build()
        workManager.enqueue(periodicWorkRequest)
    }

    fun startPeriodicWork(view: View) {
        setPeriodicWorkRequest()
    }
}
package com.nprmanbrandons11.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.nprmanbrandons11.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var  binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            createNewWorker("user",1, this)
        }
    }
    fun createNewWorker(name:String,index:Int,context: Context){
        val workerDocuments = OneTimeWorkRequestBuilder<DocumentWorker>()
        val data = workDataOf(
            DocumentWorker.KEY_USUARIO to name,
            DocumentWorker.KEY_ID_NOTIFICATION to index,

        )
        workerDocuments.setInputData(data)
        val workManager = WorkManager.getInstance(context)
        val continuation = workManager.beginUniqueWork("WORKER_PHOTO_photo.jpg",ExistingWorkPolicy.REPLACE,workerDocuments.build())
        continuation.enqueue()
        continuation.workInfosLiveData.observe(this) {list->
            list.forEach {
                if(it.state.isFinished)
                    Glide.with(this).load(it.outputData.getString("WORKER_RESULT")).into(binding.imageView)
            }
        }

    }
}
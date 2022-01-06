package com.dicoding.habitapp.ui.countdown

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.Habit
import com.dicoding.habitapp.notification.NotificationWorker
import com.dicoding.habitapp.utils.HABIT
import com.dicoding.habitapp.utils.HABIT_ID
import com.dicoding.habitapp.utils.HABIT_TITLE

class CountDownActivity : AppCompatActivity() {
    private lateinit var workManager: WorkManager
    private lateinit var oneTimeRequest: OneTimeWorkRequest
    private var startCountDown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"
        workManager = WorkManager.getInstance(this)

        val habit = intent.getParcelableExtra<Habit>(HABIT) as Habit

        findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

        val viewModel = ViewModelProvider(this)[CountDownViewModel::class.java]

        //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished DONE
        viewModel.setInitialTime(habit.minutesFocus)
        viewModel.currentTimeString.observe(this, { time ->
            findViewById<TextView>(R.id.tv_count_down).text = time
        })
        //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up. DONE

        viewModel.eventCountDownFinish.observe(this, { status ->
            updateButtonState(!status)
            if (status && startCountDown) {
                startOneTimeRequestWork(habit)
            }
        })

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            viewModel.startTimer()
            updateButtonState(true)
            startCountDown = true
        }

        findViewById<Button>(R.id.btn_stop).setOnClickListener {
            viewModel.resetTimer()
            startCountDown = false
            cancelWorkManager()
        }
    }

    private fun startOneTimeRequestWork(habit: Habit) {
        val data = Data.Builder()
            .putInt(HABIT_ID, habit.id)
            .putString(HABIT_TITLE, habit.title).build()

        oneTimeRequest =
            OneTimeWorkRequest.Builder(NotificationWorker::class.java).setInputData(data).build()

        workManager.enqueue(oneTimeRequest)
        workManager.getWorkInfoByIdLiveData(oneTimeRequest.id).observe(this, { workInfo ->
            val status = workInfo.state.name
            if (workInfo.state == WorkInfo.State.ENQUEUED) {
                Log.d(TAG, "Notification has been enqueued, this is the status: $status")
            } else if (workInfo.state == WorkInfo.State.CANCELLED) {
                Log.d(TAG, "Notification cancelled")
            }
        })
    }

    private fun cancelWorkManager() {
        try {
            workManager.cancelWorkById(oneTimeRequest.id)
            Log.d(TAG, "Cancelling WorkManager is succeed")
        } catch (e: Exception) {
            Log.d(TAG, "Canceling failed because : ${e.message}")
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }

    companion object {
        const val TAG = "COUNTDOWNACTIVITY"
    }
}
package com.dicoding.todoapp.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskActivity
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var detailActivityViewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val taskId = intent.getIntExtra(TASK_ID, 0)
        val textTitle = findViewById<TextInputEditText>(R.id.detail_ed_title)
        val description = findViewById<TextInputEditText>(R.id.detail_ed_description)
        val dueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)

        val factory = ViewModelFactory.getInstance(this)
        detailActivityViewModel =
            ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)
        detailActivityViewModel.setTaskId(taskId)

        //TODO 11 : Show detail task and implement delete action //DONE

        detailActivityViewModel.task.observe(this, {
            if (it != null) {
                textTitle.setText(it.title)
                description.setText(it.description)
                dueDate.setText(DateConverter.convertMillisToString(it.dueDateMillis))
            }
        })

        //DELETE ACTION
        findViewById<Button>(R.id.btn_delete_task).setOnClickListener {
            detailActivityViewModel.deleteTask()
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }
}
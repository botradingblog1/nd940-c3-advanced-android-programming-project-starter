package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.ViewModelProvider
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {
    private lateinit var motionLayout: MotionLayout
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        //motionLayout = findViewById(R.id.motion_layout)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        motionLayout = binding.motionLayout

        val viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)

        // Get extras from activity
        val fileName = intent.getStringExtra("file")
        val status = intent.getStringExtra("status")

        viewModel.setFile(fileName!!)
        viewModel.setStatus(status!!)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        binding.root
    }

    override fun onResume() {
        super.onResume()

        startTransitions()
    }

    // Fly in fields from the left
    private fun startTransitions() {
        motionLayout.transitionToState(R.id.end_state)
    }

}

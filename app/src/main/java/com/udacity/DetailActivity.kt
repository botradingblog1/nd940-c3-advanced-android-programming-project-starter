package com.udacity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var motionLayout: MotionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        motionLayout = findViewById(R.id.motion_layout)

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

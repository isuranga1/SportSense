package com.google.mediapipe.examples.poselandmarker.techniques
import android.view.MotionEvent
import androidx.core.content.ContextCompat


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.mediapipe.examples.poselandmarker.ActivityVideo
import com.google.mediapipe.examples.poselandmarker.FirebaseManager.fetchCollectionNames
import com.google.mediapipe.examples.poselandmarker.R

private var technique = "no"
private var sport = "no"

class ActivitySprint : AppCompatActivity() {
    private lateinit var buttonContainer: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {

        sport = intent.getStringExtra("EXTRA_MESSAGE") ?: "No message"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sprint)

        buttonContainer = findViewById(R.id.buttonContainer)
        loadButtonsFromFirestore()


    }

    private fun callActivity() {


        Intent(this, ActivityVideo::class.java).also {
            it.putExtra("EXTRA_TECHNIQUE", technique)
            it.putExtra("EXTRA_MESSAGE", sport)


            startActivity(it)
        }
    }

    private fun loadButtonsFromFirestore() {
        fetchCollectionNames(sport) { collectionNames ->
            buttonContainer.removeAllViews() // Clear existing buttons

            for (collectionName in collectionNames) {
                val button = Button(this).apply {
                    text = collectionName
                    setTextColor(ContextCompat.getColor(context, R.color.white))
                    textSize = 18f
                    setPadding(8, 8, 8, 8) // Adjust padding if necessary
                    setBackgroundResource(R.drawable.barefoot) // Use your drawable resource here

                    // Set the OnClickListener to handle the button click
                    setOnClickListener {
                        technique = (it as Button).text.toString()
                        callActivity()
                    }

                    // Add the OnTouchListener for the scaling effect
                    setOnTouchListener { v, event ->
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                v.scaleX = 0.95f
                                v.scaleY = 0.95f
                            }
                            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                v.scaleX = 1f
                                v.scaleY = 1f
                            }
                        }
                        false
                    }
                }

                // Set button size programmatically
                val layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.button_width),
                    resources.getDimensionPixelSize(R.dimen.button_height)
                ).apply {
                    // Set margins (top, right, bottom, left)
                    setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.button_margin))
                }
                button.layoutParams = layoutParams

                buttonContainer.addView(button)
            }
        }
    }



}
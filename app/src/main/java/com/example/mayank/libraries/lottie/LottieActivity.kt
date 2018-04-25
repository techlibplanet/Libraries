package com.example.mayank.libraries.lottie

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import android.animation.ValueAnimator
import android.os.Handler
import com.example.mayank.libraries.Constants.showLogDebug
import com.example.mayank.libraries.R


class LottieActivity : AppCompatActivity() {

    private val TAG = LottieActivity::class.java.simpleName

    private var animationViewCheckedDone: LottieAnimationView? = null
    private var animationViewSwing: LottieAnimationView? = null
    private var animationViewClock : LottieAnimationView? = null
    private var animationExplodingHeart : LottieAnimationView? = null
    private var animationPlane : LottieAnimationView? = null
    private var animationMotorcycle : LottieAnimationView? = null
    private var animationWorldLocations : LottieAnimationView? = null
    private var animationLocationPin : LottieAnimationView? = null

    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie)

        animationViewCheckedDone = findViewById<LottieAnimationView>(R.id.lottieAnimationViewCheckedDone)
        animationViewSwing = findViewById<LottieAnimationView>(R.id.lottieAnimationViewSwing)
        animationViewClock = findViewById<LottieAnimationView>(R.id.lottieAnimationViewClock)

        animationPlane = findViewById<LottieAnimationView>(R.id.lottieAnimationViewPlane)
        animationMotorcycle = findViewById<LottieAnimationView>(R.id.lottieAnimationViewMotor)

        animationWorldLocations = findViewById<LottieAnimationView>(R.id.lottieAnimationWorldLocation)

        animationLocationPin = findViewById<LottieAnimationView>(R.id.lottieAnimationLocationPin)

        animationExplodingHeart = findViewById<LottieAnimationView>(R.id.lottieAnimationViewExplodingHeart)


        handler = Handler()

        animationViewSwing?.setOnClickListener(View.OnClickListener {
            startSwingAnimation()
        })

        animationLocationPin?.setOnClickListener(View.OnClickListener {
            startLocationPinAnimation()
        })

        animationPlane?.setOnClickListener(View.OnClickListener {
            startPlaneAnimation()
        })

        animationViewCheckedDone?.setOnClickListener(View.OnClickListener {
            startCheckAnimation()
        })

        animationExplodingHeart?.setOnClickListener(View.OnClickListener {
            startExplodingHeart()
        })

        animationViewClock?.setOnClickListener(View.OnClickListener {
            startClockAnimation()
        })

        animationMotorcycle?.setOnClickListener(View.OnClickListener {
            startMotorcycleAnimation()
        })


        animationWorldLocations?.setOnClickListener(View.OnClickListener {
            startWorldLocationAnimation()
        })

//        handler.post(runnableCode)




    }




    private fun startExplodingHeart() {
        showLogDebug(TAG, "Inside Exploding Heart")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000)
        animator.addUpdateListener { valueAnimator -> animationExplodingHeart?.progress = valueAnimator.animatedValue as Float }

        if (animationExplodingHeart?.progress === 0f) {
            animator.start()
        } else {
            animationExplodingHeart?.progress = 0f
        }
    }

//    private val runnableCode = Runnable {
//        // Do something here on the main thread
//        showLogDebug("Handlers", "Called on main thread")
////        createList()
////        startSwingAnimation()
////        startCheckAnimation()
//        startClockAnimation()
//
//    }

    private fun startSwingAnimation(){
        showLogDebug(TAG, "Inside Swing Animation")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000)
        animator.addUpdateListener { valueAnimator -> animationViewSwing?.progress = valueAnimator.animatedValue as Float }

        if (animationViewSwing?.progress === 0f) {
            animator.start()
        } else {
            animationViewSwing?.progress = 0f
        }
//        handler.postDelayed(runnableCode, 1100)
    }

    private fun startCheckAnimation() {
        showLogDebug(TAG, "Inside Start check Animation")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(1000)
        animator.addUpdateListener { valueAnimator -> animationViewCheckedDone?.progress = valueAnimator.animatedValue as Float }
        animator.start()

//        handler.postDelayed(runnableCode, 3000)



        if (animationViewCheckedDone?.progress === 0f) {
            animator.start()
        } else {
            animationViewCheckedDone?.progress = 0f
        }
    }

    private fun startClockAnimation() {
        showLogDebug(TAG, "Inside Clock Animation")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(10000)
        animator.addUpdateListener { valueAnimator -> animationViewClock?.progress = valueAnimator.animatedValue as Float }
        animator.start()

//        handler.postDelayed(runnableCode, 11000)



        if (animationViewClock?.progress === 0f) {
            animator.start()
        } else {
            animationViewClock?.progress = 0f
        }
    }

    private fun startPlaneAnimation() {
        showLogDebug(TAG, "Inside Start Plane Animation")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(5000)
        animator.addUpdateListener { valueAnimator -> animationPlane?.progress = valueAnimator.animatedValue as Float }
        animator.start()

//        handler.postDelayed(runnableCode, 11000)



        if (animationPlane?.progress === 0f) {
            animator.start()
        } else {
            animationPlane?.progress = 0f
        }
    }

    private fun startMotorcycleAnimation() {
        showLogDebug(TAG, "Inside Motorcycle Animation")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(5000)
        animator.addUpdateListener { valueAnimator -> animationMotorcycle?.progress = valueAnimator.animatedValue as Float }
        animator.start()

        if (animationMotorcycle?.progress === 0f) {
            animator.start()
        } else {
            animationMotorcycle?.progress = 0f
        }
    }

    private fun startWorldLocationAnimation() {
        showLogDebug(TAG, "Inside Start World Location")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(5000)
        animator.addUpdateListener { valueAnimator -> animationWorldLocations?.progress = valueAnimator.animatedValue as Float }
        animator.start()

        if (animationWorldLocations?.progress === 0f) {
            animator.start()
        } else {
            animationWorldLocations?.progress = 0f
        }
    }

    private fun startLocationPinAnimation() {
        showLogDebug(TAG, "Inside Location Pin Animation")
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(5000)
        animator.addUpdateListener { valueAnimator -> animationLocationPin?.progress = valueAnimator.animatedValue as Float }
        animator.start()

        if (animationLocationPin?.progress === 0f) {
            animator.start()
        } else {
            animationLocationPin?.progress = 0f
        }
    }


}

package com.example.headsupgame

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Surface
import android.view.View
import androidx.core.view.isVisible
import com.example.headsupgame.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    private  val  START_TIME_IN_MILLIS : Long = 60000
    var startGame = false
    var celebrity = 0
    var time : Long = START_TIME_IN_MILLIS
    var arrayCelebrity : ArrayList<Celebrity> = ArrayList()
    lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCelebrity()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val rotation = windowManager.defaultDisplay.rotation
        if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
            if(startGame){
                celebrity++
                newRound(celebrity)
               //stop
                timer.cancel()
                updateStatus(false)
            }else{
                updateStatus(false)
            }
        }else{

            if(startGame){
                updateStatus(true)
                //restar
                timer.onTick(time)
                Log.e("time R", time.toString())
                timer.start()
            }else{
                updateStatus(false)
            }
        }
    }

    fun startGame(view: View) {
        newRound(celebrity)
        newTimer()
    }

    private fun newTimer() {
        if (!startGame) {
            startGame = true
            binding.tvText.text = "Please Rotate Device"
            binding.btStart.isVisible = false
            val rotate = windowManager.defaultDisplay.rotation
            if (rotate == Surface.ROTATION_180 || rotate == Surface.ROTATION_0) {
                updateStatus(false)

            } else
                updateStatus(true)


            timer = object : CountDownTimer(time, 1000) {
                override fun onTick(p0: Long) {
                    time = p0
                    Log.e("time",time.toString())
                    binding.tvTime.text = "Time ${time / 1000}"
                }

                override fun onFinish() {
                    startGame = false
                    binding.tvTime.text = "Time: --"
                    binding.tvText.text = "Heads Up!"
                    binding.btStart.isVisible = true
                    updateStatus(false)
                }

            }.start()
        }
    }

    private fun newRound(celebrityNum : Int) {

        if (celebrityNum < arrayCelebrity.size){
            binding.tvName.text = arrayCelebrity[celebrityNum].name
            binding.tvToo1.text = arrayCelebrity[celebrityNum].taboo1
            binding.tvToo2.text = arrayCelebrity[celebrityNum].taboo2
            binding.tvToo3.text = arrayCelebrity[celebrityNum].taboo3
        }
    }


    private fun getCelebrity() {
        val apiInterface = APIClient().celebrity()?.create(APIInterface :: class.java)
        apiInterface?.getAllCelebrities()?.enqueue(object : Callback<ArrayList<Celebrity>>{
            override fun onResponse(
                call: Call<ArrayList<Celebrity>>,
                response: Response<ArrayList<Celebrity>>
            ) {
                arrayCelebrity = response.body()!!
                arrayCelebrity.shuffle()
            }

            override fun onFailure(call: Call<ArrayList<Celebrity>>, t: Throwable) {
                Log.e("onFailure getCelebrity", t.localizedMessage)
            }

        })
    }


    private fun updateStatus(showCelebrity: Boolean){

        if(showCelebrity){
            binding.lyGame.isVisible = true
            binding.lyStart.isVisible = false
        }else{
            binding.lyGame.isVisible = false
            binding.lyStart.isVisible = true
        }
    }
}
package id.co.edtslib.tracker.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.co.edtslib.tracker.Tracker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Tracker.checkInstallReferrer(this)


    }

    override fun onResume() {
        super.onResume()
        //Tracker.resumePage("testlib8", "testlibaja8")
        Tracker.trackPage("testlib11", "testlib11")
        Handler(Looper.myLooper()!!).postDelayed({
            Tracker.trackPage("testlib12", "testlib12")
        }, 3000)
    }
}
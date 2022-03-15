package id.co.edtslib.tracker.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.co.edtslib.tracker.Tracker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Tracker.checkInstallReferrer(this)

        Tracker.setUserId(8888)
        Tracker.trackPage("test lib")
    }
}
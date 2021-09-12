package id.co.edtslib.tracker.example

import android.app.Application
import id.co.edtslib.tracker.Tracker

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        Tracker.init(this,"https://asia-southeast2-idm-corp-dev.cloudfunctions.net",
            "fT2vJnJu4dsxTRMphdHE3Z92uwjaBRztGR3ECdRQTEyDDZJGbvGu")
    }
}
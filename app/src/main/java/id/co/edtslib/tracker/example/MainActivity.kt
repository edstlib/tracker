package id.co.edtslib.tracker.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import id.co.edtslib.edtsds.list.menu.MenuListView
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.data.TrackerFilterDetail

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Tracker.checkInstallReferrer(this)

        val filter = mutableListOf<TrackerFilterDetail>()
        val tracker = TrackerFilterDetail(
            "Harga",
            "Button",
            listOf("adasd", "gdfgdfgf")
        )
        filter.add(tracker)
        Tracker.trackFilters(filter)

        val list = mutableListOf<String>()
        for (i in 0 until 10001) {
            list.add("abah $i")
        }


        val menuListView = findViewById<MenuListView<String>>(R.id.menuListView)
        menuListView.data = list
        Tracker.setImpressionRecyclerView("abah test", menuListView)

    }

    override fun onResume() {
        super.onResume()
        //Tracker.resumePage("testlib8", "testlibaja8")
        Tracker.trackPage("testlib11", "testlib11")
        Handler(Looper.myLooper()!!).postDelayed({
            Tracker.trackSearch("lalali", "Login")
        }, 3000)
    }
}
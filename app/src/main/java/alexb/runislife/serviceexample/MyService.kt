package alexb.runislife.serviceexample

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MyService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(LOG_TAG,"MyService onBind")
        return Binder()
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(LOG_TAG,"MyService onRebind")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(LOG_TAG,"MyService onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG,"MyService onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG,"MyService onDestroy")
    }
}

const val LOG_TAG="Logs"
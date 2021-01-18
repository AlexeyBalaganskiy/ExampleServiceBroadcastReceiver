package alexb.runislife.serviceexample

import android.content.*
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var serviceConnection: ServiceConnection? = null
    var bound = false
    lateinit var wifiManager: WifiManager
    lateinit var wifiOff: String
    lateinit var wifiOn: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wifiOff = resources.getString(R.string.wifi_off)
        wifiOn = resources.getString(R.string.wifi_on)
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        sWifi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wifiManager.isWifiEnabled = true
                sWifi.text = wifiOn
            } else {
                wifiManager.isWifiEnabled = false
                sWifi.text = wifiOff
            }
        }

        val intent = Intent(this, MyService::class.java)
        serviceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected")
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected")
                bound = false
            }
        }

        btnStart.setOnClickListener {
            startService(intent)
        }

        btnStop.setOnClickListener {
            stopService(intent)
        }

        btnBind.setOnClickListener {
            serviceConnection?.let { bindService(intent, it, BIND_AUTO_CREATE) };
        }

        btnUnbind.setOnClickListener {
            if (!bound) return@setOnClickListener
            serviceConnection?.let { unbindService(it) };
            bound = false;
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiStateReceiver)
    }

    private val wifiStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getIntExtra(
                WifiManager.EXTRA_WIFI_STATE,
                WifiManager.WIFI_STATE_UNKNOWN
            )) {
                WifiManager.WIFI_STATE_ENABLED -> {
                    sWifi.isChecked = true
                    sWifi.text = wifiOn
                    Toast.makeText(this@MainActivity, wifiOn, Toast.LENGTH_SHORT).show()
                }
                WifiManager.WIFI_STATE_DISABLED -> {
                    sWifi.isChecked = false
                    sWifi.text = wifiOff
                    Toast.makeText(this@MainActivity, wifiOff, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private val LOG_TAG = "Logs"
    }
}

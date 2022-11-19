package com.example.ibeacon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast

class CustomReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "$intent", Toast.LENGTH_LONG).show()
        Log.e("onReceive", "$intent")
        val action = intent?.action
        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
//            context?.checkConnection()
        }
    }
}
package com.example.ibeacon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.ibeacon.ui.theme.IBeaconTheme
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier

class MainActivity : ComponentActivity() {
    private val beaconState = mutableStateOf("Unknown")
    private val beaconsList = mutableStateListOf<Beacon>()
    private val txtRanging = mutableStateOf("Start Ranging")
    private val txtMonitoring = mutableStateOf("Start Monitoring")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Allow location permission in settings")
            finish()
        }

        setContent {
            IBeaconTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            Modifier
                                .padding(25.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(text = "Beacon State")
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = beaconState.value)
                            Divider(
                                Modifier
                                    .width(100.dp)
                                    .padding(vertical = 30.dp)
                            )
                            beaconsList.map { beacon ->
                                Text(text = "${beacon.id1}\nid2: ${beacon.id2} id3:  rssi: ${beacon.rssi}\nest. distance: ${beacon.distance} m")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = ::onClickRanging) {
                                Text(text = txtRanging.value)
                            }
                            Button(onClick = ::onClickMonitoring) {
                                Text(text = txtMonitoring.value)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onClickRanging() {
/*
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        if (beaconManager.rangedRegions.isEmpty()) {
            beaconManager.startRangingBeacons(iBeaconApplication.region)
            txtRanging.value = "Stop Ranging"
        } else {
            beaconManager.stopRangingBeacons(iBeaconApplication.region)
            txtRanging.value = "Start Ranging"
        }
*/
    }

    private fun onClickMonitoring() {
/*
        val beaconManager = BeaconManager.getInstanceForApplication(this)
        if (beaconManager.monitoredRegions.isEmpty()) {
            beaconManager.startMonitoring(iBeaconApplication.region)
            txtMonitoring.value = "Stop Monitoring"
        } else {
            beaconManager.stopMonitoring(iBeaconApplication.region)
            txtMonitoring.value = "Start Monitoring"
        }
*/
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

package com.example.ibeacon

import android.app.Application
import android.app.NotificationManager
import android.os.Build
import android.os.Message
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import java.text.SimpleDateFormat
import java.util.Date
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region

class IBeaconApplication : Application(), MonitorNotifier {

    lateinit var region: Region
    private lateinit var notificationManager: NotificationManager
    private val backgroundBetweenScanPeriod = 0L
    private val backgroundScanPeriod = 20000L
    private val deviceName =
        "${Build.MANUFACTURER}_${Build.MODEL}_${Build.VERSION.SDK_INT}_${backgroundScanPeriod}_${backgroundBetweenScanPeriod}"

    private lateinit var db: FirebaseFirestore
    private lateinit var collection: CollectionReference

/*
    private val centralMonitoringObserver = Observer<Int> { state ->
        val stateName = when (state) {
            MonitorNotifier.INSIDE -> "INSIDE"
            MonitorNotifier.OUTSIDE -> "OUTSIDE"
            else -> "UNKNOWN"
        }

        notificationManager.sendBeaconTestNotification(
            this,
            stateName,
            NOTIFICATION_ID_MONITOR
        )
        write(stateName, EventType.MONITOR)
    }

    private val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->
        notificationManager.sendBeaconTestNotification(
            this,
            "Ranging Complete\n${beacons}",
            NOTIFICATION_ID_RANGING
        )
        write("Ranging Complete\n${beacons}", EventType.RANGING)
        for (beacon: Beacon in beacons) {
            Log.e(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }
*/

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()
        collection = db.collection(deviceName)

        createNotificationChannel()

        notificationManager = ContextCompat
            .getSystemService(this, NotificationManager::class.java) as NotificationManager

        val beaconManager = BeaconManager.getInstanceForApplication(this)
        BeaconManager.setDebug(true)
        BeaconParser().setBeaconLayout(iBeaconLayout).let {
            Log.e("BeaconParser", "${beaconManager.beaconParsers}")
            if (!beaconManager.beaconParsers.contains(it)) {
                beaconManager.beaconParsers.add(it)
            }
        }
        beaconManager.addMonitorNotifier(this)
        beaconManager.backgroundBetweenScanPeriod = backgroundBetweenScanPeriod
        beaconManager.backgroundScanPeriod = backgroundScanPeriod
        setupForegroundService()
        // Monitoring callbacks will be delayed by up to 25 minutes on region exit
//        beaconManager.setIntentScanningStrategyEnabled(true)

        region = Region(
            "all-beacons",
            Identifier.parse("a8d278e4-35b5-44ef-9cac-0f3fc511fe3e"),
            null,
            null
        )
        beaconManager.startMonitoring(region)
//        beaconManager.startRangingBeacons(region)
//        val regionViewModel = BeaconManager
//            .getInstanceForApplication(this)
//            .getRegionViewModel(region)
//        regionViewModel.regionState.observeForever(centralMonitoringObserver)
//        regionViewModel.rangedBeacons.observeForever(centralRangingObserver)
    }

    override fun didEnterRegion(region: Region?) {
        notificationManager.sendBeaconTestNotification(this, "ENTER", NOTIFICATION_ID_NOTIFIER)
        write("ENTER", EventType.NOTIFIER)
    }

    override fun didExitRegion(region: Region?) {
        notificationManager.sendBeaconTestNotification(this, "EXIT", NOTIFICATION_ID_NOTIFIER)
        write("EXIT", EventType.NOTIFIER)
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        notificationManager.sendBeaconTestNotification(this, "DETECT", NOTIFICATION_ID_NOTIFIER)
        write("DETECT", EventType.NOTIFIER)
    }

    companion object {
        const val TAG = "IBeaconApplication"
    }

    private fun write(message: String, eventType: EventType) {
        val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa").format(Date().time)
        collection.document(eventType.name).set(mapOf(date to message), SetOptions.merge())
    }

    enum class EventType {
        MONITOR, RANGING, NOTIFIER
    }
}

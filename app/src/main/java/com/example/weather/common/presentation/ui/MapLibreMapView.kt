package com.example.weather.common.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import org.maplibre.android.maps.MapView


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        MapView(context).apply {
            id = View.generateViewId()
            onCreate(Bundle())
        }
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

@Composable
fun MapLibreMapView(modifier: Modifier = Modifier, apiKey: String = "") {
    val linkStyle: String
    if (apiKey != "") {
        linkStyle = "https://api.maptiler.com/maps/streets/style.json?key=${apiKey}"
    } else {
        linkStyle = "https://demotiles.maplibre.org/style.json"
    }
    val mapView = rememberMapViewWithLifecycle()

    AndroidView(
        modifier = modifier,
        factory = { mapView },
        update = {
            it.getMapAsync { map ->
                map.setStyle(linkStyle)
            }
        }
    )
}

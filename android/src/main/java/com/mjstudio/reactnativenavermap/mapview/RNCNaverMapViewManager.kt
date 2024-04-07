package com.mjstudio.reactnativenavermap.mapview

import android.graphics.PointF
import android.view.Gravity
import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.mjstudio.reactnativenavermap.RNCNaverMapViewManagerSpec
import com.mjstudio.reactnativenavermap.event.NaverMapCameraChangeEvent
import com.mjstudio.reactnativenavermap.event.NaverMapInitializeEvent
import com.mjstudio.reactnativenavermap.event.NaverMapOptionChangeEvent
import com.mjstudio.reactnativenavermap.event.NaverMapTapEvent
import com.mjstudio.reactnativenavermap.util.CameraAnimationUtil
import com.mjstudio.reactnativenavermap.util.RectUtil
import com.mjstudio.reactnativenavermap.util.getDoubleOrNull
import com.mjstudio.reactnativenavermap.util.getLatLng
import com.mjstudio.reactnativenavermap.util.getLatLngBoundsOrNull
import com.mjstudio.reactnativenavermap.util.isValidNumber
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMap.MapType.Basic
import com.naver.maps.map.NaverMap.MapType.Hybrid
import com.naver.maps.map.NaverMap.MapType.Navi
import com.naver.maps.map.NaverMap.MapType.NaviHybrid
import com.naver.maps.map.NaverMap.MapType.None
import com.naver.maps.map.NaverMap.MapType.Satellite
import com.naver.maps.map.NaverMap.MapType.Terrain
import com.naver.maps.map.NaverMapOptions
import kotlin.math.roundToInt


@ReactModule(name = RNCNaverMapViewManager.NAME)
class RNCNaverMapViewManager : RNCNaverMapViewManagerSpec<RNCNaverMapViewWrapper>() {
    override fun getName(): String {
        return NAME
    }

    override fun createViewInstance(context: ThemedReactContext): RNCNaverMapViewWrapper {
        return RNCNaverMapViewWrapper(context, NaverMapOptions()).also {
            context.addLifecycleEventListener(it)


        }
    }

    override fun onDropViewInstance(view: RNCNaverMapViewWrapper) {
        super.onDropViewInstance(view)
        view.onDropViewInstance()
        view.reactContext.removeLifecycleEventListener(view)
    }

    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> =
        (super.getExportedCustomDirectEventTypeConstants() ?: mutableMapOf()).apply {
            put(
                NaverMapInitializeEvent.EVENT_NAME,
                mapOf("registrationName" to NaverMapInitializeEvent.EVENT_NAME)
            )
            put(
                NaverMapOptionChangeEvent.EVENT_NAME,
                mapOf("registrationName" to NaverMapOptionChangeEvent.EVENT_NAME)
            )
            put(
                NaverMapCameraChangeEvent.EVENT_NAME,
                mapOf("registrationName" to NaverMapCameraChangeEvent.EVENT_NAME)
            )
            put(
                NaverMapTapEvent.EVENT_NAME,
                mapOf("registrationName" to NaverMapTapEvent.EVENT_NAME)
            )
        }

    private fun RNCNaverMapViewWrapper?.withMapView(callback: (mapView: RNCNaverMapView) -> Unit) {
        this?.mapView?.run(callback)
    }

    private fun RNCNaverMapViewWrapper?.withMap(callback: (map: NaverMap) -> Unit) {
        this?.mapView?.withMap(callback)
    }

    private fun View?.px(dp: Double) =
        ((this?.resources?.displayMetrics?.density ?: 1f) * dp).roundToInt()

    override fun needsCustomLayoutForChildren(): Boolean = true

    // region PROPS

    @ReactProp(name = "mapType")
    override fun setMapType(view: RNCNaverMapViewWrapper?, value: String?) = view.withMap {
        it.mapType = when (value) {
            "Basic" -> Basic
            "Navi" -> Navi
            "Satellite" -> Satellite
            "Hybrid" -> Hybrid
            "Terrain" -> Terrain
            "NaviHybrid" -> NaviHybrid
            "None" -> None
            else -> Basic
        }
    }

    @ReactProp(name = "initialCamera")
    override fun setInitialCamera(view: RNCNaverMapViewWrapper?, value: ReadableMap?) =
        view.withMapView {
            if (!it.isInitialCameraOrRegionSet) {
                it.isInitialCameraOrRegionSet = true
                if (isValidNumber(value?.getDoubleOrNull("latitude"))) {
                    setCamera(view, value)
                }
            }
        }

    @ReactProp(name = "camera")
    override fun setCamera(view: RNCNaverMapViewWrapper?, value: ReadableMap?) = view.withMap {
        value?.getLatLng()?.also { latlng ->
            val zoom = value.getDoubleOrNull("zoom") ?: it.cameraPosition.zoom
            val tilt = value.getDoubleOrNull("tilt") ?: it.cameraPosition.tilt
            val bearing = value.getDoubleOrNull("bearing") ?: it.cameraPosition.bearing

            it.cameraPosition = CameraPosition(
                latlng,
                zoom,
                tilt,
                bearing,
            )
        }
    }


    @ReactProp(name = "initialRegion")
    override fun setInitialRegion(view: RNCNaverMapViewWrapper?, value: ReadableMap?) =
        view.withMapView {
            if (!it.isInitialCameraOrRegionSet) {
                it.isInitialCameraOrRegionSet = true
                if (isValidNumber(value?.getDoubleOrNull("latitude"))) {
                    setRegion(view, value)
                }
            }
        }

    @ReactProp(name = "region")
    override fun setRegion(view: RNCNaverMapViewWrapper?, value: ReadableMap?) = view.withMap {
        value.getLatLngBoundsOrNull()?.run {
            val update = CameraUpdate.fitBounds(this)
            it.moveCamera(update)
        }
    }

    @ReactProp(name = "isIndoorEnabled")
    override fun setIsIndoorEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) = view.withMap {
        it.isIndoorEnabled = value
    }

    @ReactProp(name = "isNightModeEnabled")
    override fun setIsNightModeEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap {
            it.isNightModeEnabled = value
        }

    @ReactProp(name = "isLiteModeEnabled")
    override fun setIsLiteModeEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap {
            it.isLiteModeEnabled = value
        }

    @ReactProp(name = "lightness")
    override fun setLightness(view: RNCNaverMapViewWrapper?, value: Double) = view.withMap {
        it.lightness = value.toFloat()
    }

    @ReactProp(name = "buildingHeight")
    override fun setBuildingHeight(view: RNCNaverMapViewWrapper?, value: Double) = view.withMap {
        it.buildingHeight = value.toFloat()
    }

    @ReactProp(name = "symbolScale")
    override fun setSymbolScale(view: RNCNaverMapViewWrapper?, value: Double) = view.withMap {
        it.symbolScale = value.toFloat()
    }

    @ReactProp(name = "symbolPerspectiveRatio")
    override fun setSymbolPerspectiveRatio(view: RNCNaverMapViewWrapper?, value: Double) =
        view.withMap {
            it.symbolPerspectiveRatio = value.toFloat()
        }

    @ReactProp(name = "mapPadding")
    override fun setMapPadding(view: RNCNaverMapViewWrapper?, value: ReadableMap?) =
        view.withMapView {
            RectUtil.getRect(value, it.resources.displayMetrics.density, defaultValue = 0.0)?.run {
                it.withMap { map ->
                    map.setContentPadding(left, top, right, bottom)
                }
            }
        }


    @ReactProp(name = "minZoom")
    override fun setMinZoom(view: RNCNaverMapViewWrapper?, value: Double) = view.withMap {
        it.minZoom = value
    }

    @ReactProp(name = "maxZoom")
    override fun setMaxZoom(view: RNCNaverMapViewWrapper?, value: Double) = view.withMap {
        it.maxZoom = value
    }

    @ReactProp(name = "isShowCompass")
    override fun setIsShowCompass(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap {
            it.uiSettings.isCompassEnabled = value
        }

    @ReactProp(name = "isShowScaleBar")
    override fun setIsShowScaleBar(view: RNCNaverMapViewWrapper?, value: Boolean) = view.withMap {
        it.uiSettings.isScaleBarEnabled = value
    }

    @ReactProp(name = "isShowZoomControls")
    override fun setIsShowZoomControls(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap {
            it.uiSettings.isZoomControlEnabled = value
        }

    @ReactProp(name = "isShowIndoorLevelPicker")
    override fun setIsShowIndoorLevelPicker(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap {
            it.uiSettings.isIndoorLevelPickerEnabled = value
        }

    @ReactProp(name = "isShowLocationButton")
    override fun setIsShowLocationButton(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap {
            it.uiSettings.isLocationButtonEnabled = value
        }

    @ReactProp(name = "logoAlign")
    override fun setLogoAlign(view: RNCNaverMapViewWrapper?, value: String?) = view.withMap {
        it.uiSettings.logoGravity = when (value) {
            "TopLeft" -> Gravity.TOP or Gravity.LEFT
            "TopRight" -> Gravity.TOP or Gravity.RIGHT
            "BottomRight" -> Gravity.BOTTOM or Gravity.RIGHT
            else -> Gravity.BOTTOM or Gravity.LEFT
        }
    }

    @ReactProp(name = "logoMargin")
    override fun setLogoMargin(view: RNCNaverMapViewWrapper?, value: ReadableMap?) =
        view.withMapView {
            RectUtil.getRect(value, it.resources.displayMetrics.density, defaultValue = 0.0)?.run {
                it.withMap { map ->
                    map.uiSettings.setLogoMargin(left, top, right, bottom)
                }
            }
        }

    @ReactProp(name = "extent")
    override fun setExtent(view: RNCNaverMapViewWrapper?, value: ReadableMap?) = view.withMap {
        value.getLatLngBoundsOrNull()?.run {
            it.extent = this
        }
    }

    @ReactProp(name = "isScrollGesturesEnabled")
    override fun setIsScrollGesturesEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap { it.uiSettings.isScrollGesturesEnabled = value }

    @ReactProp(name = "isZoomGesturesEnabled")
    override fun setIsZoomGesturesEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap { it.uiSettings.isZoomGesturesEnabled = value }

    @ReactProp(name = "isTiltGesturesEnabled")
    override fun setIsTiltGesturesEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap { it.uiSettings.isTiltGesturesEnabled = value }

    @ReactProp(name = "isRotateGesturesEnabled")
    override fun setIsRotateGesturesEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap { it.uiSettings.isRotateGesturesEnabled = value }

    @ReactProp(name = "isStopGesturesEnabled")
    override fun setIsStopGesturesEnabled(view: RNCNaverMapViewWrapper?, value: Boolean) =
        view.withMap { it.uiSettings.isStopGesturesEnabled = value }

    // endregion

    // region COMMANDS
    override fun screenToCoordinate(view: RNCNaverMapViewWrapper?, x: Double, y: Double) =
        view.withMap {}


    override fun coordinateToScreen(
        view: RNCNaverMapViewWrapper?,
        latitude: Double,
        longitude: Double
    ) = view.withMap { }


    override fun animateCameraTo(
        view: RNCNaverMapViewWrapper?,
        latitude: Double,
        longitude: Double,
        duration: Int,
        easing: Int,
        pivotX: Double,
        pivotY: Double
    ) = view.withMap {
        CameraUpdate.scrollTo(LatLng(latitude, longitude))
            .animate(CameraAnimationUtil.numberToCameraAnimationEasing(easing), duration.toLong())
            .pivot(
                PointF(pivotX.toFloat(), pivotY.toFloat())
            ).run {
                it.moveCamera(this)
            }
    }

    override fun animateCameraBy(
        view: RNCNaverMapViewWrapper?,
        latitudeDelta: Double,
        longitudeDelta: Double,
        duration: Int,
        easing: Int,
        pivotX: Double,
        pivotY: Double
    ) = view.withMap {
        CameraUpdate.scrollBy(
            PointF(
                view.px(latitudeDelta).toFloat(),
                view.px(longitudeDelta).toFloat()
            )
        )
            .animate(CameraAnimationUtil.numberToCameraAnimationEasing(easing), duration.toLong())
            .pivot(
                PointF(pivotX.toFloat(), pivotY.toFloat())
            ).run {
                it.moveCamera(this)
            }
    }

    override fun animateRegionTo(
        view: RNCNaverMapViewWrapper?,
        latitude: Double,
        longitude: Double,
        latitudeDelta: Double,
        longitudeDelta: Double,
        duration: Int,
        easing: Int,
        pivotX: Double,
        pivotY: Double
    ) = view.withMap {
        CameraUpdate.fitBounds(
            LatLngBounds(
                LatLng(latitude, longitude),
                LatLng(latitude + latitudeDelta, longitude + longitudeDelta)
            )
        )
            .animate(CameraAnimationUtil.numberToCameraAnimationEasing(easing), duration.toLong())
            .pivot(
                PointF(pivotX.toFloat(), pivotY.toFloat())
            ).run {
                it.moveCamera(this)
            }
    }

    override fun cancelAnimation(view: RNCNaverMapViewWrapper?) = view.withMap {
        it.cancelTransitions()
    }

    companion object {
        const val NAME = "RNCNaverMapView"
    }
}
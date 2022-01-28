package com.munidigital.bc2201.challengfinal.main.Mapa

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.munidigital.bc2201.challengfinal.R
import com.munidigital.bc2201.challengfinal.main.Dato.DatoFragmentArgs

class MapaFragment : Fragment() {

    private val args: DatoFragmentArgs by navArgs()

    private val callback = OnMapReadyCallback { googleMap ->
        val equipo = args.equipo
        val request = Geocoder(requireActivity()).getFromLocationName(equipo.direccion + " ," + equipo.estadio,1)[0]
        val locationn = LatLng(request.latitude, request.longitude)

        googleMap.addMarker(MarkerOptions().position(locationn).title(equipo.estadio))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationn, 20.0f))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_mapa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
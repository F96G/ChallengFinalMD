package com.munidigital.bc2201.challengfinal.main

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.munidigital.bc2201.challengfinal.Equipo
import com.munidigital.bc2201.challengfinal.R

class DatoFragment : Fragment() {

    //Recibo los agrumentos
    private val args: DatoFragmentArgs by navArgs()

    private var package_name = "com.android.chrome"

    private lateinit var rootView:View
    private lateinit var mainViewModel:MainViewModel

    private lateinit var equipo: Equipo

    private lateinit var imagen: ImageView
    private lateinit var nombre: TextView
    private lateinit var nombreAlt: TextView
    private lateinit var liga1: TextView
    private lateinit var liga2: TextView
    private lateinit var anioFundado: TextView
    private lateinit var descripcion: TextView
    private lateinit var estadio: TextView
    private lateinit var capacidad: TextView
    private lateinit var ubicacion: TextView
    private lateinit var web: TextView
    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dato, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        equipo = args.equipo

        setHasOptionsMenu(true)

        imagen = rootView.findViewById<ImageView>(R.id.ivEscudo)
        nombre = rootView.findViewById<TextView>(R.id.tvNombre)
        nombreAlt = rootView.findViewById<TextView>(R.id.tvNombreAlt)
        liga1 = rootView.findViewById<TextView>(R.id.tvLiga1)
        liga2 = rootView.findViewById<TextView>(R.id.tvLiga2)
        anioFundado = rootView.findViewById<TextView>(R.id.tvFundado)
        descripcion = rootView.findViewById<TextView>(R.id.tvDescripcion)
        estadio = rootView.findViewById<TextView>(R.id.tvEstadio)
        capacidad = rootView.findViewById<TextView>(R.id.tvCapacidad)
        ubicacion = rootView.findViewById<TextView>(R.id.tvUbicacion)
        web = rootView.findViewById<TextView>(R.id.tvWeb)
        webView = rootView.findViewById<WebView>(R.id.webView)



        setDatos()

        web.setOnClickListener{
            startWebView()
        }

        return rootView
    }

    private fun setDatos() {
        Glide.with(this).load(equipo.imagen).centerCrop().into(imagen)
        nombre.text = equipo.nombre
        anioFundado.text = "Fundado: " + equipo.anioFundado.toString()

        if (!equipo.nombreAlt.isNullOrEmpty())
            nombreAlt.text = equipo.nombreAlt
        else
            nombreAlt.visibility = View.GONE

        if (equipo.idLiga1 != null)
            liga1.text = equipo.liga1
        else
            liga2.visibility = View.GONE

        if (equipo.idLiga2 != null)
            liga2.text = equipo.liga2
        else
            liga2.visibility = View.GONE

        if (!equipo.descripcion.isNullOrEmpty())
            descripcion.text = equipo.descripcion
        else
            descripcion.visibility = View.GONE

        if(!equipo.estadio.isEmpty()){
            estadio.text = "Estadio: " + equipo.estadio
            capacidad.text = "Capacidad: " + equipo.capacidad.toString()
            ubicacion.text = equipo.direccion
        }else{
            estadio.visibility = View.GONE
            capacidad.visibility = View.GONE
            ubicacion.visibility = View.GONE
        }

        if (!equipo.web.isEmpty())
            web.text = equipo.web
        else
            web.visibility = View.GONE
    }

    private fun startWebView() {

        val builder = CustomTabsIntent.Builder()

        //Color del custom tabs
        val params = CustomTabColorSchemeParams.Builder()
        params.setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.purple_500))
        builder.setDefaultColorSchemeParams(params.build())
        //Mostrar la URL
        builder.setShowTitle(true)
        //menu para compartir
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

        val customBuilder = builder.build()

        if (requireActivity().isPackageInstalled(package_name)) {
            // si chrome esta disponible usa custom tabs
            customBuilder.intent.setPackage(package_name)
            customBuilder.launchUrl(requireActivity(), Uri.parse("http://" + equipo.web))
        } else {
            // si no esta disponible usa WebView
            webView = rootView.findViewById<WebView>(R.id.webView)

            webView.webChromeClient = object : WebChromeClient(){}

            webView.webViewClient = object : WebViewClient(){}

            val settings = webView.settings
            settings.javaScriptEnabled = true
            webView.loadUrl("https://" + equipo.web)
            webView.visibility = View.VISIBLE
        }
    }

    fun Context.isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dato, menu)

        val item = menu.findItem(R.id.iAniadirFav)
        if(equipo.favorito)//Si es favorito
            item.icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_star_24_white)
        else//si no es favorito
            item.icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_star_border_24)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.iAniadirFav ->{
                mainViewModel.seleccionarFavorito(equipo)
                if(!equipo.favorito){ //Si era favorito
                    item.icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_star_border_24)
                    equipo.favorito = false
                }
                else{ //si no era favorito
                    item.icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_star_24_white)
                    equipo.favorito = true
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
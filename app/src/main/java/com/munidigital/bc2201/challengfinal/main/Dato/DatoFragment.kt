package com.munidigital.bc2201.challengfinal.main.Dato

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.munidigital.bc2201.challengfinal.Equipo
import com.munidigital.bc2201.challengfinal.R
import com.munidigital.bc2201.challengfinal.main.MainViewModel

class DatoFragment : Fragment() {

    private val args: DatoFragmentArgs by navArgs()
    //nombre del paquete de chrome
    private var package_name = "com.android.chrome"

    private lateinit var rootView:View
    private lateinit var mainViewModel: MainViewModel
    private lateinit var equipo: Equipo

    private lateinit var imagen: ImageView
    private lateinit var tvNombre: TextView
    private lateinit var tvNombreAlt: TextView
    private lateinit var tvLiga1: TextView
    private lateinit var tvLiga2: TextView
    private lateinit var tvAnioFundado: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvEstadio: TextView
    private lateinit var tvCapacidad: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvWeb: TextView
    private lateinit var webView: WebView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_dato, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        equipo = args.equipo

        //Habilita cambiar de toolbar
        setHasOptionsMenu(true)

        setDatos()

        tvWeb.setOnClickListener{
            startWebView()
        }

        tvUbicacion.setOnClickListener{
            val action = DatoFragmentDirections.actionDatoFragmentToMapaFragment(equipo)
            findNavController().navigate(action)
        }

        return rootView
    }



    private fun setDatos() {
        imagen = rootView.findViewById<ImageView>(R.id.ivEscudo)
        tvNombre = rootView.findViewById<TextView>(R.id.tvNombre)
        tvNombreAlt = rootView.findViewById<TextView>(R.id.tvNombreAlt)
        tvLiga1 = rootView.findViewById<TextView>(R.id.tvLiga1)
        tvLiga2 = rootView.findViewById<TextView>(R.id.tvLiga2)
        tvAnioFundado = rootView.findViewById<TextView>(R.id.tvFundado)
        tvDescripcion = rootView.findViewById<TextView>(R.id.tvDescripcion)
        tvEstadio = rootView.findViewById<TextView>(R.id.tvEstadio)
        tvCapacidad = rootView.findViewById<TextView>(R.id.tvCapacidad)
        tvUbicacion = rootView.findViewById<TextView>(R.id.tvUbicacion)
        tvWeb = rootView.findViewById<TextView>(R.id.tvWeb)
        webView = rootView.findViewById<WebView>(R.id.webView)

        Glide.with(this).load(equipo.imagen).centerCrop().into(imagen)
        tvNombre.text = equipo.nombre
        tvAnioFundado.text = "Fundado: " + equipo.anioFundado.toString()

        if (!equipo.nombreAlt.isNullOrEmpty())
            tvNombreAlt.text = equipo.nombreAlt
        else
            tvNombreAlt.visibility = View.GONE

        if (equipo.idLiga1 != null && equipo.idLiga1 != 4834)//4834 es el identificador de _No League Soccer

            tvLiga1.text = equipo.liga1
        else
            tvLiga2.visibility = View.GONE

        if (equipo.idLiga2 != null && equipo.idLiga1 != 4834)
            tvLiga2.text = equipo.liga2
        else
            tvLiga2.visibility = View.GONE

        if (!equipo.descripcion.isNullOrEmpty())
            tvDescripcion.text = equipo.descripcion
        else
            tvDescripcion.visibility = View.GONE

        if(!equipo.estadio.isEmpty()){
            tvEstadio.text = "Estadio: " + equipo.estadio
            tvCapacidad.text = "Capacidad: " + equipo.capacidad.toString()
            tvUbicacion.text = equipo.direccion
        }else{
            tvEstadio.visibility = View.GONE
            tvCapacidad.visibility = View.GONE
            tvUbicacion.visibility = View.GONE
        }

        if (!equipo.web.isEmpty())
            tvWeb.text = equipo.web
        else
            tvWeb.visibility = View.GONE
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

    // verifica si chrome esta instalado
    fun Context.isPackageInstalled(packageName: String): Boolean {
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
                if(equipo.favorito){ //Si no era favorito
                    item.icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_star_24_white)
                    equipo.favorito = true
                }
                else{ //si era favorito
                    item.icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_star_border_24)
                    equipo.favorito = false
                }
            }

        }

        return super.onOptionsItemSelected(item)
    }



}
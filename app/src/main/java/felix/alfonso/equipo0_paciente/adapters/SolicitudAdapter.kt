package felix.alfonso.equipo0_paciente.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import equipo0_dominio.Paciente
import felix.alfonso.equipo0_paciente.MainActivity
import felix.alfonso.equipo0_paciente.R
import felix.alfonso.equipo0_paciente.dominio.Solicitud
import felix.alfonso.equipo0_paciente.http.ChimboxJsonObjectRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.solicitud_item.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

class SolicitudAdapter : BaseAdapter {
    var lstSolicitudes: ArrayList<Solicitud>
    var context: Context
    val url = "https://192.168.0.16:8443/Equipo0_GatewayPaciente/res/paciente/actualizarSolicitud"
    var queue: RequestQueue

    constructor(lstSolicitudes: ArrayList<Solicitud>, context: Context) : super() {
        this.lstSolicitudes = lstSolicitudes
        this.context = context
        queue = Volley.newRequestQueue(context)
    }

    override fun getCount(): Int {
        return lstSolicitudes.size
    }

    override fun getItem(p0: Int): Any {
        return lstSolicitudes[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var solicitud = lstSolicitudes[p0]
        var inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.solicitud_item, null)

        val simpleDateFormat = SimpleDateFormat("MMMM dd, yyyy HH:mm")

        view.tvNombreSolicitante.text = solicitud.body
        view.tvFechaHora.text = simpleDateFormat.format(Date())
        view.btnAceptar.setOnClickListener {
            Toast.makeText(context, "Aprobada", Toast.LENGTH_SHORT).show()
            solicitud.aprobada = true
            actualizarSolicitud(solicitud)
        }
        view.btnRechazar.setOnClickListener {
            Toast.makeText(context, "Rechazada", Toast.LENGTH_SHORT).show()
            lstSolicitudes.removeAt(p0)
            notifyDataSetChanged()
            solicitud.aprobada = false
        }

        return view
    }

    private fun actualizarSolicitud(solicitud: Solicitud) {

        try {
            val jsonBody = JSONObject()
            jsonBody.put("idCita", solicitud.idCita)
            var jsonObjectRequest = ChimboxJsonObjectRequest(Request.Method.POST, url, jsonBody, {

            }, {

            }, MainActivity.token)
            queue.add(jsonObjectRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
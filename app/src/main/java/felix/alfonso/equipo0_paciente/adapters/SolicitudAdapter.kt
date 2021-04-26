package felix.alfonso.equipo0_paciente.adapters

import android.content.Context
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
import felix.alfonso.equipo0_paciente.R
import felix.alfonso.equipo0_paciente.dominio.Solicitud
import kotlinx.android.synthetic.main.solicitud_item.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

class SolicitudAdapter : BaseAdapter {
    var lstSolicitudes: ArrayList<Solicitud>
    var context: Context
    val url = "http://192.168.0.12:8084/Equipo0_GatewayPaciente/res/Paciente/actualizarSolicitud"
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

        view.tvNombreSolicitante.text = solicitud.nombreSolicitante
        view.tvFechaHora.text = simpleDateFormat.format(solicitud.fechaHora)
        view.btnAceptar.setOnClickListener {
            Toast.makeText(context, "Aceptada", Toast.LENGTH_SHORT).show()
            solicitud.aprobada = true
            actualizarSolicitud(solicitud)
        }
        view.btnRechazar.setOnClickListener {
            Toast.makeText(context, "Rechazada", Toast.LENGTH_SHORT).show()
            lstSolicitudes.removeAt(p0)
            notifyDataSetChanged()
            solicitud.aprobada = false
            actualizarSolicitud(solicitud)
        }

        return view
    }

    fun actualizarSolicitud(solicitud: Solicitud) {

        try {
            val jsonBody = JSONObject()
            jsonBody.put("solicitud", solicitud)
            val requestBody = jsonBody.toString()
            val stringRequest: StringRequest = object : StringRequest(
                    Method.POST, url,
                    Response.Listener { response ->
                        println("${response}")
                    },
                    Response.ErrorListener { error ->
                        println(error)
                    }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    try {
                        if (requestBody == null) {
                            return requestBody
                        } else {
                            return requestBody.toByteArray(charset("utf-8"))
                        }
                    } catch (uee: UnsupportedEncodingException) {
                        VolleyLog.wtf(
                                "Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody,
                                "utf-8"
                        )
                    }
                    return requestBody!!.toByteArray()
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    var responseString = ""
                    if (response != null) {
                        responseString = response.statusCode.toString()
                        // can get more details such as response.headers
                    }
                    return Response.success(
                            responseString,
                            HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
            queue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
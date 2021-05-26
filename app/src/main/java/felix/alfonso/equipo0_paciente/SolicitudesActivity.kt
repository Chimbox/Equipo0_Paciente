package felix.alfonso.equipo0_paciente

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import felix.alfonso.equipo0_paciente.adapters.SolicitudAdapter
import felix.alfonso.equipo0_paciente.dominio.Solicitud
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_solicitudes.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.*


class SolicitudesActivity : AppCompatActivity() {


    var lstSolicitudes=MainActivity.lstSolicitudes
    companion object{
        lateinit var adapter:SolicitudAdapter
        var iniciado:Boolean=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes)

        adapter=SolicitudAdapter(lstSolicitudes!!, this)
        iniciado=true

        lvSolicitudes.adapter= adapter

    }
}
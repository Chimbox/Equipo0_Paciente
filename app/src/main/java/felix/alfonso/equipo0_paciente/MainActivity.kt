package felix.alfonso.equipo0_paciente

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import equipo0_dominio.FirebaseCloudMessage
import equipo0_dominio.Paciente
import equipo0_dominio.Usuario
import felix.alfonso.equipo0_paciente.dominio.Solicitud
import felix.alfonso.equipo0_paciente.http.ChimboxJsonObjectRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    val URL_TOKEN = "https://192.168.0.16:8443/Equipo0_GatewayPaciente/res/paciente/actualizartokenfirebase"

    companion object{
        lateinit var usuarioActivo: Paciente
        lateinit var token: String
        var lstSolicitudes = ArrayList<Solicitud>()
        var servicio = NotificationService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (usuarioActivo!=null){
            tvHola.text="Hola ${usuarioActivo.nombre} ${usuarioActivo.primerApellido} ${usuarioActivo.segundoApellido}"

            var requestQueue = Volley.newRequestQueue(getApplicationContext())


            var obj = JSONObject()

            try {
                //input your API parameters
                obj.put("username", usuarioActivo.username)
                obj.put("tokenFirebase", FirebaseMessaging.getInstance().token.result)
            } catch (e: JSONException) {
                e.printStackTrace();
            }
            var jsonObjectRequest = ChimboxJsonObjectRequest(Request.Method.POST, URL_TOKEN, obj, {

            }, {

            }, token)


            requestQueue.add(jsonObjectRequest)

            btnSolicitudes.setOnClickListener {
                var intent= Intent(this, SolicitudesActivity::class.java)
                startActivity(intent)
            }
        }




    }
}
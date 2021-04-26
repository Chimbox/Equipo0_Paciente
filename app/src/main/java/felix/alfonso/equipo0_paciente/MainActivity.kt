package felix.alfonso.equipo0_paciente

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import equipo0_dominio.Paciente
import equipo0_dominio.Usuario
import felix.alfonso.equipo0_paciente.dominio.Solicitud
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var usuarioActivo: Paciente
        var lstSolicitudes = ArrayList<Solicitud>()
        var servicio = NotificationService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (usuarioActivo!=null){
            tvHola.text="Hola ${usuarioActivo.nombre} ${usuarioActivo.primerApellido} ${usuarioActivo.segundoApellido}"
        }

        servicio.obtenerToken()

        btnSolicitudes.setOnClickListener {
            var intent= Intent(this, SolicitudesActivity::class.java)
            startActivity(intent)
        }


    }
}
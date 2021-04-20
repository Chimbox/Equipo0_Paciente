package felix.alfonso.equipo0_paciente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val servicio = NotificationService()
    private val apiPaciente = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //servicio.obtenerToken()

        run("http://192.168.0.12:8084/Equipo0_GatewayPaciente/res/Paciente")
    }

    fun run(url: String) {
        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        var respuesta:Response?=null
        var mensaje:String?=null

        apiPaciente.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mensaje=e.message
            }
            override fun onResponse(call: Call, response: Response) {
                respuesta=response
            }
        })

        if(respuesta!=null) {
            Toast.makeText(applicationContext, respuesta!!.body()?.string(), Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
        }

    }
}
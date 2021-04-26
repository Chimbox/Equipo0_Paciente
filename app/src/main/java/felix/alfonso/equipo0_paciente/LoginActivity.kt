package felix.alfonso.equipo0_paciente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import equipo0_dominio.Paciente
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class LoginActivity : AppCompatActivity() {

    val URL = "http://192.168.0.12:8084/Equipo0_GatewayPaciente/res/paciente/login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {

            var requestQueue = Volley.newRequestQueue(getApplicationContext())
            var obj = JSONObject()
            try {
                //input your API parameters
                obj.put("username", etUsername.text.toString())
                obj.put("password", etPassword.text.toString())
            } catch (e: JSONException) {
                e.printStackTrace();
            }
            // Enter the correct url for your api service site
            var jsonObjectRequest = JsonObjectRequest(Request.Method.POST, URL, obj,
                {
                    var gson=GsonBuilder().create()
                    if(!it.has("estado")) {
                            MainActivity.usuarioActivo =
                                gson.fromJson(it.toString(), Paciente::class.java)
                            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG)
                                .show()
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                    }
                }, {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                });
            requestQueue.add(jsonObjectRequest);
        }

        /*try {
            val requestQueue = Volley.newRequestQueue(this)
            val jsonBody = JSONObject()
            jsonBody.put("username", etUsername.text.toString())
            jsonBody.put("password", etPassword.text.toString())
            val mRequestBody = jsonBody.toString()
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, URL,
                Response.Listener { response -> Log.i("LOG_VOLLEY", response) },
                Response.ErrorListener { error -> Log.e("LOG_VOLLEY", error.toString()) }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    try {
                        if (mRequestBody == null) null else return mRequestBody.toByteArray(charset("utf-8"))
                    } catch (uee: UnsupportedEncodingException) {
                        VolleyLog.wtf(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            mRequestBody,
                            "utf-8"
                        )

                    }
                    return "".toByteArray()
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    var responseString = ""
                    if (response != null) {
                        responseString = response.statusCode.toString()
                    }
                    return Response.success(
                        responseString,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
            requestQueue.add(stringRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }*/
    }
}
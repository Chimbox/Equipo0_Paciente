package felix.alfonso.equipo0_paciente

import android.annotation.SuppressLint
import android.content.Intent
import android.net.http.X509TrustManagerExtensions
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import equipo0_dominio.Paciente
import felix.alfonso.equipo0_paciente.http.ChimboxJsonObjectRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


class LoginActivity : AppCompatActivity() {

    val URL = "https://192.168.0.16:8443/Equipo0_GatewayPaciente/res/paciente/login"
    val URL_DATOS = "https://192.168.0.16:8443/Equipo0_GatewayPaciente/res/paciente/obtenerdatos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        handleSSLHandshake()
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
                    var gson = GsonBuilder().create()

                    var token =
                        gson.fromJson(it.toString(), JsonElement::class.java).asJsonObject.get(
                            "token"
                        ).asString

                    MainActivity.token = token

                    var requestQueue = Volley.newRequestQueue(getApplicationContext())


                    var obj = JSONObject()

                    try {
                        //input your API parameters
                        obj.put("username", etUsername.text.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace();
                    }
                    var jsonObjectRequest = ChimboxJsonObjectRequest(
                        Request.Method.POST,
                        URL_DATOS,
                        obj,
                        {
                            MainActivity.usuarioActivo =
                                gson.fromJson(it.toString(), Paciente::class.java)
                            MainActivity.usuarioActivo.username = etUsername.text.toString()
                            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG)
                                .show()
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        },
                        {

                        },
                        token
                    )


                    requestQueue.add(jsonObjectRequest)

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

    @SuppressLint("TrulyRandom")
    fun handleSSLHandshake() {
        try {
            val trustAllCerts: Array<TrustManager> =
                arrayOf<TrustManager>(object:X509TrustManager {
                    val acceptedIssuers: Array<Any?>?
                        get() = arrayOfNulls(0)

                    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                })
            val sc: SSLContext = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())
            HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
                override fun verify(arg0: String?, arg1: SSLSession?): Boolean {
                    return true
                }
            })
        } catch (ignored: Exception) {
        }
    }
}
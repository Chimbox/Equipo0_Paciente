package felix.alfonso.equipo0_paciente.http

import android.os.Parcel
import android.os.Parcelable
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class ChimboxJsonObjectRequest(
    var metodo: Int,
    var uri: String,
    var jsonObject: JSONObject,
    var listener: Response.Listener<JSONObject>,
    var errorListene: Response.ErrorListener,
    var tokenAutorizacion:String
) :
    JsonObjectRequest(metodo, uri, jsonObject, listener, errorListene) {
    override fun getHeaders(): MutableMap<String, String> {
        var headers=HashMap<String,String>(super.getHeaders())
        headers.put("Autorizacion",tokenAutorizacion)
        return headers
    }
    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        try {
            val jsonString = String(
                response.data,
                Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            return Response.success(
                JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        } catch (je: JSONException) {
            return Response.error(ParseError(je))
        }

    }
}
package com.example.klsdinfo.Volley

import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class CustomJsonObjectRequest : Request<JSONObject> {

    private val response: Response.Listener<JSONObject>? = null
    private var params: Map<String, String>? = null

    constructor(
        method: Int,
        url: String,
        params: Map<String, String>,
        response: Response.Listener<JSONObject>,
        listener: Response.ErrorListener
    ) : super(method, url, listener) {
        this.params = params
    }


    constructor(url: String, params: Map<String, String>, response: Response.Listener<JSONObject>,listener: Response.ErrorListener) : super(Request.Method.GET, url, listener) {
        this.params = params
    }


    @Throws(AuthFailureError::class)
    public override fun getParams(): Map<String, String>? {
        return params
    }


    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject>? {

        try {
            val js = String(response.data)//, HttpHeaderParser.parseCharset(response.headers))
            return Response.success(JSONObject(js), HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    override fun deliverResponse(response: JSONObject) {


    }


}
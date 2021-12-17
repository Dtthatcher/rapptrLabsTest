package com.datechnologies.androidtest.model.api.login

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface LoginInterface {

    @FormUrlEncoded
    @POST("Tests/scripts/login.php")

    suspend fun sendLogin(@FieldMap params: HashMap<String, String>): Response<ResponseBody>


}
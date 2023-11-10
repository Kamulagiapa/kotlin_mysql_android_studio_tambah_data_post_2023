package com.example.afteruts

import com.example.afteruts.Model.Response
import com.example.afteruts.Model.SubmitModel
import retrofit2.Call
import retrofit2.http.*
interface ApiServices {

   @GET("cariposts")
   fun searchPosts(@Query("q")terms:String?):Call<Response>
   @GET("data")
   fun getmahasiswa():
           Call<Response>
   @FormUrlEncoded
   @POST("storedata")
   fun kirimBiodata(
      @Field("nim") nim: String,
      @Field("namalengkap") namelengkap: String,
      @Field("usia") usia: String,
      @Field("gender") gender: String,
      @Field("alamat") alamat: String
   ): Call<SubmitModel>

}
package com.nprmanbrandons11.myapplication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PhotoService {
    @POST ("random")
    suspend fun uploadPhoto(@Body insertDocumentRequest: InsertDocumentRequest):Response<Any>
    @GET("random")
    suspend fun getPhoto():Response<DogResponse>

}
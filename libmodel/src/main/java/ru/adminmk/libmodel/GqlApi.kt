package ru.adminmk.libmodel

import retrofit2.http.Body
import retrofit2.http.POST
import ru.adminmk.libmodel.dto.graphql.SchemaQueryResult
import ru.adminmk.libmodel.dto.graphql.SchemaRequest

interface GqlApi {
    @POST(URL)
    suspend fun runQuery(@Body body: SchemaRequest): SchemaQueryResult

    companion object {
        private const val URL = "https://countries.trevorblades.com/"
    }
}

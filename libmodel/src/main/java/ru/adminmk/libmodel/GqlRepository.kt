package ru.adminmk.libmodel

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.adminmk.libmodel.dto.QueryDto
import ru.adminmk.libmodel.dto.graphql.SchemaException
import ru.adminmk.libmodel.dto.graphql.SchemaRequest
import ru.adminmk.libmodel.entity.Continent

class GqlRepository {
    private val adapter: SchemaAdapter = getGqlAdapter()

    private fun getGqlApi(): GqlApi {
        val clientBuilder = OkHttpClient.Builder().apply {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            addInterceptor(loggingInterceptor)
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://override.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()

        return retrofit.create(GqlApi::class.java)
    }

    private fun getGqlAdapter(): SchemaAdapter {
        return object : SchemaAdapter {
            private val api: GqlApi = getGqlApi()

            override suspend fun executeQuery(
                query: String,
                variables: Map<String, Any?>
            ): QueryDto {

                val requestModel = SchemaRequest(query, variables)
                val result = api.runQuery(requestModel)

                result.errors?.takeIf { it.isNotEmpty() }?.let {
                    throw SchemaException("GraphQL query failed", requestModel, it)
                }

                return result.data ?: throw SchemaException(
                    "GraphQL query completes successfully but returns no data",
                    requestModel
                )
            }
        }
    }

    suspend fun test(): List<Continent> {
        val schemaContext = schemaContextOf(adapter)
        val result = schemaContext.query {
            continents {
//            continent(code = "AF") {
                code()
//                name()
            }
        }
        return result.continents
    }
}

package com.example.data.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object EmailNotificationService {
    private const val TAG = "EmailNotification"
    private const val TARGET_EMAIL = "bernardomalele5@gmail.com"

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun sendRegistrationNotice(
        context: Context,
        nome: String,
        telemovel: String,
        idade: Int
    ): Boolean = withContext(Dispatchers.IO) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("pt", "AO"))
        val dataHora = dateFormat.format(Date())

        try {
            // Send payload to notification relay service
            // Formspree / Webhook format without embedding hardcoded credentials
            val jsonPayload = JSONObject().apply {
                put("recipient", TARGET_EMAIL)
                put("subject", "Novo Registo no App Pele Linda - $nome")
                put("nome", nome)
                put("telemovel", telemovel)
                put("idade", idade)
                put("data_hora", dataHora)
                put("origem", "Pele Linda Android App")
                put("observacao", "Utilizadora iniciou o teste gratuito de 7 dias.")
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonPayload.toString().toRequestBody(mediaType)

            // Submit to reliable public notification relay
            val request = Request.Builder()
                .url("https://formspree.io/f/mqkvyqze")
                .post(body)
                .addHeader("Accept", "application/json")
                .build()

            val response = client.newCall(request).execute()
            val success = response.isSuccessful
            Log.d(TAG, "Notificacao de registo enviada (status: ${response.code}): $success")
            success
        } catch (e: Exception) {
            Log.w(TAG, "Envio em segundo plano falhou (a conta continua ativa normalmente): ${e.localizedMessage}")
            // The registration is not blocked, the app logs and can retry silently
            false
        }
    }
}

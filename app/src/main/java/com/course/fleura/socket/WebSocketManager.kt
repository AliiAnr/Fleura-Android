package com.course.fleura.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import java.net.URISyntaxException

data class WsOrderStatusData(
    val orderId: String,
    val status: String,
    val message: String? = null
)

data class WsPaymentStatusData(
    val orderId: String,
    val status: String,
    val message: String? = null
)

class WebSocketManager {
      val TAG = "WebSocketManager"
      val DEFAULT_URL = "https://fleura-nestjs-production.up.railway.app"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _orderStatusUpdates = MutableSharedFlow<WsOrderStatusData>(
        replay = 1, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val orderStatusUpdates: SharedFlow<WsOrderStatusData> = _orderStatusUpdates

    private val _paymentStatusUpdates = MutableSharedFlow<WsPaymentStatusData>(
        replay = 1, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val paymentStatusUpdates: SharedFlow<WsPaymentStatusData> = _paymentStatusUpdates

    private var socket: Socket? = null
    private var currentOrderId: String? = null

    /**
     * Buka koneksi dan join room orderId
     */
    fun connect(orderId: String, socketUrl: String = DEFAULT_URL) {
        if (currentOrderId == orderId && socket?.connected() == true) {
            Log.d(TAG, "➤ Sudah terhubung ke room order: $orderId")
            return
        }
        // tutup koneksi lama dulu
        disconnect()

        try {
            socket = IO.socket(socketUrl).apply {
                currentOrderId = orderId

                on(Socket.EVENT_CONNECT) {
                    Log.d(TAG, "✅ Connected! joinRoom($orderId)")
                    emit("joinRoom", orderId)
                }

//                onAnyIncoming { args ->
//                    Log.d(TAG, "[IN] ${args.contentToString()}")
//                    Log.d(TAG, "ADAAAA")
//
//                    val event = args.getOrNull(0) as? String
//                    val payload = args.getOrNull(1)
//
//                    when (event) {
//                        "order:statusChanged"   -> parseAndEmitOrder(arrayOf(payload!!))
//                        "payment:statusChanged" -> parseAndEmitPayment(arrayOf(payload!!))
//                    }
//                }

                // debug: tangkap semua event


                on("order:statusChanged") { args ->
                    parseAndEmitOrder(args)
                }

                on("payment:statusChanged") { args ->
                    parseAndEmitPayment(args)
                }

                on(Socket.EVENT_DISCONNECT) {
                    Log.d(TAG, "❌ Disconnected from server (order=$orderId)")
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.e(TAG, "‼️ Connect error: ${args.getOrNull(0)}")
                }

                connect()
            }
        } catch (e: URISyntaxException) {
            Log.e(TAG, "🔗 URL tidak valid: $socketUrl", e)
        }
    }

    private fun parseAndEmitOrder(args: Array<Any>) {
        runCatching {
            // bisa saja args[0] String atau JSONObject
            val raw = args.getOrNull(0)
            val json = when (raw) {
                is JSONObject -> raw
                is String     -> JSONObject(raw)
                else           -> throw IllegalArgumentException("Payload bukan JSON: $raw")
            }
            val data = WsOrderStatusData(
                orderId = json.getString("orderId"),
                status  = json.getString("status"),
                message = json.optString("message", null)
            )
            Log.d(TAG, "🔄 Order update parsed: $data")
            scope.launch { _orderStatusUpdates.emit(data) }
        }.onFailure {
            Log.e(TAG, "⚠️ Gagal parsing order:statusChanged", it)
        }
    }

    private fun parseAndEmitPayment(args: Array<Any>) {
        runCatching {
            val raw = args.getOrNull(0)
            val json = when (raw) {
                is JSONObject -> raw
                is String     -> JSONObject(raw)
                else           -> throw IllegalArgumentException("Payload bukan JSON: $raw")
            }
            val data = WsPaymentStatusData(
                orderId = json.getString("orderId"),
                status  = json.getString("status"),
                message = json.optString("message", null)
            )
            Log.d(TAG, "🔄 Payment update parsed: $data")
            scope.launch { _paymentStatusUpdates.emit(data) }
        }.onFailure {
            Log.e(TAG, "⚠️ Gagal parsing payment:statusChanged", it)
        }
    }

    /** Tutup koneksi */
    fun disconnect() {
        socket?.let {
            if (it.connected()) {
                it.disconnect()
                Log.d(TAG, "🔒 Socket untuk order $currentOrderId ditutup")
            }
        }
        socket = null
        currentOrderId = null
    }

    /** Cek status */
    fun isConnected(): Boolean = socket?.connected() ?: false

companion object {
        @Volatile
        private var INSTANCE: WebSocketManager? = null

        fun getInstance(): WebSocketManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WebSocketManager().also { INSTANCE = it }
            }
        }
    }
}

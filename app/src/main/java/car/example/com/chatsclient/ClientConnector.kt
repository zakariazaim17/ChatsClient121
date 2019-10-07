package car.example.com.chatsclient

import com.google.gson.Gson
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.Exception


object ClientConnector: ChatClientObservable,Runnable {

    private lateinit var socket:Socket
    private lateinit var out:PrintWriter
    private var connected = true

    private var clientObservers = mutableSetOf<ChatClientObserver>()

    override fun deregisterObserver(observer: ChatClientObserver) {
        clientObservers.remove(observer)
    }

    override fun registerObserver(observer: ChatClientObserver) {
        clientObservers.add(observer)
    }
    override fun notify(msg: Message) {
        clientObservers.forEach { it.updateMessage(msg) }
    }

    override fun run() {
        try {
            socket = Socket("192.168.43.72",30003)
            println("port number: ${socket.localPort}")
            println("New connection established ${socket.inetAddress.hostAddress} ${socket.port}")
            out = PrintWriter(socket.getOutputStream(),true)
            val scanner = Scanner(socket.getInputStream())
            while (connected){
                if(scanner.hasNextLine()) {
                    val line = scanner.nextLine()
                    println(line)
                    notify(Message(line, "", ""))
                }

            }

        }catch (e: Exception){e.printStackTrace()}
    }

    fun sendToServer(msg:ChatMessage){
        try {
                val gSon = Gson()
                val msgToServer = gSon.toJson(msg)
                out.println(msgToServer)


        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }
}
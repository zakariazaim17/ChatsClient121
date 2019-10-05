package car.example.com.chatsclient

import java.util.*

class ChatMessage(var command:String,
                  private val message: String,
                  val userName:String) {

    //message structure
    override fun toString(): String {

        return "$message from $userName"
    }
}
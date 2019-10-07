package car.example.com.chatsclient

class ChatMessage(var command:String,
                  private val message: String,
                  private val userName:String) {

    //message structure
    override fun toString(): String {

        return "$message from $userName"
    }
}
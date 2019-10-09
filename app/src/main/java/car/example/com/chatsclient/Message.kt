package car.example.com.chatsclient

class Message(var chatMsg:String,  val userName:String,  val Time:String) {

    //message structure

    override fun toString(): String {
        return "$chatMsg from $userName on $Time"
    }

}
package car.example.com.chatsclient

interface ChatClientObserver {

    fun updateMessage(msg: Message){}
}
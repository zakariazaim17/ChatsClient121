package car.example.com.chatsclient

interface ChatClientObservable {
    fun deregisterObserver(observer: ChatClientObserver)
    fun registerObserver(observer: ChatClientObserver)
    fun notify(msg:Message)

}
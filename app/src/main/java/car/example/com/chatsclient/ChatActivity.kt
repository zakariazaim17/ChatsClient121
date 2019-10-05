package car.example.com.chatsclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(),ChatClientObserver {
    private lateinit var recyclerView: RecyclerView
    private lateinit var myLayoutManager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    private var message=""
    private var time=""
    private var user=""
    private val messagesList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        ClientConnector.registerObserver(this)
        createRecyclerView()

        button3.setOnClickListener {
            Thread(Runnable {
                if (editText2.text.isNotEmpty()){
                    val msg = editText2.text.toString()
                    ClientConnector.sendToServer(ChatMessage("",msg,AppUsers.user))
                    editText2.text.clear()
                }
            }).start()
        }
    }

    override fun updateMessage(msg: Message) {
        message = msg.chatMsg.substringBeforeLast("from")
        user = msg.chatMsg.substringAfterLast("from").substringBefore("at")
        time = msg.chatMsg.substringAfterLast("at")
        messagesList.add(Message(message,user,time).toString())
        runOnUiThread { myAdapter.notifyDataSetChanged() }
    }

    private fun createRecyclerView(){
        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.setHasFixedSize(true)
        myLayoutManager = LinearLayoutManager(this)
        myAdapter = MyRecyclerViewAdapter(this, messagesList)

        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = myAdapter
    }

}

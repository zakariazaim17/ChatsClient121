package car.example.com.chatsclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
        setSupportActionBar(toolbar)

        ClientConnector.registerObserver(this)
        createRecyclerView()

        button3.setOnClickListener {
            Thread(Runnable {
                if (editText2.text.isNotEmpty()) {
                    val msg = editText2.text.toString()
                    ClientConnector.sendToServer(ChatMessage("", msg, ChatAppUser.user))
                    runOnUiThread{editText2.text.clear()}
                }
            }).start()
        }

    }

    override fun updateMessage(msg: Message) {
        val mes = msg.chatMsg.removePrefix("@")
        message = mes.substringBeforeLast("from")
        user = mes.substringAfterLast("from").substringBefore("on")
        time = mes.substringAfterLast("on")
        println(Message(message, user, time).toString())
        messagesList.add(Message(message, user, time).toString())
        runOnUiThread { myAdapter.notifyDataSetChanged() }

    }

    private fun createRecyclerView(){
        recyclerView = findViewById(R.id.RecyclerView_Chat)
        recyclerView.setHasFixedSize(true)
        myLayoutManager = LinearLayoutManager(this)
        myAdapter = MyRecyclerViewAdapter(this, messagesList)

        recyclerView.layoutManager = myLayoutManager
        recyclerView.adapter = myAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.Users ->{
                val intent = Intent(this,ChatUsers::class.java)
                startActivity(intent)
            }
            R.id.Messages->{
                val intent = Intent(this,MessageListActivity::class.java)
                startActivity(intent)
            }
            R.id.TopChatters->{
                val intent = Intent(this,TopChatter::class.java)
                startActivity(intent)
            }
            R.id.LogOut->{
                Thread(Runnable {
                    ClientConnector.sendToServer(ChatMessage("exit", "", ChatAppUser.user))
                }).start()
                ClientConnector.deregisterObserver(this)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)

    }


}
//{"command":"","message":"that is from me","userName":"user2"}

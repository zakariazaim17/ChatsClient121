package car.example.com.chatsclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_message__list_.*

class MessageListActivity : AppCompatActivity(), ChatClientObserver {

    private lateinit var recyclerView3: RecyclerView
    private lateinit var myLayoutManager3: RecyclerView.LayoutManager
    private lateinit var myAdapter3: RecyclerView.Adapter<*>

    private var messageList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message__list_)
        setSupportActionBar(toolbar3)

        ClientConnector.registerObserver(this)
        createRecyclerView()


        Thread(Runnable {
            ClientConnector.sendToServer(ChatMessage("messages", "", ChatAppUser.user))
        }).start()
    }

    override fun updateMessage(msg: Message) {
        if(!messageList.contains(msg.chatMsg)&& !msg.chatMsg.startsWith("@")) {
            messageList.add(msg.chatMsg)
            runOnUiThread { myAdapter3.notifyDataSetChanged() }
        }
    }

    private fun createRecyclerView(){
        recyclerView3 = findViewById(R.id.recyclerView_Messages)
        recyclerView3.setHasFixedSize(true)
        myLayoutManager3 = LinearLayoutManager(this)
        myAdapter3 = MyRecyclerViewAdapter(this, messageList)

        recyclerView3.layoutManager = myLayoutManager3
        recyclerView3.adapter = myAdapter3
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_messages, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.Users ->{
                val intent = Intent(this,ChatUsers::class.java)
                startActivity(intent)
            }
            R.id.Chat->{
                val intent = Intent(this,ChatActivity::class.java)
                startActivity(intent)
            }
            R.id.Update->{
                Thread(Runnable {
                    ClientConnector.sendToServer(ChatMessage("messages", "", ChatAppUser.user))
                }).start()
            }
            R.id.TopChatters->{
                val intent = Intent(this, TopChatter::class.java)
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

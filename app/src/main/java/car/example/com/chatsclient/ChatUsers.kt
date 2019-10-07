package car.example.com.chatsclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat_users.*

class ChatUsers : AppCompatActivity(), ChatClientObserver {

    private lateinit var recyclerView2: RecyclerView
    private lateinit var myLayoutManager2: RecyclerView.LayoutManager
    private lateinit var myAdapter2: RecyclerView.Adapter<*>

    private var usersList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_users)
        setSupportActionBar(toolbar)

        ClientConnector.registerObserver(this)
        createRecyclerView()


        Thread(Runnable {
            ClientConnector.sendToServer(ChatMessage("users", "", ChatAppUser.user))
        }).start()

    }


    override fun updateMessage(msg: Message) {
        if(!usersList.contains(msg.chatMsg)) {
            usersList.add(msg.chatMsg)
            runOnUiThread { myAdapter2.notifyDataSetChanged() }
        }

    }

    private fun createRecyclerView(){
        recyclerView2 = findViewById(R.id.RecyclerView_User)
        recyclerView2.setHasFixedSize(true)
        myLayoutManager2 = LinearLayoutManager(this)
        myAdapter2 = MyRecyclerViewAdapter(this, usersList)

        recyclerView2.layoutManager = myLayoutManager2
        recyclerView2.adapter = myAdapter2
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.Chat ->{
                val intent = Intent(this,ChatActivity::class.java)
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
            R.id.Update->{
                Thread(Runnable {
                    ClientConnector.sendToServer(ChatMessage("users", "", ChatAppUser.user))
                }).start()
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

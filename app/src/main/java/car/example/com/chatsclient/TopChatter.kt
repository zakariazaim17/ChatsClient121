package car.example.com.chatsclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_top_chatter.*

class TopChatter : AppCompatActivity(), ChatClientObserver {

    private lateinit var recyclerView4: RecyclerView
    private lateinit var myLayoutManager4: RecyclerView.LayoutManager
    private lateinit var myAdapter4: RecyclerView.Adapter<*>

    private var topChatterList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_chatter)
        setSupportActionBar(toolbar3)

        ClientConnector.registerObserver(this)
        createRecyclerView()

        Thread(Runnable {
            ClientConnector.sendToServer(ChatMessage("topChatter", "", ChatAppUser.user))
        }).start()

    }

    override fun updateMessage(msg: Message) {
        if(!topChatterList.contains(msg.chatMsg)) {
            topChatterList.add(msg.chatMsg)
            runOnUiThread { myAdapter4.notifyDataSetChanged() }
        }

    }

    private fun createRecyclerView(){
        recyclerView4 = findViewById(R.id.RecyclerView_User)
        recyclerView4.setHasFixedSize(true)
        myLayoutManager4 = LinearLayoutManager(this)
        myAdapter4 = MyRecyclerViewAdapter(this, topChatterList)

        recyclerView4.layoutManager = myLayoutManager4
        recyclerView4.adapter = myAdapter4
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_topchatter, menu)
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
                    ClientConnector.sendToServer(ChatMessage("topChatter", "", ChatAppUser.user))
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

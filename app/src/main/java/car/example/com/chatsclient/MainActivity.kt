package car.example.com.chatsclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ChatClientObserver {

    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ClientConnector.registerObserver(this)
        Thread(ClientConnector).start()

        button2.setOnClickListener {
            Thread(Runnable {
                if (editText.text.isNotEmpty()){
                    userName = editText.text.toString()
                    ChatAppUser.user = userName
                    ClientConnector.sendToServer(ChatMessage(":user","",ChatAppUser.user))
                }else{
                    Toast.makeText(this, "Username not set", Toast.LENGTH_LONG).show()
                }
                }).start()
        }

    }

    override fun updateMessage(msg: Message) {
        if (msg.chatMsg == ":User set to $userName.") {
            val intent = Intent(this, ChatUsers::class.java)
            startActivity(intent)
        }
    }

}

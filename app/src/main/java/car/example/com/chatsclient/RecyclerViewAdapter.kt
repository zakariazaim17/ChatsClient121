package car.example.com.chatsclient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MyRecyclerViewAdapter(private val context: Context, private val myData: List<String>): RecyclerView.Adapter<MyViewHolder>(){

    override fun onCreateViewHolder(vg: ViewGroup, vt: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.myitemlayout, vg,
                false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return myData.size
    }

    override fun onBindViewHolder(vh: MyViewHolder, pos: Int) {
        vh.itemView.findViewById<TextView>(R.id.textView).text =
            myData[ pos ]
    }



}
class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


package com.example.kriptopara.adapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kriptopara.R
import com.example.kriptopara.model.CryptoModel

class RecyclerViewAdapter(private val cryptoList: ArrayList<CryptoModel>, private val listener: Listener): RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {


    interface Listener{
        fun onItemClick(cryptoModel: CryptoModel)
    }

private val colors: Array<String> = arrayOf("#B4B4B8", "#C7C8CC", "#E3E1D9", "#F2EFE5")


    class RowHolder(view:View): RecyclerView.ViewHolder(view){

        fun bind(cryptoModel: CryptoModel, colors: Array<String>, position: Int, listener: Listener){
            itemView.setOnClickListener{
                listener.onItemClick(cryptoModel)
        }
            itemView.setBackgroundColor(Color.parseColor(colors[position % 4]))
            val nameTxt = itemView.findViewById<TextView>(R.id.nameTxt)
            val priceTxt = itemView.findViewById<TextView>(R.id.priceTxt)

            nameTxt.text = cryptoModel.currency
            priceTxt.text = cryptoModel.price

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_row, parent, false)
        return RowHolder(view)
    }

    override fun getItemCount(): Int {
        return cryptoList.count()
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(cryptoList[position], colors, position, listener)
    }


}
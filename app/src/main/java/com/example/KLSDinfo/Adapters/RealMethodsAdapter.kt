package com.example.KLSDinfo.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Models.Method
import com.example.KLSDinfo.R

class RealMethodsAdapter(
    val context: Context,
    private val items: List<Method>) : RecyclerView.Adapter<RealMethodsAdapter.MethodViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealMethodsAdapter.MethodViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return MethodViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RealMethodsAdapter.MethodViewHolder, position: Int) {
        val method: Method = items[position]

        holder.nameTV.text = method.name
        holder.descTV.text = method.description

        holder.cardView.setOnClickListener {
            Toast.makeText(context,"Metodo: ${items[position].name}", Toast.LENGTH_LONG).show()

        }

        displayImage(holder, method)


    }


    private fun displayImage(holder: MethodViewHolder, method: Method){
        if(method.image != null){
            holder.imgView.setImageResource(method.image!!)
            holder.imgView.colorFilter = null
        }else{
            holder.imgView.setImageResource(R.mipmap.ic_launcher)
        }
    }


    class MethodViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nameTV: TextView = itemView.findViewById(R.id.method_description)
        val descTV: TextView = itemView.findViewById(R.id.method_description2)
        val imgView: ImageView = itemView.findViewById(R.id.method_photo)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }







}


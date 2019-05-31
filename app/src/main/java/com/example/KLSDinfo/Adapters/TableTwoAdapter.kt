package com.example.KLSDinfo.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Models.Method
import com.example.KLSDinfo.R
import com.example.KLSDinfo.RealTime.RSelectionLocationFragment
import com.example.KLSDinfo.RealTime.RSelectionPersonFragment

class TableTwoAdapter(
    private val context: Context,
    private val items: List<Method>) : RecyclerView.Adapter<TableTwoAdapter.MethodViewHolder>()

{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.method_view, parent, false)
        return MethodViewHolder(itemView)

    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        val method: Method = items[position]

        holder.nameTV.text = method.name
        holder.descTV.text = method.description

        holder.cardView.setOnClickListener {
            Toast.makeText(context,"Metodo: ${items[position].name}", Toast.LENGTH_LONG).show()
            when(position){
                0 -> {
                    navigateToFragment(RSelectionLocationFragment.newInstance(),it,true)

                }

                1 -> {
                    navigateToFragment(RSelectionPersonFragment.newInstance(), it, true)
                }
            }
        }

        displayImage(holder, method)


    }


    private fun navigateToFragment(fragToGo: Fragment,view:View, addToBackStack: Boolean = false){
        val activity = view.context as AppCompatActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
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


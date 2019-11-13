package com.example.klsdinfo.main.adapters

import android.content.Context
import android.os.Bundle
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
import com.example.klsdinfo.R
import com.example.klsdinfo.data.models.Method
import com.example.klsdinfo.main.SelectionFragments.SelectLocationAndTimeFragment
import com.example.klsdinfo.main.SelectionFragments.SelectPersonAndTimeFragment

class HistoryMethodsAdapter(
    private val context: Context,
    private val items: List<Method>) : RecyclerView.Adapter<HistoryMethodsAdapter.MethodViewHolder>()

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

        holder.overlineTV.text = "Acessing History"
        holder.nameTV.text = method.name
        holder.descTV.text = method.description

        holder.cardView.setOnClickListener {
            Toast.makeText(context,"Metodo: ${items[position].name}", Toast.LENGTH_LONG).show()

            // TODO: remenber to also pass some reference aboute the choosen method
            when(position){
//                0 -> { // person
//
//                    val frag =
//                        SelectPersonAndTimeFragment.newInstance()
//                    val bundle = Bundle()
//                    bundle.putInt("ref", 0)
//                    frag.arguments = bundle
//                    navigateToFragment(frag, it,true)
//                }
                0 -> {// person: Here we algo need to choose target person
                    val frag =
                        SelectPersonAndTimeFragment.newInstance()
                    val bundle = Bundle()
                    bundle.putInt("ref", 1)
                    frag.arguments = bundle
                    navigateToFragment(frag, it,true)

                }

                1 -> {
                    val bundle = Bundle()
                    val frag =
                        SelectLocationAndTimeFragment.newInstance()
                    bundle.putInt("ref", 2)
                    frag.arguments = bundle
                    navigateToFragment(frag, it,true)
                }
            }
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

        val overlineTV: TextView = itemView.findViewById(R.id.header)
        val nameTV: TextView = itemView.findViewById(R.id.method_name)
        val descTV: TextView = itemView.findViewById(R.id.method_description)
        val imgView: ImageView = itemView.findViewById(R.id.method_image)
        val cardView: CardView = itemView.findViewById(R.id.card_view)
    }


    private fun navigateToFragment(fragToGo: Fragment, view:View, addToBackStack: Boolean = false){
        val activity = view.context as AppCompatActivity
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragToGo)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        if(addToBackStack){
            transaction.addToBackStack(null) // Todo: verificar o ciclo de vida dos fragmentos
        }
        transaction.commit()
    }




}


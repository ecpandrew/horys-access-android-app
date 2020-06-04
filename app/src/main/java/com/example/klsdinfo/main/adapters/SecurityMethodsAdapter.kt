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
import com.example.klsdinfo.main.SecurityFragments.GenerateCSRFragment
import com.example.klsdinfo.main.SecurityFragments.ImportCertificatesFragment
import com.example.klsdinfo.main.SecurityFragments.VerifyCertificatesFragment
import com.example.klsdinfo.main.SelectionFragments.SelectLocationAndTimeFragment
import com.example.klsdinfo.main.SelectionFragments.SelectPersonAndTimeFragment

class SecurityMethodsAdapter (
    private val context: Context,
    private val items: List<Method>) : RecyclerView.Adapter<SecurityMethodsAdapter.MethodViewHolder>()

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

        holder.overlineTV.text = "Security Service"
        holder.nameTV.text = method.name
        holder.descTV.text = method.description

        holder.cardView.setOnClickListener {
            Toast.makeText(context,"Metodo: ${items[position].name}", Toast.LENGTH_LONG).show()

            // TODO: remenber to also pass some reference aboute the choosen method
            when(position){

                0 -> {// person: Here we algo need to choose target person
                    val frag =
                        GenerateCSRFragment.newInstance(context)
//                    val bundle = Bundle()
//                    bundle.putInt("ref", 1)
//                    frag.arguments = bundle
                    navigateToFragment(frag, it,true)

                }

                1 -> {
                    val frag =
                        ImportCertificatesFragment.newInstance(context)
                        navigateToFragment(frag, it,true)
                }
                2 -> {
                    val frag =
                        VerifyCertificatesFragment.newInstance(context)
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


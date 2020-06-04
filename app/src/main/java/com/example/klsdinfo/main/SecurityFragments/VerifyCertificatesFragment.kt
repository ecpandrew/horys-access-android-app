package com.example.klsdinfo.main.SecurityFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.klsdinfo.R

class VerifyCertificatesFragment  : Fragment(), LifecycleOwner {

    private lateinit var ctx: Context

    companion object {
        fun newInstance(context: Context): VerifyCertificatesFragment {
            val instance : VerifyCertificatesFragment = VerifyCertificatesFragment()
            instance.ctx = context
            return VerifyCertificatesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.verify_certificates_layout, container, false)

        return view
    }


}
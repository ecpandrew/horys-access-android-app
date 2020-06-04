package com.example.klsdinfo.main.SecurityFragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import br.ufma.lsdi.cddl.CDDL
import br.ufma.lsdi.cddl.network.SecurityService
import com.example.klsdinfo.R

class GenerateCSRFragment : Fragment(), LifecycleOwner {

    private lateinit var ctx: Context
    private lateinit var securityService: SecurityService

    private lateinit var cnET : EditText
    private lateinit var ouET : EditText
    private lateinit var oET : EditText
    private lateinit var lET : EditText
    private lateinit var sET : EditText
    private lateinit var cET : EditText

    private lateinit var cnValue : String
    private lateinit var ouValue : String
    private lateinit var oValue : String
    private lateinit var lValue : String
    private lateinit var sValue : String
    private lateinit var cValue : String

    private lateinit var checkBox: CheckBox
    private lateinit var generateBtn: Button



    companion object {
        fun newInstance(context: Context): GenerateCSRFragment {
            val instance : GenerateCSRFragment = GenerateCSRFragment()
            instance.ctx = context
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.generate_csr_layout, container, false)
        securityService = CDDL.getSecurityServiceInstance(ctx)

        bindEditTexts(view)


        generateBtn.setOnClickListener(View.OnClickListener {
            val checked : Boolean = checkBox.isChecked

            if(checked){
                getValues()
                securityService.generateCSR(cnValue, ouValue, oValue, lValue, sValue, cValue)
                Toast.makeText(ctx,"CSR Generated successfuly!", Toast.LENGTH_LONG).show()
                clearUI()
            }else{
                Toast.makeText(ctx,"You need to confirm first!", Toast.LENGTH_LONG).show()
            }
        })
        return view
    }

    private fun clearUI() {
        cnET.setText("")
        ouET.setText("")
        oET.setText("")
        lET.setText("")
        sET.setText("")
        cET.setText("")
        checkBox.isChecked = false

    }

    private fun bindEditTexts(view: View) {
        cnET = view.findViewById(R.id.cn_et)
        ouET = view.findViewById(R.id.ou_et)
        oET = view.findViewById(R.id.ou_et)
        lET = view.findViewById(R.id.l_et)
        sET = view.findViewById(R.id.s_et)
        cET = view.findViewById(R.id.c_et)
        checkBox = view.findViewById(R.id.checkBox)
        generateBtn = view.findViewById(R.id.generate_csr_btn)

    }

    private fun getValues(){
        cnValue = cnET.text.toString()
        ouValue = ouET.text.toString()
        oValue = oET.text.toString()
        lValue = lET.text.toString()
        sValue = sET.text.toString()
        cValue = cET.text.toString()
    }


}
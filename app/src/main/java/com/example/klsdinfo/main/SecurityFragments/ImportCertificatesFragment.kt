package com.example.klsdinfo.main.SecurityFragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import br.ufma.lsdi.cddl.CDDL
import br.ufma.lsdi.cddl.network.SecurityService
import com.example.klsdinfo.R
import easyfilepickerdialog.kingfisher.com.library.model.DialogConfig
import easyfilepickerdialog.kingfisher.com.library.model.SupportFile
import easyfilepickerdialog.kingfisher.com.library.view.FilePickerDialogFragment
import org.easymock.internal.matchers.Null
import java.io.File
import java.io.FileNotFoundException
import java.lang.NullPointerException
import java.security.cert.X509Certificate

class ImportCertificatesFragment : Fragment(), LifecycleOwner {

    private lateinit var ctx: Context
    private var importCaCerticiateBtn: Button? = null
    private var importCertificateBtn: Button? = null

    private lateinit var ca_tv : TextView
    private lateinit var ca_issuer_tv : TextView
    private lateinit var client_tv : TextView
    private lateinit var client_issuer_tv : TextView

    private lateinit var verift1_tv : TextView

    private lateinit var verift2_tv : TextView

    private var dialogConfig: DialogConfig? = null

    private lateinit var securityService: SecurityService

    companion object {
        fun newInstance(context: Context): ImportCertificatesFragment {
            val instance : ImportCertificatesFragment = ImportCertificatesFragment()
            instance.ctx = context
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.import_certificates_layout, container, false)
        securityService = CDDL.getSecurityServiceInstance(ctx)

        bindEditTexts(view)

        dialogConfig = DialogConfig.Builder()
            .enableMultipleSelect(true) // default is false
            .enableFolderSelect(true) // default is false
            .initialDirectory(
                Environment.getExternalStorageDirectory()
                    .absolutePath + File.separator + "Download"
            ) // default is sdcard
            .supportFiles(
                SupportFile(
                    ".crt",
                    R.drawable.ic_file
                )
            ) // default is showing all file types.
            .build()

        setOnClickListeners()
        try {
            reloadUI()
        }catch ( E : NullPointerException){


        }


        return view
    }


    private fun bindEditTexts(view: View) {
        ca_tv = view.findViewById(R.id.ca_tv)
        ca_issuer_tv = view.findViewById(R.id.ca_issuer_tv)
        client_tv = view.findViewById(R.id.client_tv)
        client_issuer_tv = view.findViewById(R.id.client_issuer_tv)
        verift1_tv = view.findViewById(R.id.verify1_tv)
        verift2_tv = view.findViewById(R.id.verify2_tv)
        importCaCerticiateBtn = view.findViewById(R.id.import_ca_btn)
        importCertificateBtn = view.findViewById(R.id.import_client_btn)


    }


    private fun reloadUI() {
        val ca =
            securityService.getCACertificate() as X509Certificate?
        val client =
            securityService.getCertificate() as X509Certificate?
        if (ca == null) {
            ca_tv.setText("No CA certificate found")
            ca_issuer_tv.setText("No CA certificate found")
        } else {
            ca_tv.setText(ca.subjectX500Principal.name)
            ca_issuer_tv.setText(ca.issuerX500Principal.name)

        }
        if (client == null) {
            client_tv.setText("No client certificate found")
            client_issuer_tv.setText("No client certificate found")
        } else {
            client_tv.setText(client.subjectX500Principal.name)
            client_issuer_tv.setText(client.issuerX500Principal.name)
        }


        val certCheck: Boolean = securityService.verifyCertificateAgainstPrivateKey()

        if (certCheck) {
            verift1_tv.setText("Verified")
            verift1_tv.setTextColor(Color.parseColor("#008000"))
        } else {
            verift1_tv.setText("NOT SECURE")
            verift1_tv.setTextColor(Color.parseColor("#FF0000"))
        }
    }

    private fun setOnClickListeners() {

        importCaCerticiateBtn!!.setOnClickListener { v: View? ->
            FilePickerDialogFragment.Builder()
                .configs(dialogConfig)
                .onFilesSelected { list ->
                    Log.e("DIALOG", "total Selected file: " + list.size)
                    for (file in list) {
                        Log.e("DIALOG", "Selected file: " + file.absolutePath)
                    }
                    if (list.size == 1) {
                        try {
                            securityService.setCaCertificate(list[0].name)
                            reloadUI()
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(
                            ctx,
                            "Select only one File!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .build()
                .show(fragmentManager!!, null)
        }
        importCertificateBtn!!.setOnClickListener { v: View? ->
            FilePickerDialogFragment.Builder()
                .configs(dialogConfig)
                .onFilesSelected { list ->
                    Log.e("DIALOG", "total Selected file: " + list.size)
                    for (file in list) {
                        Log.e("DIALOG", "Selected file: " + file.absolutePath)
                    }
                    if (list.size == 1) {
                        securityService.setCertificate(list[0].name)
                        reloadUI()
                    } else {
                        Toast.makeText(
                            ctx,
                            "Select only one File!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .build()
                .show(fragmentManager!!, null)
        }
    }


}
package com.example.KLSDinfo.Fragments.SelectionFragments
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.KLSDinfo.Adapters.SelectionAdapters.MultiCheckRoleAdapter
import com.example.KLSDinfo.Fragments.DialogFragments.FullscreenDialogFragment
import com.example.KLSDinfo.Models.MultiCheckRole
import com.example.KLSDinfo.Models.Person
import com.example.KLSDinfo.Models.Role
import com.example.KLSDinfo.R





open class RealSelectionPersonFragment : Fragment() {

    lateinit var mAdapter: MultiCheckRoleAdapter
    lateinit var items: MutableList<MultiCheckRole>

    companion object {
        fun newInstance(): RealSelectionPersonFragment {
            return RealSelectionPersonFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.real_multi_check_person, container, false)

        val rv : RecyclerView = view.findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(context)

        mAdapter = MultiCheckRoleAdapter(getMultiCheckRoles())

        rv.layoutManager = layoutManager
        rv.adapter = mAdapter


        val btnClear : Button = view.findViewById(R.id.clear_button)
        btnClear.setOnClickListener {
            mAdapter.clearChoices()
        }

        val check = view.findViewById(R.id.check_first_child) as Button
        check.setOnClickListener {
            mAdapter.checkChild(true, 0, 0)
        }

        val btnSend : Button = view.findViewById(R.id.btn_request)

        btnSend.setOnClickListener {


            val seletedElements: MutableList<Parcelable> = getSelectedElements()

            val bundle = Bundle()

            bundle.putParcelableArray("resources", seletedElements.toTypedArray())


            val dialog = FullscreenDialogFragment()
            dialog.arguments = bundle
            val activity: AppCompatActivity = view.context as AppCompatActivity
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            dialog.show(transaction, "FullScreenDialog")


        }

        return view
    }

    private fun getSelectedElements(): MutableList<Parcelable> {

        val persons: MutableList<Parcelable> = mutableListOf()
        val roles: List<MultiCheckRole> = mAdapter.groups as List<MultiCheckRole>

        for(i in 0 until roles.size){
            for (j in 0 until roles[i].selectedChildren.size){
                when(roles[i].selectedChildren[j]){
                    true -> { persons.add(items[i].persons[j])

                    }
                }
            }

        }
        Log.i("debug", "Enviado: $persons")
        return persons

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mAdapter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mAdapter.onRestoreInstanceState(savedInstanceState)

    }



    fun getMultiCheckRoles() : MutableList<MultiCheckRole>{



        val professors: MutableList<Person> = mutableListOf()
        professors.add(Person("Francisco Silva", true))
        professors.add(Person("Alex Barradas", true))
        professors.add(Person("Davi", true))

        val graduacao: MutableList<Person> = mutableListOf()
        graduacao.add(Person("André Luiz", false))
        graduacao.add(Person("Alysson Cirilo", true))
        graduacao.add(Person("Daniel CP", false))

        val master: MutableList<Person> = mutableListOf()
        master.add(Person("Aluno Mestrado 1", false))
        master.add(Person("Aluno Mestrado 2", true))
        master.add(Person("Aluno Mestrado 3", true))



        val professor = MultiCheckRole("Professores",professors, R.mipmap.ic_prof)
        val student = MultiCheckRole("Alunos de Graduação", graduacao, R.mipmap.ic_aluno)
        val masters = MultiCheckRole("Alunos de Mestrado", master, R.mipmap.ic_master)

        val roles: MutableList<MultiCheckRole> = mutableListOf()
        roles.add(professor)
        roles.add(masters)
        roles.add(student)
        items = roles
        return roles
    }



    fun getRoles(): MutableList<Role> {

        val professors: MutableList<Person> = mutableListOf()
        professors.add(Person("Francisco Silva", true))
        professors.add(Person("Alex Barradas", true))
        professors.add(Person("Davi", true))

        val graduacao: MutableList<Person> = mutableListOf()
        graduacao.add(Person("André Luiz", false))
        graduacao.add(Person("Alysson Cirilo", true))
        graduacao.add(Person("Daniel CP", false))

        val master: MutableList<Person> = mutableListOf()
        master.add(Person("Aluno Mestrado 1", false))
        master.add(Person("Aluno Mestrado 2", true))
        master.add(Person("Aluno Mestrado 3", true))





        val professor: Role = Role("Professores",professors, R.drawable.ic_banjo)
        val student: Role = Role("Alunos de Graduação", graduacao, R.drawable.ic_banjo)
        val masters: Role = Role("Alunos de Mestrado", master, R.drawable.ic_banjo)



        val roles: MutableList<Role> = mutableListOf()
        roles.add(professor)
        roles.add(masters)
        roles.add(student)
        return roles


    }


}
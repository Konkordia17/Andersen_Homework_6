package com.example.andersen_homework_6

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.andersen_homework_6.databinding.FragmentListBinding
import kotlinx.coroutines.launch

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private var usersAdapter: ContactsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState?.getParcelableArrayList<User>("key") == null) {
            getUsers()
        } else {
            usersAdapter?.updateUsers(ListUsers.users)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        val currentBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Pair<User, Int>>(RESULT_FROM_DETAIL_FRAGMENT)
            ?.observe(currentBackStackEntry, Observer { result ->
                val user = result.first
                val position = result.second
                ListUsers.users[position] = user
                usersAdapter?.updateUsers(ListUsers.users)
            })
        binding.searchContactEditText.addTextChangedListener {
            updateSearchContact()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("key", ArrayList(ListUsers.users))
    }

    private fun getUsers() {
        lifecycleScope.launch {
            try {
                val data = Networking.api.getUsers()
                ListUsers.users = (data.data + data.data).toMutableList()
                usersAdapter?.updateUsers(ListUsers.users)
            } catch (t: Throwable) {
                Log.d("f", "${t.message}")
                Toast.makeText(requireContext(), "Не возможно загрузить список", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initList() {
        usersAdapter = ContactsAdapter({ user, position ->
            val action =
                ListFragmentDirections.actionListFragmentToDetailInfoFragment(user, position)
            findNavController().navigate(action)
        }, { position ->
            openDialog(position)
        })
        with(binding.listUsers) {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
        usersAdapter?.updateUsers(ListUsers.users)
    }

    private fun deleteContact(position: Int) {
        ListUsers.users = (ListUsers.users.filterIndexed { index, user ->
            index != position
        }).toMutableList()
        usersAdapter?.updateUsers(ListUsers.users)
    }

    private fun openDialog(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage("Удалить выбранный контакт?")
            .setPositiveButton("Да") { _, _ ->
                deleteContact(position)
            }
            .setNegativeButton("Нет") { _, _ ->
                Toast.makeText(requireContext(), "Отмена", Toast.LENGTH_SHORT).show()
            }
        val dialog = builder.create()
        dialog.show()
    }

   private fun updateSearchContact() {
        val enteredText = binding.searchContactEditText.text
        if (enteredText == null) {
            usersAdapter?.updateUsers(ListUsers.users)
        } else {
            val listUsersResult = ListUsers.users.filter {
                it.name.contains(
                    enteredText.toString(),
                    true
                ) || it.surName.contains(enteredText.toString(), true)
            }
            usersAdapter?.updateUsers(listUsersResult)
        }
    }

    companion object {
        const val RESULT_FROM_DETAIL_FRAGMENT = "Result"
    }
}

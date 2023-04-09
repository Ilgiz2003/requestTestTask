package com.example.aut.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aut.Api
import com.example.aut.R
import com.example.aut.databinding.FragmentAuthorizationBinding
import com.example.aut.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class AuthorizationFragment : Fragment() {
    private var _binding: FragmentAuthorizationBinding? = null
    private val binding: FragmentAuthorizationBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthorizationBinding.inflate(inflater, container, false)

        binding.noAccount.setOnClickListener {
            openRegisterFragment()
        }
        binding.logInBtn.setOnClickListener {
            if (isDataCorrect()) {
                post()
            } else {
                Toast.makeText(requireContext(), "Нет данных", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun openRegisterFragment() {
        val registerFragment: RegisterFragment = RegisterFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.authorization_fragment, registerFragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }


    private fun post() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://front.bitqi.com/swagger/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(Api::class.java)
        val identificatorString = binding.editTextTextPassword.text.toString()
        val passwordString = binding.editTextTextPassword.text.toString()

        val jsonObject = JSONObject()
        jsonObject.put("identificator", identificatorString)
        jsonObject.put("password", passwordString)
        jsonObject.put("captchaName", "captcha")
        jsonObject.put("captchaToken", "captchaToken")

        val jsonObjectString = jsonObject.toString()

        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObjectString)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.authorizationUser(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("testLog2", "Yes")
                } else {
                    Log.d("testLog2", "Error")
                }
            }
        }
    }

    private fun isDataCorrect(): Boolean {
        return binding.editTextTextPassword.text.isNotEmpty() && binding.editTextTextEmailAddress.text.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
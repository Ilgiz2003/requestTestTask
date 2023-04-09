package com.example.aut.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.aut.Api
import com.example.aut.R
import com.example.aut.databinding.ActivityMainBinding
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

class RegisterFragment: Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding
        get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.registerBtn.setOnClickListener {
            if (isDataCorrect()) {
                post()
            } else {
                Toast.makeText(requireContext(), "Нет данных", Toast.LENGTH_SHORT).show()
            }
        }

        binding.haveAccount.setOnClickListener {
            openAuthorizationFragment()
        }
        return binding.root
    }

    private fun isDataCorrect(): Boolean{
        return (binding.userName.text.isNotEmpty()
                && binding.identificator.text.isNotEmpty()
                && binding.password.text.isNotEmpty())
    }

    private fun post() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://front.bitqi.com/swagger/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(Api::class.java)
        val userNameString = binding.userName.text.toString()
        val identificatorString = binding.identificator.text.toString()
        val passwordString = binding.password.text.toString()

        val jsonObject = JSONObject()
        jsonObject.put("userName", userNameString)
        jsonObject.put("identificator", identificatorString)
        jsonObject.put("password", passwordString)
        jsonObject.put("timeZone", TimeZone.getDefault().toString())
        jsonObject.put("captchaName", "captcha")
        jsonObject.put("captchaToken", "captchaToken")

        val jsonObjectString = jsonObject.toString()

        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObjectString)

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.registerUser(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("testLog", "Yes")
                } else {
                    Log.d("testLog", "Error")
                }
            }
        }
    }

    private fun openAuthorizationFragment(){
        val authorizationFragment: AuthorizationFragment = AuthorizationFragment()
        val transaction = childFragmentManager.beginTransaction()
        binding.registerBtn.visibility = View.GONE
        transaction.replace(R.id.registration_fragment, authorizationFragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
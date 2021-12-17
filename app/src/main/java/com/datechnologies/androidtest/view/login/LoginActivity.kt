package com.datechnologies.androidtest.view.login

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import com.datechnologies.androidtest.R
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.datechnologies.androidtest.databinding.ActivityLoginBinding
import com.datechnologies.androidtest.model.api.login.LoginRetriever
import com.datechnologies.androidtest.model.loginDTO.LoginResponse
import com.datechnologies.androidtest.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A screen that displays a login prompt, allowing the user to login to the D & A Technologies Web Server.
 *
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    //==============================================================================================
    // Lifecycle Methods
    //==============================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        val actionBar = supportActionBar!!
        actionBar.elevation = 30F
        actionBar.title = "Login"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        binding.loginButton.setOnClickListener { onLoginClick() }

        // TODO: Make the UI look like it does in the mock-up. Allow for horizontal screen rotation. DONE
        // TODO: Add a ripple effect when the buttons are clicked DONE
        // TODO: Save screen state on screen rotation, inputted username and password should not disappear on screen rotation  DONE

        // TODO: Send 'email' and 'password' to http://dev.rapptrlabs.com/Tests/scripts/login.php  DONE
        // TODO: as FormUrlEncoded parameters. DONE

        // TODO: When you receive a response from the login endpoint, display an AlertDialog.  DONE
        // TODO: The AlertDialog should display the 'code' and 'message' that was returned by the endpoint.  DONE
        // TODO: The AlertDialog should also display how long the API call took in milliseconds.  DONE
        // TODO: When a login is successful, tapping 'OK' on the AlertDialog should bring us back to the MainActivity  DONE

        // TODO: The only valid login credentials are:
        // TODO: email: info@rapptrlabs.com
        // TODO: password: Test123
        // TODO: so please use those to test the login.
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun onLoginClick(){
        val loginRetriever = LoginRetriever()
        val loginMap = HashMap<String,String>()
        val password = binding.editTextPassword.text
        val email = binding.editTextEmailAddress.text
        loginMap["email"] = email.toString()
        loginMap["password"] = password.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val startTime = System.currentTimeMillis()
            val response = loginRetriever.postLoginReceiveResponseBody(loginMap)
            withContext(Dispatchers.Main){
                if (response.code == 200) showAlertDialog(response, startTime)
                else Toast.makeText(this@LoginActivity, "Wrong Email or Password", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun showAlertDialog(response: LoginResponse, startTime: Long){
        val dialogBuilder = AlertDialog.Builder(this)
        val totalTime = System.currentTimeMillis() - startTime
        dialogBuilder.setMessage("""
            Code: ${response.code}
            Message: ${response.message}
            Call Time in Milliseconds: $totalTime
        """.trimIndent())
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id -> finish() })

        val alert = dialogBuilder.create()
        alert.setTitle("Test Alert")
        alert.show()
    }

    companion object {
        //==============================================================================================
        // Static Class Methods
        //==============================================================================================
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }
}
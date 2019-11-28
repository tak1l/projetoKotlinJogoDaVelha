package com.fapce.caio.jogodavelha

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fapce.caio.jogodavelha.Pay
import com.fapce.caio.jogodavelha.R
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {

    private var asyncTask: PaymentTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        btnCancel.setOnClickListener {
            finish()
        }
    }

    fun pay(view: View) {

        val pagar = JSONObject().apply {
            put("numero_cartao", edtCardNum.text.toString())
            put("nome_cliente", edtName.text.toString())
            put("bandeira", edtBrand.text.toString())
            put("cod_seguranca", edtSecurityCode.text.toString())
            put("valor_em_centavos", 500)

        }

        var respostaWS = 0 // ???

        if (CepHttp.hasConnection(this)) {
            if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                if(edtCardNum.text.toString() == "999.999.999.999" &&
                    edtName.text.toString() == "caio" &&
                    edtSecurityCode.text.toString() == "2019" &&
                    edtBrand.text.toString() == "MasterCard") {
                    asyncTask = PaymentTask()
                    asyncTask?.execute("pay")
                    respostaWS = 200
                }else{
                    asyncTask?.execute("error")
                    respostaWS = 401

                }
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this, "Connection Error \n", Toast.LENGTH_LONG
                ).show()
            }
        }


        val arquivo = getSharedPreferences("control_login", Context.MODE_PRIVATE)
        val editor = arquivo.edit()

        if (respostaWS == 200) {
            editor.putInt("qt_login", 1)
            editor.commit()

            Toast.makeText(
                this, "Thanks!! \n", Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        } else if (respostaWS == 401) {
            Toast.makeText(
                this, "No pay, no game! \n", Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    // ---------------------------------------------------------------------------------------------

    fun btnCEP(view: View) {
        if (CepHttp.hasConnection(this)) {
            if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                asyncTask = PaymentTask()
                asyncTask?.execute("ola")
            }else{
                asyncTask?.execute("erro")
            }
        } else {
         progressBar.visibility = View.GONE
            Toast.makeText(
                this, "Erro na conexão \n", Toast.LENGTH_LONG
            ).show()
        }

    }
    inner class PaymentTask : AsyncTask<String, Void, Pay>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg strings: String?): Pay? {
            return CepHttp.loadCep(strings[0])
        }

    }


}

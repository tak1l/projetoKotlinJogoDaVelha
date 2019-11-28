package com.fapce.caio.jogodavelha

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fapce.caio.jogodavelha.*
import kotlinx.android.synthetic.main.activity_main.edtLogin
import kotlinx.android.synthetic.main.activity_main.edtSenha

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arquivo = getSharedPreferences("login_senha", Context.MODE_PRIVATE)

        edtLogin.setText(arquivo.getString("login", ""))
        edtSenha.setText(arquivo.getString("senha", ""))
    }

    private val sqlHelper = SqlHelper(this)

    fun entrar(view: View) {
        val bd = sqlHelper.writableDatabase

        var loginInput = edtLogin.text.toString()
        var sqlLogin = "SELECT login FROM $TBL_USUARIO WHERE $TBL_USUARIO_LOGIN = ?"
        val cursor1 = bd.rawQuery(sqlLogin, arrayOf(loginInput))

        var senhaInput = edtSenha.text.toString()
        var sqlSenha = "SELECT senha FROM $TBL_USUARIO WHERE $TBL_USUARIO_SENHA = ?"
        val cursor2 = bd.rawQuery(sqlSenha, arrayOf(senhaInput))

        if (cursor1.moveToNext()) {
            edtLogin.setText(
                cursor1.getString(
                    cursor1.getColumnIndex(TBL_USUARIO_LOGIN)
                )
            )

            if (cursor2.moveToNext()) {
                edtSenha.setText(
                    cursor2.getString(
                        cursor2.getColumnIndex(TBL_USUARIO_SENHA)
                    )
                )

                enterGame()

            } else {
                Toast.makeText(
                    this, "Login e/ou senha inválida! \n" +
                            " ${edtLogin.text} \t ${edtSenha.text}", Toast.LENGTH_LONG
                ).show()
            }
        }
        else {
            val count = bd.query(TBL_USUARIO, arrayOf(TBL_USUARIO_ID), null, null, null, null, null).count

            if (count > 0) {
                Toast.makeText(
                    this, "Login e/ou senha inválido(s)! \n" +
                            " ${edtLogin.text} \t ${edtSenha.text}", Toast.LENGTH_LONG
                ).show()
            } else {
                val contentValues = ContentValues().apply {
                    put(TBL_USUARIO_LOGIN, edtLogin.text.toString())
                    put(TBL_USUARIO_SENHA, edtSenha.text.toString())
                }

                val id = bd.insert(
                    TBL_USUARIO, null, contentValues
                )

                if (id != -1L) {
                    Toast.makeText(
                        this, "Usuário inserido com sucesso! \n ID: $id", Toast.LENGTH_LONG
                    ).show()

                    enterGame()
                }
            }
        }
        bd.close()
    }

    private fun enterGame() {

        val arquivo = getSharedPreferences("control_login", Context.MODE_PRIVATE)
        val editor = arquivo.edit()
        var qt_login = arquivo.getInt ("qt_login", 1)
        if (qt_login < 4) {
            editor.putInt("qt_login", ++qt_login)
            editor.commit()

            Toast.makeText(
                this, "Acessando o programa! \n" +
                        " ${edtLogin.text} \t ${edtSenha.text}", Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

}

package com.fapce.caio.jogodavelha

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fapce.caio.jogodavelha.*
import kotlinx.android.synthetic.main.activity_sql.*

class SqlActivity : AppCompatActivity() {

    private val sqlHelper = SqlHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sql)

        btnSair.setOnClickListener {
            finish()
        }
    }


    fun lerTudo(view: View) {
        val bd = sqlHelper.writableDatabase
        val sql = "SELECT * FROM $TBL_USUARIO ORDER BY $TBL_USUARIO_LOGIN"
        val cursor = bd.rawQuery(sql, null)

        var usuarios = ""

        while (cursor.moveToNext()) {
            var id =
                cursor.getString(
                    cursor.getColumnIndex(TBL_USUARIO_ID)
                )
            var login =
                cursor.getString(
                    cursor.getColumnIndex(TBL_USUARIO_LOGIN)
                )
            var senha =
                cursor.getString(
                    cursor.getColumnIndex(TBL_USUARIO_SENHA)
                )

            usuarios += "$id - $login - $senha \n"
        }

        txtUsuarios.text = usuarios

        cursor.close()
        bd.close()
    }

    fun lerScores(view: View) {
        val bd = sqlHelper.writableDatabase
        val sql = "SELECT * FROM $TBL_JOGOS"
        val cursor = bd.rawQuery(sql, null)

        var jogos = ""

        while (cursor.moveToNext()) {
            var id =
                cursor.getString(
                    cursor.getColumnIndex(TBL_JOGOS_ID)
                )
            var player1 =
                cursor.getString(
                    cursor.getColumnIndex(TBL_JOGOS_JOGADOR_1)
                )
            var player2 =
                cursor.getString(
                    cursor.getColumnIndex(TBL_JOGOS_JOGADOR_2)
                )
            var vencedor =
                cursor.getString(
                    cursor.getColumnIndex(TBL_JOGOS_VENCEDOR)
                )

            jogos += "$id - $player1 - $player2 - $vencedor\n"
        }

        txtUsuarios.text = jogos

        cursor.close()
        bd.close()
    }

}

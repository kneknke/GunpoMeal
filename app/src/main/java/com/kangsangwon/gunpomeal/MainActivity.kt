package com.kangsangwon.gunpomeal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jsoup.Jsoup
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar
        setSupportActionBar(toolbar)

        //crawling
        retrieveWebInfo()

        //Popup
        buttonAlergy.setOnClickListener {
            showSettingPopup()
        }
    }

    //menu function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.action_menu -> {
                Toast.makeText(this, "메뉴는 준비중입니다.", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //popup function
    private fun showSettingPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val textView : TextView = view.findViewById(R.id.textView)
        //popup text
        textView.text = "1.난류 2.우유 3.메밀 4.땅콩 5.대두 6.밀 7.고등어 8.게 9.새우 10.돼지고기 11.복숭아 12.토마토 13.아황산류 14.호두 15.닭고기 16.쇠고기 17.오징\n" +
                "어 18.조개류(굴, 전복, 홍합 포함) 19.잣"

        //popup support info
        val alertDialog =  AlertDialog.Builder(this)
            .setTitle("알레르기 정보")
            .setNegativeButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }

    //crawling information from gunpo.hs.kr
    private fun retrieveWebInfo() {
        thread {
            //link with Jsoup
            val doc = Jsoup.connect("http://www.gunpo.hs.kr/main.php?menugrp=040100&master=meal2&act=list").get()

            //select html tag
            val imageElements = doc.select(".meal_table tbody td p img")
            val textElements = doc.select(".meal_table tbody td")

            //image link crawling
            val imageURL = imageElements[0].absUrl("src")

            this.runOnUiThread {
                //text change
                txtTitle.text = textElements[0].text()
                //image change with Piccaso
                Picasso.get()
                    .load(imageURL)
                    .placeholder(R.drawable.error)
                    .resize(250,250)
                    .into(imgTitle)
            }
        }
    }
}
package com.example.project

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

// ナビゲーションなどが入っている AppCompatActivity クラスを継承
class MainActivity : AppCompatActivity() {

    // override で AppCompatActivity()クラス の onCreateメソッドを上書き
    // Bundle は、状態を保存・復元するためのクラス
    override fun onCreate(savedInstanceState: Bundle?) { // 状態の保存
        // super は、スーパークラスの MainActivity
        super.onCreate(savedInstanceState)

        // レイアウトを設定
        setContentView(R.layout.activity_main)

        // ボトムナビゲーションの設定
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // ボトムナビゲーションの画面遷移の設定
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_record, R.id.navigation_calendar))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

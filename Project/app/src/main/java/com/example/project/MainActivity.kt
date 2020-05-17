package com.example.project

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

// 画面制御の基本的な機能を提供している AppCompatActivity クラスを継承
class MainActivity : AppCompatActivity() {

    // override で AppCompatActivity()クラス の onCreateメソッドを上書き
    // Bundle は、状態を保存・復元するためのクラス
    override fun onCreate(savedInstanceState: Bundle?) { // 状態の保存
        // super は、スーパークラスの MainActivity
        super.onCreate(savedInstanceState)

        // レイアウトを設定
        setContentView(R.layout.activity_main)

        // ボトムナビゲーションの表示領域
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        // findNavController で、ボトムナビゲーションによって切り替える領域を設定
        val navController = findNavController(R.id.nav_host_fragment)

        // ボトムナビゲーションの画面遷移の設定
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_record, R.id.navigation_calendar))

        // 表示の切り替え領域と遷移画面を関連付け
        setupActionBarWithNavController(navController, appBarConfiguration)
        // ボトムナビゲーションのデザインとナビゲーションのコントローラーを関連付け
        navView.setupWithNavController(navController)
    }

    // 戻るボタンの実装
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}

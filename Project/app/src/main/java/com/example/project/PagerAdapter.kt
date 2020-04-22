package com.example.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(fm: FragmentManager, private val fragmentList: List<Fragment>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // 表示するフラグメントを制御する
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    // viewPagerにセットするコンテンツ(フラグメントリスト)のサイズ
    override fun getCount(): Int {
        return fragmentList.size
    }
}

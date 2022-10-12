package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.vate.R

class MypageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_mypage, container, false)
        initButton(view)
        return view
    }

    private fun initButton(view: View) {
        view.findViewById<TextView>(R.id.myTimetable)?.setOnClickListener {
            val intent = Intent(it.context, TimeTableActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): MypageFragment {
            return MypageFragment()
        }
    }
}
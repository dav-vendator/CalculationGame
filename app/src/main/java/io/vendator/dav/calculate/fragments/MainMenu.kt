package io.vendator.dav.calculate.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

import io.vendator.dav.calculate.R
import kotlinx.android.synthetic.main.fragment_main_menu.view.*

class MainMenu : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btPlay.setOnClickListener {
            val dialog = BottomNumberDialog()
            dialog.mItemSelectedListener = { difficulty ->
                navigateToPlay(difficulty)
                dialog.dismiss()
            }
            dialog.show(fragmentManager!!, dialog::class.java.simpleName)
        }

        view.btStats.setOnClickListener {
            val action = MainMenuDirections.actionMainMenuToStatsFragment()
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun navigateToPlay(difficulty: Int) {
        val action = MainMenuDirections.actionMainMenuToGameFragment()
        action.difficultyLevel = difficulty
        NavHostFragment.findNavController(this).navigate(action)
    }

}

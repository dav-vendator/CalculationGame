package io.vendator.dav.calculate.fragments


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import io.vendator.dav.calculate.R
import io.vendator.dav.calculate.busineslogic.Problem
import io.vendator.dav.calculate.busineslogic.ProblemGenerator
import io.vendator.dav.calculate.room.DatabaseProvider
import io.vendator.dav.calculate.room.GameStat
import io.vendator.dav.calculate.room.GameStatDb
import io.vendator.dav.calculate.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class GameFragment : Fragment() {
    private lateinit var mProblemGenerator: ProblemGenerator
    private var mDifficultyLevel: Int = -1
    private var currentProblem: Problem? = null
    private lateinit var mViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        mDifficultyLevel = GameFragmentArgs.fromBundle(arguments!!).difficultyLevel
        mProblemGenerator = ProblemGenerator(mDifficultyLevel)
        view.tvTitle.text = getString(R.string.game_title, mDifficultyLevel)

        if (mViewModel.currentScore.value == null) {
            mViewModel.currentScore.value = 0
            mViewModel.currentTotal.value = 0
        }

        view.tvCorrect.text = getString(R.string.game_correct, mViewModel.currentScore.value)
        view.tvTotalCount.text = getString(R.string.game_total, mViewModel.currentTotal.value)

        mViewModel.currentTotal.observe(this, Observer {
            view.tvTotalCount.text = getString(R.string.game_total, mViewModel.currentTotal.value)
        })

        mViewModel.currentScore.observe(this, Observer {
            view.tvCorrect.text = getString(R.string.game_correct, mViewModel.currentScore.value)
        })

        startGameLoop()
        view.btRefresh.performClick()
    }

    private fun startGameLoop() {
        view!!.btRefresh.setOnClickListener {
            mViewModel.startTime()
            if (mViewModel.currentTotal.value != null) {
                mViewModel.currentTotal.value = mViewModel.currentTotal.value!! + 1
            }
            currentProblem = mProblemGenerator.generateRandomProblem()
            view!!.tvNumberOne.text = currentProblem!!.numberList[0].toString()
            view!!.tvNumberTwo.text = currentProblem!!.numberList[1].toString()
            view!!.tvOperator.text = currentProblem!!.getCharacter(currentProblem!!.operation)
        }

        view!!.btNext.setOnClickListener {
            if (currentProblem != null) {
                if (!view!!.etAnswer.editText!!.text.isNullOrBlank()) {
                    mViewModel.stopTime()
                    hideKeyboard()
                    try {
                        val answer = view!!.etAnswer.editText!!.text.toString().toInt()
                        if (answer == currentProblem!!.answer.toInt()) {
                            if (mViewModel.currentScore.value != null) {
                                mViewModel.currentScore.value = mViewModel.currentScore.value!! + 1
                            }
                            refreshProblem()
                        } else {
                            val snackbar = Snackbar.make(
                                view!!, "Correct answer was: ${currentProblem!!.answer}",
                                Snackbar.LENGTH_INDEFINITE
                            )
                            snackbar.setAction("OK") {
                                snackbar.dismiss()
                                refreshProblem()
                            }
                            snackbar.show()
                        }
                    } catch (e: NumberFormatException) {
                        Snackbar.make(view!!, "Not a valid number", Snackbar.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Snackbar.make(view!!, "A error occurred while parsing input", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun refreshProblem() {
        currentProblem = null
        view!!.btRefresh.performClick()
        view!!.etAnswer.editText!!.text.clear()
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity!!.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDetach() {
        super.onDetach()
        val dao = DatabaseProvider(context!!).db
            .gameStatDao()

        Thread(Runnable {
            dao.insertAll(
                    GameStat(
                        uid = 0,
                        difficultyLevel = mDifficultyLevel,
                        date = Date(),
                        accuracy = (mViewModel.currentScore.value!! / mViewModel.currentTotal.value!!.toFloat()) * 100,
                        averageTime = TimeUnit.MILLISECONDS.toSeconds(
                            (
                                    mViewModel.accumulatedTime / mViewModel.currentTotal.value!!.toFloat()).toLong()
                        ),
                        totalProblem = mViewModel.currentTotal.value!!
                    )
                )
        }).start()
    }

}

package io.vendator.dav.calculate.fragments


import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.vendator.dav.calculate.R
import io.vendator.dav.calculate.adapter.StatsListAdapter
import io.vendator.dav.calculate.room.CalculationDao
import io.vendator.dav.calculate.room.DatabaseProvider
import io.vendator.dav.calculate.room.GameStat
import kotlinx.android.synthetic.main.fragment_stats.view.*
import java.lang.ref.WeakReference


class StatsFragment : androidx.fragment.app.Fragment() {
    private lateinit var mDao: CalculationDao
    private val mStatsListAdapter = StatsListAdapter(listOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btDelete.setOnClickListener {
            if (view.difficultySpinner.selectedItemPosition > 0) {
                println("Hello World")
                DeleteListAsync(
                    mDao,
                    WeakReference(mStatsListAdapter)
                ).execute(view.difficultySpinner.selectedItemPosition - 1)
            } else {
                Toast.makeText(context, "Please select a difficulty first!", Toast.LENGTH_SHORT).show()
            }
        }
        setupSpinner()
        view.recyclerView.layoutManager = LinearLayoutManager(context!!)
        view.recyclerView.adapter = mStatsListAdapter
        mDao = DatabaseProvider(context!!).db
            .gameStatDao()
    }

    private fun setupSpinner() {
        view!!.difficultySpinner.adapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.difficulty,
            android.R.layout.simple_spinner_item
        )

        view!!.difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    GetListAsync(mDao, WeakReference(mStatsListAdapter)).execute(position)
                }
            }

        }
    }

    class GetListAsync(
        val dao: CalculationDao,
        var adapter: WeakReference<StatsListAdapter>
    ) : AsyncTask<Int, Void, List<GameStat>>() {
        override fun doInBackground(vararg params: Int?): List<GameStat> {
            return dao.getAllGamesWith(params[0]!!)
        }

        override fun onPostExecute(result: List<GameStat>?) {
            super.onPostExecute(result)
            val mAdapter = adapter.get()
            mAdapter!!.array = result!!
            mAdapter.notifyDataSetChanged()
        }
    }

    class DeleteListAsync(
        val dao: CalculationDao,
        var adapter: WeakReference<StatsListAdapter>
    ) : AsyncTask<Int, Void?, Void?>() {
        override fun doInBackground(vararg params: Int?): Void? {
            print(adapter.get()?.array?.size)
            adapter.get()?.array
                ?.forEach { gameStat ->
                    println(dao.delete(gameStat))
                }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            val mAdapter = adapter.get()
            mAdapter!!.array = listOf()
            mAdapter.notifyDataSetChanged()
        }
    }


}

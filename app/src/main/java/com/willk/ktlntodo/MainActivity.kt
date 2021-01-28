package com.willk.ktlntodo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity(), RecClickListener {
   private lateinit var recyclerView: RecyclerView
   private lateinit var recyclerAdapter: Adapter<*>
   private lateinit var viewManager: RecyclerView.LayoutManager
   private  var tasks: ArrayList<Task>? = null
   private lateinit var fab: FloatingActionButton
   private lateinit var addlo: ConstraintLayout
   private lateinit var addBt: ImageButton
   private lateinit var editText: EditText
   private lateinit var noTask: TextView
   private var isaddVisible: Boolean = false
    private val listType: Type = object : TypeToken<ArrayList<Task>>() {}.type


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        noTask = findViewById(R.id.noTasks)
        fab = findViewById(R.id.floatingActionButton)
        addlo = findViewById(R.id.add_lo)
        addBt = findViewById(R.id.add_bt)
        editText = findViewById(R.id.editText)
        emptyCheck()

        recyclerAdapter = tasks?.let { RecyclerAdapter(it, this) }!!
        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply{
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recyclerAdapter
        }

        fab.setOnClickListener(View.OnClickListener {
        addlo.visibility = VISIBLE
        editText.text = null
        editText.requestFocus()
        isaddVisible = true
        showkeyboard()})
        addBt.setOnClickListener(View.OnClickListener {
           if(editText.text != null) {tasks!!.add(Task(editText.text.toString()))}
            editText.text = null

            recyclerView.adapter?.notifyDataSetChanged()
            editText.clearFocus()
            hidekeyboard(addlo)
            addlo.visibility = View.INVISIBLE
            isaddVisible = false
            emptyCheck()
            tasks?.let{saveData(it)}

        })
    }

    override fun onPause() {
        super.onPause()
        Log.d("lifecycle", "onPause")
        tasks?.let { saveData(it) }

    }

    override fun onStop() {
        super.onStop()
        Log.d("lifecycle", "onStop")
        tasks?.let { saveData(it) }
    }

    private fun emptyCheck() {
        if(tasks?.isEmpty()!!){noTask.visibility = VISIBLE
        } else {noTask.visibility = View.GONE}
    }

    override fun onBackPressed() {
       if(!isaddVisible) {super.onBackPressed()}
        if (isaddVisible) { addlo.visibility = INVISIBLE
        isaddVisible = false}

    }

    private fun showkeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hidekeyboard(view: View) {

        if(view != null)
        {val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        } else window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun serialize(list: ArrayList<Task>): String? {
        return Gson().toJson(list)
    }

    private fun saveData (list: ArrayList<Task>){
       val sharedpref: SharedPreferences = getSharedPreferences("tasksData", Context.MODE_PRIVATE)
       val editor: SharedPreferences.Editor = sharedpref.edit()
        editor.putString("tasks", serialize(list))
        editor.apply()
        Log.d("data", "dataSaved")
    }

    private fun loadData(){
        val sharedpref: SharedPreferences? = getSharedPreferences("tasksData", Context.MODE_PRIVATE)
        tasks = Gson().fromJson<ArrayList<Task>>(sharedpref?.getString("tasks", null), listType)
        Log.d("data", "dataLoaded")
        if (tasks == null){
           tasks = ArrayList()
        }

    }

    override fun onCheck(pos: Int, chk: Boolean) {
        tasks?.get(pos)?.isChecked = chk
    }

    override fun onDel(pos: Int) {
        tasks?.removeAt(pos)
        recyclerView.adapter?.notifyDataSetChanged()
        emptyCheck()
    }
}

package ovh.tomus.iut.flotte.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_list.*
import ovh.tomus.iut.flotte.R
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.view.View
class ListActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        getContainerShips()
    }

    @SuppressLint("SetTextI18n")
    fun getContainerShips() {
        db.collection("containerShips").get().addOnCompleteListener{ task ->
            if (task.isSuccessful) {
                val boatnames = mutableMapOf<String, String>()
                for (boat in task.result!!)
                    boatnames[boat.data["boatName"].toString()] = boat.reference.path

                val adapter = ArrayAdapter<String>(this, R.layout.listview_item, boatnames.keys.toTypedArray())
                listview.setAdapter(adapter)

                val page = Intent(this, BoatActivity::class.java)

                list_empty.visibility = if (adapter.isEmpty) View.VISIBLE else View.GONE

                listview.onItemClickListener = object : OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val item = parent!!.getItemAtPosition(position)
                        page.putExtra("DOCREF", boatnames[item])
                        startActivity(page)
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

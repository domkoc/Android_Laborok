package hu.bme.aut.androidwallet

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.salary_row.view.*

class MainActivity : AppCompatActivity() {

    var sum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save_button.setOnClickListener {
            if (salary_name.text.toString().isEmpty() || salary_amount.text.toString().isEmpty()) {
                //Toast.makeText(this, R.string.warn_message, Toast.LENGTH_LONG).show()
                Snackbar.make(findViewById(R.id.main_layout), R.string.warn_message, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener;
            }

            val rowItem = LayoutInflater.from(this).inflate(R.layout.salary_row, null)

            rowItem.salary_direction_icon.setImageResource(
                if (expense_or_income.isChecked) R.drawable.expense
                else R.drawable.income)
            rowItem.row_salary_name.text = salary_name.text.toString()
            rowItem.row_salary_amount.text = salary_amount.text.toString()

            var sumLayout: LinearLayout = findViewById(R.id.sum_layout)
            sumLayout.visibility = View.VISIBLE

            if (expense_or_income.isChecked) sum -= Integer.parseInt(salary_amount.text.toString())
            else sum += Integer.parseInt(salary_amount.text.toString())

            var sumView:TextView = findViewById(R.id.sum)
            sumView.text = sum.toString()

            list_of_rows.addView(rowItem)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.delete_all -> {
                list_of_rows.removeAllViews()
                sum = 0
                var sumLayout: LinearLayout = findViewById(R.id.sum_layout)
                sumLayout.visibility = View.GONE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
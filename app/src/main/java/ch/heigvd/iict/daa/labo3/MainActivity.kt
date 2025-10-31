package ch.heigvd.iict.daa.labo3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // --- Views ---
        val radioGroupOccupation = findViewById<RadioGroup>(R.id.radio_group_occupation)
        val radioStudent = findViewById<RadioButton>(R.id.radio_button_student)
        val radioWorker = findViewById<RadioButton>(R.id.radio_button_worker)

        // Student fields
        val textStudentTitle = findViewById<TextView>(R.id.text_student_title)
        val textStudentYear = findViewById<TextView>(R.id.text_view_student_year)
        val editStudentYear = findViewById<EditText>(R.id.edit_text_student_year)
        val textStudentSchool = findViewById<TextView>(R.id.text_view_student_school)
        val editStudentSchool = findViewById<EditText>(R.id.edit_text_student_school)

        // Worker fields
        val textWorkerTitle = findViewById<TextView>(R.id.text_worker_title)
        val textWorkerCompany = findViewById<TextView>(R.id.text_view_worker_company)
        val editWorkerCompany = findViewById<EditText>(R.id.edit_text_worker_company)
        val textWorkerExperience = findViewById<TextView>(R.id.text_view_worker_experience)
        val editWorkerExperience = findViewById<EditText>(R.id.edit_text_worker_experience)
        val textWorkerSector = findViewById<TextView>(R.id.text_view_worker_sector)
        val spinnerWorkerSector = findViewById<Spinner>(R.id.spinner_worker_sector)

        val sectorAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.sectors,
            android.R.layout.simple_spinner_item
        )
        sectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWorkerSector.adapter = sectorAdapter



        val editBirthdate = findViewById<EditText>(R.id.edit_text_birthdate)
        val imageCake = findViewById<ImageButton>(R.id.image_button_cake)

        val buttonCancel = findViewById<Button>(R.id.button_cancel)
        val buttonOk = findViewById<Button>(R.id.button_ok)

        val spinnerNationality = findViewById<Spinner>(R.id.spinner_nationality)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.nationalities,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerNationality.adapter = adapter


        // --- DatePicker ---
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editBirthdate.setText(dateFormat.format(cal.time))
        }

        fun showDatePicker() {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        editBirthdate.setOnClickListener { showDatePicker() }
        imageCake.setOnClickListener { showDatePicker() }

        // --- Show / hide student or worker fields ---
        fun showStudent() {
            textStudentTitle.visibility = View.VISIBLE
            textStudentYear.visibility = View.VISIBLE
            editStudentYear.visibility = View.VISIBLE
            textStudentSchool.visibility = View.VISIBLE
            editStudentSchool.visibility = View.VISIBLE

            textWorkerTitle.visibility = View.GONE
            textWorkerCompany.visibility = View.GONE
            editWorkerCompany.visibility = View.GONE
            textWorkerExperience.visibility = View.GONE
            editWorkerExperience.visibility = View.GONE
            textWorkerSector.visibility = View.GONE
            spinnerWorkerSector.visibility = View.GONE

            editWorkerCompany.text.clear()
            editWorkerExperience.text.clear()
        }

        fun showWorker() {
            textWorkerTitle.visibility = View.VISIBLE
            textWorkerCompany.visibility = View.VISIBLE
            editWorkerCompany.visibility = View.VISIBLE
            textWorkerExperience.visibility = View.VISIBLE
            editWorkerExperience.visibility = View.VISIBLE
            textWorkerSector.visibility = View.VISIBLE
            spinnerWorkerSector.visibility = View.VISIBLE

            textStudentTitle.visibility = View.GONE
            textStudentYear.visibility = View.GONE
            editStudentYear.visibility = View.GONE
            textStudentSchool.visibility = View.GONE
            editStudentSchool.visibility = View.GONE

            editStudentYear.text.clear()
            editStudentSchool.text.clear()
        }

        fun hideBoth() {
            textStudentTitle.visibility = View.GONE
            textStudentYear.visibility = View.GONE
            editStudentYear.visibility = View.GONE
            textStudentSchool.visibility = View.GONE
            editStudentSchool.visibility = View.GONE

            textWorkerTitle.visibility = View.GONE
            textWorkerCompany.visibility = View.GONE
            editWorkerCompany.visibility = View.GONE
            textWorkerExperience.visibility = View.GONE
            editWorkerExperience.visibility = View.GONE
            textWorkerSector.visibility = View.GONE
            spinnerWorkerSector.visibility = View.GONE
        }

        // initial state: hide all
        hideBoth()

        radioGroupOccupation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_student -> showStudent()
                R.id.radio_button_worker -> showWorker()
                else -> hideBoth()
            }
        }

        // --- Buttons ---
        buttonCancel.setOnClickListener {
            findViewById<EditText>(R.id.edit_text_name).text.clear()
            findViewById<EditText>(R.id.edit_text_surname).text.clear()
            editBirthdate.text.clear()
            findViewById<Spinner>(R.id.spinner_nationality).setSelection(0)
            radioGroupOccupation.clearCheck()
            hideBoth()
            Toast.makeText(this, "Formulaire réinitialisé", Toast.LENGTH_SHORT).show()
        }

        buttonOk.setOnClickListener {
            val name = findViewById<EditText>(R.id.edit_text_name).text.toString().trim()
            val surname = findViewById<EditText>(R.id.edit_text_surname).text.toString().trim()
            if (name.isEmpty() || surname.isEmpty()) {
                Toast.makeText(this, "Nom et prénom requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Formulaire OK", Toast.LENGTH_SHORT).show()
        }
    }
}

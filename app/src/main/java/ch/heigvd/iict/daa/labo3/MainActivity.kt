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
import android.util.Log
import android.view.inputmethod.EditorInfo
import ch.heigvd.iict.daa.labo3.model.Person
import ch.heigvd.iict.daa.labo3.model.Student
import ch.heigvd.iict.daa.labo3.model.Worker


class MainActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private lateinit var datePickerDialog : DatePickerDialog

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

        datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        fun showDatePicker() {
            datePickerDialog.show()
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

        fun populateView(person: Person) {
            findViewById<EditText>(R.id.edit_text_name).setText(person.name)
            findViewById<EditText>(R.id.edit_text_surname).setText(person.firstName)
            findViewById<EditText>(R.id.edit_text_birthdate).setText(Person.dateFormatter.format(person.birthDay.time))
            findViewById<Spinner>(R.id.spinner_nationality).setSelection(
                (0 until spinnerNationality.adapter.count).firstOrNull {
                    spinnerNationality.adapter.getItem(it).toString() == person.nationality
                } ?: 0
            )
            findViewById<EditText>(R.id.edit_text_address).setText(person.email)
            findViewById<EditText>(R.id.edit_text_comments).setText(person.remark)

            when (person) {
                is Student -> {
                    radioGroupOccupation.check(R.id.radio_button_student)
                    showStudent()
                    editStudentSchool.setText(person.university)
                    editStudentYear.setText(person.graduationYear.toString())
                }
                is Worker -> {
                    radioGroupOccupation.check(R.id.radio_button_worker)
                    showWorker()
                    editWorkerCompany.setText(person.company)
                    editWorkerExperience.setText(person.experienceYear.toString())
                    spinnerWorkerSector.setSelection(
                        (0 until spinnerWorkerSector.adapter.count).firstOrNull {
                            spinnerWorkerSector.adapter.getItem(it).toString() == person.sector
                        } ?: 0
                    )
                }
            }
        }

        populateView(Person.exampleStudent) // or Person.exampleWorker

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
            val birthdateStr = editBirthdate.text.toString().trim()
            val nationality = spinnerNationality.selectedItem.toString()
            val email = findViewById<EditText>(R.id.edit_text_address).text.toString().trim()
            val remark = findViewById<EditText>(R.id.edit_text_comments).text.toString().trim()

            if (nationality == getString(R.string.nationality_empty)) {
                Toast.makeText(this, "Veuillez choisir une nationalité", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty() || surname.isEmpty() || birthdateStr.isEmpty()) {
                Toast.makeText(this, "Nom, prénom et date de naissance requis", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val birthdate = Calendar.getInstance()
            try {
                birthdate.time = Person.dateFormatter.parse(birthdateStr)!!
            } catch (e: Exception) {
                Toast.makeText(this, "Date invalide", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkedId = radioGroupOccupation.checkedRadioButtonId
            val person: Person = when (checkedId) {
                R.id.radio_button_student -> {
                    val university = editStudentSchool.text.toString()
                    val graduationYear = editStudentYear.text.toString().toIntOrNull() ?: 0
                    Student(name, surname, birthdate, nationality, university, graduationYear, email, remark)
                }
                R.id.radio_button_worker -> {
                    val company = editWorkerCompany.text.toString()
                    val experience = editWorkerExperience.text.toString().toIntOrNull() ?: 0
                    val sector = spinnerWorkerSector.selectedItem.toString()

                    if (sector == getString(R.string.sectors_empty)) {
                        Toast.makeText(this, "Choisissez un secteur", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    Worker(name, surname, birthdate, nationality, company, sector, experience, email, remark)
                }
                else -> {
                    Toast.makeText(this, "Choisissez une occupation", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            Log.i("MainActivity", person.toString())
            Toast.makeText(this, "Créé: ${person::class.simpleName}", Toast.LENGTH_SHORT).show()
        }

        findViewById<EditText>(R.id.edit_text_address).setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                buttonOk.performClick()
                true
            } else {
                false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (datePickerDialog.isShowing) {
            datePickerDialog.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        if (datePickerDialog.isShowing) {
            datePickerDialog.dismiss()
        }
    }
}

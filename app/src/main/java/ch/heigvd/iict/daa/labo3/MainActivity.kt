package ch.heigvd.iict.daa.labo3

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ch.heigvd.iict.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.iict.daa.labo3.model.Person
import ch.heigvd.iict.daa.labo3.model.Student
import ch.heigvd.iict.daa.labo3.model.Worker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupDatePicker()
        setupSpinners()
        setupOccupationVisibility()
        setupButtons()

        populateView(Person.exampleStudent)


        binding.editTextAddress.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                binding.buttonOk.performClick()
                true
            } else {
                false
            }
        }
    }
    private fun setupDatePicker() {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            binding.editTextBirthdate.setText(dateFormat.format(cal.time))
        }

        datePickerDialog = DatePickerDialog(
            this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        fun showDatePicker() = datePickerDialog.show()
        binding.editTextBirthdate.setOnClickListener { showDatePicker() }
        binding.imageButtonCake.setOnClickListener { showDatePicker() }
    }

    private fun setupSpinners() {
        val nationalityAdapter = ArrayAdapter.createFromResource(
            this, R.array.nationalities, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerNationality.adapter = nationalityAdapter

        val sectorAdapter = ArrayAdapter.createFromResource(
            this, R.array.sectors, android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerWorkerSector.adapter = sectorAdapter
    }

    private fun setupButtons() {
        binding.buttonCancel.setOnClickListener {
            binding.apply {
                editTextName.text.clear()
                editTextSurname.text.clear()
                editTextBirthdate.text.clear()
                spinnerNationality.setSelection(0)
                radioGroupOccupation.clearCheck()

                editTextStudentSchool.text.clear()
                editTextStudentYear.text.clear()

                editTextWorkerCompany.text.clear()
                spinnerWorkerSector.setSelection(0)
                editTextWorkerExperience.text.clear()

                editTextAddress.text.clear()
                editTextComments.text.clear()
            }
            Toast.makeText(this, "Formulaire réinitialisé", Toast.LENGTH_SHORT).show()
        }

        binding.buttonOk.setOnClickListener {
            validateForm()?.let { person ->
                Log.i("MainActivity", person.toString())
                Toast.makeText(this, "Créé: ${person::class.simpleName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupOccupationVisibility() {
        binding.radioGroupOccupation.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_button_student -> {
                    binding.groupStudent.visibility = Group.VISIBLE
                    binding.groupWorker.visibility = Group.GONE
                }

                R.id.radio_button_worker -> {
                    binding.groupStudent.visibility = Group.GONE
                    binding.groupWorker.visibility = Group.VISIBLE
                }

                else -> {
                    binding.groupStudent.visibility = Group.GONE
                    binding.groupWorker.visibility = Group.GONE
                }
            }
        }

        // Hide both by default
        binding.groupWorker.visibility = Group.GONE
        binding.groupStudent.visibility = Group.GONE
    }

    private fun populateView(person: Person) {
        binding.apply {
            editTextName.setText(person.name)
            editTextSurname.setText(person.firstName)
            editTextBirthdate.setText(Person.dateFormatter.format(person.birthDay.time))
            spinnerNationality.setSelection(
                (0 until spinnerNationality.adapter.count).firstOrNull {
                    spinnerNationality.adapter.getItem(it).toString() == person.nationality
                } ?: 0
            )
            editTextAddress.setText(person.email)
            editTextComments.setText(person.remark)
        }

        when (person) {
            is Student -> {
                binding.radioGroupOccupation.check(R.id.radio_button_student)
                binding.editTextStudentSchool.setText(person.university)
                binding.editTextStudentYear.setText(person.graduationYear.toString())
            }
            is Worker -> {
                binding.radioGroupOccupation.check(R.id.radio_button_worker)
                binding.editTextWorkerCompany.setText(person.company)
                binding.editTextWorkerExperience.setText(person.experienceYear.toString())
                binding.spinnerWorkerSector.setSelection(
                    (0 until binding.spinnerWorkerSector.adapter.count).firstOrNull {
                        binding.spinnerWorkerSector.adapter.getItem(it).toString() == person.sector
                    } ?: 0
                )
            }
        }
    }

    private fun validateForm(): Person?{
        val name = binding.editTextName.text.toString().trim()
        val surname = binding.editTextSurname.text.toString().trim()
        val birthdateStr = binding.editTextBirthdate.text.toString().trim()
        val nationality = binding.spinnerNationality.selectedItem.toString()
        val email = binding.editTextAddress.text.toString().trim()
        val remark = binding.editTextComments.text.toString().trim()

        if (nationality == getString(R.string.nationality_empty)) {
            Toast.makeText(this, "Veuillez choisir une nationalité", Toast.LENGTH_SHORT).show()
            return null
        }

        if (name.isEmpty() || surname.isEmpty() || birthdateStr.isEmpty()) {
            Toast.makeText(this, "Nom, prénom et date de naissance requis", Toast.LENGTH_SHORT).show()
            return null
        }

        val birthdate = Calendar.getInstance()
        try {
            birthdate.time = Person.dateFormatter.parse(birthdateStr)!!
        } catch (e: Exception) {
            Toast.makeText(this, "Date invalide", Toast.LENGTH_SHORT).show()
            return null
        }

        val checkedId = binding.radioGroupOccupation.checkedRadioButtonId
        return when (checkedId) {
            R.id.radio_button_student -> {
                val university = binding.editTextStudentSchool.text.toString()
                val graduationYear = binding.editTextStudentYear.text.toString().toIntOrNull() ?: 0
                Student(name, surname, birthdate, nationality, university, graduationYear, email, remark)
            }
            R.id.radio_button_worker -> {
                val company = binding.editTextWorkerCompany.text.toString()
                val experience = binding.editTextWorkerExperience.text.toString().toIntOrNull() ?: 0
                val sector = binding.spinnerWorkerSector.selectedItem.toString()

                if (sector == getString(R.string.sectors_empty)) {
                    Toast.makeText(this, "Choisissez un secteur", Toast.LENGTH_SHORT).show()
                    return null
                }

                Worker(name, surname, birthdate, nationality, company, sector, experience, email, remark)
            }
            else -> {
                Toast.makeText(this, "Choisissez une occupation", Toast.LENGTH_SHORT).show()
                null
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (datePickerDialog.isShowing) datePickerDialog.dismiss()
    }

    override fun onPause() {
        super.onPause()
        if (datePickerDialog.isShowing) datePickerDialog.dismiss()
    }
}

package br.com.brunoti.kotlintodolist

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ReminderValidationUnitTest {

    @Test
    fun validateInput_emptyName_shouldReturnFalse() {
        val isValid = validateInput("", "01/01/2025")
        assertFalse(isValid)
    }

    @Test
    fun validateInput_emptyDate_shouldReturnFalse() {
        val isValid = validateInput("Test Reminder", "")
        assertFalse(isValid)
    }

    @Test
    fun validateInput_pastDate_shouldReturnFalse() {
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val isValid = validateInput("Test Reminder", currentDate)
        assertFalse(isValid)
    }

    @Test
    fun validateInput_validInput_shouldReturnTrue() {
        val futureDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().apply { add(Calendar.DATE, 1) }.time)
        val isValid = validateInput("Test Reminder", futureDate)
        assertTrue(isValid)
    }

    private fun validateInput(name: String, dateString: String): Boolean {
        if (name.isBlank()) {
            return false
        }

        if (dateString.isBlank()) {
            return false
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val selectedDate = dateFormat.parse(dateString)!!

        return selectedDate.after(currentDate)
    }
}

package com.example.weather

import org.junit.Test

class CommonFunctionsTest {
    @Test
    fun `Given a date-hour in iso8601 format when function formatToHourPeriod is called then returns the format that data is shown on the UI`() {
        //Given
        val date = "2025-07-02T18:30"

        //When
        val actual = formatToHourPeriod(date)

        //Then
        assertEqualsVerbose("06 pm", actual)
    }

    @Test
    fun `Given a date-hour in iso8601 format when function formatToFullDate is called then returns the format that data is shown on the UI`() {
        //Given
        val date = "2025-07-02T18:30"

        //When
        val actual = formatToFullDate(date)

        //Then
        assertEqualsVerbose("02 July, Wednesday", actual)
    }
}
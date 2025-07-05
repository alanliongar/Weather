package com.example.weather

import org.junit.Assert.assertEquals

fun <T> assertEqualsVerbose(expected: T, actual: T) {
    if (expected != actual) {
        println("❌ Test Failed → expected: $expected | actual: $actual")
    } else {
        println("✅ Test Passed → expected: $expected | actual: $actual")
    }
    assertEquals(expected, actual)
}

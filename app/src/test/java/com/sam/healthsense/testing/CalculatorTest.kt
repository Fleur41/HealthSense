package com.sam.healthsense.testing

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    private lateinit var calculator: Calculator

    @Before
    fun setup(){
        println("This will be printed before each test case")
        calculator = Calculator()
    }

    @After
    fun tearDown(){
        println("This will be printed after each test case")

    }
    @Test
    fun testAddition(){
        //Arrange
        //val calculator = Calculator()
        //Action
        val result = calculator.add(4, 4)
        //Assert
        Assert.assertEquals(8,result)
    }

    @Test
    fun testSubtraction(){

        //Action
        val result = calculator.subtract(8, 4)
        //Assert
        Assert.assertEquals(4,result)
    }

    @Test
    fun testMultiplication(){

        //Action
        val result = calculator.multiply(4, 4)
        //Assert
        Assert.assertEquals(16,result)
    }

    @Test(expected = IllegalArgumentException::class)
    //fun testDivisionWithException(){
    fun `dividing by zero should throw exception`(){

        //Action
        val result = calculator.divide(8, 0)
        //Assert
        Assert.assertEquals(2,result)
    }

}
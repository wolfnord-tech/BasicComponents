package com.example.kotlinlab7


import android.os.SystemClock.sleep
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pro.wolfnord.basiccomponents.MainActivity


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun onCreate(){
        //Проверка видимости
        onView(withId(R.id.button_to_add)).check(matches(isDisplayed()))
        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())))
        onView(withId(R.id.editTextText)).check(matches(isDisplayed()))
    }

    @Test
    fun editTextTextTest(){
        //Ввод значения
        onView(withId(R.id.editTextText)).perform(typeText("input"), closeSoftKeyboard())
    }

    @Test
    fun  defaultImageBtn(){
        //Проверка,что при нажатии кнопки imageView не ломается
        onView(withId(R.id.button_to_add)).perform(click())
        sleep(5000)
        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testImageLoadSuccess() {
        // Ввод корректной ссылки в EditText
        onView(withId(R.id.editTextText))
            .perform(typeText("https://i.pinimg.com/736x/a8/69/aa/a869aa2f1c3cddbf8813d565fcf5bec8.jpg"), closeSoftKeyboard())

        //нажатие на кнопку
        onView(withId(R.id.button_to_add)).perform(click())
        sleep(5000)
        // Проверка, что изображение загружено в ImageView
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testImageLoadFailure() {
        // Ввод не корректной ссылки в EditText
        onView(withId(R.id.editTextText))
            .perform(typeText("https://i.pinimg.com/736x/a8/69/aa/a869aa2f1c3cddbf8813d565fcf5bec8"), closeSoftKeyboard())

        //нажатие на кнопку
        onView(withId(R.id.button_to_add)).perform(click())

        // Проверка, что изображение не изменилось в ImageView
        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())))


    }

}







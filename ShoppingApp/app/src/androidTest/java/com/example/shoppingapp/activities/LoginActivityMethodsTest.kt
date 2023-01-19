package com.example.shoppingapp.activities


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityMethodsTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginActivityMethodsTest() {
        val button = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.email_button), withText("Sign in with email"),
                withParent(
                    allOf(
                        withId(com.firebase.ui.auth.R.id.btn_holder)
                    )
                ),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withText("Sign in with Google"),
                withParent(
                    allOf(
                        withId(com.firebase.ui.auth.R.id.btn_holder)
                    )
                ),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withText("Sign in with GitHub"),
                withParent(
                    allOf(
                        withId(com.firebase.ui.auth.R.id.btn_holder)
                    )
                ),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))
    }
}

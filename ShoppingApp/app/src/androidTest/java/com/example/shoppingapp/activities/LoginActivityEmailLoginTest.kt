package com.example.shoppingapp.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityEmailLoginTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginActivityEmailLoginTest() {
        val supportVectorDrawablesButton = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.email_button), withText("Sign in with email"),
                childAtPosition(
                    allOf(
                        withId(com.firebase.ui.auth.R.id.btn_holder)
                    ),
                    0
                )
            )
        )
        supportVectorDrawablesButton.perform(scrollTo(), click())

        val textInputEditText = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.email),
                childAtPosition(
                    childAtPosition(
                        withId(com.firebase.ui.auth.R.id.email_layout),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("test@test.com"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.button_next), withText("Next"),
                childAtPosition(
                    allOf(
                        withId(com.firebase.ui.auth.R.id.email_top_layout),
                        childAtPosition(
                            withClassName(`is`("android.widget.ScrollView")),
                            0
                        )
                    ),
                    2
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val textView = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.welcome_back_password_body),
                withText("You've already used test@test.com to sign in. Enter your password for that account."),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("You've already used test@test.com to sign in. Enter your password for that account.")))

        val textInputEditText2 = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(com.firebase.ui.auth.R.id.password_layout),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("test123"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(com.firebase.ui.auth.R.id.button_done), withText("Sign in"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        materialButton2.perform(scrollTo(), click())


    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

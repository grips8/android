package com.example.shoppingapp.activities


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.shoppingapp.R
import com.example.shoppingapp.TestUser
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ViewProductTest {

    @Before
    fun logUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInWithEmailAndPassword(TestUser.email, TestUser.password)
        }
    }

    @Test
    fun viewProductTest() {
        Thread.sleep(3000)  // to ensure user is logged in?
        ActivityScenario.launch(LoginActivity::class.java).use {
            val textView4 = onView(
                allOf(
                    withId(R.id.productNameTextView), withText("Avocado"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView4.check(matches(withText("Avocado")))

            val textView5 = onView(
                allOf(
                    withId(R.id.productPriceTextView), withText("7.99"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView5.check(matches(withText("7.99")))

            val recyclerView = onView(
                allOf(
                    withId(R.id.productRecyclerView),
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    )
                )
            )
            recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

            val textView = onView(
                allOf(
                    withId(R.id.detailsName), withText("Avocado"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()
                )
            )
            textView.check(matches(withText("Avocado")))

            val textView2 = onView(
                allOf(
                    withId(R.id.detailsPrice), withText("7.99"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()
                )
            )
            textView2.check(matches(withText("7.99")))

            val textView3 = onView(
                allOf(
                    withId(R.id.detailsCategoryCont), withText("Food"),
                    withParent(withParent(withId(android.R.id.content))),
                    isDisplayed()
                )
            )
            textView3.check(matches(withText("Food")))

            pressBack()

            val bottomNavigationItemView = onView(
                allOf(
                    withId(R.id.userFragment), withContentDescription("Account"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView.perform(click())

            val materialButton3 = onView(
                allOf(
                    withId(R.id.logoutButton), withText("LOGOUT"),
                    childAtPosition(
                        childAtPosition(
                            withClassName(`is`("android.widget.FrameLayout")),
                            0
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            materialButton3.perform(click())
        }
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

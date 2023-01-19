package com.example.shoppingapp.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.shoppingapp.R
import com.example.shoppingapp.TestUser
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegularUserMenuTest {

    @Before
    fun logUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInWithEmailAndPassword(TestUser.email, TestUser.password)
        }
    }

    @Test
    fun regularUserMenuTest() {
        Thread.sleep(3000)  // to ensure user is logged in?
        ActivityScenario.launch(LoginActivity::class.java).use {
            val frameLayout = onView(
                allOf(
                    withId(R.id.productsListFragment), withContentDescription("Products"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                    isDisplayed()
                )
            )
            frameLayout.check(matches(isDisplayed()))

            val frameLayout2 = onView(
                allOf(
                    withId(R.id.categoriesListFragment), withContentDescription("Categories"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                    isDisplayed()
                )
            )
            frameLayout2.check(matches(isDisplayed()))

            val frameLayout3 = onView(
                allOf(
                    withId(R.id.basketFragment), withContentDescription("Basket"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                    isDisplayed()
                )
            )
            frameLayout3.check(matches(isDisplayed()))

            val frameLayout4 = onView(
                allOf(
                    withId(R.id.userFragment), withContentDescription("Account"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                    isDisplayed()
                )
            )
            frameLayout4.check(matches(isDisplayed()))

            val frameLayout5 = onView(
                allOf(
                    withId(R.id.adminFragment), withContentDescription("Admin"),
                    withParent(withParent(withId(R.id.bottom_nav_view)))
                )
            )
            frameLayout5.check(matches(not(isDisplayed())))

            onView(withId(R.id.userFragment)).perform(click())
            onView(withId(R.id.logoutButton)).perform(click())
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

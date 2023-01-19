package com.example.shoppingapp.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
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
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductAddTest {

    @Before
    fun logUser() {
        val auth = FirebaseAuth.getInstance()
//        auth.signOut()
        auth.signInWithEmailAndPassword(TestUser.adminEmail, TestUser.adminPassword)
    }

    @Test
    fun productAddTest() {
        Thread.sleep(3000)  // to ensure user is logged in?
        ActivityScenario.launch(LoginActivity::class.java).use {
            val bottomNavigationItemView = onView(
                allOf(
                    withId(R.id.adminFragment), withContentDescription("Admin"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        4
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView.perform(click())

            val editText = onView(
                allOf(
                    withId(R.id.adminNameInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText.check(matches(isDisplayed()))

            val editText2 = onView(
                allOf(
                    withId(R.id.adminPriceInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText2.check(matches(isDisplayed()))

            val editText3 = onView(
                allOf(
                    withId(R.id.adminDescriptionInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText3.check(matches(isDisplayed()))

            val spinner = onView(
                allOf(
                    withId(R.id.adminCategorySpinner),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            spinner.check(matches(isDisplayed()))
            spinner.perform(click())
            onData(
                allOf(
                    `is`(instanceOf(String::class.java)),
                    `is`("Meds")
                )
            ).perform(click())
            spinner.check(matches(withSpinnerText(containsStringIgnoringCase("Meds"))))
            spinner.perform(click())
            onData(
                allOf(
                    `is`(instanceOf(String::class.java)),
                    `is`("Food")
                )
            ).perform(click())
            spinner.check(matches(withSpinnerText(containsStringIgnoringCase("Food"))))

            val imageButton = onView(
                allOf(
                    withId(R.id.adminSubmit),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            imageButton.check(matches(isDisplayed()))

            val bottomNavigationItemView2 = onView(
                allOf(
                    withId(R.id.categoriesListFragment), withContentDescription("Categories"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView2.perform(click())

            val textView = onView(
                allOf(
                    withText("Food"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView.check(matches(isDisplayed()))

            val textView2 = onView(
                allOf(
                    withText("Meds"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView2.check(matches(withText("Meds")))

            val bottomNavigationItemView3 = onView(
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
            bottomNavigationItemView3.perform(click())

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

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
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BasketTest {

    @Before
    fun logUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInWithEmailAndPassword(TestUser.email, TestUser.password)
        }
    }

    @Test
    fun basketTest() {
        Thread.sleep(3000)  // to ensure user is logged in?
        ActivityScenario.launch(LoginActivity::class.java).use {
            val appCompatImageButton = onView(
                allOf(
                    withId(R.id.productAddToBasketButton),
                    childAtPosition(
                        childAtPosition(
                            childAtPosition(
                                withId(R.id.productRecyclerView),
                                0
                            ),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton.perform(click())

            val bottomNavigationItemView = onView(
                allOf(
                    withId(R.id.basketFragment), withContentDescription("Basket"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView.perform(click())

            val textView = onView(
                allOf(
                    withId(R.id.bProductQty), withText("1"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView.check(matches(withText("1")))

            val textView2 = onView(
                allOf(
                    withId(R.id.bProductNameTextView), withText("Avocado"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView2.check(matches(withText("Avocado")))

            val textView3 = onView(
                allOf(
                    withId(R.id.bProductPriceTextView), withText("7.99"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView3.check(matches(withText("7.99")))

            val bottomNavigationItemView2 = onView(
                allOf(
                    withId(R.id.productsListFragment), withContentDescription("Products"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView2.perform(click())

            val appCompatImageButton2 = onView(
                allOf(
                    withId(R.id.productAddToBasketButton),
                    childAtPosition(
                        childAtPosition(
                            childAtPosition(
                                withId(R.id.productRecyclerView),
                                0
                            ),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton2.perform(click())

            val bottomNavigationItemView3 = onView(
                allOf(
                    withId(R.id.basketFragment), withContentDescription("Basket"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView3.perform(click())

            val textView4 = onView(
                allOf(
                    withId(R.id.bProductQty), withText("2"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView4.check(matches(withText("2")))

            val appCompatImageButton3 = onView(
                allOf(
                    withId(R.id.bProductAddQty),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.card_view),
                            0
                        ),
                        5
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton3.perform(click())

            val textView5 = onView(
                allOf(
                    withId(R.id.bProductQty), withText("3"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView5.check(matches(withText("3")))

            val appCompatImageButton4 = onView(
                allOf(
                    withId(R.id.bProductRemoveQty),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.card_view),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton4.perform(click())

            val appCompatImageButton5 = onView(
                allOf(
                    withId(R.id.bProductRemoveQty),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.card_view),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton5.perform(click())

            val appCompatImageButton6 = onView(
                allOf(
                    withId(R.id.bProductRemoveQty),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.card_view),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton6.perform(click())

            val bottomNavigationItemView4 = onView(
                allOf(
                    withId(R.id.productsListFragment), withContentDescription("Products"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView4.perform(click())

            val appCompatImageButton7 = onView(
                allOf(
                    withId(R.id.productAddToBasketButton),
                    childAtPosition(
                        childAtPosition(
                            childAtPosition(
                                withId(R.id.productRecyclerView),
                                0
                            ),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton7.perform(click())

            val bottomNavigationItemView5 = onView(
                allOf(
                    withId(R.id.basketFragment), withContentDescription("Basket"),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.bottom_nav_view),
                            0
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )
            bottomNavigationItemView5.perform(click())

            val textView6 = onView(
                allOf(
                    withId(R.id.bProductQty), withText("1"),
                    withParent(withParent(withId(R.id.card_view))),
                    isDisplayed()
                )
            )
            textView6.check(matches(withText("1")))

            val appCompatImageButton8 = onView(
                allOf(
                    withId(R.id.bProductRemoveQty),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.card_view),
                            0
                        ),
                        3
                    ),
                    isDisplayed()
                )
            )
            appCompatImageButton8.perform(click())

            val bottomNavigationItemView6 = onView(
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
            bottomNavigationItemView6.perform(click())

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

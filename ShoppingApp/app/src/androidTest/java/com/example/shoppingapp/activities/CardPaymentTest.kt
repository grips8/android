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
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.pressBack

@LargeTest
@RunWith(AndroidJUnit4::class)
class CardPaymentTest {

    @Before
    fun logUser() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInWithEmailAndPassword(TestUser.email, TestUser.password)
        }
    }

    @Test
    fun cardPaymentTest() {
        Thread.sleep(3000)  // to ensure user is logged in?
        ActivityScenario.launch(LoginActivity::class.java).use {
            val appCompatImageButton = onView(
                allOf(
                    withId(R.id.productAddToBasketButton),
                    childAtPosition(
                        allOf(
                            withId(R.id.product_row_layout),
                            childAtPosition(
                                childAtPosition(
                                    withId(R.id.productRecyclerView),
                                    2
                                ),
                                0
                            )
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

            val materialButton3 = onView(
                allOf(
                    withId(R.id.orderButton), withText("GO TO CHECKOUT"),
                    childAtPosition(
                        allOf(
                            withId(R.id.texture),
                            childAtPosition(
                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                1
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            materialButton3.perform(click())

            val textView = onView(
                allOf(
                    withText("Choose payment method: "),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            textView.check(matches(withText("Choose payment method: ")))

            val button = onView(
                allOf(
                    withId(R.id.cardButton), withText("CREDIT CARD"),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            button.check(matches(isDisplayed()))

            val button2 = onView(
                allOf(
                    withId(R.id.stripeButton), withText("STRIPE"),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            button2.check(matches(isDisplayed()))

            val button3 = onView(
                allOf(
                    withId(R.id.payuButton), withText("PAYU (TBA)"),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            button3.check(matches(isDisplayed()))

            val frameLayout = onView(
                allOf(
                    withId(R.id.productsListFragment), withContentDescription("Products"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            )
            frameLayout.check(matches(not(isDisplayed())))

            val frameLayout2 = onView(
                allOf(
                    withId(R.id.categoriesListFragment), withContentDescription("Categories"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            )
            frameLayout2.check(matches(not(isDisplayed())))

            val frameLayout3 = onView(
                allOf(
                    withId(R.id.basketFragment), withContentDescription("Basket"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            )
            frameLayout3.check(matches(not(isDisplayed())))

            val frameLayout4 = onView(
                allOf(
                    withId(R.id.userFragment), withContentDescription("Account"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            )
            frameLayout4.check(matches(not(isDisplayed())))

            val frameLayout5 = onView(
                allOf(
                    withId(R.id.adminFragment), withContentDescription("Admin"),
                    withParent(withParent(withId(R.id.bottom_nav_view)))
                )
            )
            frameLayout5.check(matches(not(isDisplayed())))

            val materialButton4 = onView(
                allOf(
                    withId(R.id.cardButton), withText("credit card"),
                    childAtPosition(
                        childAtPosition(
                            withClassName(`is`("android.widget.FrameLayout")),
                            0
                        ),
                        1
                    ),
                    isDisplayed()
                )
            )
            materialButton4.perform(click())

            val textView2 = onView(
                allOf(
                    withId(R.id.label), withText("ENTER MAGIC NUMBERS :)"),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            textView2.check(matches(withText("ENTER MAGIC NUMBERS :)")))

            val textView3 = onView(
                allOf(
                    withId(R.id.cardName), withText("name and surname: "),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            textView3.check(matches(withText("name and surname: ")))

            val editText = onView(
                allOf(
                    withId(R.id.cardNameInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText.check(matches(isDisplayed()))
            editText.check(matches(withHint(containsStringIgnoringCase("ewan mcgregor"))))

            val textView4 = onView(
                allOf(
                    withId(R.id.cardNumber), withText("card number: "),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            textView4.check(matches(withText("card number: ")))

            val editText2 = onView(
                allOf(
                    withId(R.id.cardNumberInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText2.check(matches(isDisplayed()))
            editText2.check(matches(withHint(containsStringIgnoringCase("1234 5678 9000 1234"))))


            val textView5 = onView(
                allOf(
                    withId(R.id.cardExp), withText("exp. date: "),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            textView5.check(matches(withText("exp. date: ")))

            val editText3 = onView(
                allOf(
                    withId(R.id.cardExpInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText3.check(matches(isDisplayed()))
            editText3.check(matches(withHint(containsStringIgnoringCase("MM/YYYY"))))


            val textView6 = onView(
                allOf(
                    withId(R.id.cardCVV), withText("CVV code: "),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            textView6.check(matches(withText("CVV code: ")))

            val editText4 = onView(
                allOf(
                    withId(R.id.cardCVVInput),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            editText4.check(matches(isDisplayed()))
            editText4.check(matches(withHint(containsStringIgnoringCase("000"))))

            val imageButton = onView(
                allOf(
                    withId(R.id.orderSubmit),
                    withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                    isDisplayed()
                )
            )
            imageButton.check(matches(isDisplayed()))

            pressBack()
            pressBack()

            val appCompatImageButton2 = onView(
                allOf(
                    withId(R.id.bProductRemoveQty),
                    childAtPosition(
                        childAtPosition(
                            childAtPosition(
                                withId(R.id.basketRecyclerView),
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

            onView(
                allOf(
                    withId(R.id.productsListFragment), withContentDescription("Products"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            ).check(matches(isDisplayed()))
            onView(
                allOf(
                    withId(R.id.categoriesListFragment), withContentDescription("Categories"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            ).check(matches(isDisplayed()))
            onView(
                allOf(
                    withId(R.id.basketFragment), withContentDescription("Basket"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            ).check(matches(isDisplayed()))
            onView(
                allOf(
                    withId(R.id.userFragment), withContentDescription("Account"),
                    withParent(withParent(withId(R.id.bottom_nav_view))),
                )
            ).check(matches(isDisplayed()))
            onView(
                allOf(
                    withId(R.id.adminFragment), withContentDescription("Admin"),
                    withParent(withParent(withId(R.id.bottom_nav_view)))
                )
            ).check(matches(not(isDisplayed())))

            val bottomNavigationItemView2 = onView(
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
            bottomNavigationItemView2.perform(click())

            val materialButton5 = onView(
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
            materialButton5.perform(click())
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

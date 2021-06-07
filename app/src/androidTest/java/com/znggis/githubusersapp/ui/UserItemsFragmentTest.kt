package com.znggis.githubusersapp.ui

import android.app.Application
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModelStore
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.znggis.githubusersapp.MainActivity
import com.znggis.githubusersapp.R
import com.znggis.githubusersapp.launchFragmentInHiltContainer
import com.znggis.githubusersapp.launchFragmentInHiltContainer2
import com.znggis.githubusersapp.repo.FakeRepo
import com.znggis.githubusersapp.repo.Repository
import com.znggis.githubusersapp.repo.model.GitHubItem
import com.znggis.githubusersapp.ui.adapter.ItemViewHolder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


val item = GitHubItem(
    avatarUrl = "test",
    score = 1,
    isFavourite = false,
    item = "Login",
    id = 111
)

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class UserItemsFragmentTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: Repository

    @Before
    fun init() = runBlockingTest {
        hiltRule.inject()
    }


    @Test
    fun loadDataSuccess_displayListOfData() {

        (repo as FakeRepo).items.add(item)

        launchFragmentInHiltContainer<UserItemsFragment>()

        onView(withId(R.id.list)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            assertEquals(1, recyclerView.adapter?.itemCount)
        }
        onView(withText(item.item)).check(ViewAssertions.matches(isDisplayed()))

    }

    @Test
    fun loadDataSuccess_clickOnItem_ShowDetailFrg() {

        (repo as FakeRepo).items.add(item)

        launchActivity<MainActivity>()

        onView(withId(R.id.list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ItemViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        onView(withId(R.id.tv_user_score))
            .check(
                ViewAssertions.matches(withText("Score: " + item.score))
            )

        onView(withId(R.id.tv_user_name))
            .check(
                ViewAssertions.matches(withText(item.item))
            )
    }

}
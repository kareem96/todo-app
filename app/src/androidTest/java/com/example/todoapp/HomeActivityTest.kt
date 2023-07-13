package com.example.todoapp

import androidx.test.core.app.ActivityScenario
import com.example.todoapp.presentation.add.AddTaskActivity
import com.example.todoapp.presentation.home.HomeActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest {
        @Before
        fun setUp() {
            ActivityScenario.launch(HomeActivity::class.java)
            init()
        }

        @Test
        fun loadAddTaskActivity() {
            onView(withId(R.id.fl_add_task)).perform(click())
            intended(hasComponent(AddTaskActivity::class.java.name))
        }

        @After
        fun afterLoad() {
            release()
        }
}
package com.bennettzhang.zooseeker;

import android.view.KeyEvent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;


@RunWith(AndroidJUnit4.class)
public class SearchExhibitTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mScenarioTestRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void searchBoxUITest() {
        ViewInteraction autoCompleteTextView = onView(withId(R.id.search_box));

        autoCompleteTextView.check(matches(isDisplayed()));
    }

    @Test
    public void searchBoxUIClickTest() {
        ViewInteraction autoCompleteTextView = onView(withId(R.id.search_box));

        autoCompleteTextView.check(matches(isDisplayed()));

        autoCompleteTextView.perform(click());
        autoCompleteTextView.perform(typeText("Arctic Foxes"));
        autoCompleteTextView.perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onData(hasToString(startsWith("Arctic Foxes")))
                .inRoot(RootMatchers.isPlatformPopup())
                .atPosition(0)
                .onChildView(withId(R.id.autocomplete_checkbox))
                .perform(click());

//        autoCompleteTextView.check(matches(withText("Search for exhibits")));

//        autoCompleteTextViewonView(withText("Arctic Foxes")).perform(click());
//        autoCompleteTextView.perform(click(withText("Arctic Foxes")));

//        onData(withChild(withText("Arctic Foxes"))).perform(click());
//        onView(withText(containsString("Arctic Foxes"))).perform(click());

//        onView(withText("Arctic Foxes"))
//                .inRoot(RootMatchers.isPlatformPopup())
//                .perform(click());

//        autoCompleteTextView.perform(click());

//        ViewInteraction checkBox = onView(
//                allOf(withId(R.id.autocomplete_checkbox),
//                        withParent(withParent(withId(R.id.container))),
//                        isDisplayed()));

//        onData(anything()).atPosition(0).check(matches(hasDescendant(withText(("Arctic Foxes")))));

//        checkBox.perform(click());
    }

    @Test
    public void searchBoxUIClickTest2() {
        ViewInteraction autoCompleteTextView = onView(withId(R.id.search_box));

        autoCompleteTextView.check(matches(isDisplayed()));

        autoCompleteTextView.perform(click());
        autoCompleteTextView.perform(typeText("A"));
        autoCompleteTextView.perform(pressKey(KeyEvent.KEYCODE_ENTER));

        onData(hasToString(startsWith("Gorillas")))
                .inRoot(RootMatchers.isPlatformPopup())
                .onChildView(withId(R.id.autocomplete_checkbox))
                .perform(click());
    }
}

package com.example.franck.myapplication;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.ViewPagerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.franck.myapplication.activity.MainActivity;
import com.example.franck.myapplication.utils.TestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.espresso.contrib.RecyclerViewActions;

@RunWith(AndroidJUnit4.class)
public class RecyclerTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setUp() {
        activityRule.launchActivity(null);

    }

    @After
    public void after() {
        setWifiEnabled(activityRule.getActivity().getApplicationContext(),true);
    }


    //Turn off wifi, test start_page
    @Test
    public void withoutInternetTest() {
        startPageTest();
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        startPageTest();
    }

    //test recycler by clicking on each image LandScape
    @Test
    public void orientationScreenTestClick() {
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        testRecycler(TestHelper.RecyclerState.CLICK);
    }

    //test recycler by scrolling all images LandScape
    @Test
    public void orientationScreenTestScroll() {
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        testRecycler(TestHelper.RecyclerState.SCROLL);
    }

    //test recycler by clicking on each image Portrait
    @Test
    public void testRecyclerByClickView() {
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        testRecycler(TestHelper.RecyclerState.CLICK);
    }

    //test recycler by scrolling all images Portrait
    @Test
    public void testRecyclerByScrollView() throws Exception {
        activityRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        testRecycler(TestHelper.RecyclerState.SCROLL);
    }

    private void setWifiEnabled(Context context, boolean status) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(status);
    }

    private void startPageTest() {
        SystemClock.sleep(1000);
        setWifiEnabled(activityRule.getActivity().getApplicationContext(),false);
        onView(withId(R.id.startimage)).check(matches(isCompletelyDisplayed()));
        SystemClock.sleep(1000);
        onView(withId(R.id.fab)).perform(click());
        SystemClock.sleep(1000);
        onView(withId(R.id.startimage)).check(matches(isCompletelyDisplayed()));
    }

    private void testRecycler(TestHelper.RecyclerState recyclerState) {
        for (int j = 0; j < MainActivity.AMOUNT_PAGES; j++) {
            onView(withId(R.id.fab)).perform(click());
            SystemClock.sleep(500);
            int count = TestHelper.getCountFromRecyclerView(R.id.recycler_view);
            switch (recyclerState) {
                case CLICK:
                    for (int i = 0; i < count; i++) {
                        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
                        SystemClock.sleep(100);
                        onView(withId(R.id.lbl_count)).check(matches(isCompletelyDisplayed()));
                        Espresso.pressBack();
                    }
                    break;
                case SCROLL:
                    onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
                    for (int i = 0; i < count; i++) {
                        onView(withId(R.id.lbl_count)).check(matches(isCompletelyDisplayed()));
                        onView(withId(R.id.viewpager)).perform(ViewPagerActions.scrollRight());
                        SystemClock.sleep(200);
                    }
                    Espresso.pressBack();
                    break;
            }
        }
    }
}


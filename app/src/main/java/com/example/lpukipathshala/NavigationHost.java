package com.example.lpukipathshala;

import android.support.v4.app.Fragment;

interface NavigationHost {
     void navigateTo(Fragment fragment, boolean addToBackstack);
}

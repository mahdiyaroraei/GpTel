package ir.parhoonco.traccar.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ir.parhoonco.traccar.R;

/**
 * Created by Parhoon on 7/26/2016.
 */
public class FragmentHelper {
    private static FragmentHelper instance;
    private static Fragment currentFragment;
    private FragmentManager fragmentManager;

    public FragmentHelper(Context context) {
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    public static FragmentHelper getInstance(Context context) {
        if (instance == null) {
            instance = new FragmentHelper(context);
        }
        return instance;
    }

    public void addToStack(Fragment fragment) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (currentFragment != null) {
                transaction.remove(currentFragment);
                transaction.commit();
                transaction = fragmentManager.beginTransaction();
            }
            transaction.replace(R.id.fragmentLayout, fragment);
            transaction.commit();
            currentFragment = fragment;
        } catch (Exception e) {
        }
    }

    public void add(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.commit();
    }
}

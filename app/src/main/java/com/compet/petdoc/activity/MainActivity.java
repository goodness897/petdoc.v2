package com.compet.petdoc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.compet.petdoc.R;
import com.compet.petdoc.fragment.RegionListFragment;
import com.compet.petdoc.fragment.SearchFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private long backKeyPressedTime = 0;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar(getString(R.string.app_name));

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new RegionListFragment(), "RegionItem").commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            fm.beginTransaction()
              .replace(R.id.container, new SearchFragment(), "RegionItem")
              .addToBackStack(null)
              .commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

}

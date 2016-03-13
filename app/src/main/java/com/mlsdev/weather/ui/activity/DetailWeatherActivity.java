package com.mlsdev.weather.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mlsdev.weather.model.Weather;
import com.mlsdev.weather.presenter.WeatherDetailFrPresenter;
import com.mlsdev.weather.services.impl.ServiceManager;
import com.mlsdev.weather.ui.fragment.impl.WeatherDetailFr;

import java.io.Serializable;
import java.util.List;

import mlsdev.com.weather.R;

/**
 * Created by romakukhar on 05.02.15.
 */
public class DetailWeatherActivity extends BaseActivity implements Serializable {

    private static int PAGES_NUMBER;
    private List<Weather> weatherList;
    public static String CURRENT_ITEM = "CURRENT_ITEM";
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private ProgressDialog progressDialog;

    private WeatherDetailFrPresenter presenter = new WeatherDetailFrPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        initComponents();
    }

    private void initList() {
        weatherList = ServiceManager.getWeatherService().getAllWeathers();
        PAGES_NUMBER = weatherList.size();
    }

    private void initComponents() {
        viewPager = (ViewPager) findViewById(R.id.detail_weather_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(getIntent().getIntExtra(CURRENT_ITEM, 0));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected int getLayout() {
        return R.layout.detail_weather_container_layout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.update_item_action:
                presenter.updateDailyWeather(weatherList.get(viewPager.getCurrentItem()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Weather weather = weatherList.get(position);
            return WeatherDetailFr.newInstance(weather, getActivity());
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return PAGES_NUMBER;
        }
    }

    public void updateViewPager() {
        initList();
        pagerAdapter.notifyDataSetChanged();
    }

    public void showProgressDialog(String tittle, String message) {
        progressDialog = new ProgressDialog(getActivity(), R.style.progressDialogStyle);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.setTitle(tittle);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    public void showErrorUpdateDailyWeather(String error){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    public DetailWeatherActivity getActivity() {
        return this;
    }

}

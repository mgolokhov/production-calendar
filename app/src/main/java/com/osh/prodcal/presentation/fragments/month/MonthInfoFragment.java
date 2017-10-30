package com.osh.prodcal.presentation.fragments.month;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osh.prodcal.R;
import com.osh.prodcal.application.di.AppComponent;
import com.osh.prodcal.common.presentation.adapter.ViewEntityAdapter;
import com.osh.prodcal.domain.MonthEntity;
import com.osh.prodcal.presentation.fragments.common.BaseFragment;
import com.osh.prodcal.presentation.presenters.MonthInfoPresenter;
import com.osh.prodcal.presentation.views.MonthInfoView;
import com.osh.prodcal.presentation.views.TitleValueItemView;
import com.osh.prodcal.presentation.views.utils.CollectionUtils;
import com.osh.prodcal.presentation.views.utils.StringUtils;
import com.osh.prodcal.presentation.views.utils.ViewUtils;

import java.util.Calendar;

import javax.inject.Inject;

public class MonthInfoFragment extends BaseFragment<MonthInfoPresenter> implements MonthInfoView {

    /*
    private ViewGroup holidaysContainer;
    private ViewGroup dayStatContainer;
    private ViewGroup weekHoursStatContainer;
    private ViewGroup otherContainer;
    */
    private RecyclerView list;
    private ViewEntityAdapter<TitleValueItemView, TitleValueItemView.Data, TitleValueItemView.Listener> adapter;

    public MonthInfoFragment(){
        setRetainInstance(true);
    }

    @Inject
    public MonthInfoPresenter presenter;

    @Override
    public MonthInfoPresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_month_info, container, false);
        initView(fragmentView);
        getPresenter().setView(this);
        return fragmentView;
    }

    private void initView(View view){
        /*
        holidaysContainer = ViewUtils.findViewById(view, R.id.holidaysContainer);
        dayStatContainer = ViewUtils.findViewById(view, R.id.dayStatContainer);
        weekHoursStatContainer = ViewUtils.findViewById(view, R.id.weekHoursStatContainer);
        otherContainer = ViewUtils.findViewById(view, R.id.otherContainer);
        */
        list = ViewUtils.findViewById(view, R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);
        adapter = new ViewEntityAdapter<>(R.layout.view_title_value_item, d->{});
        list.setAdapter(adapter);
    }

    @Override
    public void showMonth(MonthEntity monthEntity) {

        adapter.setItems(CollectionUtils.list(
                new TitleValueItemView.Data(getString(R.string.title_number_of_days_total), StringUtils.toString(monthEntity.getNumberOfDay())),
                new TitleValueItemView.Data(getString(R.string.title_number_of_working_days), StringUtils.toString(monthEntity.getNumberOfWorkingDay())),
                new TitleValueItemView.Data(getString(R.string.title_number_of_non_working_days), StringUtils.toString(monthEntity.getNumberOfNonWorkingDay())),

                new TitleValueItemView.Data(getString(R.string.title_number_of_working_hours_40h), StringUtils.toString(monthEntity.getNumberOfHoursForWorkWeek(40))),
                new TitleValueItemView.Data(getString(R.string.title_number_of_working_hours_36h), StringUtils.toString(monthEntity.getNumberOfHoursForWorkWeek(36))),
                new TitleValueItemView.Data(getString(R.string.title_number_of_working_hours_24h), StringUtils.toString(monthEntity.getNumberOfHoursForWorkWeek(24))),

                new TitleValueItemView.Data(getString(R.string.title_month), StringUtils.toString(monthEntity.getCurrentMonth().get(Calendar.MONTH)+1)),
                new TitleValueItemView.Data(getString(R.string.title_quarter), StringUtils.toString(monthEntity.getCurrentQuarter())),
                new TitleValueItemView.Data(getString(R.string.title_half_year), StringUtils.toString(monthEntity.getCurrentHalfYear())),
                new TitleValueItemView.Data(getString(R.string.title_year), StringUtils.toString(monthEntity.getCurrentYear()))

        ));

        /*
        if(monthEntity.getHolidays().size()>0){
            ViewUtils.show(getView(), R.id.holidaysContainer);
            ViewUtils.show(getView(), R.id.holidaysTitle);
            ViewUtils.fillContainerWithItems(holidaysContainer,
                    R.layout.view_holiday_info_item,
                    monthEntity.getHolidays(),
                    (data, view)->view.setOnClickListener((v) -> presenter.onHolidayClicked(data)));

        }else{
            ViewUtils.hide(getView(), R.id.holidaysContainer);
            ViewUtils.hide(getView(), R.id.holidaysTitle);
        }

        ViewUtils.fillContainerWithItems(dayStatContainer,
                R.layout.view_title_value_item,
                CollectionUtils.list(
                        new TitleValueItemView.Data(getString(R.string.title_number_of_days_total), StringUtils.toString(monthEntity.getNumberOfDay())),
                        new TitleValueItemView.Data(getString(R.string.title_number_of_working_days), StringUtils.toString(monthEntity.getNumberOfWorkingDay())),
                        new TitleValueItemView.Data(getString(R.string.title_number_of_non_working_days), StringUtils.toString(monthEntity.getNumberOfNonWorkingDay()))
                ),
                null);

        ViewUtils.fillContainerWithItems(weekHoursStatContainer,
                R.layout.view_title_value_item,
                CollectionUtils.list(
                        new TitleValueItemView.Data(getString(R.string.title_number_of_working_hours_40h), StringUtils.toString(monthEntity.getNumberOfHoursForWorkWeek(40))),
                        new TitleValueItemView.Data(getString(R.string.title_number_of_working_hours_36h), StringUtils.toString(monthEntity.getNumberOfHoursForWorkWeek(36))),
                        new TitleValueItemView.Data(getString(R.string.title_number_of_working_hours_24h), StringUtils.toString(monthEntity.getNumberOfHoursForWorkWeek(24)))
                ),
                null);


        ViewUtils.fillContainerWithItems(otherContainer,
                R.layout.view_title_value_item,
                CollectionUtils.list(
                        new TitleValueItemView.Data(getString(R.string.title_month), StringUtils.toString(monthEntity.getCurrentMonth().get(Calendar.MONTH)+1)),
                        new TitleValueItemView.Data(getString(R.string.title_quarter), StringUtils.toString(monthEntity.getCurrentQuarter())),
                        new TitleValueItemView.Data(getString(R.string.title_half_year), StringUtils.toString(monthEntity.getCurrentHalfYear())),
                        new TitleValueItemView.Data(getString(R.string.title_year), StringUtils.toString(monthEntity.getCurrentYear()))
                ),
                null);
                */

    }

}
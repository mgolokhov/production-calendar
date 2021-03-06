package com.osh.prodcal.domain.usecase;

import com.osh.prodcal.domain.MonthEntity;
import com.osh.prodcal.domain.repository.MonthRepository;
import com.osh.mvp.domain.executor.PostExecutionThread;
import com.osh.mvp.domain.executor.ThreadExecutor;
import com.osh.mvp.domain.usecase.BaseUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by olegshatava on 22.10.17.
 */

public class GetMonthEntitiesList extends BaseUseCase<List<MonthEntity>, Void, MonthRepository>{


    @Inject
    public GetMonthEntitiesList(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, MonthRepository monthRepository) {
        super(threadExecutor, postExecutionThread, monthRepository);
    }

    @Override
    public void execute(final Void data, Consumer<List<MonthEntity>> onData, Consumer<Throwable> onError) {
        buildAndSubscribe(getRepository().getMonths(), onData, onError);
    }

}

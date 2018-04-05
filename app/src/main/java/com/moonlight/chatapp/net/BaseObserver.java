package com.moonlight.chatapp.net;

import com.moonlight.chatapp.bean.BaseResponse;
import com.moonlight.chatapp.common.ResponseCode;
import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/7/24.
 */

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private final String SUCCESS_CODE = Integer.toString(ResponseCode.SUCCESS);

    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(final BaseResponse<T> value) {
        if (value.getReturnCode().equals(SUCCESS_CODE)) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getReturnCode(), value.getReturnMsg());
        }
    }


    @Override
    public void onError(Throwable e) {
        Logger.e("网络异常，请稍后再试---error:" + e.toString());
//        onHandleError("", "网络异常，请稍后再试---error:" + e.toString());
        onHandleError("", "");
    }

    @Override
    public void onComplete() {

    }

    public abstract void onHandleSuccess(T t);

    public abstract void onHandleError(String code, String message);
}

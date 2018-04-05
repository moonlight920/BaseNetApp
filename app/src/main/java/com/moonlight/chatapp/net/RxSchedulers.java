package com.moonlight.chatapp.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.moonlight.chatapp.MyApplication;
import com.moonlight.chatapp.utils.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by songyifeng on 21/08/2017.
 */

public class RxSchedulers {

    private static final long RETRY_TIMES = 0;

    public static <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .retry(RETRY_TIMES)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (!isNetworkAvailable(MyApplication.getInstance())) {
                                    ToastUtil.show("网络不可用，请检查网络后重试");
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 判断网络是否可用
     */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isAvailable();
    }
}

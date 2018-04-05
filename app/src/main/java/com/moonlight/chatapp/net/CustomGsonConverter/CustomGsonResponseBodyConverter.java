package com.moonlight.chatapp.net.CustomGsonConverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by moonlight on 16/08/2017.
 */

final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();

        // 服务器返回的data由字符串变为对象！！！
        // 特殊处理
//        if(response.contains("data")){
//            HttpStatus httpStatus = gson.fromJson(response, HttpStatus.class);
//
//            if (!httpStatus.getData().toString().contains("{") ||
//                    "".equals(httpStatus.getData()) || httpStatus.getData() == null) {
//                httpStatus.setData(new Object());
//                response = gson.toJson(httpStatus);
//            }
//        }

        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}

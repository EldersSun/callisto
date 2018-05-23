package com.maizi.http.builder;


import com.maizi.http.RequestCall;
import com.maizi.http.request.PostFormRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daixinglong on 2017/5/12.
 */

public class PostFormBuilder extends BaseBuilder<PostFormBuilder> implements AddParams {


    private List<FileInput> files = new ArrayList<>();

    @Override
    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }


    public PostFormBuilder files(Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(filename, filename, files.get(filename)));
        }
        return this;
    }

    public PostFormBuilder addFile(String filename, File file) {
        files.add(new FileInput(filename, filename, file));
        return this;
    }


    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, requestId, invokeType, cache, hasResponseCode, files, null).build();
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }


}

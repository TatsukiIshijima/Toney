package com.io.tatsuki.toney.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * 画像系ユーティリティクラス
 */

public class ImageUtil {

    /**
     * 画像をダウンロードしてViewにセットする
     * @param context
     * @param path          画像ファイルパス
     * @param imageView     View
     */
    public static void setDownloadImage(@NonNull Context context, String path, ImageView imageView) {
        Picasso.with(context).load(new File(path)).fit().into(imageView);
    }
}

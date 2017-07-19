package com.io.tatsuki.toney.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    /**
     * 画像のデコード
     * @param path  画像パス
     * @param w     横幅
     * @param h     縦幅
     * @return
     */
    public static Bitmap decodeBitmap(String path, int w, int h) {
        // 画像のサイズの取得
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 画像サイズの変更(inSampleSizeの算出)
        options.inSampleSize = calcInSampleSize(options, w, h);
        // inSampleSizeを設定し，デコード
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * inSampleSizeの算出
     * @param options
     * @param w         横幅
     * @param h         縦幅
     * @return
     */
    public static int calcInSampleSize(BitmapFactory.Options options, int w, int h) {
        int inSampleSize = 1;
        float imgScaleWidth = (float) options.outWidth / w;
        float imgScaleHeight = (float) options.outHeight / w;
        // 縮小可能の場合，縮小する
        if (imgScaleWidth > 2 && imgScaleHeight > 2) {
            // 縦横，小さいにスケール合わせ
            int imgScale = (int) Math.floor(imgScaleWidth > imgScaleHeight ? imgScaleHeight : imgScaleWidth);
            for (int i = 2; i <= imgScale; i++) {inSampleSize = i;}
        }
        return inSampleSize;
    }
}

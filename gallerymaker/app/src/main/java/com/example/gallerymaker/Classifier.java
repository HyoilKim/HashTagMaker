package com.example.gallerymaker;


import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class Classifier {
    private static final String LOG_TAG = Classifier.class.getSimpleName();

    private static final String MODEL_NAME = "network_model_lite.tflite";

    private static final int BATCH_SIZE = 1;
    public static final int IMG_HEIGHT = 400;
    public static final int IMG_WIDTH = 400;
    private static final int NUM_CHANNEL = 1;
    private static final int NUM_CLASSES = 17;

    private final Interpreter.Options options = new Interpreter.Options();
    private final Interpreter mInterpreter;
    private final ByteBuffer mImageData;
    private final int[] mImagePixels = new int[IMG_HEIGHT * IMG_WIDTH];
    private final float[][] mResult = new float[1][NUM_CLASSES];

    public Classifier(Activity activity) throws IOException {
        mInterpreter = new Interpreter(loadModelFile(activity), options);
        mImageData = ByteBuffer.allocateDirect(
                4 * BATCH_SIZE * IMG_HEIGHT * IMG_WIDTH * NUM_CHANNEL);
        mImageData.order(ByteOrder.nativeOrder());
    }

    public Result classify(Bitmap bitmap) {
        long startTime = SystemClock.uptimeMillis();
        mInterpreter.run(convertBitmapToArray(bitmap), mResult);
        long endTime = SystemClock.uptimeMillis();
        long timeCost = endTime - startTime;
        Log.v(LOG_TAG, "classify(): result = " + Arrays.toString(mResult[0])
                + ", timeCost = " + timeCost);
        Result result = new Result(mResult[0], timeCost);
        return result;
    }

    private float[][][][] convertBitmapToArray(Bitmap bitmap) {
        int pixel = 0;
        bitmap.getPixels(mImagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
        float[][][][] picarray = new float[1][400][400][3];
        for(int i= 0; i < IMG_WIDTH; ++i){
            for(int j=0; j< IMG_WIDTH; ++j) {
                int value = mImagePixels[pixel++];
                picarray[0][i][j][0] = ((value >> 16) & 0xFF)/255;
                picarray[0][i][j][1] = ((value >> 8) & 0xFF)/255;
                picarray[0][i][j][2] = (value & 0xFF)/255;
            }
        }
        return picarray;
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {

        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_NAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (mImageData == null) {
            return;
        }
        mImageData.rewind();

        bitmap.getPixels(mImagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < IMG_WIDTH; ++i) {
            for (int j = 0; j < IMG_HEIGHT; ++j) {
                int value = mImagePixels[pixel++];
                mImageData.putFloat(convertPixel(value));
            }
        }
    }

    private static float convertPixel(int color) {
//        return (255-(((color >> 16) & 0xFF) * 0.299f
//                + ((color >> 8) & 0xFF) * 0.587f
//                + (color & 0xFF) * 0.114f)) / 255.0f;
        return ((color & 0xFF)) / 255.0f;
    }
}
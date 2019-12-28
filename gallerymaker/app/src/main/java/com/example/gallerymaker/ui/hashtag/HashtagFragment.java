package com.example.gallerymaker.ui.hashtag;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ExifInterface;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.gallerymaker.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Result;


public class HashtagFragment extends Fragment {
    private static final String TAG = "blackjin";
    private Boolean isPermission = true;
    public View view;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;


    private SurfaceView mSurfaceView, mSurfaceView_transparent;
    private SurfaceHolder mSurfaceViewHolder, mSurfaceViewHolder_transparent;
    private Handler mHandler;
    private ImageReader mImageReader;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mSession;
    private int mDeviceRotation;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private SensorManager mSensorManager;
    //private DeviceOrientation deviceOrientation;
    int mDSI_height, mDSI_width;
    int  deviceHeight,deviceWidth;
    int previewHeight, previewWidth;
    private int RectLeft, RectTop,RectRight,RectBottom ;
    int imageWidth, imageHeight;



    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        tedPermission();
        view = inflater.inflate(R.layout.activity_get_image, container, false);
        view.findViewById(R.id.btnGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission) goToAlbum();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 권한 허용에 동의하지 않았을 경우 토스트를 띄웁니다.
                if(isPermission)  takePhoto();
                else Toast.makeText(view.getContext(), getResources().getString(R.string.permission_2), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getView().getContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if(tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e(TAG, tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : " + photoUri);

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile Uri : " + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == PICK_FROM_CAMERA) {

            setImage();

        }
    }

    /**
     *  앨범에서 이미지 가져오기
     */
    public void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    /**
     *  카메라에서 이미지 가져오기
     */
    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(getActivity(), "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            e.printStackTrace();
        }
        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                Uri photoUri = FileProvider.getUriForFile(getActivity(),
                        "gallerymaker.provider", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            } else {

                Uri photoUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);

            }
        }
    }

    /**
     *  폴더 및 파일 만들기
     */
    public File createImageFile() throws IOException {

        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "몰입캠프" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/몰입캠프/");
        if (!storageDir.exists()) storageDir.mkdirs();

        // 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        return image;
    }

    /**
     *  tempFile 을 bitmap 으로 변환 후 ImageView 에 설정한다.
     */
    public void setImage() {
        ImageView imageView = view.findViewById(R.id.imageView123);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(tempFile.getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap bmRotated = rotateBitmap(originalBm, orientation);
        Log.d(TAG, "setImage : " + tempFile.getAbsolutePath());
        imageView.setImageBitmap(bmRotated);


        try {
            mClassifier = new Classifier(this);
        } catch (IOException e) {
            Toast.makeText(getActivity(),"Failed to create Classifier", Toast.LENGTH_LONG).show();
            Log.e("@@@", "Failed to create Classifier", e);
        }

        new SaveImageTask().execute(bmRotated);



        /**
         *  tempFile 사용 후 null 처리를 해줘야 합니다.
         *  (resultCode != RESULT_OK) 일 때 tempFile 을 삭제하기 때문에
         *  기존에 데이터가 남아 있게 되면 원치 않은 삭제가 이뤄집니다.
         */
        if (tempFile == null) {
            Toast.makeText(getActivity(), "실패", Toast.LENGTH_SHORT).show();
        }
        tempFile = null;

    }

    //회전
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1,1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try{
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     *  권한 설정
     */
    public void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }


    Mat image_resize(Mat image, int height)
    {

        int h = image.rows();
        int w = image.cols();

        float r = height / (float)h;
        org.opencv.core.Size dim = new org.opencv.core.Size((int)(w * r), height);

        Mat resized= new Mat();
        Imgproc.resize(image, resized, dim, 0,0, Imgproc.INTER_AREA);

        return resized;
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }


    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }
    private class SaveImageTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);

            Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
            //Toast.makeText(MainActivity.this, "사진을 저장하였습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Bitmap... data) {

            Bitmap bitmap = null;

            // Bitmap to Mat
            Mat image = new Mat();
            Utils.bitmapToMat(bitmap, image);

            // 프리뷰 크기로 저장할 이미지 크기 조정
            Imgproc.resize(image, image, new org.opencv.core.Size(imageWidth, imageHeight), 0, 0, Imgproc.INTER_AREA );

            // Color to Gray
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGRA2GRAY);

            //ROI
            Mat roi = image.submat(new org.opencv.core.Rect(RectLeft, RectTop, RectRight-RectLeft, RectBottom-RectTop));

            org.opencv.core.Size size_28x28 = new org.opencv.core.Size(Classifier.IMG_WIDTH, Classifier.IMG_HEIGHT);
            Imgproc.resize( roi, roi, size_28x28 , 0, 0, Imgproc.INTER_AREA);

            // 이진화
            Imgproc.adaptiveThreshold(roi, roi,
                    255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 201, 20
            );
//            Imgproc.threshold(roi, roi, 0, 255, Imgproc.THRESH_OTSU|Imgproc.THRESH_BINARY_INV);


//            Mat kernel = new Mat(new org.opencv.core.Size(3, 3), CvType.CV_8UC1, new Scalar(255));
//            Imgproc.morphologyEx(roi, roi, Imgproc.MORPH_OPEN, kernel);
//            Imgproc.morphologyEx(roi, roi, Imgproc.MORPH_CLOSE, kernel);


            // 컨투어의 중심을 이미지 중앙으로 이동
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(roi, contours, new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

            int maxAreaIndex = -1;
            double maxArea = -1;
            for (int i = 0; i < contours.size(); i++) {
                double area = Imgproc.contourArea(contours.get(i));
                if (area > maxArea) {
                    maxArea = area;
                    maxAreaIndex = i;
                }
            }


            // 무게 중심 대신 경계 사각형 중심 사용
            org.opencv.core.Rect r = Imgproc.boundingRect(contours.get(maxAreaIndex));

            int center_x = r.x + (r.width / 2);
            int center_y = r.y + (r.height / 2);

            int shiftx = (int) (roi.cols()/2.0-center_x);
            int shifty = (int) (roi.rows()/2.0-center_y);

            Mat translationMatrix = new Mat(2, 3, CvType.CV_32FC1);
            float[] array = {1, 0, shiftx, 0, 1, shifty};
            translationMatrix.put(0, 0, array);
            Imgproc.warpAffine(roi, roi, translationMatrix, roi.size());


            Imgproc.cvtColor(roi, roi, Imgproc.COLOR_GRAY2BGRA);
            Bitmap resultBitmap = Bitmap.createBitmap(roi.cols(), roi.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(roi, resultBitmap);

            Bitmap bmp = toGrayscale(resultBitmap);

            Result result = mClassifier.classify(bmp);

            insertImage(getActivity().getContentResolver(), bmp, "" + System.currentTimeMillis(), "");

            return "" + result.getNumber();

        }

    }

















}
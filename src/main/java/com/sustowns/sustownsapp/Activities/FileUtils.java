package com.sustowns.sustownsapp.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    protected  static final int GALLERY_IMAGE = 1;
    protected  static final int CAMERA_CAPTURE = 2;
    protected static final int PICK_IMAGE = 1;
    protected  static final int CAMERA_CAPTURE_PERMIT= 3;

    public static String launchPermitCamera(Activity activity, Integer count, boolean isCount) {
        File fileImage;
        try {
            fileImage = new File(getFolder() + "/img" + System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            activity.startActivityForResult(intent, CAMERA_CAPTURE);
            return fileImage.getPath();
        } catch (ActivityNotFoundException e) {
            return "";
        }
    }

    public static String launchServiceCamera(Activity activity, Integer count, boolean isCount) {
        File fileImage;
        try {
            fileImage = new File(getFolder() + "/img" + System.currentTimeMillis() + ".jpg");
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            activity.startActivityForResult(intent, CAMERA_CAPTURE);
            return fileImage.getPath();
        } catch (ActivityNotFoundException e) {
            return "";
        }
    }
    public static String launchLicenceGallery(Activity activity, Integer count, boolean isCount) {
        File fileImage;
        try {
            fileImage = new File(getFolder() + "/img" + System.currentTimeMillis() + ".jpg");
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            activity.startActivityForResult(galleryIntent, PICK_IMAGE);
            return fileImage.getPath();
        } catch (ActivityNotFoundException e) {
            return "";
        }
    }
    public static String launchPermitGallery(Activity activity, Integer count, boolean isCount) {
        File fileImage;
        try {
            fileImage = new File(getFolder() + "/img" + System.currentTimeMillis() + ".jpg");
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            activity.startActivityForResult(galleryIntent, PICK_IMAGE);
            return fileImage.getPath();
          /*  fileImage = new File(getFolder() + "/img" + System.currentTimeMillis() + ".jpg");
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImage));
            activity.startActivityForResult(galleryIntent, PICK_IMAGE);
            return fileImage.getPath();*/
        } catch (ActivityNotFoundException e) {
            return "";
        }
    }

    public static String getFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/BestCare/Media");

        if (!folder.exists())
            folder.mkdirs();
        return folder.getAbsolutePath();
    }

    public static String getFolderDocs() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Mashhor/Documents");

        if (!folder.exists())
            folder.mkdirs();
        return folder.getAbsolutePath();
    }

    public static String processImage(String imagePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; //Downsample 10x
        if (bitmap != null)
            bitmap.recycle();
        bitmap = BitmapFactory.decodeFile(imagePath, options);

        int imageOrientation = getImageOrientation(imagePath);
//        if(imageOrientation == 0 || imageOrientation == 6)
        if (imageOrientation == 6)
            bitmap = getRotatedBitmap(bitmap, imageOrientation);

//        bitmap = changeBitmapContrastBrightness(bitmap, 1, 10);
        imagePath = saveBitmap(bitmap, imagePath);
        return imagePath;
    }

    public static String saveBitmap(Bitmap rotatedBitmap, String existingFilePath) {
        File file = new File(existingFilePath);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, bos);
            bos.flush();
            bos.close();
            return file.getPath();
        } catch (Exception e) {
            return "";
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static int getImageOrientation(String imagePath) {
        int imageOrientation = 0;

        try {
            ExifInterface exif;
            exif = new ExifInterface(imagePath);
            imageOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return imageOrientation;
        }
    }
    public static Bitmap getRotatedBitmap(Bitmap bitmap, int imageOrientation) {
        Matrix matrix = new Matrix();
        if (imageOrientation == 0 || imageOrientation == 6 || imageOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
        } else if (imageOrientation == 3 || imageOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            matrix.postRotate(180);
        } else if (imageOrientation == 8 || imageOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
        } else if (imageOrientation == ExifInterface.ORIENTATION_NORMAL) {

        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }
    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(ret);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);
        return ret;
    }

    @SuppressLint("NewApi")
    public static String getFilePath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove) // && DocumentsContract.isDocumentUri(context, uri))
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn_FilePath(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn_FilePath(context, contentUri, selection, selectionArgs);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn_FilePath(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn_FilePath(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getPath(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    public static String getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(getFolder() + "/imgChat" + System.currentTimeMillis() + ".jpg");
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
//            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri.getPath();
    }

    public static String downloadFileUsingUri(Uri fileUri, String destFile, Context context) {
        FileOutputStream fos = null;
        BufferedOutputStream out = null;
        InputStream in = null;

        try {
            fos = new FileOutputStream(destFile);
            out = new BufferedOutputStream(fos);
            in = context.getContentResolver().openInputStream(fileUri);

            byte[] buffer = new byte[8192];
            int len = 0;

            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.getFD().sync();
                out.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return destFile;
    }

}

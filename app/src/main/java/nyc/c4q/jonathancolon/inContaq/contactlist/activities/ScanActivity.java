package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.FaceClient;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.GalleryImagesHelper;

//public class ScanActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan);
//
//        final List<String> scannedList = new ArrayList<>();
//        String imageToScan = AddFaceToPersonActivity.mImageUriStr;
//        String personGroupId = AddFaceToPersonActivity.
//        final FaceServiceClient faceServiceClient = FaceClient.getFaceServiceClient();
//
//
//        final String imagescanned = AddFaceToPersonActivity.mImageUriStr;
//        try {
//            new AsyncTask<Void, Void, FaceDetector.Face[]>() {
//                String imageToScan = AddFaceToPersonActivity.mImageUriStr;
//                InputStream iStream = getContentResolver().openInputStream(Uri.parse(imageToScan));
//                byte[] inputData = getBytes(iStream);
//
//                @Override
//                protected FaceDetector.Face[] doInBackground(Void... params) {
//                    Log.d("FACE TASK", "doinback: ");
//
//                    try {
//                        faceServiceClient.detect(String.valueOf(inputData[0]), true, true, new FaceServiceClient.FaceAttributeType[]{
//                                FaceServiceClient.FaceAttributeType.Age,
//                                FaceServiceClient.FaceAttributeType.Gender,
//                                FaceServiceClient.FaceAttributeType.Glasses,
//                                FaceServiceClient.FaceAttributeType.Smile,
//                                FaceServiceClient.FaceAttributeType.HeadPose});
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return new FaceDetector.Face[0];
//                }
//
//                @Override
//                protected void onPostExecute(FaceDetector.Face[] faces) {
//                    Log.d("FACE TASK", "onPostExecute: " + faces[0].eyesDistance());
//                    //                scannedList.add();
//                    super.onPostExecute(faces);
//                }
//            };
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<String> imagesPath = GalleryImagesHelper.getAllImagesPath(getApplicationContext());
//
//        for (int i = 0; i < imagesPath.size(); i++) {
//
//            //imageToScan is equal to a face in the gallery
//
//
//        }
//    }
//
//    // Called when the "Detect" button is clicked.
//    public void identify(View view) {
//        // Start a background task to identify faces in the image.
//        List<UUID> faceIds = new ArrayList<>();
//        for (Face face : mFaceListAdapter.faces) {
//            faceIds.add(face.faceId);
//        }
//        new IdentificationActivity.IdentificationTask().execute(
//                faceIds.toArray(new UUID[faceIds.size()]));
//    }
//
//    public byte[] getBytes(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//
//        int len = 0;
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//        return byteBuffer.toByteArray();
//    }
//
//}
//

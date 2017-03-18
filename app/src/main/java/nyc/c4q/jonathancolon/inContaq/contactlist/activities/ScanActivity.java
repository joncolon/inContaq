package nyc.c4q.jonathancolon.inContaq.contactlist.activities;

import android.media.FaceDetector;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nyc.c4q.jonathancolon.inContaq.R;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.FaceClient;
import nyc.c4q.jonathancolon.inContaq.contactlist.faceapi.GalleryImagesHelper;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        final List<String> scannedList = new ArrayList<>();
        String imageToScan = AddFaceToPersonActivity.mImageUriStr;
        final FaceServiceClient faceServiceClient = FaceClient.getFaceServiceClient();

        final String imagescanned = AddFaceToPersonActivity.mImageUriStr;
        new AsyncTask<Void, Void, FaceDetector.Face[]>(){
            @Override
            protected FaceDetector.Face[] doInBackground(Void... params) {
                Log.d("FACE TASK", "doinback: ");

                try {
                    faceServiceClient.detect(imagescanned, true, true, new FaceServiceClient.FaceAttributeType[] {
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Glasses,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.HeadPose});
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new FaceDetector.Face[0];
            }

            @Override
            protected void onPostExecute(FaceDetector.Face[] faces) {
                Log.d("FACE TASK", "onPostExecute: "+ faces[0].eyesDistance());
//                scannedList.add();
                super.onPostExecute(faces);
            }
        };

        ArrayList<String> imagesPath = GalleryImagesHelper.getAllImagesPath(getApplicationContext());

        for (int i = 0; i < imagesPath.size(); i++) {

              //imageToScan is equal to a face in the gallery


        }
    }
}

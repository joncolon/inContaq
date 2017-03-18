package nyc.c4q.jonathancolon.inContaq.contactlist.faceapi;

import android.os.AsyncTask;
import android.util.Log;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.InputStream;

/**
 * Created by wesniemarcelin on 3/18/17.
 */

public class FaceDetectionCall extends AsyncTask<InputStream, String, Face[]> {
    // Background task of face detection.
    private boolean mSucceed = true;

    @Override
    protected Face[] doInBackground(InputStream... params) {
        // Get an instance of face service client to detect faces in image.
        FaceServiceClient faceServiceClient = FaceClient.getFaceServiceClient();
        Log.d("Face Service Client", " fc is " + faceServiceClient);

        try {
            publishProgress("Detecting...");
            Log.d("Publish progress", "in the try catch");

            // Start detection.
            return faceServiceClient.detect(

                    params[0],  /* Input stream of image to detect */
                    true,       /* Whether to return face ID */
                    true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                    new FaceServiceClient.FaceAttributeType[]{
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Glasses,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.HeadPose
                    });
        } catch (Exception e) {
            mSucceed = false;
            publishProgress(e.getMessage());
            addLog(e.getMessage());
            Log.d("Detecting...", "null in detecting.");
            return null;
        }
    }
    // Add a log item.
    private void addLog(String log) {
        LogHelper.addDetectionLog(log);
    }
}


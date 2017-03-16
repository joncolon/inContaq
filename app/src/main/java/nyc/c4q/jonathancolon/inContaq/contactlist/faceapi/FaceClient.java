package nyc.c4q.jonathancolon.inContaq.contactlist.faceapi;

import android.app.Application;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

import nyc.c4q.jonathancolon.inContaq.R;

public class FaceClient extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        sFaceServiceClient = new FaceServiceRestClient("1169d07917894faea0e3dc56de461b9d");
    }

    public static FaceServiceClient getFaceServiceClient() {
        return sFaceServiceClient;
    }

    private static FaceServiceClient sFaceServiceClient;
}

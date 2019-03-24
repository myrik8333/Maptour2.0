package com.example.alexandr.maptour;

//import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.sceneform.rendering.ExternalTexture;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.ActivityManager;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;




import android.support.annotation.Nullable;

public class arcore_activity extends AppCompatActivity {
    private static final String TAG = arcore_activity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;



    @Nullable private ModelRenderable vRend;
    private MediaPlayer mPlayer;

    // устанавливаем цветдля  фильтрации из видео
    private static final Color color = new Color(0.1843f, 1.0f, 0.098f);

    // уствановим  верхнюю границу  высоты  видео
    private static final float height = 0.85f;


    private ArFragment arFrag;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        */
        setContentView(R.layout.activity_arcore_activity);
        arFrag = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.frag);

        // Create an ExternalTexture for displaying the contents of the video.
        ExternalTexture texture = new ExternalTexture();

        // Create an Android MediaPlayer to capture the video on the external texture's surface.

        mPlayer = MediaPlayer.create(this, R.raw.lion_chroma);

        mPlayer.setSurface(texture.getSurface());
        mPlayer.setLooping(true);

        // Create a renderable with a material that has a parameter of type 'samplerExternal' so that
        // it can display an ExternalTexture. The material also has an implementation of a chroma key
        // filter.
        ModelRenderable.builder().setSource(this, R.raw.video).build().thenAccept(renderable -> {
            vRend = renderable;
            renderable.getMaterial().setExternalTexture("videoTexture", texture);
            renderable.getMaterial().setFloat4("keyColor", color);
        })
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load video renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        arFrag.setOnTapArPlaneListener(
                (HitResult hRes, Plane pl, MotionEvent mEvent) -> {
                    if (vRend == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anch = hRes.createAnchor();

                    AnchorNode nAnch = new AnchorNode(anch);
                    nAnch.setParent(arFrag.getArSceneView().getScene());

                    // Create a node to render the video and add it to the anchor.
                    Node nVideo = new Node();

                    nVideo.setParent(nAnch);

                    // Set the scale of the node so that the aspect ratio of the video is correct.
                    float videoWidth = mPlayer.getVideoWidth();
                    float videoHeight = mPlayer.getVideoHeight();

                    nVideo.setLocalScale(
                            new Vector3(height * (videoWidth / videoHeight), height, 1.0f));

                    // Start playing the video when the first node is placed.
                    if (!mPlayer.isPlaying()) {
                        mPlayer.start();

                        // Wait to set the renderable until the first frame of the  video becomes available.
                        // This prevents the renderable from briefly appearing as a black quad before the video
                        // plays.
                        texture.getSurfaceTexture().setOnFrameAvailableListener((SurfaceTexture surfaceTexture) -> {
                            nVideo.setRenderable(vRend);
                            texture.getSurfaceTexture().setOnFrameAvailableListener(null);
                        });
                    } else {
                        nVideo.setRenderable(vRend);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    //проверка на поддердку  устройства
    //в  случае неподходящего устройства  сообщение  об ошибке


    //проверка версии openGL
    /*public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "your device  is  not  supported, u  might use  android  7 or  later");
            Toast.makeText(activity, "your device  is  not  supported, u  might use  android  7 or  later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "U might  use  opengl ES 3.o minimum");
            Toast.makeText(activity, "U might  use  opengl ES 3.o minimum", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
    */
}

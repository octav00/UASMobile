package id.ac.umn.uasmobile;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.github.florent37.camerafragment.listeners.CameraFragmentStateAdapter;
import com.github.florent37.camerafragment.listeners.CameraFragmentVideoRecordTextListener;
import com.github.florent37.camerafragment.listeners.CameraFragmentVideoRecordedListener;

import java.io.File;

public class CameraFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            startCamera();
        }
    }

    private void startCamera() {
        final CameraFragment cameraFragment = CameraFragment.newInstance(new Configuration.Builder()
                .setCamera(Configuration.CAMERA_FACE_REAR)
                .setFlashMode(Configuration.FLASH_MODE_AUTO)
                .build());

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.container, cameraFragment, "FRAGMENT_TAG")
                .commit();

        cameraFragment.setStateListener(new CameraFragmentStateAdapter() {
            @Override
            public void onCurrentCameraBack() {
                Toast.makeText(getActivity(), "onCurrentCameraBack", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCurrentCameraFront() {
                Toast.makeText(getActivity(), "onCurrentCameraFront", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFlashAuto() {
                Toast.makeText(getActivity(), "onFlashAuto", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFlashOn() {
                Toast.makeText(getActivity(), "onFlashOn", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFlashOff() {
                Toast.makeText(getActivity(), "onFlashOff", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCameraSetupForPhoto() {
                Toast.makeText(getActivity(), "onCameraSetupForPhoto", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCameraSetupForVideo() {
                Toast.makeText(getActivity(), "onCameraSetupForVideo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void shouldRotateControls(int degrees) {
                Toast.makeText(getActivity(), "shouldRotateControls " + degrees, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRecordStateVideoReadyForRecord() {
                Toast.makeText(getActivity(), "onRecordStateVideoReadyForRecord", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRecordStateVideoInProgress() {
                Toast.makeText(getActivity(), "onRecordStateVideoInProgress", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRecordStatePhoto() {
                Toast.makeText(getActivity(), "onRecordStatePhoto", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopVideoRecord() {
                Toast.makeText(getActivity(), "onStopVideoRecord", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartVideoRecord(File outputFile) {
                Toast.makeText(getActivity(), "onStartVideoRecord " + outputFile, Toast.LENGTH_SHORT).show();
            }
        });

        cameraFragment.setResultListener(new CameraFragmentResultListener() {
            @Override
            public void onVideoRecorded(String filePath) {
                Toast.makeText(getActivity(), "onVideoRecorded " + filePath, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPhotoTaken(byte[] bytes, String filePath) {
                Toast.makeText(getActivity(), "onPhotoTaken " + filePath, Toast.LENGTH_SHORT).show();
            }
        });

        cameraFragment.setVideoRecordedListener(new CameraFragmentVideoRecordedListener() {
            @Override
            public void onVideoRecorded(String filePath) {
                Toast.makeText(getActivity(), "onVideoRecorded " + filePath, Toast.LENGTH_SHORT).show();
            }
        });

        cameraFragment.setVideoRecordTextListener(new CameraFragmentVideoRecordTextListener() {
            @Override
            public String getTextRecord(int currentTime) {
                return "REC " + currentTime;
            }

            @Override
            public String getTextHint() {
                return "HINT";
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(getActivity(), "Please grant camera permission to use the app", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
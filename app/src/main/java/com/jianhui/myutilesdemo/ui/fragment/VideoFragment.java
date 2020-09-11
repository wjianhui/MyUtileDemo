package com.jianhui.myutilesdemo.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jianhui.myutilesdemo.Permission;
import com.jianhui.myutilesdemo.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Administrator
 * 2020/09/03 0003 9:56
 * 备份一份自己写的视频录制功能
 */
public class VideoFragment extends BaseListFragment implements View.OnClickListener {

    private static final String TAG_LOG = VideoFragment.class.getSimpleName();
    private SurfaceView surfaceView;
    private Button startVideo;
    private Button stopVideo;

    private SurfaceHolder mSurfaceHolder;

    // 存储文件
    private File mVecordFile;
    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private String currentVideoFilePath;
    private String saveVideoPath = "";
    private Camera.Parameters mParameters;
    private Camera.Size mOptimalSize;
    private List<int[]> mFpsRange;
    private int displayOrientation;

    public static VideoFragment newInstance(int index) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected int findView() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View view) {
        surfaceView = view.findViewById(R.id.surface_view_video);
        startVideo = view.findViewById(R.id.bu_start_video);
        stopVideo = view.findViewById(R.id.bu_stop_video);
        startVideo.setOnClickListener(this);
        stopVideo.setOnClickListener(this);

    }

    @Override
    protected void initData() throws NullPointerException {

        Permission.getInstance().permissionInit(requireContext());
        //配置SurfaceHolder
        mSurfaceHolder = surfaceView.getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        //回调接口
        mSurfaceHolder.addCallback(mSurfaceCallBack);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }


    private int mScreenWidth;
    private int mScreenHeight;
    private SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@androidx.annotation.NonNull SurfaceHolder surfaceHolder) {
            initCamera();
        }

        @Override
        public void surfaceChanged(@androidx.annotation.NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            if (mSurfaceHolder.getSurface() == null) {
                return;
            }
            mScreenWidth = i1;
            mScreenHeight = i2;

        }

        @Override
        public void surfaceDestroyed(@androidx.annotation.NonNull SurfaceHolder surfaceHolder) {
            releaseCamera();
        }
    };

    //设置相机参数
    private void setCameraParameters() {
        try {
            if (mCamera == null)
                return;

            //可以通过获取相机的参数实例，设置里面各种效果，包括刚刚的预览图，前置摄像头，闪光灯等
            mParameters = mCamera.getParameters();// 获得相机参数

            //该方法返回了SurfaceView的宽与高，根据给出的尺寸与宽高比例，获取一个最适配的预览尺寸
            List<Camera.Size> mSupportedPreviewSizes = mParameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = mParameters.getSupportedVideoSizes();

            mOptimalSize = getOptimalPreviewSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, mScreenWidth, mScreenHeight);
            //该方法是获取最佳的预览与摄像尺寸。然后设置预览图像大小
//            mParameters.setPreviewSize(mOptimalSize.width, mOptimalSize.height); // 设置预览图像大小

            mParameters.setPreviewSize(mSupportedPreviewSizes.get(0).width, mSupportedPreviewSizes.get(0).height);


            displayOrientation = getCameraDisplayOrientation(requireActivity(), Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCamera.setDisplayOrientation(displayOrientation);
            mFpsRange = mParameters.getSupportedPreviewFpsRange();
            mParameters.setFocusMode(getAutoFocus());
            //缩短Recording启动时间
            mParameters.setRecordingHint(true);
            //影像稳定能力
            if (mParameters.isVideoStabilizationSupported())
                mParameters.setVideoStabilization(true);

            mCamera.setParameters(mParameters);// 设置相机参数
            mCamera.startPreview();// 开始预览

        } catch (Exception io) {
            io.printStackTrace();
        }
    }

    //自动对焦
    private String getAutoFocus() {

        Camera.Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (((Build.MODEL.startsWith("GT-I950")) || (Build.MODEL.endsWith("SCH-I959")) || (Build.MODEL.endsWith("MEIZU MX3")))
                && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            return Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            return Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;
        } else {
            return Camera.Parameters.FOCUS_MODE_FIXED;
        }
    }

    //得到摄像旋转角度
    private int getCameraDisplayOrientation(Activity activity, int cameraId) {

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }


    /**
     * 初始化摄像头
     *
     * @author liuzhongjun
     */
    private void initCamera() {

        if (mCamera != null) {
            releaseCamera();
        }

        if (checkCameraFacing(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            Toast.makeText(requireContext(), "未发现有可用摄像头", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCamera == null) {
            Toast.makeText(requireContext(), "未能获取到相机！", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            //将相机与SurfaceHolder绑定
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            setCameraParameters();

        } catch (IOException e) {
            //有的手机会因为兼容问题报错，这就需要开发者针对特定机型去做适配了
            Log.d(TAG_LOG, "Error starting camera preview: " + e.getMessage());
        }
    }


    /**
     * 检查是否有摄像头
     *
     * @param facing 前置还是后置
     * @return
     */
    private boolean checkCameraFacing(int facing) {
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    /**
     * 释放摄像头资源
     *
     * @date 2016-2-5
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始录制视频
     */
    public boolean startRecord() {

        initCamera();
        //录制视频前必须先解锁Camera
        mCamera.unlock();
        configMediaRecorder();
        try {
            //开始录制
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 停止录制视频
     */
    public void stopRecord() {

        if (mediaRecorder != null) {

            // 设置后不会崩
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.setOnInfoListener(null);
            mediaRecorder.setPreviewDisplay(null);

            //停止录制
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException iae) {
            }
            mediaRecorder.reset();

            //释放资源
            mediaRecorder.release();
            mediaRecorder = null;
        }

    }

    private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 配置MediaRecorder()
     */

    private void configMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setOnErrorListener(OnErrorListener);

        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
        mediaRecorder.setAudioChannels(1);
        //设置最大录像时间 单位：毫秒
        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
        mediaRecorder.setMaxFileSize(60 * 1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(22050);
        //设置码率
        if (mProfile.videoBitRate > 2 * 1024 * 1024)
            mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
        else
            mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
//        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置帧率
        int mFps = mProfile.videoFrameRate;
        for (int[] ints : mFpsRange) {
            if (ints[0] >= 20000) {
                mFps = ints[0] / 1000;
                break;
            }

        }
        mediaRecorder.setVideoFrameRate(mFps);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mediaRecorder.setOrientationHint(displayOrientation);
        //设置录像的分辨率
        mediaRecorder.setVideoSize(1280, 720);

        //设置录像视频输出地址
        mediaRecorder.setOutputFile(currentVideoFilePath);


        Log.e("CustomRecordActivity", "CustomRecordActivity:" + getSDPath(requireContext()) + "append.mp4");
    }

    /**
     * 创建视频文件保存路径
     */
    public static String getSDPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(context, "请查看您的SD卡是否存在！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File sdDir = Environment.getExternalStorageDirectory();
        File eis = new File(sdDir.toString() + "/cqytjr/");
        if (!eis.exists()) {
            eis.mkdir();
        }
        return sdDir.toString() + "/cqytjr/";
    }

    private String getVideoName() {
        return "video" + ".mp4";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bu_start_video:
                if (getSDPath(requireContext()) == null)
                    return;

                //视频文件保存路径，configMediaRecorder方法中会设置
                currentVideoFilePath = getSDPath(requireContext()) + getVideoName();
                //开始录制视频
                if (!startRecord())
                    return;

//                refreshControlUI();

//                mRecorderState = STATE_RECORDING;
                break;
            case R.id.bu_stop_video:
                //停止视频录制
                stopRecord();
                //先给Camera加锁后再释放相机
                mCamera.lock();
                releaseCamera();
//                refreshControlUI();

                //判断是否进行视频合并
                if ("".equals(saveVideoPath)) {
                    saveVideoPath = currentVideoFilePath;
                }

//                mRecorderState = STATE_INIT;

                //延迟一秒跳转到播放器，（确保视频合并完成后跳转） TODO 具体的逻辑可根据自己的使用场景跳转
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(requireContext(), "录制成功", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
                    }
                }, 1000);
                break;
        }

    }

    public Camera.Size getOptimalPreviewSize(List<Camera.Size> mSupportedVideoSizes, List<Camera.Size> previewSizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        List<Camera.Size> videoSizes;
        if (mSupportedVideoSizes != null) {
            videoSizes = mSupportedVideoSizes;
        } else {
            videoSizes = previewSizes;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : videoSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : videoSizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}

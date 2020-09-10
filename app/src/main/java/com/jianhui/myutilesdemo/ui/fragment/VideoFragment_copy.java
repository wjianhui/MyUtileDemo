package com.jianhui.myutilesdemo.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
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
public class VideoFragment_copy extends BaseListFragment implements View.OnClickListener {

    private static final String TAG_LOG = VideoFragment_copy.class.getSimpleName();
    private SurfaceView surfaceView;
    private Button startVideo;
    private Button stopVideo;

    private SurfaceHolder mSurfaceHolder;


    //DATA
    //录像机状态标识
    private int mRecorderState;

    public static final int STATE_INIT = 0; //开始录制
    public static final int STATE_RECORDING = 1;//结束录制


    // 存储文件
    private File mVecordFile;
    private Camera mCamera;
    private MediaRecorder mediaRecorder;
    private String currentVideoFilePath;
    private String saveVideoPath = "";
    private Camera.Parameters mParameters;
    private Camera.Size mOptimalSize;
    private List<int[]> mFpsRange;

    public static VideoFragment_copy newInstance(int index) {
        VideoFragment_copy videoFragment = new VideoFragment_copy();
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
            setCameraParameters();

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
            mParameters.setPreviewSize(mOptimalSize.width, mOptimalSize.height); // 设置预览图像大小


            mCamera.setDisplayOrientation(getDegree());
            List<String> focusModes = mParameters.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mFpsRange = mParameters.getSupportedPreviewFpsRange();

            List<String> modes = mParameters.getSupportedFocusModes();
            if (modes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                //支持自动聚焦模式
                mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            //缩短Recording启动时间
            mParameters.setRecordingHint(true);
            //影像稳定能力
            if (mParameters.isVideoStabilizationSupported())
                mParameters.setVideoStabilization(true);

            mCamera.setParameters(mParameters);// 设置相机参数
//            mCamera.startPreview();// 开始预览

        } catch (Exception io) {
            io.printStackTrace();
        }
    }


    private int getDegree() {
        //获取当前屏幕旋转的角度
        int rotating = requireActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;//度数
        //根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
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

        if (checkCameraFacing(1)) {
            mCamera = Camera.open(1);
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
//            configCameraParams();
//            setCameraParameters();
            //启动相机预览
            mCamera.startPreview();
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
     * 设置摄像头为竖屏
     */
    private void configCameraParams() {
        Camera.Parameters params = mCamera.getParameters();
        //设置相机的横竖屏(竖屏需要旋转90°)
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
        } else {
            params.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }
        //设置聚焦模式
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //缩短Recording启动时间
        params.setRecordingHint(true);
        //影像稳定能力
        if (params.isVideoStabilizationSupported())
            params.setVideoStabilization(true);
        mCamera.setParameters(params);
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
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
//        mediaRecorder.setAudioEncodingBitRate(44100);
//        if (mProfile.videoBitRate > 2 * 1024 * 1024)
//            mediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
//        else
//            mediaRecorder.setVideoEncodingBitRate(1024 * 1024);
//        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        mediaRecorder.setOrientationHint(90);
        //设置录像的分辨率
//        mediaRecorder.setVideoSize(352, 288);

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

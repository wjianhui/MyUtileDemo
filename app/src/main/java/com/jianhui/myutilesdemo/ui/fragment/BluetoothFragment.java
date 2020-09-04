package com.jianhui.myutilesdemo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.internal.ManufacturerUtils;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.beacon.BeaconItem;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.jianhui.myutilesdemo.R;

/**
 * @author Administrator
 * 2020/09/03 0003 9:47
 */
public class BluetoothFragment extends BaseListFragment implements View.OnClickListener {

    private static final String TAG_LOG = BluetoothFragment.class.getSimpleName();
    private Button blueButton;
    private Context mContext;
    private Button blueButtonEnd;
    private BluetoothClient mClient;
    private Button startBluetooth;
    private Button endBluetooth;


    public static BluetoothFragment newInstance(int index) {
        BluetoothFragment bluetoothFragment = new BluetoothFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        bluetoothFragment.setArguments(bundle);
        return bluetoothFragment;

    }


    @Override
    protected int findView() {
        return R.layout.fragment_bluetooth;
    }

    @Override
    protected void initView(View view) {
        mContext = requireContext();
        blueButton = view.findViewById(R.id.blue_button);
        blueButtonEnd = view.findViewById(R.id.blue_button_end);
        startBluetooth = view.findViewById(R.id.start_bluetooth);
        endBluetooth = view.findViewById(R.id.end_bluetooth);
        blueButton.setOnClickListener(this);
        blueButtonEnd.setOnClickListener(this);
        startBluetooth.setOnClickListener(this);
        endBluetooth.setOnClickListener(this);

    }

    @Override
    protected void initData() throws NullPointerException {
        mClient = new BluetoothClient(mContext);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blue_button:
                startBluetooth();
                break;
            case R.id.blue_button_end:
                if (mClient != null) {
                    mClient.stopSearch();
                }
                break;
            case R.id.start_bluetooth:
                if (mClient != null) {
                    mClient.openBluetooth();
                }
                break;
            case R.id.end_bluetooth:
                if (mClient != null) {
                    mClient.closeBluetooth();
                }
                break;
        }

    }


    /**
     * 开始扫描蓝牙设备
     */
    private void startBluetooth() {

        SearchRequest build = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)  // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000)  // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)   // 再扫BLE设备2s
                .build();

        mClient.search(build, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                Log.i(TAG_LOG, "  onSearchStarted  ");
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
//                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));


                Log.i(TAG_LOG, String.format("beacon for %s\n%s", device.getAddress(), device.getName()));
//                if (!device.getName().equals("NULL")) {
//                }else {
//                }


            }

            @Override
            public void onSearchStopped() {
                Log.i(TAG_LOG, "  onSearchStopped  ");

            }

            @Override
            public void onSearchCanceled() {
                Log.i(TAG_LOG, "  onSearchCanceled  ");

            }
        });
    }
}

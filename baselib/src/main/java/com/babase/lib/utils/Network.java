package com.babase.lib.utils;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

/**
 * 网络管理监听，
 * 需要在application中new出来，然后在application 的onterminate中取消注册，从而达到全局监听网络变化的需求
 *
 * @author bauer
 */
public class Network {
    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * 流量网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_MOBILE = 1;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 2;

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private NetBroadCastReceiver netBroadCastReciver;
    private Context mContext;
    private NetWorkChangeListener mNetWorkChangeListner;

    public Network(Context mContext) {
        this.mContext = mContext;
        registNetWorkConnect();
    }

    public void setNetWorkChangeListener(NetWorkChangeListener mNetWorkChangeListner) {
        this.mNetWorkChangeListner = mNetWorkChangeListner;
    }

    /**
     * 注册监听
     */
    private void registNetWorkConnect() {
        if (Build.VERSION.SDK_INT > 21) {
            registerNetWorkRequest();
        } else {
            registerConnectivityReceiver();
        }
    }

    /**
     * 取消注册
     */
    public void unregistNetWorkConnect() {
        if (Build.VERSION.SDK_INT > 21) {
            unRegisterNetWorkRequest();
        } else {
            unRegisterConnectivityReceiver();
        }
    }

    /**
     * 注册networkcallback
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void registerNetWorkRequest() {
        connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        NetworkRequest networkRequest = builder.build();

        networkCallback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(android.net.Network network) {
                super.onAvailable(network);
                Logger.d(" ＝＝＝＝＝ ", "有网络连接");
                if (mNetWorkChangeListner != null) {
                    mNetWorkChangeListner.netWorkStatus(true);
                }
            }

            @Override
            public void onLost(android.net.Network network) {
                super.onLost(network);
                Logger.d(" ＝＝＝＝＝ ", "无网络连接");
                if (mNetWorkChangeListner != null) {
                    mNetWorkChangeListner.netWorkStatus(false);
                }
            }
        };
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    /**
     * 取消注册networkcallback
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void unRegisterNetWorkRequest() {
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    /**
     * 注册广播
     */
    private void registerConnectivityReceiver() {
        netBroadCastReciver = new NetBroadCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(netBroadCastReciver, filter);
    }

    /**
     * 取消注册广播
     */
    private void unRegisterConnectivityReceiver() {
        if (netBroadCastReciver != null) {
            mContext.unregisterReceiver(netBroadCastReciver);
        }
    }

    /**
     * 网络监听广播
     */
    private class NetBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果是在开启wifi连接和有网络状态下
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null && NetworkInfo.State.CONNECTED == info.getState()) {
                    Logger.d(" ＝＝＝＝＝ ", "有网络连接");
                    if (mNetWorkChangeListner != null) {
                        mNetWorkChangeListner.netWorkStatus(true);
                    }
                } else {
                    Logger.d(" ＝＝＝＝＝ ", "无网络连接");
                    if (mNetWorkChangeListner != null) {
                        mNetWorkChangeListner.netWorkStatus(false);
                    }
                }
            }
        }
    }

    /**
     * 网络变化监听返回接口
     */
    public interface NetWorkChangeListener {
        void netWorkStatus(boolean connected);
    }

    /**
     * 获取网络连接的状态
     *
     * @param context
     * @return
     */
    public static int getNetWorkType(Context context) {
        int mNetWorkType = NETWORKTYPE_INVALID;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if ("WIFI".equalsIgnoreCase(type)) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if ("MOBILE".equalsIgnoreCase(type)) {
                mNetWorkType = NETWORKTYPE_MOBILE;
            }
        }
        return mNetWorkType;
    }

    /**
     * 判断网络是否连接
     *
     * @param act
     * @return
     */
    public static boolean isNetWorkConnect(Context act) {
        ConnectivityManager manager = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isAvailable();
    }
}

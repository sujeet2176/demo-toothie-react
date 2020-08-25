package com.demotoothie.comm;

import android.os.Handler;
import android.os.Looper;

import com.demotoothie.application.Config;
import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.impl.client.action.ActionDispatcher;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

//import cn.com.buildwin.gotrack.config.Config;

public class TCPClient {
    private static final String TAG = TCPClient.class.getSimpleName();

    private ConnectionInfo mInfo;
    private OkSocketOptions mOptions;
    private IConnectionManager mManager;

    private TCPClientAdapter mAdapter;

    public TCPClient() {
        init(Config.TCP_SERVER_HOST, Config.TCP_SERVER_PORT);
    }

    public TCPClient(String host, int port) {
        init(host, port);
    }

    public void init(String host, int port) {
        mInfo = new ConnectionInfo(host, port);

        final Handler handler = new Handler(Looper.getMainLooper());
        mOptions = new OkSocketOptions.Builder()
                .setPulseFrequency(Config.TCP_ALIVE_INTERVAL)
                .setConnectTimeoutSecond(Config.TCP_RECONNECTION_INTERVAL)
                .setReconnectionManager(new InfiniteReconnectionManager())
                .setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
                    @Override
                    public void handleCallbackEvent(ActionDispatcher.ActionRunnable actionRunnable) {
                        handler.post(actionRunnable);
                    }
                })
                .setReaderProtocol(new DefaultReaderProtocol())
                .build();

        mManager = OkSocket.open(mInfo).option(mOptions);
        mManager.registerReceiver(adapter);
    }

    public boolean isConnected() {
        return mManager.isConnect();
    }

    /**
     * 连接
     */
    public void connect() {
        mManager.connect();
    }

    /**
     * 断开
     */
    public void disconnect() {
        mManager.disconnect();
    }

    public void sendMessage(TCPMessage message) {
        mManager.send(message);
    }


    /* 网络回调 */

    private SocketActionAdapter adapter = new SocketActionAdapter() {
        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            if (mAdapter != null) {
                mAdapter.onConnectionSuccess();
            }

            // 开始发心跳包
            mManager.getPulseManager()
                    .setPulseSendable(new PulseData())
                    .pulse();
        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
//            // 非正常断开，调用回调通知断开
//            if (e != null) {
                if (mAdapter != null) {
                    mAdapter.onDisconnection();
                }
//            }
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            if (mAdapter != null) {
                mAdapter.onConnectionFailed();
            }
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            if (mAdapter != null) {
                mAdapter.onReceiveData(data.getBodyBytes());
            }
        }

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            if (mAdapter != null) {
                mAdapter.onSendData();
            }
        }

        @Override
        public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
            // feed dog
            mManager.getPulseManager().feed();
        }
    };

    /* 回调 */

    public TCPClientAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(TCPClientAdapter adapter) {
        mAdapter = adapter;
    }

    public interface TCPClientAdapter {
        void onConnectionSuccess();
        void onDisconnection();
        void onConnectionFailed();
        void onReceiveData(byte[] data);
        void onSendData();
    }
}

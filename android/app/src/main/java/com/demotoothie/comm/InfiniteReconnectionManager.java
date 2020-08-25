package com.demotoothie.comm;

import com.xuhao.didi.core.utils.SLog;
import com.xuhao.didi.socket.client.impl.exceptions.ManuallyDisconnectException;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.connection.AbsReconnectionManager;
import com.xuhao.didi.socket.common.interfaces.basic.AbsLoopThread;
import com.xuhao.didi.socket.common.interfaces.utils.ThreadUtils;

import java.util.Iterator;

public class InfiniteReconnectionManager extends AbsReconnectionManager {

    private volatile ReconnectThread mReconnectThread;

    public InfiniteReconnectionManager() {
        mReconnectThread = new ReconnectThread();
    }

    @Override
    public void onSocketDisconnection(ConnectionInfo connectionInfo, String s, Exception e) {
        if (isNeedReconnect(e)) {//break with exception
            reconnectDelay();
        } else {
            resetThread();
        }
    }

    @Override
    public void onSocketConnectionSuccess(ConnectionInfo connectionInfo, String s) {
        resetThread();
    }

    @Override
    public void onSocketConnectionFailed(ConnectionInfo connectionInfo, String s, Exception e) {
        if (e != null) {
            // 无限重连
            reconnectDelay();
        }
    }

    /**
     * 是否需要重连
     *
     * @param e
     * @return
     */
    private boolean isNeedReconnect(Exception e) {
        synchronized (mIgnoreDisconnectExceptionList) {
            if (e != null && !(e instanceof ManuallyDisconnectException)) {//break with exception
                Iterator<Class<? extends Exception>> it = mIgnoreDisconnectExceptionList.iterator();
                while (it.hasNext()) {
                    Class<? extends Exception> classException = it.next();
                    if (classException.isAssignableFrom(e.getClass())) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * 重置重连线程,关闭线程
     */
    private synchronized void resetThread() {
        if (mReconnectThread != null) {
            mReconnectThread.shutdown();
        }
    }

    /**
     * 开始延迟重连
     */
    private void reconnectDelay() {
        synchronized (mReconnectThread) {
            if (mReconnectThread.isShutdown()) {
                mReconnectThread.start();
            }
        }
    }

    @Override
    public void detach() {
        super.detach();
    }

    private class ReconnectThread extends AbsLoopThread {
        /**
         * 延时连接时间
         */
        private long mReconnectTimeDelay = 5 * 1000;

        @Override
        protected void beforeLoop() throws Exception {
            super.beforeLoop();
            if (mReconnectTimeDelay < mConnectionManager.getOption().getConnectTimeoutSecond() * 1000) {
                mReconnectTimeDelay = mConnectionManager.getOption().getConnectTimeoutSecond() * 1000;
            }
        }

        @Override
        protected void runInLoopThread() throws Exception {
            if (mDetach) {
                SLog.i("ReconnectionManager already detached by framework. We decide gave up this reconnection mission!");
                shutdown();
                return;
            }

            //延迟执行
            SLog.i("Reconnect after " + mReconnectTimeDelay + " mills ...");
            ThreadUtils.sleep(mReconnectTimeDelay);

            if (mDetach) {
                SLog.i("ReconnectionManager already detached by framework. We decide gave up this reconnection mission!");
                shutdown();
                return;
            }

            if (mConnectionManager.isConnect()) {
                shutdown();
                return;
            }
            boolean isHolden = mConnectionManager.getOption().isConnectionHolden();

            if (!isHolden) {
                detach();
                shutdown();
                return;
            }
            ConnectionInfo info = mConnectionManager.getConnectionInfo();
            SLog.i("Reconnect the server " + info.getIp() + ":" + info.getPort() + " ...");
            synchronized (mConnectionManager) {
                if (!mConnectionManager.isConnect()) {
                    mConnectionManager.connect();
                } else {
                    shutdown();
                }
            }
        }


        @Override
        protected void loopFinish(Exception e) {
        }
    }

}

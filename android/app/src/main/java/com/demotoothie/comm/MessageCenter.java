package com.demotoothie.comm;

import com.demotoothie.eventbus.BusProvider;
import com.example.sdkpoc.buildwin.common.eventbus.events.MessageCenterConnectionEvent;

import java.util.Arrays;
import java.util.HashMap;
public class MessageCenter {

    private static final String TAG = MessageCenter.class.getSimpleName();

    private TCPClient mClient;
    private HashMap<String, MessageDeliverListener> mPostmen;
    private byte lastSessionId;

    // 存储卡状态
    public static final int CardStatusNone          = 0;
    public static final int CardStatusOK            = 1;
    public static final int CardStatusUnformatted   = 2;
    private int mCardStatus;

    // 单例
    private static final MessageCenter ourInstance = new MessageCenter();

    public static MessageCenter getInstance() {
        return ourInstance;
    }


    public MessageCenter() {
        mPostmen = new HashMap<>();
        mClient = new TCPClient();
        mClient.setAdapter(mAdapter);
    }

    /**
     * 设备连接状态
     */
    public boolean isDeviceConnected() {
        return mClient.isConnected();
    }

    /**
     * 开始启动
     */
    public void start() {
        mClient.connect();
    }

    /**
     * 停止
     */
    public void stop() {
        mClient.disconnect();
    }

    /**
     * 获取最新SessionId，范围0-255
     * @return 最新SessionId
     */
    private byte getLatestSessionId() {
        return ++lastSessionId;
    }

    /**
     * 获取对应SessionId的键
     */
    private String getKeyOfSessionId(Byte sessionId) {
        return sessionId.toString();
    }

    /**
     * 发送消息
     * @param message 消息
     */
    public void sendMessage(TCPMessage message) {
        sendMessage(message, null);
    }

    /**
     * 发送消息，回传后调用回调
     * @param message   消息
     * @param listener  回调
     */
    public void sendMessage(TCPMessage message, MessageDeliverListener listener) {
        Byte sessionId = getLatestSessionId();
        String keySessionId = getKeyOfSessionId(sessionId);
        if (listener != null) {
            mPostmen.put(keySessionId, listener);
        } else {
            // 如果没有提供listener，移除相同SessionID的DeliverListener
            mPostmen.remove(keySessionId);
        }

        message.setSessionId(sessionId);
        mClient.sendMessage(message);
    }

    /* Message Shortcut */

    public void sendMessage(byte mid, byte[] data) {
        TCPMessage message = new TCPMessage(mid, data);
        sendMessage(message);
    }

    public void sendMessage(byte mid, byte value) {
        TCPMessage message = new TCPMessage(mid, value);
        sendMessage(message);
    }

    // Time Calibration

    public void sendMessageTimeCalibration(byte mid, byte[] data) {
        TCPMessage message = new TCPMessage(mid, data);
        sendMessage(message);
    }

    /* Preview */

    public void sendMessagePreviewResolution(byte value) {
        byte messageId = TCPMessage.MSG_ID_PREVIEW_RESOLUTION;
        sendMessage(messageId, value);
    }

    public void sendMessagePreviewQuality(byte value) {
        byte messageId = TCPMessage.MSG_ID_PREVIEW_QUALITY;
        sendMessage(messageId, value);
    }

    public void sendMessagePreviewSound(byte value) {
        byte messageId = TCPMessage.MSG_ID_PREVIEW_SOUND;
        sendMessage(messageId, value);
    }

    /* Video */

    public void sendMessageVideoResolution(byte value) {
        byte messageId = TCPMessage.MSG_ID_VIDEO_RESOLUTION;
        sendMessage(messageId, value);
    }

    public void sendMessageVideoQuality(byte value) {
        byte messageId = TCPMessage.MSG_ID_VIDEO_QUALITY;
        sendMessage(messageId, value);
    }

    public void sendMessageVideoSound(boolean on) {
        byte messageId = TCPMessage.MSG_ID_VIDEO_SOUND;
        byte value = on ? TCPMessage.VIDEO_SOUND_ON : TCPMessage.VIDEO_SOUND_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageVideoCyclicRecord(byte value) {
        byte messageId = TCPMessage.MSG_ID_VIDEO_CYCLIC_RECORD;
        sendMessage(messageId, value);
    }

    /* Photo */

    public void sendMessagePhotoResolution(byte value) {
        byte messageId = TCPMessage.MSG_ID_PHOTO_RESOLUTION;
        sendMessage(messageId, value);
    }

    public void sendMessagePhotoQuality(byte value) {
        byte messageId = TCPMessage.MSG_ID_PHOTO_QUALITY;
        sendMessage(messageId, value);
    }

    public void sendMessagePhotoBurst(byte value) {
        byte messageId = TCPMessage.MSG_ID_PHOTO_BURST;
        sendMessage(messageId, value);
    }

    public void sendMessagePhotoTimelapse(byte value) {
        byte messageId = TCPMessage.MSG_ID_PHOTO_TIMELAPSE;
        sendMessage(messageId, value);
    }

    /* Visual Effect */

    public void sendMessageWhiteBalance(byte value) {
        byte messageId = TCPMessage.MSG_ID_WHITE_BALANCE;
        sendMessage(messageId, value);
    }

    public void sendMessageExposureCompensation(byte value) {
        byte messageId = TCPMessage.MSG_ID_EXPOSURE_COMPENSATION;
        sendMessage(messageId, value);
    }

    public void sendMessageSharpness(byte value) {
        byte messageId = TCPMessage.MSG_ID_SHARPNESS;
        sendMessage(messageId, value);
    }

    public void sendMessageISO(byte value) {
        byte messageId = TCPMessage.MSG_ID_ISO;
        sendMessage(messageId, value);
    }

    public void sendMessageAntiBanding(byte value) {
        byte messageId = TCPMessage.MSG_ID_ANTI_BANDING;
        sendMessage(messageId, value);
    }

    public void sendMessageWDR(boolean on) {
        byte messageId = TCPMessage.MSG_ID_WDR;
        byte value = on ? TCPMessage.WDR_ON : TCPMessage.WDR_OFF;
        sendMessage(messageId, value);
    }

    /* Control */

    public void sendMessageRecordVideo(byte value) {
        byte messageId = TCPMessage.MSG_ID_RECORD_VIDEO;
        sendMessage(messageId, value);
    }

    public void sendMessageTakePhoto() {
        byte messageId = TCPMessage.MSG_ID_TAKE_PHOTO;
        sendMessage(messageId, null);
    }

    public void seneMessageGetSensorResolution() {
        byte messageId = TCPMessage.MSG_ID_GET_SENSOR_RESOLUTION;
        sendMessage(messageId, null);
    }

    public void seneMessageSetSensorResolution(byte value) {
        byte messageId = TCPMessage.MSG_ID_SET_SENSOR_RESOLUTION;
        sendMessage(messageId, value);
    }

    /* Common */

    public void sendMessageWiFiSettings(byte[] data) {
        byte messageId = TCPMessage.MSG_ID_WIFI_SETTINGS;
        sendMessage(messageId, data);
    }

    public void sendMessageLanguage(byte value) {
        byte messageId = TCPMessage.MSG_ID_LANGUAGE;
        sendMessage(messageId, value);
    }

    public void sendMessageMotionDetection(boolean on) {
        byte messageId = TCPMessage.MSG_ID_MOTION_DETECTION;
        byte value = on ? TCPMessage.MOTION_DETECTION_ON : TCPMessage.MOTION_DETECTION_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageAntiShake(boolean on) {
        byte messageId = TCPMessage.MSG_ID_ANTI_SHAKE;
        byte value = on ? TCPMessage.ANTI_SHAKE_ON : TCPMessage.ANTI_SHAKE_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageDateStamp(boolean on) {
        byte messageId = TCPMessage.MSG_ID_DATE_STAMP;
        byte value = on ? TCPMessage.DATE_STAMP_ON : TCPMessage.DATE_STAMP_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageScreenSaver(byte value) {
        byte messageId = TCPMessage.MSG_ID_SCREEN_SAVER;
        sendMessage(messageId, value);
    }

    public void sendMessageRotation(boolean on) {
        byte messageId = TCPMessage.MSG_ID_ROTATION;
        byte value = on ? TCPMessage.ROTATION_ON : TCPMessage.ROTATION_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageAutoShutdown(byte value) {
        byte messageId = TCPMessage.MSG_ID_AUTO_SHUTDOWN;
        sendMessage(messageId, value);
    }

    public void sendMessageButtonSound(boolean on) {
        byte messageId = TCPMessage.MSG_ID_BUTTON_SOUND;
        byte value = on ? TCPMessage.BUTTON_SOUND_ON : TCPMessage.BUTTON_SOUND_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageOSDMode(boolean on) {
        byte messageId = TCPMessage.MSG_ID_OSD_MODE;
        byte value = on ? TCPMessage.OSD_MODE_ON : TCPMessage.OSD_MODE_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageCarMode(boolean on) {
        byte messageId = TCPMessage.MSG_ID_CAR_MODE;
        byte value = on ? TCPMessage.CAR_MODE_ON : TCPMessage.CAR_MODE_OFF;
        sendMessage(messageId, value);
    }

    public void sendMessageFormatCard() {
        byte messageId = TCPMessage.MSG_ID_FORMAT_CARD;
        sendMessage(messageId, null);
    }

    public void sendMessageFactoryReset() {
        byte messageId = TCPMessage.MSG_ID_FACTORY_RESET;
        sendMessage(messageId, null);
    }

    /* Setter & Getter */

    public int getCardStatus() {
        return mCardStatus;
    }

    /* Device Internal Message */

    private boolean processInternalMessage(byte messageId, byte[] contentData) {
        if (contentData.length == 0) return false;

        switch (messageId) {
            case TCPMessage.MSG_ID_CARD_STATUS: {
                byte status = contentData[0];
                if (status == TCPMessage.CARD_STATUS_OK) {
                    mCardStatus = CardStatusOK;
                } else if (status == TCPMessage.CARD_STATUS_UNFORMATTED) {
                    mCardStatus = CardStatusUnformatted;
                } else {
                    // 默认状态用None
                    mCardStatus = CardStatusNone;
                }
                return true;
            }

            default:
                break;
        }

        return false;
    }

    /* TCPClient 回调 */

    private TCPClient.TCPClientAdapter mAdapter = new TCPClient.TCPClientAdapter() {
        @Override
        public void onConnectionSuccess() {
//            Log.d(TAG, ">>>>>> onConnectionSuccess");

            // 发送连接通知
            BusProvider.getBus().post(new MessageCenterConnectionEvent(true));
        }

        @Override
        public void onDisconnection() {
//            Log.d(TAG, ">>>>>> onDisconnection");

            // 发送断开通知
            BusProvider.getBus().post(new MessageCenterConnectionEvent(false));
        }

        @Override
        public void onConnectionFailed() {

        }

        @Override
        public void onReceiveData(byte[] data) {
            // data = messageId(1) + sessionId(1) + reserved(2) + info
            byte messageId = data[0];
//            byte sessionId = data[1];
            byte[] infoData = Arrays.copyOfRange(data, 4, data.length);

            // 首先进行内部消息处理
            if (processInternalMessage(messageId, infoData)) {
                // 如果已经处理过了，返回
                return;
            }

            // 用于每个发送的回调
//            String keySessionId = getKeyOfSessionId(sessionId);
//            MessageDeliverListener listener = mPostmen.get(keySessionId);
//            if (listener != null) {
//                mPostmen.remove(keySessionId);
//                listener.onDelivered(messageId, infoData);
//            }

            // 分发消息
            TCPMessage message = new TCPMessage(messageId, infoData);
            BusProvider.getBus().post(message);
        }

        @Override
        public void onSendData() {

        }
    };

    /* 发送消息回调 */

    public interface MessageDeliverListener {
        void onDelivered(byte messageId, byte[] data);
    }
}

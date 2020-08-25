package com.demotoothie.comm;

import androidx.annotation.Nullable;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

import java.nio.ByteBuffer;

public class TCPMessage implements ISendable {

    private byte mMessageId;
    private byte mSessionId;
    private byte[] mContent;

    /**
     * 消息ID，用以表明消息用途
     */

    // !!! This section sync with Firmware and iOS !!!

    /* System */    /* 0x00-0x07 */

    public static final byte MSG_ID_HEARTBEAT               = 0x00; // pulse

    public static final byte MSG_ID_REQUEST_REPORT          = 0x01;

    public static final byte MSG_ID_REPORT                  = 0x02; // JSON, (Client <-> Server)

    public static final byte MSG_ID_DEVICE_STATUS           = 0x03;
    public static final byte    DEVICE_STATUS_DISCONNECTED  = 0x00;
    public static final byte    DEVICE_STATUS_IDLE          = 0x01;
    public static final byte    DEVICE_STATUS_BUSY          = 0x02;
    public static final byte    DEVICE_STATUS_NO_CARD       = 0x03;
    public static final byte    DEVICE_STATUS_INSUFFICIENT_STORAGE = 0x04;

    public static final byte MSG_ID_CARD_STATUS             = 0x04;
    public static final byte    CARD_STATUS_NONE            = 0x00;
    public static final byte    CARD_STATUS_OK              = 0x01;
    public static final byte    CARD_STATUS_UNFORMATTED     = 0x02;

    public static final byte MSG_ID_TIME_CALIBRATION        = 0x05;

    /* Preview */    /* 0x08-0x0F */

    public static final byte MSG_ID_PREVIEW_RESOLUTION      = 0x08;
    public static final byte    PREVIEW_RESOLUTION_SD       = 0x00;
    public static final byte    PREVIEW_RESOLUTION_HD       = 0x01;
    public static final byte    PREVIEW_RESOLUTION_FHD      = 0x02;

    public static final byte MSG_ID_PREVIEW_QUALITY         = 0x09;
    public static final byte    PREVIEW_QUALITY_LOW         = 0x00;
    public static final byte    PREVIEW_QUALITY_MID         = 0x01;
    public static final byte    PREVIEW_QUALITY_HIGH        = 0x02;

    public static final byte MSG_ID_PREVIEW_SOUND           = 0x0A;
    public static final byte    PREVIEW_SOUND_TOGGLE        = 0x00;
    public static final byte    PREVIEW_SOUND_OFF           = 0x01;
    public static final byte    PREVIEW_SOUND_ON            = 0x02;


    /* Video */    /* 0x10-0x17 */

    public static final byte MSG_ID_VIDEO_RESOLUTION        = 0x10;
    public static final byte    VIDEO_RESOLUTION_SD         = 0x00;
    public static final byte    VIDEO_RESOLUTION_HD         = 0x01;
    public static final byte    VIDEO_RESOLUTION_FHD        = 0x02;

    public static final byte MSG_ID_VIDEO_QUALITY           = 0x11;
    public static final byte    VIDEO_QUALITY_LOW           = 0x00;
    public static final byte    VIDEO_QUALITY_MID           = 0x01;
    public static final byte    VIDEO_QUALITY_HIGH          = 0x02;

    public static final byte MSG_ID_VIDEO_SOUND             = 0x12;
    public static final byte    VIDEO_SOUND_OFF             = 0x00;
    public static final byte    VIDEO_SOUND_ON              = 0x01;

    public static final byte MSG_ID_VIDEO_CYCLIC_RECORD     = 0x13;
    public static final byte    VIDEO_CYCLIC_RECORD_OFF     = 0x00;
    public static final byte    VIDEO_CYCLIC_RECORD_1MIN    = 0x01;
    public static final byte    VIDEO_CYCLIC_RECORD_2MIN    = 0x02;
    public static final byte    VIDEO_CYCLIC_RECORD_3MIN    = 0x03;
    public static final byte    VIDEO_CYCLIC_RECORD_4MIN    = 0x04;
    public static final byte    VIDEO_CYCLIC_RECORD_5MIN    = 0x05;


    /* Photo */    /* 0x18-0x1F */

    public static final byte MSG_ID_PHOTO_RESOLUTION        = 0x18;
    public static final byte    PHOTO_RESOLUTION_SD         = 0x00;
    public static final byte    PHOTO_RESOLUTION_HD         = 0x01;
    public static final byte    PHOTO_RESOLUTION_FHD        = 0x02;
    public static final byte    PHOTO_RESOLUTION_QHD        = 0x03;
    public static final byte    PHOTO_RESOLUTION_UHD        = 0x04;

    public static final byte MSG_ID_PHOTO_QUALITY           = 0x19;
    public static final byte    PHOTO_QUALITY_LOW           = 0x00;
    public static final byte    PHOTO_QUALITY_MID           = 0x01;
    public static final byte    PHOTO_QUALITY_HIGH          = 0x02;

    public static final byte MSG_ID_PHOTO_BURST             = 0x1A;
    public static final byte    PHOTO_BURST_OFF             = 0x00;
    public static final byte    PHOTO_BURST_2               = 0x01;
    public static final byte    PHOTO_BURST_3               = 0x02;
    public static final byte    PHOTO_BURST_5               = 0x03;
    public static final byte    PHOTO_BURST_10              = 0x04;

    public static final byte MSG_ID_PHOTO_TIMELAPSE         = 0x1B;
    public static final byte    PHOTO_TIMELAPSE_OFF         = 0x00;
    public static final byte    PHOTO_TIMELAPSE_2           = 0x01;
    public static final byte    PHOTO_TIMELAPSE_3           = 0x02;
    public static final byte    PHOTO_TIMELAPSE_5           = 0x03;
    public static final byte    PHOTO_TIMELAPSE_10          = 0x04;
    public static final byte    PHOTO_TIMELAPSE_15          = 0x05;
    public static final byte    PHOTO_TIMELAPSE_20          = 0x06;
    public static final byte    PHOTO_TIMELAPSE_25          = 0x07;
    public static final byte    PHOTO_TIMELAPSE_30          = 0x08;


    /* Visual Effect */    /* 0x20-0x3F */

    public static final byte MSG_ID_WHITE_BALANCE           = 0x20;
    public static final byte    WHITE_BALANCE_AUTO          = 0x00;
    public static final byte    WHITE_BALANCE_DAYLIGHT      = 0x01;
    public static final byte    WHITE_BALANCE_CLOUDY        = 0x02;
    public static final byte    WHITE_BALANCE_SHADE         = 0x03;
    public static final byte    WHITE_BALANCE_FLASH         = 0x04;
    public static final byte    WHITE_BALANCE_TUNGSTEN      = 0x05;
    public static final byte    WHITE_BALANCE_FLUORESCENT   = 0x06;

    public static final byte MSG_ID_EXPOSURE_COMPENSATION   = 0x21;
    public static final byte    EXPOSURE_COMPENSATION_1     = 0x00;
    public static final byte    EXPOSURE_COMPENSATION_2     = 0x01;
    public static final byte    EXPOSURE_COMPENSATION_3     = 0x02;
    public static final byte    EXPOSURE_COMPENSATION_4     = 0x03;
    public static final byte    EXPOSURE_COMPENSATION_5     = 0x04;

    public static final byte MSG_ID_SHARPNESS               = 0x22;
    public static final byte    SHARPNESS_SOFT              = 0x00;
    public static final byte    SHARPNESS_NORMAL            = 0x01;
    public static final byte    SHARPNESS_STRONG            = 0x02;

    public static final byte MSG_ID_ISO                     = 0x23; // TODO: define

    public static final byte MSG_ID_ANTI_BANDING            = 0x24;
    public static final byte    ANTI_BANDING_OFF            = 0x00;
    public static final byte    ANTI_BANDING_50HZ           = 0x01;
    public static final byte    ANTI_BANDING_60HZ           = 0x02;
    public static final byte    ANTI_BANDING_AUTO           = 0x03;

    public static final byte MSG_ID_WDR                     = 0x25;
    public static final byte    WDR_OFF                     = 0x00;
    public static final byte    WDR_ON                      = 0x01;

    /* Control */   /* 0x40-0x4F */

    public static final byte MSG_ID_RECORD_VIDEO            = 0x40;
    public static final byte    RECORD_VIDEO_TOGGLE         = 0x00;
    public static final byte    RECORD_VIDEO_START          = 0x01;
    public static final byte    RECORD_VIDEO_STOP           = 0x02;

    public static final byte MSG_ID_TAKE_PHOTO              = 0x41; // empty body

    public static final byte MSG_ID_GET_SENSOR_RESOLUTION   = 0x42;
    public static final byte MSG_ID_SET_SENSOR_RESOLUTION   = 0x43;

    /* Common */    /* 0x50-0x7F */

    public static final byte MSG_ID_WIFI_SETTINGS           = 0x50; // JSON

    public static final byte MSG_ID_LANGUAGE                = 0x51;
    public static final byte    LANGUAGE_EN_US              = 0x00;
    public static final byte    LANGUAGE_ZH_CN              = 0x01;
    public static final byte    LANGUAGE_ZH_TW              = 0x02;
    public static final byte    LANGUAGE_JA_JP              = 0x03;

    public static final byte MSG_ID_MOTION_DETECTION        = 0x52;
    public static final byte    MOTION_DETECTION_OFF        = 0x00;
    public static final byte    MOTION_DETECTION_ON         = 0x01;

    public static final byte MSG_ID_ANTI_SHAKE              = 0x53;
    public static final byte    ANTI_SHAKE_OFF              = 0x00;
    public static final byte    ANTI_SHAKE_ON               = 0x01;

    public static final byte MSG_ID_DATE_STAMP              = 0x54;
    public static final byte    DATE_STAMP_OFF              = 0x00;
    public static final byte    DATE_STAMP_ON               = 0x01;

    public static final byte MSG_ID_SCREEN_SAVER            = 0x55;
    public static final byte    SCREEN_SAVER_OFF            = 0x00;
    public static final byte    SCREEN_SAVER_1MIN           = 0x01;
    public static final byte    SCREEN_SAVER_3MIN           = 0x02;
    public static final byte    SCREEN_SAVER_5MIN           = 0x03;

    public static final byte MSG_ID_ROTATION                = 0x56;
    public static final byte    ROTATION_OFF                = 0x00;
    public static final byte    ROTATION_ON                 = 0x01;

    public static final byte MSG_ID_AUTO_SHUTDOWN           = 0x57;
    public static final byte    AUTO_SHUTDOWN_OFF           = 0x00;
    public static final byte    AUTO_SHUTDOWN_1MIN          = 0x01;
    public static final byte    AUTO_SHUTDOWN_3MIN          = 0x02;
    public static final byte    AUTO_SHUTDOWN_5MIN          = 0x03;

    public static final byte MSG_ID_BUTTON_SOUND            = 0x58;
    public static final byte    BUTTON_SOUND_OFF            = 0x00;
    public static final byte    BUTTON_SOUND_ON             = 0x01;

    public static final byte MSG_ID_OSD_MODE                = 0x59;
    public static final byte    OSD_MODE_OFF                = 0x00;
    public static final byte    OSD_MODE_ON                 = 0x01;

    public static final byte MSG_ID_CAR_MODE                = 0x5A;
    public static final byte    CAR_MODE_OFF                = 0x00;
    public static final byte    CAR_MODE_ON                 = 0x01;

    public static final byte MSG_ID_FORMAT_CARD             = 0x5B; // empty body

    public static final byte MSG_ID_FACTORY_RESET           = 0x5C; // empty body

    public static final byte MSG_ID_BATTERY                 = 0x5D;

    public byte getMessageId() {
        return mMessageId;
    }

    public byte getSessionId() {
        return mSessionId;
    }

    public void setSessionId(byte sessionId) {
        mSessionId = sessionId;
    }

    public byte[] getContent() {
        return mContent;
    }

    public TCPMessage(byte mid, @Nullable byte[] data) {
        mMessageId = mid;
        mContent = data;
    }

    public TCPMessage(byte mid, byte value) {
        mMessageId = mid;

        ByteBuffer bb = ByteBuffer.allocate(1);
        bb.put(value);

        mContent = bb.array();
    }

    @Override
    public byte[] parse() {
        // content = length(4) + [type(id:1) + sessionId(1) + reserved(2)](4) + body

        int contentLength = 0;
        if (mContent != null) {
            contentLength = mContent.length;
        }

        // length not including header
        int length = 4 + contentLength;
        // total length = header(4) + length
        ByteBuffer bb = ByteBuffer.allocate(4 + length);
        // header
        byte[] header = new byte[4];
        header[0] = (byte) (length >> 24);
        header[1] = (byte) (length >> 16);
        header[2] = (byte) (length >> 8);
        header[3] = (byte) (length);
        bb.put(header);
        // ids
        byte[] ids = new byte[4];
        ids[0] = mMessageId;
        ids[1] = mSessionId;
        ids[2] = 0; // reserved
        ids[3] = 0; // reserved
        bb.put(ids);

        if (mContent != null) {
            bb.put(mContent);
        }

        return bb.array();
    }
}

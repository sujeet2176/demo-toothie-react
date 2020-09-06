import { requireNativeComponent } from 'react-native';
import React, { Component } from 'react';

const MODULE_NAME = 'RNTCameraView';
const CamView = requireNativeComponent(MODULE_NAME, null);

type PropsType = {};

class CameraView extends Component<PropsType> {
    constructor(props){
        super(props);
    }


    render() {
        const {isConnected, isRotate, click, isRecording, getPhotoPath, getVideoPath} = this.props;
        console.log(isConnected); 
    //    alert (isConnected);
            return (
            <CamView style={{flex: 1}}
            isConnected = {true}
            isRecording = {isRecording}
            isRotate = {isRotate}
            click = {click}
            onClickPic = {(event) => getPhotoPath(event.nativeEvent.photoPath)}
            onRecordVideo = {(event) => getVideoPath(event.nativeEvent.videoPath)}
            />
                );

    }
}

export default CameraView;
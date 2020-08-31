import { requireNativeComponent } from 'react-native';
import React, { Component } from 'react';

const MODULE_NAME = 'RNTCameraView';
const CamView = requireNativeComponent(MODULE_NAME, null);

type PropsType = {};

class CameraView extends Component<PropsType> {
    render() {

        return (
            <CamView style={{flex: 1, width: '100%', height: '85%'}}/>
        );
    }
}

export default CameraView;
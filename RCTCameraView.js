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
        const {isConnected} = this.props;
        console.log(isConnected); 
    //    alert (isConnected);
            return (
            <CamView style={{flex: 1, width: '100%', height: '85%'}}
            isConnected = {isConnected}
            click = {1}
            onClickPic = {(event) => console.log('onClickPic',event.nativeEvent.message)}
            />
                );

    }
}

export default CameraView;
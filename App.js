/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useRef, useState } from 'react';
import CameraView from './RCTCameraView.js';
// import {  } from 'react-native';
// import ToastExample from './ToastExample';
import { requireNativeComponent , NativeModules} from 'react-native';
import {
  SafeAreaView,
  StyleSheet,
  StatusBar,
  TouchableOpacity,
  Text,
  View,
} from 'react-native';

// let RNTCameraView = requireNativeComponent('RNTCameraView')

const App = () => {
  const [isConnected, setIsConnected] = useState(true);
  const camRef = useRef();
  const onCapturePress = () => {
    // alert('Connect called')
    // NativeModules.ToastExample.show('Awesome', NativeModules.ToastExample.SHORT);
    // NativeModules.RNTCameraView.connect('test');
    // CameraView.connect('test');
    setIsConnected(!isConnected);

  //   try{
  //   } catch (err) {
  //     // error(err);
  //     console.log(err);
  // }
    // camRef.current.captureImage();
  };
  const onRecordPress = async () => {

    // await NativeModules.RNTCameraView.reconnect();
    // camRef.current.recordVideo();
  };
  const onStopPress = () => {
    // camRef.current.stopRecording();
  };
  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView style={styles.flex}>
        {/** Change View to your CamView */}
        <CameraView
          style={styles.container}
          ref={camRef}
          isConnected = {isConnected}
        >
        </CameraView>

        {/* <RNTCameraView></RNTCameraView> */}

        <View style={styles.buttonContainer}>
          <TouchableOpacity onPress={onCapturePress} style={styles.button}>
            <Text>{'Connect'}</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={onRecordPress} style={styles.button}>
            <Text>{'Reconnect'}</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={onStopPress} style={styles.button}>
            <Text>{'Stop'}</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  flex: {
    flex: 1,
  },
  container: {
    flex: 1,
    backgroundColor: 'grey',
  },

  camera: {
    flex: 1,
    backgroundColor: 'red',
  },

  buttonContainer: {
    flexDirection: 'row',
  },
  button: {
    flex: 1,
    paddingVertical: 20,
    alignItems: 'center',
  },
});

export default App;
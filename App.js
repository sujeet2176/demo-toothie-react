/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useRef } from 'react';
import CameraView from './RCTCameraView.js';
import { NativeModules, NativeEventEmitter } from 'react-native';


import {
  SafeAreaView,
  StyleSheet,
  StatusBar,
  TouchableOpacity,
  Text,
  View,
} from 'react-native';


const App = () => {

  const cameraViewEventManager = new NativeEventEmitter(NativeModules.RNTCameraViewEventManager)
  cameraViewEventManager.addListener("StartRecording", res => {
    console.log("** Start Recording in React Native **");
  });

  cameraViewEventManager.addListener("StoppedRecording", res => {
    console.log("** Stop Recording in React Native **");
  });

  const camRef = useRef();
  const onConnectPress = async () => {
    await NativeModules.RNTCameraView.connect();
  };

  const onCapturePress = async () => {
    await NativeModules.RNTCameraView.captureWithCompletion(path => {
      console.log(path)
    })
  };

  const onStartPress = async() => {
    await NativeModules.RNTCameraView.recordWithCompletion(path => {
      console.log(path)
    })
  };

  const onStopPress = async () => {
    await NativeModules.RNTCameraView.recordWithCompletion(path => {
      console.log(path)
    })
  };

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView style={styles.flex}>
        {/** Change View to your CamView */}
        <CameraView
          style={styles.container}
          ref={camRef}
        >
        </CameraView>

        <View style={styles.buttonContainer}>
          <TouchableOpacity onPress={onConnectPress} style={styles.button}>
            <Text>{'Connect'}</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={onCapturePress} style={styles.button}>
            <Text>{'Capture'}</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={onStartPress} style={styles.button}>
            <Text>{'Start'}</Text>
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

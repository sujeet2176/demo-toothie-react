/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, { useRef } from 'react';
import CameraView from './RCTCameraView.js';
import { NativeModules } from 'react-native';


import {
  SafeAreaView,
  StyleSheet,
  StatusBar,
  TouchableOpacity,
  Text,
  View,
} from 'react-native';


const App = () => {

  const camRef = useRef();
  const onCapturePress = () => {
    // camRef.current.captureImage();

  };
  const onRecordPress = () => {
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
        >
            
        </CameraView>



        <View style={styles.buttonContainer}>
          <TouchableOpacity onPress={onCapturePress} style={styles.button}>
            <Text>{'Capture'}</Text>
          </TouchableOpacity>
          <TouchableOpacity onPress={onRecordPress} style={styles.button}>
            <Text>{'Record'}</Text>
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

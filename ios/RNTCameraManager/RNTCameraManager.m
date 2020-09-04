//
//  RNTCameraManager.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import "RNTCameraManager.h"
#import "RNTCameraViewEventManager.h"

@interface RNTCameraManager() <IJKCameraViewDelegate>

@end

@implementation RNTCameraManager

IJKCameraView *cameraView;
RNTCameraViewEventManager *cameraViewEventManager;

RCTResponseSenderBlock callbackImageCapture;
RCTResponseSenderBlock callbackVideoRecording;

RCT_EXPORT_MODULE(RNTCameraView)
- (UIView *)view {
  cameraView = [[IJKCameraView alloc] init];
  cameraViewEventManager = [[RNTCameraViewEventManager alloc] init];
  cameraView.delegate = self;
  cameraView.eventDelegate = cameraViewEventManager;
  return cameraView;
}

RCT_EXPORT_METHOD(connect) {
  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView doReconnect];
  }];
}

#pragma mark - Image Capture

/// This function will save the image at given path
/// @param atPath NSString path of the directory where image to save
/// RCTResponseSenderBlock provides the path of image along with image name.
RCT_EXPORT_METHOD(captureWithCompletion: (RCTResponseSenderBlock)callback) {
  callbackImageCapture = callback;

  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView takePicture];
  }];
}

#pragma mark - Video Rotation Methods

/// This function will start and stop the video recording. If idle then start recording else stop.
/// @param atPath NSString path of the directory where video to save
/// RCTResponseSenderBlock provides the path of image along with image name.
RCT_EXPORT_METHOD(startRecording) {
  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView startRecording];
  }];
}

///  This func will stop recording
/// @param callback give an array, first element will contain relative  file path
RCT_EXPORT_METHOD(stopRecordingWithCompletion: (RCTResponseSenderBlock)callback) {
  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView stopRecordingWithCallback:^(NSString * _Nonnull filePath) {
      callback(@[filePath]);
    }];
  }];
}

/// This will rotate the image to 90 degree onwards.
RCT_EXPORT_METHOD(rotate) {
  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView doSetVideoRotation];
  }];
}

/// This will rotate the image to 180 degree onwards.
RCT_EXPORT_METHOD(rotateTo180) {
  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView doSetVideoRotation180];
  }];
}

#pragma mark - IJKCameraViewDelegate Method
- (void)successCaptureImageAt:(NSString *)path {
  callbackImageCapture(@[path]);
}

- (void)errorCaptureImageAt:(nonnull NSString *)path {
}

# pragma mark - Initialiser

@end

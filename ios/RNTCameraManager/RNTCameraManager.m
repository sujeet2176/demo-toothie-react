//
//  RNTCameraManager.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import "RNTCameraManager.h"

@interface RNTCameraManager() <IJKCameraViewDelegate>

@end

@implementation RNTCameraManager

IJKCameraView *cameraView;
RCTResponseSenderBlock callbackImageCapture;
RCTResponseSenderBlock callbackVideoRecording;

RCT_EXPORT_MODULE(RNTCameraView)
- (UIView *)view {
  cameraView = [[IJKCameraView alloc] init];
  cameraView.delegate = self;
  return cameraView;
}

RCT_EXPORT_METHOD(reconnect) {
  runOnMainQueueWithoutDeadlocking(^{
      //Do stuff
    [cameraView doReconnect];
  });
}


RCT_EXPORT_METHOD(connect) {
  runOnMainQueueWithoutDeadlocking(^{
      //Do stuff
    [cameraView doReconnect];
    //[cameraView openVideo];
  });
}


#pragma mark - Image Capture
/// This function will save the image at given path
/// @param atPath NSString path of the directory where image to save
/// RCTResponseSenderBlock provides the path of image along with image name.
RCT_EXPORT_METHOD(capture:(NSString *)atPath completion: (RCTResponseSenderBlock)callback) {
  callbackImageCapture = callback;
  runOnMainQueueWithoutDeadlocking(^{
      //Do stuff
    [cameraView takePicture:atPath];
  });
}

/// This function will save the image at given path
/// @param atPath NSString path of the directory where image to save
/// RCTResponseSenderBlock provides the path of image along with image name.
RCT_EXPORT_METHOD(captureWithCompletion: (RCTResponseSenderBlock)callback) {
  callbackImageCapture = callback;

  runOnMainQueueWithoutDeadlocking(^{
      //Do stuff
    [cameraView takePicture];
  });
}

#pragma mark - Video Rotation Methods

/// This function will start and stop the video recording. If idle then start recording else stop.
/// @param atPath NSString path of the directory where video to save
/// RCTResponseSenderBlock provides the path of image along with image name.
RCT_EXPORT_METHOD(recordWithCompletion: (RCTResponseSenderBlock)callback) {
  callbackVideoRecording = callback;

  [AppUtility runOnMainQueueWithoutDeadlocking:^{
    [cameraView recordVideo];
  }];
}


/// This will rotate the image to 90 degree onwards.
RCT_EXPORT_METHOD(rotate) {
  [cameraView doSetVideoRotation];
}

/// This will rotate the image to 180 degree onwards.
RCT_EXPORT_METHOD(rotateTo180) {
  [cameraView doSetVideoRotation180];
}

#pragma mark - Native Usage Methods
void runOnMainQueueWithoutDeadlocking(void (^block)(void)) {
    if ([NSThread isMainThread]) {
        block();
    } else {
        dispatch_sync(dispatch_get_main_queue(), block);
    }
}

#pragma mark - IJKCameraViewDelegate Method
- (void)successCaptureImageAt:(NSString *)path {
  callbackImageCapture(@[path]);
}

- (void)didStartRecordingAt:(NSString *)path {

}

- (void)didStopRecordingAt:(NSString *)path {
  callbackVideoRecording(@[path]);
}

- (void)moviePlaybackStateStopped {

}

- (void)moviePlaybackStatePlaying {

}

- (void)moviePlaybackStatePause {

}

- (void)moviePlaybackStateError {
  
}

# pragma mark - Initialiser

@end

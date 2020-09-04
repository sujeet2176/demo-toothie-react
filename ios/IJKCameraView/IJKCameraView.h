//
//  IJKCameraView.h
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import <UIKit/UIKit.h>
#import <IJKMediaFramework/IJKMediaFramework.h>
#import "AppUtility.h"
#import "HZRecorder.h"


NS_ASSUME_NONNULL_BEGIN

@protocol IJKCameraViewEventDelegate <NSObject>
- (void) startReceivingData;

  - (void)moviePlaybackStateStopped;
  - (void)moviePlaybackStatePlaying;
  - (void)moviePlaybackStatePause;
  - (void)moviePlaybackStateError;

  - (void)didStartRecordingAt:(NSString *)path;
  - (void)didStopRecordingAt:(NSString *)path;
  - (void)didErrorRecordingAt:(NSString *)path;
@end

@protocol IJKCameraViewDelegate <NSObject>
- (void)successCaptureImageAt:(NSString *)path;
- (void)errorCaptureImageAt:(NSString *)path;
@end

@interface IJKCameraView : UIView <IJKFFMoviePlayerDelegate>

- (void)doReconnect;
- (void)openVideo;

// Start/Stop Recording
- (void) startRecording;
- (void) startRecordingAtPath:(NSString *)path;
- (void) stopRecordingWithCallback:(RNTCompletedCallBack)callback;

// It will rotate the video to 90 degree and so on
- (void)doSetVideoRotation;
- (void)doSetVideoRotation180;

- (void)takePicture;
- (void)takePicture:(NSString *)atPath;

@property(atomic,strong) NSURL *url;
@property(atomic, retain) id<IJKMediaPlayback> player;

@property (nonatomic, weak) id <IJKCameraViewDelegate> delegate;
@property (nonatomic, weak) id <IJKCameraViewEventDelegate> eventDelegate;

@end

NS_ASSUME_NONNULL_END

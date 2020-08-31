//
//  RNTCameraViewEventManager.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 31/08/20.
//

#import "RNTCameraViewEventManager.h"

@implementation RNTCameraViewEventManager

#define MoviePlaybackStateStopped @"MoviePlaybackStateStopped"
#define MoviePlaybackStatePlaying @"MoviePlaybackStatePlaying"
#define MoviePlaybackStatePause @"MoviePlaybackStatePause"
#define MoviePlaybackStateError @"MoviePlaybackStateError"

//This Events for video recording state change
#define StartRecording @"StartRecording"
#define StoppedRecording @"StoppedRecording"
#define FailedRecording @"FailedRecording"

//This Method will notify when conenction stablished and started receivng data
#define StartReceivingData @"StartReceivingData"

RCT_EXPORT_MODULE(RNTCameraViewEventManager)

// Start Receiving Data
-(void) startReceivingData {
  NSDictionary *data = [NSDictionary new];
  [self sendEventWithName:StartReceivingData body:data];
}

- (void)moviePlaybackStateStopped {
  NSDictionary *data = [NSDictionary new];
  [self sendEventWithName:MoviePlaybackStateStopped body:data];
}

- (void)moviePlaybackStatePlaying {
  NSDictionary *data = [NSDictionary new];
   [self sendEventWithName:MoviePlaybackStatePlaying body:data];
}

- (void)moviePlaybackStatePause {
  NSDictionary *data = [NSDictionary new];
   [self sendEventWithName:MoviePlaybackStatePause body:data];
}

- (void)moviePlaybackStateError {
  NSDictionary *data = [NSDictionary new];
   [self sendEventWithName:MoviePlaybackStateError body:data];
}

// Recording Delegates
- (void)didStartRecordingAt:(NSString *)path {
  NSMutableDictionary *data = [NSMutableDictionary new];
  data[@"path"] = path;
  [self sendEventWithName:StartRecording body:data];
}

- (void)didStopRecordingAt:(NSString *)path {
  NSMutableDictionary *data = [NSMutableDictionary new];
  data[@"path"] = path;
  [self sendEventWithName:StoppedRecording body:data];
}

- (void)didErrorRecordingAt:(NSString *)path {
  NSMutableDictionary *data = [NSMutableDictionary new];
  data[@"path"] = path;
  [self sendEventWithName:FailedRecording body:data];
}



@end

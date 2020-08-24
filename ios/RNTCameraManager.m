//
//  RNTCameraManager.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import "RNTCameraManager.h"
#import "TestView.h"

@implementation RNTCameraManager

TestView *testView;

RCT_EXPORT_MODULE(RNTCameraView)
- (UIView *)view
{
  testView = [[TestView alloc] init];
  return testView;
}

RCT_EXPORT_METHOD(reconnect)
{
  runOnMainQueueWithoutDeadlocking(^{
      //Do stuff
    [testView doReconnect];
  });
}

void runOnMainQueueWithoutDeadlocking(void (^block)(void))
{
    if ([NSThread isMainThread])
    {
        block();
    }
    else
    {
        dispatch_sync(dispatch_get_main_queue(), block);
    }
}

RCT_EXPORT_METHOD(connect)
{
  runOnMainQueueWithoutDeadlocking(^{
      //Do stuff
    [testView openVideo];
  });

}




@end

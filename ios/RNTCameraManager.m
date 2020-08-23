//
//  RNTCameraManager.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import "RNTCameraManager.h"
#import "TestView.h"

@implementation RNTCameraManager

RCT_EXPORT_MODULE(RNTCameraView)
- (UIView *)view
{
  return [[TestView alloc] init];
}


RCT_EXPORT_METHOD(findEvents:(RCTResponseSenderBlock)callback)
{
  NSArray *events = @[@1,@2];
  callback(@[[NSNull null], events]);
}


@end

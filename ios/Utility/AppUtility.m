//
//  AppUtility.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 25/08/20.
//

#import "AppUtility.h"

@implementation AppUtility

+(void) runOnMainQueueWithoutDeadlocking:(void (^)(void))block {
    if ([NSThread isMainThread])
    {
        block();
    }
    else
    {
        dispatch_sync(dispatch_get_main_queue(), block);
    }
}

@end

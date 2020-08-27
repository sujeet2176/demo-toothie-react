//
//  AppUtility.h
//  demoToothie
//
//  Created by Sujeet Shrivastav on 25/08/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface AppUtility : NSObject

+(void) runOnMainQueueWithoutDeadlocking:(void (^)(void))block;

@end

NS_ASSUME_NONNULL_END

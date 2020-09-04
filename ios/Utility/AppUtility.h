//
//  AppUtility.h
//  demoToothie
//
//  Created by Sujeet Shrivastav on 25/08/20.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^RNTCompletedCallBack) (NSString *filePath);

@interface AppUtility : NSObject

+(void) runOnMainQueueWithoutDeadlocking:(void (^)(void))block;

+ (NSString *)mediaFileName ;
+ (NSString *)mediaImagesDirPath ;
+ (NSURL *)videoFileURL ;

@end

NS_ASSUME_NONNULL_END

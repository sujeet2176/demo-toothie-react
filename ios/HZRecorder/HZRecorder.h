//
//  HZRecorder.h
//  Glimpse
//
//  Created by JinTao on 2020/7/1.
//  Copyright © 2020 Wess Cope. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIView (CVPixelBuffer)

- (CVPixelBufferRef)CVPixelBufferRef;

@end


@interface HZRecorder : NSObject

- (void)startRecordingView:(UIView *)view outputURL:(NSURL *)outputURL;

- (void)stop;

@end

NS_ASSUME_NONNULL_END

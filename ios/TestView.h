//
//  TestView.h
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import <UIKit/UIKit.h>
#import <IJKMediaFramework/IJKMediaFramework.h>


NS_ASSUME_NONNULL_BEGIN

@interface TestView : UIView <IJKFFMoviePlayerDelegate>

- (void)doReconnect;
- (void)openVideo;

@property(atomic,strong) NSURL *url;
@property(atomic, retain) id<IJKMediaPlayback> player;


@end

NS_ASSUME_NONNULL_END

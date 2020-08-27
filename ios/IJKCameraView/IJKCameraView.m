//
//  IJKCameraView.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import "IJKCameraView.h"

#define RECONNECTION_INTERVAL   0.5
#define IMAGE_DIRECTORY_NAME @"Images"
#define Video_DIRECTORY_NAME @"Videos"
#define URL_STRING @"rtsp://192.168.1.1:7070/webcam"


@implementation IJKCameraView 
{
  int videoRotation;
  NSData *cifData;
  NSTimer *insertTimer;
  BOOL inserting;
}

#pragma mark - Life Cycle Methods
- (instancetype)init {
  self = [super init];
  if (self) {
    //show video
    self.url = [NSURL URLWithString: URL_STRING];

    // Cannot place in installMovieNotificationObservers, if so, it takes no effect
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(willResignActive:)
                                                 name:UIApplicationWillResignActiveNotification
                                               object:_player];


    NSString *cifDataPath = [[NSBundle mainBundle] pathForResource:@"cif" ofType:@"yuv444p"];
    cifData = [[NSData alloc] initWithContentsOfFile:cifDataPath];
  }
  return self;
}

#pragma mark - Exposed Methods for Camera
- (void)openVideo {
    IJKFFOptions *options = [IJKFFOptions optionsByDefault];

  // The JPEG parsing method uses the padding method by default (that is, if the network packet is lost, the last frame of data is used to make up), which can be changed to DROP (the entire frame is lost when the packet is lost, do not use it if the network is not good), ORIGIN (original method, Do not use)
    [options setPlayerOptionIntValue:RtpJpegParsePacketMethodOrigin forKey:@"rtp-jpeg-parse-packet-method"];

  // 读帧超时时间，单位us
    [options setPlayerOptionIntValue:5000 * 1000 forKey:@"readtimeout"];

  // Image type
    [options setPlayerOptionIntValue:PreferredImageTypeJPEG forKey:@"preferred-image-type"];

  // Image quality, available for lossy format (min and max are both from 1 to 51, 0 < min <= max, smaller is better, default is 2 and 31)
    [options setPlayerOptionIntValue:2 forKey:@"image-quality-min"];
    [options setPlayerOptionIntValue:31 forKey:@"image-quality-max"];

  // video
    [options setPlayerOptionIntValue:PreferredVideoTypeMJPEG    forKey:@"preferred-video-type"];
    [options setPlayerOptionIntValue:1                          forKey:@"video-need-transcoding"];
    [options setPlayerOptionIntValue:MjpegPixFmtYUVJ420P        forKey:@"mjpeg-pix-fmt"];

  // Video quality, for MJPEG and MPEG4
    [options setPlayerOptionIntValue:2                          forKey:@"video-quality-min"];
    [options setPlayerOptionIntValue:20                         forKey:@"video-quality-max"];

  // x264 preset, tune and profile, for H264
    [options setPlayerOptionIntValue:X264OptionPresetUltrafast  forKey:@"x264-option-preset"];
    [options setPlayerOptionIntValue:X264OptionTuneZerolatency  forKey:@"x264-option-tune"];
    [options setPlayerOptionIntValue:X264OptionProfileMain      forKey:@"x264-option-profile"];
    [options setPlayerOptionValue:@"crf=20"                     forKey:@"x264-params"];

    IJKFFMoviePlayerController *moviePlayerController = [[IJKFFMoviePlayerController alloc] initWithContentURL:self.url withOptions:options];
    moviePlayerController.delegate = self;

    self.player = moviePlayerController;
    self.player.view.autoresizingMask = UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight;
    self.player.view.frame = self.bounds;

  // Stretching method, choose proportional stretching or full-screen stretching according to your needs
    self.player.scalingMode = IJKMPMovieScalingModeAspectFit;
    self.player.shouldAutoplay = YES;
    self.autoresizesSubviews = YES;
    [self insertSubview:self.player.view atIndex:0];

    videoRotation = 0;

    // put log setting here to make it fresh
#ifdef DEBUG
    [IJKFFMoviePlayerController setLogReport:YES];
    [IJKFFMoviePlayerController setLogLevel:k_IJK_LOG_INFO];
#else
    [IJKFFMoviePlayerController setLogReport:NO];
    [IJKFFMoviePlayerController setLogLevel:k_IJK_LOG_SILENT];
#endif

    // [IJKFFMoviePlayerController checkIfFFmpegVersionMatch:YES];
    // [IJKFFMoviePlayerController checkIfPlayerVersionMatch:YES major:1 minor:0 micro:0];
}

#pragma mark - Handle Notification
- (void)loadStateDidChange:(NSNotification*)notification {
    //    MPMovieLoadStateUnknown        = 0,
    //    MPMovieLoadStatePlayable       = 1 << 0,
    //    MPMovieLoadStatePlaythroughOK  = 1 << 1, // Playback will be automatically started in this state when shouldAutoplay is YES
    //    MPMovieLoadStateStalled        = 1 << 2, // Playback will be automatically paused in this state, if started

    IJKMPMovieLoadState loadState = _player.loadState;

    if ((loadState & IJKMPMovieLoadStatePlaythroughOK) != 0) {
        NSLog(@"loadStateDidChange: IJKMPMovieLoadStatePlaythroughOK: %d\n", (int)loadState);
    } else if ((loadState & IJKMPMovieLoadStateStalled) != 0) {
        NSLog(@"loadStateDidChange: IJKMPMovieLoadStateStalled: %d\n", (int)loadState);
    } else {
        NSLog(@"loadStateDidChange: ???: %d\n", (int)loadState);
    }
}

// End play notification
- (void)moviePlayBackDidFinish:(NSNotification*)notification {
    //    MPMovieFinishReasonPlaybackEnded,
    //    MPMovieFinishReasonPlaybackError,
    //    MPMovieFinishReasonUserExited
    int reason = [[[notification userInfo] valueForKey:IJKMPMoviePlayerPlaybackDidFinishReasonUserInfoKey] intValue];

    switch (reason)
    {
        case IJKMPMovieFinishReasonPlaybackEnded:
            NSLog(@"playbackStateDidChange: IJKMPMovieFinishReasonPlaybackEnded: %d\n", reason);
            break;

        case IJKMPMovieFinishReasonUserExited:
            NSLog(@"playbackStateDidChange: IJKMPMovieFinishReasonUserExited: %d\n", reason);
            break;

        case IJKMPMovieFinishReasonPlaybackError:
            NSLog(@"playbackStateDidChange: IJKMPMovieFinishReasonPlaybackError: %d\n", reason);
            // Player stopped but have not release resources
            // So, we must shut it down manually
            // And wait for delegate method
            [self.player shutdown];
            break;

        default:
            NSLog(@"playbackPlayBackDidFinish: ???: %d\n", reason);
            break;
    }
}


// Ready to start previewing notifications
- (void)mediaIsPreparedToPlayDidChange:(NSNotification*)notification {
    NSLog(@"mediaIsPreparedToPlayDidChange\n");
}

- (void)moviePlayBackStateDidChange:(NSNotification*)notification {
  //    MPMoviePlaybackStateStopped,
  //    MPMoviePlaybackStatePlaying,
  //    MPMoviePlaybackStatePaused,
  //    MPMoviePlaybackStateInterrupted,
  //    MPMoviePlaybackStateSeekingForward,
  //    MPMoviePlaybackStateSeekingBackward


  switch (_player.playbackState) {
    case IJKMPMoviePlaybackStateStopped: {
      NSLog(@"IJKMPMoviePlayBackStateDidChange %d: stoped", (int)_player.playbackState);
      [_delegate moviePlaybackStateStopped];
      break;
    }
    case IJKMPMoviePlaybackStatePlaying: {
      NSLog(@"IJKMPMoviePlayBackStateDidChange %d: playing", (int)_player.playbackState);
      [_delegate moviePlaybackStatePlaying];
      break;
    }
    case IJKMPMoviePlaybackStatePaused: {
      NSLog(@"IJKMPMoviePlayBackStateDidChange %d: paused", (int)_player.playbackState);
      [_delegate moviePlaybackStatePause];
      break;
    }
    case IJKMPMoviePlaybackStateInterrupted: {
      NSLog(@"IJKMPMoviePlayBackStateDidChange %d: interrupted", (int)_player.playbackState);
      break;
    }
    case IJKMPMoviePlaybackStateSeekingForward:
    case IJKMPMoviePlaybackStateSeekingBackward: {
      NSLog(@"IJKMPMoviePlayBackStateDidChange %d: seeking", (int)_player.playbackState);
      break;
    }
    default: {
      NSLog(@"IJKMPMoviePlayBackStateDidChange %d: unknown", (int)_player.playbackState);
      [_delegate moviePlaybackStateError];
      break;
    }
  }
}

#pragma mark - IJKMoviePlayerControllerDelegate

- (void)moviePlayerDidShutdown:(IJKFFMoviePlayerController *)player {
    IJKFFMoviePlayerController *mpc = self.player;
    [mpc setDelegate:nil];

    [self.player.view removeFromSuperview];
    [self removeMovieNotificationObservers];
    [self performSelector:@selector(doReconnect)
               withObject:self
               afterDelay:RECONNECTION_INTERVAL];
}

- (void)doReconnect {
        NSLog(@"doReconnect");
        [self openVideo];
        [self installMovieNotificationObservers];
        [self.player prepareToPlay];
}

- (void)willResignActive:(NSNotification *)notification {
    NSLog(@"ControlPanelViewController:willResignActive");

    [self.player stopRecordVideo];
}

// Received data transfer from the video board
/**
 * Old proxy method
 * Receive data from firmware and use RTCP channel, so if you want to use it, you must add logic to distinguish the original RTCP data (use unique packet structure or check)
 */
- (void)player:(IJKFFMoviePlayerController *)player didReceiveRtcpSrData:(NSData *)data {
    // Because the data channel is shared with RTCP, the return data needs to be distinguished from the RTCP Sender Report, and you need to add your own logo to distinguish
    // RTCP sends Sender Report every 5 seconds by default
    // Will be encapsulated in the future, send and receive data directly
    NSLog(@"didReceiveRtcpSrData: %@", data);
}

/**
* New agent method
* Receive data from the firmware, use RTCP channel, built-in logic to distinguish the original RTCP data, the data can be used directly, need to use with the firmware new API
* The data is received through UDP protocol, and the resource occupation is less. Although 100% reception is not guaranteed, the success rate is okay.
* If you need to ensure 100% successful reception, you can create a new TCP Socket for sending and receiving data
*/
- (void)player:(IJKFFMoviePlayerController *)player didReceiveData:(NSData *)data {
    // work with firmware api -> wifi_data_send
    NSLog(@"didReceiveData: %@", data);
}

// Photo callback
// resultCode, <0 An error occurred, = 0 take the next photo, =1, complete the photo
- (void)playerDidTakePicture:(IJKFFMoviePlayerController *)player resultCode:(int)resultCode fileName:(NSString *)fileName {
    if (resultCode == 1) {
        // End of the photo
        [self showInfo:@"End of taking pictures"];
    }
    else if (resultCode == 0) {
        // Successfully saved a photo
      [_delegate successCaptureImageAt: fileName];
        //[self showInfo:[NSString stringWithFormat:@"Photo saved successfully: %@", fileName]];
    }
    else if (resultCode < 0) {
        // Failed to take pictures
        [self showInfo:@"Photo failed"];
    }
}

// Video callback
// resultCode, <0 An error occurred, = 0 to start recording, otherwise the recording is successfully saved
- (void)playerDidRecordVideo:(IJKFFMoviePlayerController *)player resultCode:(int)resultCode fileName:(NSString *)fileName {
  if (resultCode < 0) {
    // Video failed
    [self showInfo:@"Video failed"];
  }
  else if (resultCode == 0) {
    // Start recording
    [_delegate didStartRecordingAt:fileName];
    [player startInsertVideoWithWidth:356 height:288 pixelFormat:5];
  }
  else {
    // End of recording
    [_delegate didStopRecordingAt:fileName];
    [player stopInsertVideo];
  }
}

// Insert external video callback
// resultCode, <0 an error occurred, = 0 can start to insert video,> 0 to end inserting video
- (void)playerDidInsertVideo:(IJKFFMoviePlayerController *)player resultCode:(int)resultCode
{
    if (resultCode < 0) {
        inserting = NO;
        [self showInfo:@"Failed to initialize video insertion"];
    }
    else if (resultCode == 0) {
        // 50ms, 20fps
        insertTimer = [NSTimer scheduledTimerWithTimeInterval:0.05 target:self selector:@selector(insertVideoData) userInfo:nil repeats:YES];

        inserting = YES;
        [self showInfo:@"Initial insertion video successfully"];
    }
    else {
        [self showInfo:@"End insert video"];
        inserting = NO;
    }
}

- (void)playerDidReceivedFrameData:(NSData *)frameData width:(int)width height:(int)height pixelFormat:(int)pixelFormat
{
    NSLog(@"playerDidReceivedFrameData: len = %lu, w = %d, h = %d, pf = %d", (unsigned long)[frameData length], width, height, pixelFormat);
}

#pragma mark - Install Movie Notifications

/* Register observers for the various movie object notifications. */
-(void)installMovieNotificationObservers
{
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(loadStateDidChange:)
                                                 name:IJKMPMoviePlayerLoadStateDidChangeNotification
                                               object:_player];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayBackDidFinish:)
                                                 name:IJKMPMoviePlayerPlaybackDidFinishNotification
                                               object:_player];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(mediaIsPreparedToPlayDidChange:)
                                                 name:IJKMPMediaPlaybackIsPreparedToPlayDidChangeNotification
                                               object:_player];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayBackStateDidChange:)
                                                 name:IJKMPMoviePlayerPlaybackStateDidChangeNotification
                                               object:_player];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayerDidShutdown:)
                                                 name:IJKMPMoviePlayerDidShutdownNotification
                                               object:_player];
}

#pragma mark Remove Movie Notification Handlers

/* Remove the movie notification observers from the movie object. */
-(void)removeMovieNotificationObservers {
    NSLog(@"removeMovieNotificationObservers");
    [[NSNotificationCenter defaultCenter]removeObserver:self name:IJKMPMoviePlayerLoadStateDidChangeNotification object:_player];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:IJKMPMoviePlayerPlaybackDidFinishNotification object:_player];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:IJKMPMediaPlaybackIsPreparedToPlayDidChangeNotification object:_player];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:IJKMPMoviePlayerPlaybackStateDidChangeNotification object:_player];
    [[NSNotificationCenter defaultCenter]removeObserver:self name:IJKMPMoviePlayerDidShutdownNotification object:_player];
}

-(void)removePlayerNotificationObservers {
    NSLog(@"removePlayerNotificationObservers");
    [[NSNotificationCenter defaultCenter]removeObserver:self name:UIApplicationWillResignActiveNotification object:_player];
}

#pragma mark -
inline static IJKFFMoviePlayerController *ffplayerInstance(id<IJKMediaPlayback> player) {
    return player;
}

inline static VideoRecordingStatus ffplayerVideoRecordingStatus(IJKFFMoviePlayerController *ffplayer) {
    return ffplayer.videoRecordingStatus;
}

#pragma mark - testing

/**
 * 执行拍照
 */
- (void)doTakePicture:(int)number atPath:(NSString *)dirPath {
    NSString *fileName = [self mediaFileName];

    // Photo parameter description
    // 1. Directory path, the directory needs to be created first, otherwise an error is returned
    // 2. File name, no need to specify extension
    // 3 and 4, the width and height of the saved image, if both are -1 (only one -1 is not allowed), the original image size is saved, if it is other, then stretched to the set value
    // 5. Number of continuous photos, continuous photos, no interval in between
    [self.player takePictureAtPath:dirPath withFileName:fileName width:-1 height:-1 number:number];
}

- (void)doTakePicture:(int)number {
    NSString *dirPath = [self mediaImagesDirPath];
    NSString *fileName = [self mediaFileName];

    // Photo parameter description
    // 1. Directory path, the directory needs to be created first, otherwise an error is returned
    // 2. File name, no need to specify extension
    // 3 and 4, the width and height of the saved image, if both are -1 (only one -1 is not allowed), the original image size is saved, if it is other, then stretched to the set value
    // 5. Number of continuous photos, continuous photos, no interval in between
    [self.player takePictureAtPath:dirPath withFileName:fileName width:-1 height:-1 number:number];
}


- (void)takePicture {
    [self doTakePicture:1];
}

- (void)takePicture:(NSString *)path {
  [self doTakePicture:1 atPath:path];
}

/**
* Video
* Set recording parameters in openVideo
 */
- (void)doRecordVideo {
    NSString *dirPath = [self mediaVideosDirPath];
    NSString *fileName = [self mediaFileName];
    // Video parameter description
    // 1. Directory path, the directory needs to be created first, otherwise an error is returned
    // 2. File name, no need to specify extension
    // 3 and 4, save the width and height of the video, if both are -1 (only one -1 is not allowed), then save the original image size, if it is other, then stretch to the set value
    [self.player startRecordVideoAtPath:dirPath withFileName:fileName width:-1 height:-1];
}

- (void)recordVideo {
    IJKFFMoviePlayerController *ffplayer = ffplayerInstance(self.player);
    VideoRecordingStatus status = ffplayerVideoRecordingStatus(ffplayer);

    // take recording action and wait for delegate method
    // if idle, start recording
    if (status == VideoRecordingStatusIdle) {
        [self doRecordVideo];
    }
    // if recording, stop it
    else if (status == VideoRecordingStatusRecording) {
        if (inserting)
            [self insertVideo];
        [self.player stopRecordVideo];
    }
    // if stopping, do nothing
    else {
        NSLog(@"Recording video: Do nothing");
    }
}

- (void)outputVideo {
    static BOOL enable = NO;
    enable = !enable;

    IJKFFMoviePlayerController *ffplayer = ffplayerInstance(self.player);
    [ffplayer setOutputVideo:enable];
}

#define USE_NEW_SEND_DATA_API   1
/**
* Send flight control data
* Old API: Because the data channel is shared with RTCP, the data needs to be distinguished from the RTCP Receive Report, you need to add your own logo to distinguish
* New API: data can be sent directly, the firmware uses a new interface to receive data
* Data is sent via UDP protocol, with less resource consumption, although 100% transmission cannot be guaranteed, but the success rate is OK
* If you need to ensure 100% successful transmission, you can create a new TCP Socket for sending and receiving data
*/
- (void)doSendData {
#if USE_NEW_SEND_DATA_API
    Byte controlBytes[5];

    controlBytes[0] = 0x80;
    controlBytes[1] = 0x80;
    controlBytes[2] = 0x80;
    controlBytes[3] = 0x80;
    controlBytes[4] = 0x00;

    NSData *controlData = [NSData dataWithBytes:controlBytes length:5];
    // New API, used with firmware new API
    [self.player sendData:controlData];
#else
    Byte controlBytes[8];

    controlBytes[0] = 0x66;
    controlBytes[1] = 0x80;
    controlBytes[2] = 0x80;
    controlBytes[3] = 0x80;
    controlBytes[4] = 0x80;
    controlBytes[5] = 0x00;
    controlBytes[6] = 0x00;
    controlBytes[7] = 0x99;

    NSData *controlData = [NSData dataWithBytes:controlBytes length:8];
    // Old API, the data needs to add the front and back flag bytes and check bytes
    // The flags are 0x66 and 0x99 directly before and after, the firmware is not strictly verified, the penultimate is the XOR check byte, and the XOR value is stored in addition to the above three bytes, the firmware will be strictly calibrated Test
    [self.player sendRtcpRrData:controlData];
#endif
}

/**
* Set VR mode (left and right split screen display)
 */
- (void)doSetVrMode {
    [self.player setVrMode:!self.player.isVrMode];
}

/**
* The software rotates the screen (display rotation), and rotates clockwise (non-Sensor rotation, the rotation angle of the image transmitted from the image transmission board is unchanged, the angle of the rendered image is changed, and the photo and video are not affected)
 */
- (void)doSetVideoRotation {
// flip at 90 degrees
    IJKFFMoviePlayerController *ffplayer = ffplayerInstance(self.player);
    videoRotation += 90;
    [ffplayer setVideoRotation:videoRotation];  // Not available in stretch mode (IJKMPMovieScalingModeFill)
}

/**
* The software rotates the screen (image frame rotation), because it needs to keep the width and height all the time, so it only supports 180° (non-Sensor rotation, the rotation angle of the picture passed from the picture transmission board does not change, what changes is the angle of the output image, taking pictures and recording The angle will change)
*/
- (void)doSetVideoRotation180 {
    // flip at 180 degrees
    [self.player setRotation180:!self.player.isRotation180];
}


//! ! ! The player configuration needs to display or open "video-need-transcoding" implicitly
- (void)insertVideo {
    IJKFFMoviePlayerController *player = ffplayerInstance(self.player);

    if (inserting) {
        if (insertTimer) {
            [insertTimer invalidate];
            insertTimer = nil;
        }
        [player stopInsertVideo];
    } else {
        // cif, yuv444p
        [player startInsertVideoWithWidth:356 height:288 pixelFormat:5];
    }
}

- (void)insertVideoData {
    IJKFFMoviePlayerController *player = ffplayerInstance(self.player);
    [player insertVideoData:cifData align:1 copy:YES];
}

- (void)showInfo:(NSString *)infoText {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil
                                                        message:infoText
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
    [alertView show];
}

/**
* Return to Document path
*
* @return Document path
*/
- (NSString *)documentPath {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    return [paths firstObject];
}

/**
* Return the directory name with today's date as the path
* Automatically create a directory, successfully return the directory name, unsuccessfully return nil
*/
- (NSString *)mediaImagesDirPath {
  NSString *dirName = IMAGE_DIRECTORY_NAME;
  NSString *dirPath = [[self documentPath] stringByAppendingPathComponent:dirName];

  if ([[NSFileManager defaultManager] createDirectoryAtPath:dirPath withIntermediateDirectories:YES attributes:nil error:nil])
    return dirPath;

  return nil;
}

/**
* Return the directory name with today's date as the path
* Automatically create a directory, successfully return the directory name, unsuccessfully return nil
*/
- (NSString *)mediaVideosDirPath {
  NSString *dirName = Video_DIRECTORY_NAME;
  NSString *dirPath = [[self documentPath] stringByAppendingPathComponent:dirName];

  if ([[NSFileManager defaultManager] createDirectoryAtPath:dirPath withIntermediateDirectories:YES attributes:nil error:nil])
    return dirPath;

  return nil;
}

/**
* Return the file name with time as path
 */
- (NSString *)mediaFileName {
  NSDate *date = [NSDate date];
  NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
  [dateFormatter setDateFormat:@"dd_MM_yyyy_HH_mm_ss"];
  NSString *fileName = [NSString stringWithFormat:@"%@", [dateFormatter stringFromDate:date]];
  return fileName;
}

@end

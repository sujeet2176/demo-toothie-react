//
//  AppUtility.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 25/08/20.
//

#import "AppUtility.h"

@implementation AppUtility

#define IMAGE_DIRECTORY_NAME @"Images"
#define Video_DIRECTORY_NAME @"Videos"

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

/**
* Return to Document path
*
* @return Document path
*/
+ (NSString *)documentPath {
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    return [paths firstObject];
}

/**
* Return the directory name with today's date as the path
* Automatically create a directory, successfully return the directory name, unsuccessfully return nil
*/
+ (NSString *)mediaImagesDirPath {
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
+ (NSString *)mediaVideoDirPath {
  NSString *dirName = Video_DIRECTORY_NAME;
  NSString *dirPath = [[self documentPath] stringByAppendingPathComponent:dirName];

  if ([[NSFileManager defaultManager] createDirectoryAtPath:dirPath withIntermediateDirectories:YES attributes:nil error:nil])
    return dirPath;

  return dirPath;
}

+(NSString *)videoFileName {
  NSString *fileName = [NSString stringWithFormat:@"%@.mp4", [self mediaFileName]];
  return fileName;
}

+(NSString *)imageFileName {
  NSString *fileName = [NSString stringWithFormat:@"%@.jpg", [self mediaFileName]];
  return fileName;
}

/**
* Return the file name with time as path
 */
+ (NSString *)mediaFileName {
  NSDate *date = [NSDate date];
  NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
  [dateFormatter setDateFormat:@"yyyyMMdd_HHmmssS"];
  NSString *fileName = [dateFormatter stringFromDate:date];
  return fileName;
}

+ (NSURL *)videoFileURL
{
    NSString *path              = [NSString stringWithFormat:@"%@/%@", [self mediaVideoDirPath], [self videoFileName]];
    NSFileManager *fileManager  = [NSFileManager defaultManager];

    if([fileManager fileExistsAtPath:path])
        [fileManager removeItemAtPath:path error:nil];

    NSLog(@"OUTPUT: %@", path);
    NSURL *url = [NSURL fileURLWithPath:path];
    NSLog(@"OUTPUT URL: %@", [url absoluteString]);
    return url;
}


@end

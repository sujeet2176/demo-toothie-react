//
//  ShareValue.h
//  IJKMediaFramework
//
//  Created by dadousmart on 2019/1/5.
//  Copyright © 2019年 bilibili. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface ShareValue : NSObject

+(ShareValue*)shareInstance;

//第一次登陆
@property (nonatomic, assign) BOOL firstlaunch;

//前面的角度
@property (nonatomic, assign) NSInteger firstangle;

//后来的角度
@property (nonatomic, assign) NSInteger lastangle;

@property (nonatomic, assign) BOOL isaienable;

@property (nonatomic, assign) NSInteger angleshow;

@property (nonatomic, assign) NSInteger type;//为牙镜添加  lgh

@end

NS_ASSUME_NONNULL_END

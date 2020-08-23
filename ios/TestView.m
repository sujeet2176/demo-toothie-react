//
//  TestView.m
//  demoToothie
//
//  Created by Sujeet Shrivastav on 23/08/20.
//

#import "TestView.h"

@implementation TestView

UILabel *test;

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

- (void)awakeFromNib {
  [super awakeFromNib];

  //show video

  
}

- (void)layoutSubviews {
  [super layoutSubviews];

  test = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 200, 50)];
  test.text = @"Test Lable";
  [self addSubview:test];
}


@end

//
//  ViewController.m
//  webviewx-example
//
//  Created by Wiki on 2021/7/17.
//

#import "ViewController.h"
#import <WebKit/WebKit.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    WKWebView * _webView = [[WKWebView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT) configuration:config];
    
    // Do any additional setup after loading the view.
}


@end

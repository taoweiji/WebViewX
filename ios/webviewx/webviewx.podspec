Pod::Spec.new do |s|
  s.name             = 'WebViewX'
  s.version          = '0.0.1'
  s.summary          = 'WebViewX library'
  s.description      = 'WebViewX library'
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'taoweiji2008@qq.com' }
  s.source           = { :path => '.' }
  # 设置源文件，切记不要把测试代码包含进来
  s.source_files = 'Classes/**/*'
  # 暴露头文件，否则引用该spec的项目无法找到头文件
  s.public_header_files = 'Classes/**/*.h'
  s.platform = :ios, '8.0'
end

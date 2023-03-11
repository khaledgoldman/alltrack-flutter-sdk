Pod::Spec.new do |s|
  s.name                  = 'alltrack_sdk'
  s.version               = '4.33.1'
  s.summary               = 'Alltrack Flutter SDK for iOS platform'
  s.description           = <<-DESC
                            Alltrack Flutter SDK for iOS platform.
                            DESC
  s.homepage              = 'http://www.alltrack.com'
  s.license               = { :file => '../LICENSE' }
  s.author                = { 'Alltrack' => 'sdk@alltrack.com' }
  s.source                = { :path => '.' }
  s.source_files          = 'Classes/**/*'
  s.public_header_files   = 'Classes/**/*.h'
  s.ios.deployment_target = '8.0'

  s.dependency 'Flutter'
  s.dependency 'Alltrack', '0.0.1'
end

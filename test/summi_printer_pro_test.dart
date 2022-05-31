import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:sunmi_printer_pro/sunmi_printer_pro.dart';

void main() {
  const MethodChannel channel = MethodChannel('sunmi_printer_pro');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await SunmiPrinterPro.platformVersion, '42');
  });
}

# sunmi_printer_pro

# This is a fork from [sunmi_printer](https://pub.dev/packages/sunmi_printer) and ideal [sunmi_printer_pro](https://pub.dev/packages/sunmi_printer_pro), but i implemented a lot of other features described below

## Important: 
  **THIS PACKAGE WILL WORK ONLY IN ANDROID!**

Support Sunmi and Null Safety.
I build this flutter plugin based on this:
[Official Sunmi Inner Printer Doc](http://sunmi-ota.oss-cn-hangzhou.aliyuncs.com/DOC/resource/re_cn/SunmiPrinterPro%E5%BC%80%E5%8F%91%E8%80%85%E6%96%87%E6%A1%A31.1.191128.pdf). But not all method from doc was included in this package, beacuse i don't have equipment. If you have and can help me, just contact me on github!

## Installation  

```bash
flutter pub add sunmi_printer_pro
```

## What this package do
- [x] Write some text (with style or not!)
- [x] Change font size
- [x] Jump (n) lines
- [x] Draw a divisor line
- [x] Bold mode on/off
- [x] Underline mode on/off
- [x] Print Qrcodes with custom width and error-level
- [x] Print image from asset or from web (example show how to print both)
- [x] Print rows like recepit with custom width and alignment
- [x] Able to combine with some esc/pos code that you already have!
- [x] Cut paper - Dedicated method just to cut the line
- [x] Printer serial no - Get the serial number of the printer
- [x] Printer version - Get the printer's version
- [x] Printer paper size - Get the paper size ( 0: 80mm 1: 58mm)


## Tested Devices
sunmi_printer_proSunmiPrinterPro
```bash
Sunmi V2 Pro 
Sunmi T2 mini
IPos B08
```



```dart
// import packages
import 'package:sunmi_printer_pro/sunmi_printer_pro.dart';


// all method from sunmi printer need to async await
await SunmiPrinterProPro.bindingPrinter(); // must bind the printer first. for more exmaple.. pls refer to example tab.

```
## Example code when use for transaction printing

```dart
  await SunmiPrinterPro.startTransactionPrint(true);

  // Right align
  await SunmiPrinterPro.printText('Align right',sunmiPrintAlign: SunmiPrintAlign.right);

  // Left align
  await SunmiPrinterPro.printText('Align left',sunmiPrintAlign: SunmiPrintAlign.left);

  // Center align
  await SunmiPrinterPro.printText('Align center',sunmiPrintAlign: SunmiPrintAlign.center,);

  await SunmiPrinterPro.lineWrap(2); // Jump 2 lines

  // Set font to very large
  await SunmiPrinterPro.printText('Very Large font!',fontSize: 36);

  await SunmiPrinterPro.printQRCode('https://github.com/HVLoc/sunmi_printer_pro'); // PRINT A QRCODE
  await SunmiPrinterPro.submitTransactionPrint(); // SUBMIT and cut paper
  await SunmiPrinterPro.exitTransactionPrint(true); // Close the transaction

```



### List of enum Alignments
```dart
enum SunmiPrintAlign {
  left,
  center,
  right,
}
```

### List of enum Qrcode levels
```dart
enum SunmiQrcodeLevel {
  LEVEL_L,
  LEVEL_M,
  LEVEL_Q,
  LEVEL_H,
}
```

### List of enum Barcode types
```dart
enum SunmiBarcodeType {
  UPCA,
  UPCE,
  JAN13,
  JAN8,
  CODE39,
  ITF,
  CODABAR,
  CODE93,
  CODE128,
}
```


### List of enum Text position in barcode
```dart
enum SunmiBarcodeTextPos {
  NO_TEXT,
  TEXT_ABOVE,
  TEXT_UNDER,
  BOTH,
}
```

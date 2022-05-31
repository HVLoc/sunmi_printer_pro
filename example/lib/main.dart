import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sunmi_printer_pro/column_maker.dart';

import 'dart:async';

import 'package:sunmi_printer_pro/sunmi_printer_pro.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await SystemChrome.setPreferredOrientations(
      [DeviceOrientation.landscapeRight, DeviceOrientation.landscapeRight]);
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Sunmi Printer Pro',
        theme: ThemeData(
          primaryColor: Colors.black,
        ),
        debugShowCheckedModeBanner: false,
        home: const Home());
  }
}

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  bool printBinded = false;
  int paperSize = 0;
  String serialNumber = "";
  String printerVersion = "";
  @override
  void initState() {
    super.initState();

    _bindingPrinter().then((bool? isBind) async {
      SunmiPrinterPro.paperSize().then((int size) {
        setState(() {
          paperSize = size;
        });
      });

      SunmiPrinterPro.printerVersion().then((String version) {
        setState(() {
          printerVersion = version;
        });
      });

      SunmiPrinterPro.serialNumber().then((String serial) {
        setState(() {
          serialNumber = serial;
        });
      });

      setState(() {
        printBinded = isBind!;
      });
    });
  }

  /// must binding ur printer at first init in app
  Future<bool?> _bindingPrinter() async {
    final bool? result = await SunmiPrinterPro.bindingPrinter();
    return result;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Sunmi printer Example'),
        ),
        body: SingleChildScrollView(
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.only(
                  top: 10,
                ),
                child: Text("Print binded: " + printBinded.toString()),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 2.0),
                child: Text("Paper size: " + paperSize.toString()),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 2.0),
                child: Text("Serial number: " + serialNumber),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 2.0),
                child: Text("Printer version: " + printerVersion),
              ),
              const Divider(),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printQRCode(
                              'https://github.com/HVLoc/sunmi_printer_pro');
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Print qrCode')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printBarCode('1234567890',
                              barcodeType: SunmiBarcodeType.CODE128,
                              textPosition: SunmiBarcodeTextPos.TEXT_UNDER,
                              height: 20);
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Print barCode')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.line();
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Print line')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.lineWrap(2);
                        },
                        child: const Text('Wrap line')),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printText(
                            'Hello I\'m bold',
                            isBold: true,
                          );
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Bold Text')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printText('Very small!',
                              fontSize: 36);
                          await SunmiPrinterPro.lineWrap(2);

                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Very small font')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printText('Very small!',
                              fontSize: 18);
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Small font')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printText('Normal font',
                              fontSize: 24);

                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Normal font')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.printText('Large font',
                              fontSize: 36);

                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Large font')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);

                          await SunmiPrinterPro.printText('Very Large font!');
                          await SunmiPrinterPro.resetFontSize();
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Very large font')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.setCustomFontSize(13);
                          await SunmiPrinterPro.printText('Very Large font!');
                          await SunmiPrinterPro.resetFontSize();
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Custom size font')),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();
                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printText('Align right',
                              sunmiPrintAlign: SunmiPrintAlign.right);
                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Align right')),
                    ElevatedButton(
                        onPressed: () async {
                          await SunmiPrinterPro.initPrinter();

                          await SunmiPrinterPro.startTransactionPrint(true);
                          await SunmiPrinterPro.printText('Align left',
                              sunmiPrintAlign: SunmiPrintAlign.left);

                          await SunmiPrinterPro.lineWrap(2);
                          await SunmiPrinterPro.exitTransactionPrint(true);
                        },
                        child: const Text('Align left')),
                    ElevatedButton(
                      onPressed: () async {
                        await SunmiPrinterPro.initPrinter();

                        await SunmiPrinterPro.startTransactionPrint(true);
                        await SunmiPrinterPro.printText(
                          'Align center/ LARGE TEXT AND BOLD',
                          sunmiPrintAlign: SunmiPrintAlign.center,
                          isBold: true,
                          fontSize: 36,
                        );

                        await SunmiPrinterPro.lineWrap(2);
                        await SunmiPrinterPro.exitTransactionPrint(true);
                      },
                      child: const Text('Align center'),
                    ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    GestureDetector(
                      onTap: () async {
                        await SunmiPrinterPro.initPrinter();

                        Uint8List byte =
                            await _getImageFromAsset('assets/images/dash.jpeg');
                        await SunmiPrinterPro.setAlignment(
                            SunmiPrintAlign.center);

                        await SunmiPrinterPro.startTransactionPrint(true);
                        await SunmiPrinterPro.printImage(byte);
                        await SunmiPrinterPro.lineWrap(2);
                        await SunmiPrinterPro.exitTransactionPrint(true);
                      },
                      child: Column(
                        children: [
                          Image.asset(
                            'assets/images/dash.jpeg',
                            width: 100,
                          ),
                          const Text('Print this image from asset!')
                        ],
                      ),
                    ),
                    GestureDetector(
                      onTap: () async {
                        await SunmiPrinterPro.initPrinter();

                        String url =
                            'https://avatars.githubusercontent.com/u/14101776?s=100';
                        // convert image to Uint8List format
                        Uint8List byte =
                            (await NetworkAssetBundle(Uri.parse(url)).load(url))
                                .buffer
                                .asUint8List();
                        await SunmiPrinterPro.setAlignment(
                            SunmiPrintAlign.center);
                        await SunmiPrinterPro.startTransactionPrint(true);
                        await SunmiPrinterPro.printImage(byte);
                        await SunmiPrinterPro.lineWrap(2);
                        await SunmiPrinterPro.exitTransactionPrint(true);
                      },
                      child: Column(
                        children: [
                          Image.network(
                              'https://avatars.githubusercontent.com/u/14101776?s=100'),
                          const Text('Print this image from WEB!')
                        ],
                      ),
                    ),
                  ],
                ),
              ),
              const Divider(),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                          onPressed: () async {
                            await SunmiPrinterPro.cut();
                          },
                          child: const Text('CUT PAPER')),
                    ]),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20),
                child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      ElevatedButton(
                          onPressed: () async {
                            await SunmiPrinterPro.initPrinter();

                            await SunmiPrinterPro.startTransactionPrint(true);
                            await SunmiPrinterPro.setAlignment(
                                SunmiPrintAlign.center);
                            await SunmiPrinterPro.line();
                            await SunmiPrinterPro.printText('Payment receipt');
                            await SunmiPrinterPro.printText(
                                'Using the old way to bold!');
                            await SunmiPrinterPro.line();

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'Name',
                                  width: 12,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: 'Qty',
                                  width: 6,
                                  align: SunmiPrintAlign.center),
                              ColumnMaker(
                                  text: 'UN',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                              ColumnMaker(
                                  text: 'TOT',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'Fries',
                                  width: 12,
                                  align: SunmiPrintAlign.center),
                              ColumnMaker(
                                  text: '4x',
                                  width: 6,
                                  align: SunmiPrintAlign.center),
                              ColumnMaker(
                                  text: '3.00',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                              ColumnMaker(
                                  text: '12.00',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'Strawberry',
                                  width: 12,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: '1x',
                                  width: 6,
                                  align: SunmiPrintAlign.center),
                              ColumnMaker(
                                  text: '24.44',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                              ColumnMaker(
                                  text: '24.44',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'Soda',
                                  width: 12,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: '1x',
                                  width: 6,
                                  align: SunmiPrintAlign.center),
                              ColumnMaker(
                                  text: '1.99',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                              ColumnMaker(
                                  text: '1.99',
                                  width: 6,
                                  align: SunmiPrintAlign.right),
                            ]);

                            await SunmiPrinterPro.line();
                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'TOTAL',
                                  width: 25,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: '38.43',
                                  width: 5,
                                  align: SunmiPrintAlign.right),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'ARABIC TEXT',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: 'اسم المشترك',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'اسم المشترك',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: 'اسم المشترك',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'RUSSIAN TEXT',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: 'Санкт-Петербу́рг',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                            ]);
                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'Санкт-Петербу́рг',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: 'Санкт-Петербу́рг',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                            ]);

                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: 'CHINESE TEXT',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: '風俗通義',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                            ]);
                            await SunmiPrinterPro.printRow(cols: [
                              ColumnMaker(
                                  text: '風俗通義',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                              ColumnMaker(
                                  text: '風俗通義',
                                  width: 15,
                                  align: SunmiPrintAlign.left),
                            ]);

                            await SunmiPrinterPro.setAlignment(
                                SunmiPrintAlign.center);
                            await SunmiPrinterPro.line();
                            await SunmiPrinterPro.bold();
                            await SunmiPrinterPro.printText(
                                'Transaction\'s Qrcode');
                            await SunmiPrinterPro.resetBold();
                            await SunmiPrinterPro.printQRCode(
                                'https://github.com/brasizza/sunmi_printer');
                            await SunmiPrinterPro.lineWrap(2);
                            await SunmiPrinterPro.exitTransactionPrint(true);
                          },
                          child: const Text('TICKET EXAMPLE')),
                    ]),
              ),
            ],
          ),
        ));
  }
}

Future<Uint8List> readFileBytes(String path) async {
  ByteData fileData = await rootBundle.load(path);
  Uint8List fileUnit8List = fileData.buffer
      .asUint8List(fileData.offsetInBytes, fileData.lengthInBytes);
  return fileUnit8List;
}

Future<Uint8List> _getImageFromAsset(String iconPath) async {
  return await readFileBytes(iconPath);
}

package com.simplepos.sunmi_printer_pro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.simplepos.sunmi_printer_pro.utils.StarPOSPrintHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import io.flutter.Log;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "sunmi_printer_pro";

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            switch (call.method) {
                                case "BIND_PRINTER_SERVICE":
                                    StarPOSPrintHelper.getInstance().initStarPOSPrinterService(this);
                                    result.success(true);

                                    break;
                                case "UNBIND_PRINTER_SERVICE":
                                    StarPOSPrintHelper.getInstance().deInitStarPOSPrinterService(this);
                                    result.success(true);

                                    break;
                                case "INIT_PRINTER":
                                    // ICallback callback = call.argument("callback");
                                    StarPOSPrintHelper.getInstance().initPrinter();
                                    result.success(true);
                                    break;
                                case "GET_UPDATE_PRINTER":
                                    final int status_code = StarPOSPrintHelper.getInstance().updatePrinter();

                                    String status_msg = "";

                                    // response printer status
                                    switch (status_code) {
                                        case 0:
                                            status_msg = "ERROR";
                                            break;
                                        case 1:
                                            status_msg = "NORMAL";
                                            break;
                                        case 2:
                                            status_msg = "ABNORMAL_COMMUNICATION";
                                            break;
                                        case 3:
                                            status_msg = "OUT_OF_PAPER";
                                            break;
                                        case 4:
                                            status_msg = "PREPARING";
                                            break;
                                        case 5:
                                            status_msg = "OVERHEATED";
                                            break;
                                        case 6:
                                            status_msg = "OPEN_THE_LID";
                                            break;
                                        case 7:
                                            status_msg = "PAPER_CUTTER_ABNORMAL";
                                            break;
                                        case 8:
                                            status_msg = "PAPER_CUTTER_RECOVERED";
                                            break;
                                        case 9:
                                            status_msg = "NO_BLACK_MARK";
                                            break;
                                        case 505:
                                            status_msg = "NO_PRINTER_DETECTED";
                                            break;
                                        case 507:
                                            status_msg = "FAILED_TO_UPGRADE_FIRMWARE";
                                            break;
                                        default:
                                            status_msg = "EXCEPTION";
                                    }

                                    result.success(status_msg);
                                    break;
                                case "PRINT_TEXT":
                                    String text = call.argument("text");
                                    double temp = call.argument("fontSize") ;
                                    float size = (float) temp;
                                    boolean isBold = call.argument("isBold");
                                    boolean isUnderLine = call.argument("isUnderLine");

                                    StarPOSPrintHelper.getInstance().printText(text, size, isBold, isUnderLine);
                                    result.success(true);

                                    break;
                                case "RAW_DATA":
                                    StarPOSPrintHelper.getInstance().sendRawData((byte[]) call.argument("data"));
                                    result.success(true);
                                    break;
                                case "PRINT_QRCODE":
                                    String data = call.argument("data");
                                    int modulesize = call.argument("modulesize");
                                    int errorlevel = call.argument("errorlevel");
                                    StarPOSPrintHelper.getInstance().printQr(data, modulesize, errorlevel);
                                    result.success(true);
                                    break;
                                case "PRINT_BARCODE":
                                    String barCodeData = call.argument("data");
                                    int barcodeType = call.argument("barcodeType");
                                    int textPosition = call.argument("textPosition");
                                    int width = call.argument("width");
                                    int height = call.argument("height");
                                    StarPOSPrintHelper.getInstance().printBarCode(
                                            barCodeData,
                                            barcodeType,
                                            textPosition,
                                            width,
                                            height
                                    );
                                    StarPOSPrintHelper.getInstance().lineWrap(1);

                                    result.success(true);
                                    break;
                                // void printBarCode(String data, int symbology, int height, int width, int textposition,  in ICallback callback);

                                case "LINE_WRAP":
                                    int lines = call.argument("lines");
                                    StarPOSPrintHelper.getInstance().lineWrap(lines);
                                    result.success(true);
                                    break;
//                                case "FONT_SIZE":
//                                    int fontSize = call.argument("size");
//                                    StarPOSPrintHelper.getInstance().setFontSize(fontSize);
//                                    result.success(true);
//                                    break;
                                case "SET_ALIGNMENT":
                                    int alignment = call.argument("alignment");
                                    StarPOSPrintHelper.getInstance().setAlignment(alignment);
                                    result.success(true);
                                    break;
                                case "PRINT_IMAGE":
                                    byte[] bytes = call.argument("bitmap");
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    StarPOSPrintHelper.getInstance().printBitmap(bitmap, 0);
                                    result.success(true);

                                    break;
                                case "GET_PRINTER_MODE":
                                    final int mode_code = StarPOSPrintHelper.getInstance().getPrinterMode();

                                    String mode_desc = "";

                                    // response printer status
                                    switch (mode_code) {
                                        case 0:
                                            mode_desc = "NORMAL_MODE";
                                            break;
                                        case 1:
                                            mode_desc = "BLACK_LABEL_MODE";
                                            break;
                                        case 2:
                                            mode_desc = "LABEL_MODE";
                                            break;
                                        case 3:
                                            mode_desc = "ERROR";
                                            break;
                                        default:
                                            mode_desc = "EXCEPTION";
                                    }

                                    result.success(mode_desc);
                                    break;
                                // case "LABEL_LOCATE":
                                //   StarPOSPrintHelper.getInstance().labelLocate();
                                //   result.success(true);
                                //   break;
                                // case "LABEL_OUTPUT":
                                //   StarPOSPrintHelper.getInstance().labelOutput();
                                //   result.success(true);
                                //   break;
                                case "ENTER_PRINTER_BUFFER":
                                    Boolean clearEnter = call.argument("clearEnter");
                                    StarPOSPrintHelper.getInstance().printTrans(clearEnter);
                                    result.success(true);

                                    break;
//                                case "COMMIT_PRINTER_BUFFER":
//                                    StarPOSPrintHelper.getInstance().commitPrinterBuffer();
//                                    result.success(true);
//                                    break;
                                case "CUT_PAPER":
                                    StarPOSPrintHelper.getInstance().cutpaper();
                                    result.success(true);
                                    break;
                                case "OPEN_DRAWER":
                                    StarPOSPrintHelper.getInstance().openDrawer();
                                    result.success(true);
                                    break;

//                                case "DRAWER_OPENED":
//                                    result.success(StarPOSPrintHelper.getInstance().timesOpened());
//                                    break;
//
//                                case "DRAWER_STATUS":
//                                    result.success(StarPOSPrintHelper.getInstance().drawerStatus());
//                                    break;
                                case "PRINT_ROW":
                                    String colsStr = call.argument("cols");

                                    try {
                                        JSONArray cols = new JSONArray(colsStr);
                                        String[] colsText = new String[cols.length()];
                                        int[] colsWidth = new int[cols.length()];
                                        int[] colsAlign = new int[cols.length()];
                                        for (int i = 0; i < cols.length(); i++) {
                                            JSONObject col = cols.getJSONObject(i);
                                            String textColumn = col.getString("text");
                                            int widthColumn = col.getInt("width");
                                            int alignColumn = col.getInt("align");
                                            colsText[i] = textColumn;
                                            colsWidth[i] = widthColumn;
                                            colsAlign[i] = alignColumn;
                                        }

                                        StarPOSPrintHelper.getInstance().printColumn(colsText, colsWidth, colsAlign);
                                        result.success(true);
                                    } catch (Exception err) {
                                        Log.d("SunmiPrinter", err.getMessage());
                                    }
                                    break;
                                case "EXIT_PRINTER_BUFFER":
                                    Boolean clearExit = call.argument("clearExit");
                                    StarPOSPrintHelper.getInstance().exitPrinterBuffer(clearExit);
                                    result.success(true);
                                    break;
                                case "PRINTER_SERIAL_NUMBER":
                                    final String serial = StarPOSPrintHelper.getInstance().getPrinterSerialNo();
                                    result.success(serial);
                                    break;
                                case "PRINTER_VERSION":
                                    final String printer_verison = StarPOSPrintHelper.getInstance().getPrinterVersion();
                                    result.success(printer_verison);
                                    break;
                                case "PAPER_SIZE":
                                    final String paper = StarPOSPrintHelper.getInstance().getPrinterPaper();
                                    result.success(paper);
                                    break;

                                // LCD METHODS
                                case "LCD_COMMAND":
                                    int flag = call.argument("flag");
                                    StarPOSPrintHelper.getInstance().sendLCDCommand(flag);
                                    result.success(true);
                                    break;
//                                case "LCD_STRING":
//                                    String lcdString = call.argument("string");
//                                    StarPOSPrintHelper.getInstance().sendLCDString(lcdString);
//                                    result.success(true);
//                                    break;
                                case "LCD_BITMAP":
                                    byte[] lcdBitmapData = call.argument("bitmap");
                                    Bitmap lcdBitmap = BitmapFactory.decodeByteArray(
                                            lcdBitmapData, 0, lcdBitmapData.length);
                                    StarPOSPrintHelper.getInstance().sendLCDBitmap(lcdBitmap);
                                    result.success(true);
                                    break;
//                                case "LCD_DOUBLE_STRING":
//                                    String topText = call.argument("topText");
//                                    String bottomText = call.argument("bottomText");
//                                    StarPOSPrintHelper.getInstance().sendLCDDoubleString(topText, bottomText);
//                                    result.success(true);
//                                    break;
                                case "LCD_FILL_STRING":
                                    String lcdFillString = call.argument("string");
                                    int lcdFillSize = call.argument("size");
                                    boolean lcdFill = call.argument("fill");
                                    StarPOSPrintHelper.getInstance().sendLCDFillString(lcdFillString, lcdFillSize, lcdFill);
                                    result.success(true);
                                    break;
//                                case "LCD_MULTI_STRING":
//                                    ArrayList<String> lcdTextAL = call.argument("text");
//                                    String[] lcdText = Utilities.arrayListToString(lcdTextAL);
//                                    ArrayList<Integer> lcdAlignAL = call.argument("align");
//                                    int[] lcdAlign = Utilities.arrayListToIntList(lcdAlignAL);
//                                    StarPOSPrintHelper.getInstance().sendLCDMultiString(lcdText, lcdAlign);
//                                    result.success(true);
//                                    break;

                                default:
                                    result.notImplemented();
                                    break;
                            }
                        }
                );
    }
}

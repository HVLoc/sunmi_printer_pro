package com.simplepos.sunmi_printer_pro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

//import com.starpos.printerhelper.R;
import com.sunmi.peripheral.printer.ExceptionConst;
import com.sunmi.peripheral.printer.InnerLcdCallback;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallback;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.WoyouConsts;

/**
 * <pre>
 *      This class is used to demonstrate various printing effects
 *      Developers need to repackage themselves, for details please refer to
 *      http://sunmi-ota.oss-cn-hangzhou.aliyuncs.com/DOC/resource/re_cn/Sunmiprinter%E5%BC%80%E5%8F%91%E8%80%85%E6%96%87%E6%A1%A31.1.191128.pdf
 * </pre>
 *
 * @author kaltin
 * @since create at 2020-02-14
 */
public class StarPOSPrintHelper {

    private final String TAG = StarPOSPrintHelper.class.getName();

    public static int NoStarPOSPrinter = 0x00000000;
    public static int CheckStarPOSPrinter = 0x00000001;
    public static int FoundStarPOSPrinter = 0x00000002;
    public static int LostStarPOSPrinter = 0x00000003;

    /**
     * starPOS means checking the printer connection status
     */
    public int starPOSPrinter = CheckStarPOSPrinter;
    /**
     */
    private SunmiPrinterService mPrinterService;

    private static StarPOSPrintHelper helper = new StarPOSPrintHelper();

    private StarPOSPrintHelper() {
    }

    public static StarPOSPrintHelper getInstance() {
        return helper;
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            Log.i(TAG, "== onConnected printer service");
            mPrinterService = service;
            checkStarPOSPrinterService(service);
        }

        @Override
        protected void onDisconnected() {
            Log.i(TAG, "== onDisconnected printer service");
            mPrinterService = null;
            starPOSPrinter = LostStarPOSPrinter;
        }
    };

    /**
     * init sunmi print service
     */
    public void initStarPOSPrinterService(Context context) {
        Log.i("Printer", "== initPrinterService");
        try {
            boolean ret = InnerPrinterManager.getInstance().bindService(context,
                    innerPrinterCallback);
            Log.i("Printer", "== ret = " + ret);
            if (!ret) {
                starPOSPrinter = NoStarPOSPrinter;
            }
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }

    /**
     * deInit starPOS print service
     */
    public void deInitStarPOSPrinterService(Context context) {
        Log.i("Printer", "== deInitPrinterService == ");
        try {
            if (mPrinterService != null) {
                InnerPrinterManager.getInstance().unBindService(context, innerPrinterCallback);
                mPrinterService = null;
                starPOSPrinter = NoStarPOSPrinter;
            }
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check the printer connection,
     * like some devices do not have a printer but need to be connected to the cash
     * drawer through a print service
     */
    private void checkStarPOSPrinterService(SunmiPrinterService service) {
        boolean ret = false;
        try {
            ret = InnerPrinterManager.getInstance().hasPrinter(service);
            Log.i("PrinterHelp", "== checkStarPOSPrinterService = " + ret);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
        starPOSPrinter = ret ? FoundStarPOSPrinter : NoStarPOSPrinter;
    }

    /**
     * Some conditions can cause interface calls to fail
     * For example: the version is too low、device does not support
     * You can see {@link ExceptionConst}
     * So you have to handle these exceptions
     */
    private void handleRemoteException(RemoteException e) {
        // TODO process when get one exception
    }

    /**
     * send esc cmd
     */
    public void sendRawData(byte[] data) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            mPrinterService.sendRAWData(data, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Printer cuts paper and throws exception on machines without a cutter
     */
    public void cutpaper() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            mPrinterService.cutPaper(null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Initialize the printer
     * All style settings will be restored to default
     */
    public void initPrinter() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            Log.i("PrinterHelper", "== Init Printer ==");
            mPrinterService.printerInit(null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * paper feed three lines
     * Not disabled when line spacing is set to 0
     */
    public void print3Line() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.lineWrap(3, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    public void lineWrap(int line) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.lineWrap(line, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Get printer serial number
     */
    public String getPrinterSerialNo() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return "";
        }
        try {
            return mPrinterService.getPrinterSerialNo();
        } catch (RemoteException e) {
            handleRemoteException(e);
            return "";
        }
    }

    /**
     * Get device model
     */
    public String getDeviceModel() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return "";
        }
        try {
            return mPrinterService.getPrinterModal();
        } catch (RemoteException e) {
            handleRemoteException(e);
            return "";
        }
    }

    /**
     * Get firmware version
     */
    public String getPrinterVersion() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return "";
        }
        try {
            return mPrinterService.getPrinterVersion();
        } catch (RemoteException e) {
            handleRemoteException(e);
            return "";
        }
    }

    /**
     * Get paper specifications
     */
    public String getPrinterPaper() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return "";
        }
        try {
            return mPrinterService.getPrinterPaper() == 1 ? "58mm" : "80mm";
        } catch (RemoteException e) {
            handleRemoteException(e);
            return "";
        }
    }

    /**
     * Get paper specifications
     */
    public void getPrinterHead(InnerResultCallback callbcak) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            mPrinterService.getPrinterFactory(callbcak);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Get printing distance since boot
     * Get printing distance through interface callback since 1.0.8(printerlibrary)
     */
//    public void getPrinterDistance(InnerResultCallback callback) {
//        if (mPrinterService == null) {
//            // TODO Service disconnection processing
//            return;
//        }
//        try {
//            mPrinterService.getPrintedLength(callback);
//        } catch (RemoteException e) {
//            handleRemoteException(e);
//        }
//    }

    /**
     * Set printer alignment
     */
    public void setAlign(int align) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            mPrinterService.setAlignment(align, null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Due to the distance between the paper hatch and the print head,
     * the paper needs to be fed out automatically
     * But if the Api does not support it, it will be replaced by printing three
     * lines
     */
    public void feedPaper() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.autoOutPaper(null);
        } catch (RemoteException e) {
            print3Line();
        }
    }

    /**
     * print text
     * setPrinterStyle Api require V4.2.22 or later, So use esc cmd instead when not
     * supported
     * More settings reference documentation {@link WoyouConsts}
     */
    public void printText(String content, float size, boolean isBold, boolean isUnderLine) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            try {
                mPrinterService.setPrinterStyle(WoyouConsts.ENABLE_BOLD,
                        isBold ? WoyouConsts.ENABLE : WoyouConsts.DISABLE);
            } catch (RemoteException e) {
                if (isBold) {
                    mPrinterService.sendRAWData(ESCUtil.boldOn(), null);
                } else {
                    mPrinterService.sendRAWData(ESCUtil.boldOff(), null);
                }
            }
            try {
                mPrinterService.setPrinterStyle(WoyouConsts.ENABLE_UNDERLINE,
                        isUnderLine ? WoyouConsts.ENABLE : WoyouConsts.DISABLE);
            } catch (RemoteException e) {
                if (isUnderLine) {
                    mPrinterService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
                } else {
                    mPrinterService.sendRAWData(ESCUtil.underlineOff(), null);
                }
            }
            mPrinterService.printTextWithFont(content, null, size, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * print Bar Code
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.printBarCode(data, symbology, height, width, textposition, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * print Qr Code
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.printQRCode(data, modulesize, errorlevel, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print a row of a table
     */
    public void printTable(String[] txts, int[] width, int[] align) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.printColumnsString(txts, width, align, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print pictures and text in the specified orde
     * After the picture is printed,
     * the line feed output needs to be called,
     * otherwise it will be saved in the cache
     * In this example, the image will be printed because the print text content is
     * added
     */
    public void printBitmap(Bitmap bitmap, int orientation) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            if (orientation == 0) {
                mPrinterService.printBitmap(bitmap, null);
                // sunmiPrinterService.printText("横向排列\n", null);
                // sunmiPrinterService.printBitmap(bitmap, null);
                // sunmiPrinterService.printText("横向排列\n", null);
            } else {
                mPrinterService.printBitmap(bitmap, null);
                // sunmiPrinterService.printText("\n纵向排列\n", null);
                // sunmiPrinterService.printBitmap(bitmap, null);
                // sunmiPrinterService.printText("\n纵向排列\n", null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getPrinterMode() {
        try {
            final int mode = mPrinterService.getPrinterMode();
            return mode;
        } catch (RemoteException e) {
            return 3; // error;
        } catch (NullPointerException e) {
            return 3;
        }
    }

    /**
     * Gets whether the current printer is in black mark mode
     */
    public boolean isBlackLabelMode() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return false;
        }
        try {
            return mPrinterService.getPrinterMode() == 1;
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     * Gets whether the current printer is in label-printing mode
     */
    public boolean isLabelMode() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return false;
        }
        try {
            return mPrinterService.getPrinterMode() == 2;
        } catch (RemoteException e) {
            return false;
        }
    }

    /**
     *  Transaction printing:
     *  enter->print->exit(get result) or
     *  enter->first print->commit(get result)->twice print->commit(get result)->exit(don't care
     *  result)
     */
    public void printTrans(boolean isClear){
        if(mPrinterService == null){
            //TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.enterPrinterBuffer(isClear);

//            mPrinterService.exitPrinterBufferWithCallback(true,null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     */
    public void openDrawer() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.openDrawer(null);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    public Boolean printColumn(
            String[] stringColumns,
            int[] columnWidth,
            int[] columnAlignment
    ) {


        try {

            mPrinterService.printColumnsText(
                    stringColumns,
                    columnWidth,
                    columnAlignment,
                    null
            );

            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }


    public Boolean setAlignment(Integer alignment) {
        try {
            mPrinterService.setAlignment(alignment, null);
            return true;
        } catch (RemoteException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void exitPrinterBuffer(Boolean clear) {
        try {
            this.mPrinterService.exitPrinterBuffer(clear);
        } catch (RemoteException e) {
        } catch (NullPointerException e) {
        }
    }



    /**
     * LCD screen control
     * 
     * @param flag 1 —— Initialization
     *             2 —— Light up screen
     *             3 —— Extinguish screen
     *             4 —— Clear screen contents
     */
    public void sendLCDCommand(int flag) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.sendLCDCommand(flag);
        } catch (RemoteException e) {
            handleRemoteException(e);
        }
    }

    /**
     * Display text SUNMI,font size is 16 and format is fill
     * sendLCDFillString(txt, size, fill, callback)
     * Since the screen pixel height is 40, the font should not exceed 40
     */
    public void sendLCDFillString(String text, int size, boolean isFill) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.sendLCDFillString(text, size, isFill, new InnerLcdCallback() {
                @Override
                public void onRunResult(boolean show) throws RemoteException {
                    // TODO handle result
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display two lines and one empty line in the middle
     */
    public void sendTextsToLcd() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            String[] texts = { "STARPOS", null, "STARPOS" };
            int[] align = { 2, 1, 2 };
            mPrinterService.sendLCDMultiString(texts, align, new InnerLcdCallback() {
                @Override
                public void onRunResult(boolean show) throws RemoteException {
                    // TODO handle result
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Display one 128x40 pixels and opaque picture
     */
    public void sendLCDBitmap(Bitmap pic) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }

        try {
            mPrinterService.sendLCDBitmap(pic, new InnerLcdCallback() {
                @Override
                public void onRunResult(boolean show) throws RemoteException {
                    // TODO handle result
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * Used to report the real-time query status of the printer, which can be used
     * before each
     * printing
     */
    public int updatePrinter() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return 0;
        }

        try {
            final int status =  mPrinterService.updatePrinterState();
            return status;
        } catch (RemoteException e) {
            return 0; // error
        } catch (NullPointerException e) {
            return 0;
        }

    }

    /**
     * Demo printing a label
     * After printing one label, in order to facilitate the user to tear the paper,
     * call
     * labelOutput to push the label paper out of the paper hatch
     * 演示打印一张标签
     * 打印单张标签后为了方便用户撕纸可调用labelOutput,将标签纸推出纸舱口
     */
    public void printOneLabel() {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            mPrinterService.labelLocate();
            printLabelContent();
            mPrinterService.labelOutput();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Demo printing multi label
     *
     * After printing multiple labels, choose whether to push the label paper to the
     * paper hatch according to the needs
     * 演示打印多张标签
     * 打印多张标签后根据需求选择是否推出标签纸到纸舱口
     */
    public void printMultiLabel(int num) {
        if (mPrinterService == null) {
            // TODO Service disconnection processing
            return;
        }
        try {
            for (int i = 0; i < num; i++) {
                mPrinterService.labelLocate();
                printLabelContent();
            }
            mPrinterService.labelOutput();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Custom label ticket content
     * In the example, not all labels can be applied. In actual use, please pay
     * attention to adapting the size of the label. You can adjust the font size and
     * content position.
     * 自定义的标签小票内容
     * 例子中并不能适用所有标签纸，实际使用时注意要自适配标签纸大小，可通过调节字体大小，内容位置等方式
     */
    private void printLabelContent() throws RemoteException {
        mPrinterService.setPrinterStyle(WoyouConsts.ENABLE_BOLD, WoyouConsts.ENABLE);
        mPrinterService.lineWrap(1, null);
        mPrinterService.setAlignment(0, null);
        mPrinterService.printText("商品         豆浆\n", null);
        mPrinterService.printText("到期时间         12-13  14时\n", null);
        mPrinterService.printBarCode("{C1234567890123456", 8, 90, 2, 2, null);
        mPrinterService.lineWrap(1, null);
    }
}

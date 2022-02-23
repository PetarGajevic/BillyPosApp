package com.wind.billypos.utils.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wind.billypos.R;
import com.wind.billypos.utils.PreferenceHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnection {

    public static BluetoothConnection mInstance;
    private BluetoothAdapter mBluetoothAdapter;
    private AppCompatActivity appCompatActivity;
    private BluetoothDevice pairedDevice;
    private List<BluetoothDevice> discoveredDevices;
    private BluetoothSocket mSocket;
    private OutputStream mOutputStream;
    private PrinterCallback printerCallback;

    public static BluetoothConnection getInstance(AppCompatActivity appCompatActivity, PrinterCallback printerCallback) {
        if (mInstance == null) {
            mInstance = new BluetoothConnection(appCompatActivity);
            mInstance.setPairedDevice(PreferenceHelper.INSTANCE.getPairedPrinterAddress(PreferenceHelper.INSTANCE.customPreference(appCompatActivity)));
            mInstance.setPrinterCallback(printerCallback);
        }
        return mInstance;
    }

    public static BluetoothConnection getInstance(AppCompatActivity appCompatActivity) {
        return getInstance(appCompatActivity, new PrinterCallback() {
            @Override
            public void onPrintingStarted() {

            }

            @Override
            public void onPrintingFinish() {

            }
        });
    }

    public BluetoothConnection(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        this.discoveredDevices = new ArrayList<>();
    }

    public BluetoothDevice getPairedDevice(String address) {
        try {
            return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }


    private Boolean connect(BluetoothDevice bdDevice) {
        Boolean isConnected = false;
        try {
            Log.i("Log", "service method is called ");
            Class cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class[] par = {};
            Method method = cl.getMethod("createBond", par);
            Object[] args = {};
            isConnected = (Boolean) method.invoke(bdDevice);//, args);// this invoke creates the detected devices paired.
            //Log.i("Log", "This is: "+bool.booleanValue());
            //Log.i("Log", "devicesss: "+bdDevice.getName());
        } catch (Exception e) {
            Log.i("Log", "Inside catch of serviceFromDevice Method");
            e.printStackTrace();
        }
        return isConnected;
    }

    public void discoverBluetoothDevices() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                Toast.makeText(appCompatActivity.getApplicationContext(), "No bluetooth adapter available", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                appCompatActivity.startActivityForResult(enableBluetooth, 0);
                return;
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    discoveredDevices.add(device);
                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
//                    if (device.getName().equals("RPP300")) {
//                        pairedDevice = device;
//                        break;
//                    }
                }
            }

            //TODO device found here
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // tries to open a connection to the bluetooth printer device
    public void createConnection() throws IOException {

        try {
            if (mSocket != null) {
                return;
            }
            pairedDevice = getPairedDevice();
            if (pairedDevice == null) {
                //TODO User has not any paired device set up
                return;
            }
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mSocket = pairedDevice.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect();
            mOutputStream = mSocket.getOutputStream();

            //TODO bluetooth opened

        } catch (Exception e) {
            e.printStackTrace();
            mSocket = null;
            pairedDevice = null;
            mOutputStream = null;
        }
    }

    void sendData(String stringToWrite) {
        ArrayList<Object> textToPrint = new ArrayList<>();
        textToPrint.add(stringToWrite);
        sendData(textToPrint);
    }

    void sendData(ArrayList<Object> textToPrint) {
        try {
            createConnection();
            if (!mSocket.isConnected()) {
                mSocket.connect();
            }
            mOutputStream.flush();
            printerCallback.onPrintingStarted();

            resetPrint();

            byte[] printformat = new byte[]{0x1B, 0x21, 0x03};
            mOutputStream.write(printformat);

            for (Object txt : textToPrint) {
                if (txt instanceof String) {
                    mOutputStream.write(((String) txt).getBytes());
                } else if(txt != null) {
                    mOutputStream.write((byte[]) txt);
                }
            }

            mOutputStream.write(PrinterCommands.FEED_LINE);
            mOutputStream.write(PrinterCommands.FEED_LINE);
            mOutputStream.write(PrinterCommands.FEED_LINE);
            mOutputStream.write(PrinterCommands.FEED_LINE);
            mOutputStream.write(PrinterCommands.FEED_LINE);
            mOutputStream.flush();

            printerCallback.onPrintingFinish();

        } catch (IOException e) {
            appCompatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appCompatActivity.getApplicationContext(), appCompatActivity.getString(R.string.bluetooth_connection_error), Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
            mSocket = null;
            pairedDevice = null;
            mOutputStream = null;
        } catch (Exception e) {
            appCompatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appCompatActivity.getApplicationContext(), appCompatActivity.getString(R.string.bluetooth_connection_error), Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
            mSocket = null;
            pairedDevice = null;
            mOutputStream = null;
        }
    }

    public void resetPrint() {
        try {
            mOutputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            mOutputStream.write(PrinterCommands.FS_FONT_ALIGN);
            mOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            mOutputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            mOutputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testPrinter(String printerName) {
        sendData("\n\n\nTesting printer... \n"+printerName+"... OK\n\n\n");
    }

    public List<BluetoothDevice> getDiscoveredDevices() {
        return discoveredDevices;
    }

    public BluetoothDevice getPairedDevice() {
        return pairedDevice;
    }

    public void setPairedDevice(String address) {
        pairedDevice = getPairedDevice(address);
    }


//     Todo (Odraditi za print inovice)
//    public void printInvoice(@NotNull InvoiceTemplate invoice) {
//
//        sendData(invoice.generatePrint());
//    }



    public void setPrinterCallback(PrinterCallback printerCallback) {
        this.printerCallback = printerCallback;
    }
}
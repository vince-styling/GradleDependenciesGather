package com.example.sublibrary;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import com.android.net.IProxyService;

import java.io.FileDescriptor;

/**
 * Dummy code to make layoutlib.jar been used.
 */
public class LayoutLibUsage {
    public static void execute() {
        IProxyService proxyService = takeIProxyService();
        Log.e("Testt", "takeIProxyService obc " + proxyService);
    }

    private static IProxyService takeIProxyService() {
        return new IProxyService() {
            @Override
            public String resolvePacFile(String s, String s1) throws RemoteException {
                return null;
            }

            @Override
            public void setPacFile(String s) throws RemoteException {
                System.out.println(s);
            }

            @Override
            public void startPacSystem() throws RemoteException {
            }

            @Override
            public void stopPacSystem() throws RemoteException {
            }

            @Override
            public IBinder asBinder() {
                return new IBinder() {
                    @Override
                    public String getInterfaceDescriptor() throws RemoteException {
                        return null;
                    }

                    @Override
                    public boolean pingBinder() {
                        return false;
                    }

                    @Override
                    public boolean isBinderAlive() {
                        return false;
                    }

                    @Override
                    public IInterface queryLocalInterface(String descriptor) {
                        return null;
                    }

                    @Override
                    public void dump(FileDescriptor fd, String[] args) throws RemoteException {

                    }

                    @Override
                    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {

                    }

                    @Override
                    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                        return false;
                    }

                    @Override
                    public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {

                    }

                    @Override
                    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
                        return false;
                    }
                };
            }
        };
    }
}

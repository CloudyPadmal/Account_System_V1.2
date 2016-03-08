package com.padmal.helpers;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.os.ParcelFileDescriptor;

import java.io.IOException;

public class Helper_BackUp extends BackupAgentHelper {

    private static final String DATABASE_FILE_NAME = "Accounts.db";

    @Override
    public void onCreate() {

        FileBackupHelper backupHelper = new FileBackupHelper(this, DATABASE_FILE_NAME);
        addHelper("DB Files", backupHelper);
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
    }


}

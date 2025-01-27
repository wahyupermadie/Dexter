/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karumi.dexter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

/**
 * Wrapper class for all the static calls to the Android permission system
 */
class AndroidPermissionService {

    /**
     * @see PermissionChecker#checkSelfPermission
     */
    int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return PermissionChecker.checkSelfPermission(context, permission);
    }

    /**
     * @see ActivityCompat#requestPermissions
     */
    void requestPermissions(@Nullable Activity activity, @NonNull String[] permissions,
                            int requestCode) {
        if (activity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    String[] permissionRequest = new String[1];
                    permissionRequest[0] = permission;
                    ActivityCompat.requestPermissions(activity, permissionRequest, requestCode);
                }
            }
        }
    }

    /**
     * @see ActivityCompat#shouldShowRequestPermissionRationale
     */
    boolean shouldShowRequestPermissionRationale(@Nullable Activity activity,
                                                 @NonNull String permission) {
        if (activity == null) {
            return false;
        }

        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    boolean isPermissionPermanentlyDenied(@Nullable Activity activity,
                                          @NonNull String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        return !shouldShowRequestPermissionRationale(activity, permission);
    }
}

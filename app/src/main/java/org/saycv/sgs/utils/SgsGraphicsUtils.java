/*
 * Copyright (C) 2014, sayCV.
 *
 * Copyright 2014 The sayCV's Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.saycv.sgs.utils;

import org.saycv.sgs.SgsApplication;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class SgsGraphicsUtils {
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		final int w = bm.getWidth();
		final int h = bm.getHeight();
		final float sw = ((float) newWidth) / w;
		final float sh = ((float) newHeight) / h;

		Matrix matrix = new Matrix();
		matrix.postScale(sw, sh);
		return Bitmap.createBitmap(bm, 0, 0, w, h, matrix, false);
	}
	
	public static int getSizeInPixel(int dp){
		final float scale = SgsApplication.getContext().getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}

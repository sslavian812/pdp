/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package lib.com.jhlabs.map.proj;

import java.awt.geom.Point2D;

public class LinearProjection extends Projection {
	
	public Point2D.Double transform(Point2D.Double src, Point2D.Double dst) {
		dst.x = src.x;
		dst.y = src.y;
		return dst;
	}

	public void transform(double[] srcPoints, int srcOffset, double[] dstPoints, int dstOffset, int numPoints) {
		for (int i = 0; i < numPoints; i++) {
			dstPoints[dstOffset++] = srcPoints[srcOffset++];
			dstPoints[dstOffset++] = srcPoints[srcOffset++];
		}
	}

	public Point2D.Double inverseTransform(Point2D.Double src, Point2D.Double dst) {
		dst.x = src.x;
		dst.y = src.y;
		return dst;
	}

	public void inverseTransform(double[] srcPoints, int srcOffset, double[] dstPoints, int dstOffset, int numPoints) {
		for (int i = 0; i < numPoints; i++) {
			dstPoints[dstOffset++] = srcPoints[srcOffset++];
			dstPoints[dstOffset++] = srcPoints[srcOffset++];
		}
	}

	public boolean hasInverse() {
		return true;
	}

	public boolean isRectilinear() {
		return true;
	}

	public String toString() {
		return "Linear";
	}

}

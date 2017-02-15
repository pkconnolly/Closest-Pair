/*
Peter Connolly '18
COSC301 Prof. Glenn

Project 1 - Closest Pair
Version 1.0 - 09-25-2016
*/

import java.util.*;
import java.awt.geom.Point2D;

public class FindClosest{
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);

	// read number of points and make arrays
		int numPoints = scan.nextInt();
		Point2D.Double[] points = new Point2D.Double[numPoints];

	// read points and add to array
		for (int p = 0; p < numPoints; p++){
			points[p] = new Point2D.Double(scan.nextDouble(), scan.nextDouble());
		}
		
		System.out.println(PointSet.closestPair(points));
	}
}

class PointSet{

	public static PointPair closestPair(java.awt.geom.Point2D.Double[] pts){
		java.awt.geom.Point2D.Double[] ptsX = pts.clone();
		PointSet.sortX(ptsX);

		java.awt.geom.Point2D.Double[] ptsY = pts.clone();
		PointSet.sortY(ptsY);

		return cp(ptsX, ptsY);
	}

	public static PointPair cp(java.awt.geom.Point2D.Double[] ptsX, java.awt.geom.Point2D.Double[] ptsY){
		
		if(ptsX.length < 4){
			return BruteForce(ptsX);
		}
		else{
			//Make and fill Xl, Xr
			Point2D.Double[] ptsXL = new Point2D.Double[(ptsX.length)/2];
			Point2D.Double[] ptsXR = new Point2D.Double[(ptsX.length)-(ptsXL.length)];
			for (int i=0; i<(ptsX.length/2); i++){
				ptsXL[i] = ptsX[i];
			}
			for (int i=0; i<((ptsX.length)-(ptsXL.length)); i++){
				ptsXR[i] = ptsX[(ptsX.length/2) + i];
			}

			//Make and fill Yl, Yr
			Point2D.Double[] ptsYL = new Point2D.Double[(ptsXL.length)];
			Point2D.Double[] ptsYR = new Point2D.Double[(ptsXR.length)];
			Double mid = ptsXL[ptsXL.length-1].getX();
			int li = 0;
			int ri = 0;
			for (int i=0; i<ptsY.length; i++){
				if(ptsY[i].getX() <= mid && li<ptsXL.length){
					ptsYL[li] = ptsY[i];
					li++;
				}
				else{
					ptsYR[ri] = ptsY[i];
					ri++;
				}
			}

			//Subproblems
			PointPair leftPair = cp(ptsXL, ptsYL);
			PointPair rightPair = cp(ptsXR, ptsYR);
			PointPair ret = rightPair;
			if(leftPair.getDistance() < ret.getDistance()) ret = leftPair;

			Double min = Math.min(leftPair.getDistance(), rightPair.getDistance());

			//Make and fill my
			ArrayList<Point2D.Double> my = new ArrayList<Point2D.Double>();
			for (int i=0; i<ptsY.length; i++){
				if(Math.abs(ptsY[i].getX()-mid) <= min){
					my.add(ptsY[i]);
				}
			}

			//Search my strip for smaller pairs across divide
			for (int i=0; i<my.size(); i++) {
				Point2D.Double point = my.get(i);
				for (int j=i+1; j<(i+8); j++) {
					if(j < my.size()){
						if(point.distance(my.get(j)) < min){
						ret = new PointPair(point, my.get(j));
						min = ret.getDistance();
						}
					}else break;
				}
			}
			return ret;
		}
	}

	public static PointPair BruteForce(java.awt.geom.Point2D.Double[] pts){
		PointPair pp = new PointPair(pts[0], pts[1]);

		for(int i = 0; i < pts.length-1; i++){
			for(int j = i+1; j < pts.length-1; j++){
				if(pts[i].distance(pts[j]) < pp.getDistance())
					pp = new PointPair(pts[i], pts[j]);
			}
		}

		return pp;
	}

	public static void sortX(java.awt.geom.Point2D.Double[] pts){
		java.awt.geom.Point2D.Double[] tempArray = new java.awt.geom.Point2D.Double[pts.length];
		mergeSortX(pts, tempArray,0, pts.length-1);
	}

	public static void mergeSortX(java.awt.geom.Point2D.Double[] pts, java.awt.geom.Point2D.Double[] tempArray, int lowerIndex, int upperIndex){
		if(lowerIndex == upperIndex){
			return;
		}else{
			int mid = (lowerIndex+upperIndex)/2;
			mergeSortX(pts, tempArray, lowerIndex, mid);
			mergeSortX(pts, tempArray, mid+1, upperIndex);
			mergeX(pts, tempArray, lowerIndex, mid+1, upperIndex);
		}
	}

	public static void mergeX(java.awt.geom.Point2D.Double[] pts, java.awt.geom.Point2D.Double[] tempArray, int lowerIndexCursor, int higherIndex, int upperIndex){
		int tempIndex=0;
		int lowerIndex = lowerIndexCursor;
		int midIndex = higherIndex-1;
		int totalItems = upperIndex-lowerIndex+1;
		while(lowerIndex <= midIndex && higherIndex <= upperIndex){
			if(pts[lowerIndex].getX() < pts[higherIndex].getX()){
				tempArray[tempIndex++] = pts[lowerIndex++];
			}else{
				tempArray[tempIndex++] = pts[higherIndex++];
			}
		}

		while(lowerIndex <= midIndex){
			tempArray[tempIndex++] = pts[lowerIndex++];
		}

		while(higherIndex <= upperIndex){
			tempArray[tempIndex++] = pts[higherIndex++];
		}

		for(int i=0;i<totalItems;i++){
			pts[lowerIndexCursor+i] = tempArray[i];
		}
	}

	public static void sortY(java.awt.geom.Point2D.Double[] pts){
		java.awt.geom.Point2D.Double[] tempArray = new java.awt.geom.Point2D.Double[pts.length];
		mergeSortY(pts, tempArray,0, pts.length-1);
	}

	public static void mergeSortY(java.awt.geom.Point2D.Double[] pts, java.awt.geom.Point2D.Double[] tempArray, int lowerIndex, int upperIndex){
		if(lowerIndex == upperIndex){
			return;
		}else{
			int mid = (lowerIndex+upperIndex)/2;
			mergeSortY(pts, tempArray, lowerIndex, mid);
			mergeSortY(pts, tempArray, mid+1, upperIndex);
			mergeY(pts, tempArray, lowerIndex, mid+1, upperIndex);
		}
	}

	public static void mergeY(java.awt.geom.Point2D.Double[] pts, java.awt.geom.Point2D.Double[] tempArray, int lowerIndexCursor, int higherIndex, int upperIndex){
		int tempIndex=0;
		int lowerIndex = lowerIndexCursor;
		int midIndex = higherIndex-1;
		int totalItems = upperIndex-lowerIndex+1;
		while(lowerIndex <= midIndex && higherIndex <= upperIndex){
			if(pts[lowerIndex].getY() < pts[higherIndex].getY()){
				tempArray[tempIndex++] = pts[lowerIndex++];
			}else{
				tempArray[tempIndex++] = pts[higherIndex++];
			}
		}

		while(lowerIndex <= midIndex){
			tempArray[tempIndex++] = pts[lowerIndex++];
		}

		while(higherIndex <= upperIndex){
			tempArray[tempIndex++] = pts[higherIndex++];
		}

		for(int i=0;i<totalItems;i++){
			pts[lowerIndexCursor+i] = tempArray[i];
		}
	}
}
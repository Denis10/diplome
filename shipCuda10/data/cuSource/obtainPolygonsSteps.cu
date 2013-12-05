extern "C"
#define n (3)
#define qPoints (58)
#define qPolygons (96)
__global__ void obtainPolygonsSteps(int* dev_S,int* dev_polygonToFillX,int* dev_polygonToFillY,int* dev_shipLocationX,
int* dev_shipLocationZ,float* dev_matrixC,int* dev_points,int* dev_polygons,int* dev_normals,const int N)
{	
	int j = threadIdx.x;
	if (j<N){		
		//__shared__ 
		float observerCoord[3*qPoints];
		for (int i = 0; i < qPoints; i++) {
			observerCoord[3*i] = (dev_points[3*i]+dev_shipLocationX[j]) * dev_matrixC[0] + dev_points[3*i+1]
			* dev_matrixC[1] + (dev_points[3*i+2]+dev_shipLocationZ[j]) * dev_matrixC[2] +dev_matrixC[3];			
			observerCoord[3*i+1] = (dev_points[3*i]+dev_shipLocationX[j]) * dev_matrixC[4] + dev_points[3*i+1]
			* dev_matrixC[5] + (dev_points[3*i+2]+dev_shipLocationZ[j]) * dev_matrixC[6] + dev_matrixC[7];
			observerCoord[3*i+2] = (dev_points[3*i]+dev_shipLocationX[j]) * dev_matrixC[8] + dev_points[3*i+1]
			* dev_matrixC[9] + (dev_points[3*i+2]+dev_shipLocationZ[j]) * dev_matrixC[10] + dev_matrixC[11];
		}
		/*
		float to2d[2*qPoints];
		for (int i = 0; i < qPoints; i++) {
			to2d[2*i] = 0.6 * observerCoord[3*i+2] / observerCoord[3*i];
			to2d[2*i+1] = 0.6 * observerCoord[3*i+1] / observerCoord[3*i];
		}
		// ---------------to2dmm----------------------------------------------
		double to2dmm[2*qPoints];
		for (int i = 0; i < qPoints; i++) {
			to2dmm[2*i] = 160 * to2d[2*i];
			to2dmm[2*i+1] = 160 * to2d[2*i+1];
		}
		// -----------------------spX, spY--------------------------------------
		int spX[qPoints];
		int spY[qPoints];
		for (int i = 0; i < qPoints; i++) {
			spX[i] = (int) (4.35 * to2dmm[2*i]);
			spY[i] = (int)(4.35 * to2dmm[2*i+1]);
		}
		int xp[qPoints];
		int yp[qPoints];
		for (int i = 0; i < qPoints; i++) {
			xp[i] = spX[i] + 350;
			yp[i] = -spY[i] + 350;
		}	*/	
		int xp[qPoints];
		int yp[qPoints];
		for (int i = 0; i < qPoints; i++) {
			xp[i] = (int) (4.35f * 160 * 0.6f * observerCoord[3*i+2] / observerCoord[3*i]) + 350;
			yp[i] = -(int) (4.35f * 160 * 0.6f * observerCoord[3*i+1] / observerCoord[3*i]) + 350;
		}
		float NormalObs[3*qPolygons];
		for (int i = 0; i < qPolygons; i++) {
			NormalObs[3*i] = dev_normals[3*i] * dev_matrixC[0] + dev_normals[3*i+1]
					* dev_matrixC[1] + dev_normals[3*i+2] * dev_matrixC[2];
			NormalObs[3*i+1] = dev_normals[3*i] * dev_matrixC[4] + dev_normals[3*i+1]
					* dev_matrixC[5] + dev_normals[3*i+2] *dev_matrixC[6];
			NormalObs[3*i+2] = dev_normals[3*i] * dev_matrixC[8] + dev_normals[3*i+1]
					* dev_matrixC[9] + dev_normals[3*i+2] * dev_matrixC[10];
		}
		for (int i = 0; i < qPolygons; i++) {
			if (
				(
				NormalObs[3*i] * observerCoord[3*dev_polygons[3*i]] + NormalObs[3*i+1]
					*observerCoord[3*dev_polygons[3*i]+1] + NormalObs[3*i+2]
					* observerCoord[3*dev_polygons[3*i]+2]
				)<0){
					dev_S[j*qPolygons+i]=1;
			}
			else {
			dev_S[j*qPolygons+i]=0;
			}
		}
		for (int i = 0; i < qPolygons; i++) {
			dev_polygonToFillX[j*qPolygons*n+3*i] = xp[dev_polygons[3*i]];
			dev_polygonToFillX[j*qPolygons*n+3*i+1] = xp[dev_polygons[3*i+1]];
			dev_polygonToFillX[j*qPolygons*n+3*i+2] = xp[dev_polygons[3*i+2]];
			dev_polygonToFillY[j*qPolygons*n+3*i] = yp[dev_polygons[3*i]];
			dev_polygonToFillY[j*qPolygons*n+3*i+1] = yp[dev_polygons[3*i+1]];
			dev_polygonToFillY[j*qPolygons*n+3*i+2] = yp[dev_polygons[3*i+2]];
		}			
	}
}
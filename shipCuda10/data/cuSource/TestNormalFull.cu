extern "C"

__device__ void createNewNormal(int* x, int* y, int* z,int* nX, int* nY, int* nZ, int i)
{
int j = 0;
nX[i]=0;
nY[i]=0;
nZ[i]=0;
        for (int k = 0; k < 3; k++) {
            if (k < 2) {
                j = k + 1;
                nX[i] += -(y[k] - y[j]) * (z[k] + z[j]);
                nY[i] += -(z[k] - z[j]) * (x[k] + x[j]);
                nZ[i] += -(x[k] - x[j]) * (y[k] + y[j]);
            } else {
                j = 0;
                nX[i] += -(y[k] - y[j]) * (z[k] + z[j]);
                nY[i] += -(z[k] - z[j]) * (x[k] + x[j]);
                nZ[i] += -(x[k] - x[j]) * (y[k] + y[j]);
            }
        }
//return x;
}

extern "C"

__device__ void createNewNormalSecond(int* x, int* y, int* z,int* nX, int* nY, int* nZ, int i)
{
int j = 0;
int bufX=0,bufY=0,bufZ=0; 
        for (int k = 0; k < 3; k++) {
            if (k < 2) {
                j = k + 1;
                bufX += -(y[k] - y[j]) * (z[k] + z[j]);
                bufY += -(z[k] - z[j]) * (x[k] + x[j]);
                bufZ += -(x[k] - x[j]) * (y[k] + y[j]);
            } else {
                j = 0;
                bufX += -(y[k] - y[j]) * (z[k] + z[j]);
                bufY += -(z[k] - z[j]) * (x[k] + x[j]);
                bufZ += -(x[k] - x[j]) * (y[k] + y[j]);
            }
        }
nX[i]=bufX;
nY[i]=bufY;
nZ[i]=bufZ;
}


extern "C"
__global__ void TestNormalFull(int* point, int*polygon, int* nX, int* nY, int* nZ, int N) 

{ 
int i = blockDim.x * blockIdx.x + threadIdx.x; 

if (i < N){
int xxx[3];
int yyy[3];
int zzz[3];
for (int j=0;j<3;j++)
{
  xxx[j]= point[3*polygon[3*i+j]];
  yyy[j]= point[3*polygon[3*i+j]+1];
  zzz[j]= point[3*polygon[3*i+j]+2];
}

/*
xxx[0]= point[3*polygon[3*i]];
xxx[1]= point[3*polygon[3*i+1]];
xxx[2]= point[3*polygon[3*i+2]];
yyy[0]= point[3*polygon[3*i]+1];
yyy[1]= point[3*polygon[3*i+1]+1];
yyy[2]= point[3*polygon[3*i+2]+1];
zzz[0]= point[3*polygon[3*i]+2];
zzz[1]= point[3*polygon[3*i+1]+2];
zzz[2]= point[3*polygon[3*i+2]+2];
*/

createNewNormal(xxx,yyy,zzz,nX,nY,nZ,i);
}
}
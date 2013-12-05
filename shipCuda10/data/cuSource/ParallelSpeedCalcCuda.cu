#define size (8000)
extern "C"

__device__ float vxd(const float m,float Vy, float w, float ZpTei, float Xr, float Kvx)
{
	if (Vy > 0) {
            return ((m * Vy * w - Xr + ZpTei) * Kvx);
        } else {
            return ((m * Vy * w * 1.09f - Xr + ZpTei) * Kvx);//Vx*1.061 ;N0=3;k11=580.91f Ubuntu
            //return ((m * Vy * w  - Xr + ZpTei) * Kvx);
        }//*1.1045; k11=5.91f windows ; 1.09; k11=580.91f
}

extern "C"

__device__ float vyd(const float m,float Vx, float w, float ZpYri, float Yr, float Kvy) {
        if (Vx > 0) {
            return ((Yr - ZpYri - m * Vx * w * 1.0165f) * Kvy);//Vx*1.0179
            //return ((Yr - ZpYri - m * Vx * w) * Kvy);
        } else {//*1.0165; k11=5.91f windows
            return ((Yr - ZpYri - m * Vx * w) * Kvy);
        }
    }
extern "C"

__device__ float wd(const float lr,float Mr, float ZpYri, float Yr, float Kw) {
        return ((Mr + lr * ZpYri) * Kw);
    }
	
extern "C"

__global__ void ParallelSpeedCalcCuda(float* VX, float* VY, float* ww, float* X, float* Y,
float* W, float* Xobs, float* Yobs,const int nShips) 
{ 
int j = blockDim.x * blockIdx.x + threadIdx.x; 
if (j<nShips){
		float k11, k22, k66, k26;
        
		float devVX[size];
		float devVY[size];
		float devww[size];
		float devX[size];
		float devY[size];
		float devW[size];
		float devXobs[size];
		float devYobs[size];
		
		const float T = 4.0f;
		const float L = 80.0f;
		const float B = 15.0f;// осадка, длина, ширина
															// c 323, 321
		const float m = 20000000000.0f; // масса судна
		const float lr = 40.0f; // расстояние от ДРК до ЦТ судна
		float Vx = 5.0f, Vy = 0.0f;// скорости по осям
		float v = 0.0f, w = 0.0f;// линейная и угловая скорости
		
        const float delta = 0.7f;//коэффициент общей полноты
        float c1, c2, c3, m1, m2;//коэффициеты Р.Я.Першица
        float w_ = 0.0f;//угол дрейфа						approx
        float betta_d = 0.0f;//угол дрейфа в центре тяжести (rad)
        float Cxr, Cyr, Cmr;//коэффициенты Cxr, боковой силы, момента
        //	float L1=B,T1=L/2;//хорда, полудлина вертикального крыла ????????????????????
        const float p = 1000.0f;//плотность воды
        float Xr, Yr, Mr;//гидродинамические усилия
        float ZpTei = 0;//полезная тяга винтов при равномерном прямолинейном движении =R+Xa
        float Jz;//момент инерции массы судна относительно оси Gz
        float ZpYri;//боковая сила ДРК 
        const float a66 = 0.31f;//числовой коэффициент
        //float Ramp = 0.0f;//для одиночных рулей
        const float lyamdaR = 1.4f;//удлинение c 335 //0.5--1.4 //1
        
        const float deltaR = 0.349f;//угол перекладки руля  - 20% //0.349f
        
//	float Va;//скорость натекания воды на руль
        float Yri, Ysi;//боковая и стабилизирубщая силы
        const float Ar = 6.0f;//площадь перьев//5 можно увеличить
        const float D = 1.5f;//диаметр диска винта//2 можно уменьшить
        const float Cta = 10.0f, Ctt = 50.0f;//c 334 //Cta<=20, 0.5--30, Ctt<=20, 1--50 UP!!!
        //Ctt =30
        float Kvx, Kvy, Kw;//коэффиценты для производных
        const float No = 3.0f;//обороты двигателя //Першин//3 оборота
        const float Re = 5000000.0f;//Число Рейнольда >5E8
        float K_betta;
        float fit;
        const float xk = 1.0f;
        const float bettar = 0.9f;
        const float fik = 0.95f;
        float ld_;//относительная длина насадки c228
        float betta_D;//коэффициент расширения
        float fiD;//c 338
        float CyD1;
        float CyD;
        float A0;//площадь диска винта
        float xD;//c 339
        float viv = 0.0f;//угол для неподвижной системы координат с 27 ch3_2
		float Rmatr[3][3]={cosf(viv),-sinf(viv),0.0f,sinf(viv),cosf(viv),0.0f,0.0f,0.0f,1.0f};
		 //------------------------------------------------------------
//c 330         5.91-в книге, 580.91 - выравнять Х
        k11 = (580.91f * (float) pow(B / L, 2.0f) + 7.76f * (B / L) - 0.259f) / (48.4f - 6.89f * (B / T) + 1.47f * (float) pow(B / T, 2.0f) - 0.0475f * (float) pow(B / T, 3.0f));

        k22 = ((0.722f + 0.224f * delta) * (1.022f - (float) pow(B / L, 2.0f))) / (0.264f + 0.368f * (B / T));
        k66 = (2.0f * T / B) * (2.59f + 0.781f * delta) * (0.357f - 1.77f * (float) pow(B / L, 2.0f));
        k26 = k22;
        //k26=0;       
//c 323
        c1 = 3.14f * (T / L) * (float) pow((0.63f / delta), (5.0f / 2.0f)) * (float) pow(L / (6.0f * B), (1.0f / 3.0f)) - 0.032f;
        c2 = -2.0f * k11 * delta * (B / L);
        c3 = 1.35f * (float) pow(T / B, (1.0f / 2.0f)) * (float) pow((0.63f / delta), (3.0f / 2.0f)) - 0.029f;
        m1 = 1.67f * (T / L) - 0.56f * delta + 0.43f;
        m2 = -0.44f * (T / L) - 0.0375f;
        // System.out.printf("c1=%f\tc2=%f\tc3=%f\tm1=%f\tm2=%f\n", c1, c2, c3, m1, m2);
        Jz = (m * (float) pow(L, 2.0f) / 12.4f) * (0.463f + 0.574f * (float) pow(delta, a66) + (float) pow(B / L, 2.0f));//c 330
        
        Kvx = 1 / (m * (1 + k11));
        Kvy = 1 / (m * (1 + k22));
        Kw = 1 / (Jz * (1 + k66));//????? m        
//----------------------------------------------------------------
        float k1, k2, k3, k4;
        float q1, q2, q3, q4;
        float z1, z2, z3, z4;
        float j1, j2, j3, j4;
        //t = 0.0f; // шаг времени		
        int t = 0;
        float h = 1.0f;		
		for (int i = 0; i < size; i++) {	//16550
                v = (float) sqrt((float) pow(Vx, 2.0f) + (float) pow(Vy, 2.0f));
                //assert(Vx==0);
                if (Vx != 0) {
				//c 353 ?????????????????????????
                    w_ = w * L / v;//??????????????????????
                    betta_d = -(float) atan(Vy / Vx);//c 350
                } else {
                    w_ = w * L / v;
                    //betta_d = 0;
                    betta_d = -(float) atan(Vy / Vx);//c 350
                }
                
                Cxr = 0.01f * (1.0f + 170.0f * (T / L));// для плота c 119
                Cyr = c1 * betta_d + c2 * w_ + c3 * betta_d * abs(betta_d);//c 323
                Cmr = m1 * betta_d + m2 * w_;

                Xr = Cxr * L * T * (float) pow(v, 2.0f) * p / 2.0f;//c 320
                Yr = Cyr * L * T * (float) pow(v, 2.0f) * p / 2.0f;
                Mr = Cmr * L * T * (float) pow(v, 2.0f) * p / 2.0f;

                K_betta = 0.43f * (float) pow(Ctt, -0.6f);
                fit = (float) pow(1.0f + Ctt, 0.508f);
                //IMPORTANT!!!  deltaR
                Yri = 3.14f * (deltaR - K_betta * xk * (betta_d + lr * w_)) * p * Ar * (float) pow(v * fik * fit, 2.0f) / (1.0f + 2.2f / (float) pow(lyamdaR, 2.0f / 3.0f));

                ld_ = 0.77f - 0.125f * (float) sqrt(Ctt) / (1.65f * (float) sqrt(Ctt) - 1.0f);
                betta_D = 1.22f - 0.0563f * (float) sqrt(Ctt) / (1.65f * (float) sqrt(Ctt) - 1.0f);
                fiD = 0.5f * ((float) sqrt(1.0f + 2.0f * Ctt / betta_D) + 1.0f);

                CyD1 = 12.0f * ld_ / (1.0f + 1.56f * ld_);
                CyD = CyD1 + 2.0f * betta_D * (float) pow(fiD, 2.0f);
                xD = xk * (CyD1 + 2.0f * betta_D * fiD) / (CyD1 + 2.0f * betta_D * (float) pow(fiD, 2.0f));
                A0 = 3.14f * (float) pow(D, 2.0f) / 4.0f;
                Ysi = CyD * (xD - 0.02f * xk) * (betta_d + lr * w_) * (p / 2.0f) * A0 * (float) pow(v, 2.0f) * (float) pow(fik, 2.0f);                
                ZpTei = 1000000.0f * (9.740f * (float) pow(No, 2.0f) - 2.23f * v); //Пелевин//9.740f                
                ZpYri = 2.0f * (Yri - Ysi);//2 винта
               
                k1 = h * vxd(m,Vy, w, ZpTei, Xr, Kvx);
                q1 = h * vyd(m,Vx, w, ZpYri, Yr, Kvy);
                z1 = h * wd(lr,Mr, ZpTei, Yr, Kw);

                k2 = h * vxd(m,Vy + q1 / 2.0f, w + z1 / 2.0f, ZpTei, Xr, Kvx);
                q2 = h * vyd(m,Vx + k1 / 2.0f, w + z1 / 2.0f, ZpYri, Yr, Kvy);
                z2 = h * wd(lr,Mr, ZpYri, Yr, Kw);

                k3 = h * vxd(m,Vy + q2 / 2.0f, w + z2 / 2.0f, ZpTei, Xr, Kvx);
                q3 = h * vyd(m,Vx + k2 / 2.0f, w + z2 / 2.0f, ZpYri, Yr, Kvy);
                z3 = h * wd(lr,Mr, ZpYri, Yr, Kw);

                k4 = h * vxd(m,Vy + q3, w + z3, ZpTei, Xr, Kvx);
                q4 = h * vyd(m,Vx + k3, w + z3, ZpYri, Yr, Kvy);
                z4 = h * wd(lr,Mr, ZpYri, Yr, Kw);

                Vx = Vx + (1.0f / 6.0f) * (k1 + 2.0f * k2 + 2.0f * k3 + k4);
                //devVX[t] = Vx / 1.24f;
                devVX[t] = Vx;
                Vy = Vy + (1.0f / 6.0f) * (q1 + 2.0f * q2 + 2.0f * q3 + q4);
                devVY[t] = Vy;
                w = w + (1.0f / 6.0f) * (z1 + 2.0f * z2 + 2.0f * z3 + z4);
                devww[t] = w;

//---второй интеграл-----------------------------------------
                k1 = h * vxd(m,Vy, w, ZpTei, Xr, Kvx);
                q1 = h * vyd(m,Vx, w, ZpYri, Yr, Kvy);
                z1 = h * wd(lr,Mr, ZpTei, Yr, Kw);

                k2 = h * vxd(m,Vy + q1 / 2.0f, w + z1 / 2.0f, ZpTei, Xr, Kvx);
                q2 = h * vyd(m,Vx + k1 / 2.0f, w + z1 / 2.0f, ZpYri, Yr, Kvy);
                z2 = h * wd(lr,Mr, ZpYri, Yr, Kw);

                k3 = h * vxd(m,Vy + q2 / 2.0f, w + z2 / 2.0f, ZpTei, Xr, Kvx);
                q3 = h * vyd(m,Vx + k2 / 2.0f, w + z2 / 2.0f, ZpYri, Yr, Kvy);
                z3 = h * wd(lr,Mr, ZpYri, Yr, Kw);

                k4 = h * vxd(m,Vy + q3, w + z3, ZpTei, Xr, Kvx);
                q4 = h * vyd(m,Vx + k3, w + z3, ZpYri, Yr, Kvy);
                z4 = h * wd(lr,Mr, ZpYri, Yr, Kw);

                devX[t] = Vx + (1.0f / 6.0f) * (k1 + 2.0f * k2 + 2.0f * k3 + k4);
                devY[t] = Vy + (1.0f / 6.0f) * (q1 + 2.0f * q2 + 2.0f * q3 + q4);
                devW[t] = w + (1.0f / 6.0f) * (z1 + 2.0f * z2 + 2.0f * z3 + z4);                
                //угол для неподвижной системы координат                
                viv = devW[t];                                
                //-------
                Rmatr[0][0] = (float)cos(viv);
                Rmatr[0][1] = -(float)sin(viv);
                Rmatr[1][0] = (float)sin(viv);
                Rmatr[1][1] = (float)cos(viv);
                devXobs[t] = Rmatr[0][0] * devX[t] + Rmatr[0][1] * devY[t];
                devYobs[t] = Rmatr[1][0] * devX[t] + Rmatr[1][1] * devY[t];                
                //----------
                t++;               
                       
        }
		VX[j]=devVX[size-1];
		VY[j]=devVY[size-1];	
		ww[j]=devww[size-1];
		X[j]=devX[size-1];
		Y[j]=devY[size-1];
		W[j]=devW[size-1];
		Xobs[j]=devXobs[size-1];
		Yobs[j]=devYobs[size-1];		
	}
}
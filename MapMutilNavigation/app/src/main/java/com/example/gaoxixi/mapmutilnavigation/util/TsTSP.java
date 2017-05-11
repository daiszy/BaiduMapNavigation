package com.example.gaoxixi.mapmutilnavigation.util;



import java.util.ArrayList;

import java.util.List;



/**
 * Created by GaoXixi on 2017/4/19.
 */

/**贪心算法计算多个地点的路径顺序*/

public class TsTSP {

    private int destiNum;   //目标点数量

    private Double[][] distance;  //距离矩阵

    private int[] col;    //代表列，选过置0

    private int[] row;   //代表行，选过置0



    public List pathOrder = new ArrayList();

    // List<int> pathOrder;



    public TsTSP(int destiNum,Double[][] distance)
    {
        this.destiNum = destiNum;
        this.distance = distance;
    }

    public void init()
    {
        pathOrder.add(0);
        col = new int[destiNum];
        col[0] = 0;
        for(int i = 1; i < destiNum; i++)
        {
            col[i] = 1;
        }
        row = new int[destiNum];
        for(int i = 0; i < destiNum; i++)
        {
            row[i] = 1;
        }
    }

    public void solve()
    {
        Double[] temp = new Double[destiNum+1];
        Double s = 0.0;    //计算距离
        int i = 0;       //当前节点
        int j = 0;       //下一个节点
        while (row[i] == 1)
        {
            for(int k = 0;k < destiNum; k++)
            {
                temp[k] = distance[i][k];
            }

            /**选择下一个节点，要求不是已经走过，并且与i不同*/

            j = selectMin(temp);

            //找出下一节点  
            row[i] = 0;     //行置0，表示已经选过  
            col[j] = 0;    //列0，表示已经走过  

            pathOrder.add(j);

            s = s + distance[i][j];

            /**当前节点指向下一个节点*/
            i = j;
        }
    }

    private int selectMin(Double[] p) {
        int j = 0,  k = 0;
        Double m = p[0];
        /**寻找第一个可用节点，注意最后一次寻找，没有可用节点*/
        while (col[j] == 0)
        {
            j++;
            if(j >= destiNum)
            {
                m = p[0];
                break;
            }else {
                m = p[j];
            }
        }
        /**从可用节点j开始往后扫描，找出距离最小节点*/
        for(; j < destiNum; j++) {
            if(col[j] == 1) {
                if(m > p[j]) {
                    m = p[j];
                }
                k = j;
            }
        }
        return k;
    }
}
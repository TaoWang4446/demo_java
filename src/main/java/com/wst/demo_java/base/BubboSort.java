package com.wst.demo_java.base;

public class BubboSort {
    //N个数字冒泡排序，总共要进行N-1趟比较，每趟的排序次数为(N-i)次比较
    public static void bubbleSort(int[] arr){
        //一定要记住判断边界条件，很多人不注意这些细节，面试官看到你的代码的时候都懒得往下看，你的代码哪个项目敢往里面加？
        if(arr==null||arr.length<2){
            return;
        }
        //需要进行arr.length趟比较

        for(int i = 0 ;i<arr.length-1;i++){
            //第i趟比较
            for(int j = 0 ;j<arr.length-i-1;j++){
                //开始进行比较，如果arr[j]比arr[j+1]的值大，那就交换位置
                if(arr[j]>arr[j+1]){
                    int temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                }
            }

        }
        System.out.println("最终得出的数组为：");
        for (int k =0 ; k < arr.length;k++){
            System.out.print(arr[k]+" ");
        }
    }

    public static void main(String[] args) {
        int[] arry = {3,5,7,1,2,6,0};
        bubbleSort(arry);
    }
}

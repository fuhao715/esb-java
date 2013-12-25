package com.fuhao.esb.test.path;

/**
 * package name is  com.fuhao.esb.test.path
 * Created by fuhao on 13-12-24.
 * Project Name esb-java
 */
public class HashMapTest implements Runnable{
    public static void main(String args[]){
          for(int i =0 ;i<10;i++){
             Thread t =  new Thread(new HashMapTest()) ;
              t.start();
          }

    }

        @Override
        public void run() {
            Test t =MapTest.getTest("t");
            t.add(1);
        }

}

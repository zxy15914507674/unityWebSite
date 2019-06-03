package qiutest;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.ArrayList;
import java.util.List;


public class Mytest {
    public static void main(String[] args) {
        //Md5Hash md5Hash=new Md5Hash("123","unity");
       //System.out.println(md5Hash.toString());
        List<String> list= new ArrayList<>();
        list.add("0");
        list.add("1");
        list.add("2");
        List<String> str=list.subList(0,0);
        for(String s:str){
            System.out.println(s);
        }
        System.out.println("结束");
    }
}

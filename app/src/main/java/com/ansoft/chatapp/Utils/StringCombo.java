package com.ansoft.chatapp.Utils;

/**
 * Created by user on 11/9/2015.
 */
public class StringCombo {

    int a[] = new int[10];
    int b[] = new int[10];
    String line1;
    String line2;

    public StringCombo(String line1, String line2) {
        this.line1 = line1;
        this.line2 = line2;
    }

    public String getEncryptedValue(){
        int sum=0;
        for (int i=0; i<10; i++){
            a[i]=(int)line1.charAt(i);
        }
        for (int i=0; i<10; i++){
            b[i]=(int)line2.charAt(i);
        }
        for (int i=0; i<10; i++){
            sum+=a[i]*b[i];
        }
        return sum+"";
    }
}

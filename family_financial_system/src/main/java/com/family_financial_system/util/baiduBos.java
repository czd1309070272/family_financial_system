package com.family_financial_system.util;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObjectSummary;
import com.baidubce.services.bos.model.ListObjectsResponse;
import com.baidubce.services.bos.model.PutObjectResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class baiduBos {
    private String ACCESS_KEY_ID = "97246a67c14d46dfa52e24f6fcb5c41b";                   // 用户的Access Key ID
    private String SECRET_ACCESS_KEY = "c4e6bb8743284f01a2d739dbe6ba40b8";           // 用户的Secret Access Key
    private String ENDPOINT = "gz.bcebos.com";                               // 用户自己指定的域名

    public BosClient BosClientService(){
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID,SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        config.setConnectionTimeoutInMillis(5000);
        config.setSocketTimeoutInMillis(2000);
        BosClient client = new BosClient(config);
        return client;
    }
    public String SearchPicNameOrNot(BosClient client, String bucketName,String userName) {

        // 获取指定Bucket下的所有Object信息
        ListObjectsResponse listing = client.listObjects(bucketName);

        // 遍历所有Object
        for (BosObjectSummary objectSummary : listing.getContents()) {
            if(Objects.equals(objectSummary.getKey(), ""+userName+".gif")){
                return objectSummary.getKey();
            }
        }
        return null;

    }
    public String generatePresignedUrl(BosClient client, String bucketName, String objectKey) {

        //指定用户需要获取的Object所在的Bucket名称、该Object名称、URL的有效时长
        URL url = client.generatePresignedUrl(bucketName, objectKey, -1);


        return url.toString();
    }
    public void PutObject(BosClient client, String bucketName, String objectKey, byte[] byte1) throws FileNotFoundException {
//        // 获取指定文件
//        File file = new File("/path/to/file.zip");
//        // 获取数据流
//        InputStream inputStream = new FileInputStream("/path/to/test.zip");
//
//        // 以文件形式上传Object
//        PutObjectResponse putObjectFromFileResponse = client.putObject(bucketName, objectKey, file);
//        // 以数据流形式上传Object
//        PutObjectResponse putObjectResponseFromInputStream = client.putObject(bucketName, objectKey, inputStream);
        // 以二进制串上传Object
        PutObjectResponse putObjectResponseFromByte = client.putObject(bucketName, objectKey, byte1);
        // 以字符串上传Object
//        PutObjectResponse putObjectResponseFromString = client.putObject(bucketName, objectKey, string1);


        // 打印ETag
        System.out.println(putObjectResponseFromByte.getETag());
    }
}

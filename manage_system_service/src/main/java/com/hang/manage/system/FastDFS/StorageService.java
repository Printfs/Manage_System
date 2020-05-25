package com.hang.manage.system.FastDFS;

/*
文件存储服务接口
 */

public interface StorageService {

    /**
     * 上传文件
     *
     */
    public String upload(byte[] data, String extName);

    /**
     * 删除文件
     *
     * @param fileId 被删除的文件id
     * @return 删除成功后返回0，失败后返回错误代码
     */
    public int delete(String fileId);
}

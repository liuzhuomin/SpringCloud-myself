package xinrui.cloud.dto;

import java.util.Date;

public class FileDto {
	/**
	 * 文件名称
	 */
	private String name;
	/**
	 * 文件下载链接(完整路径)
	 */
	private String url;

	/**
	 * 文件上传时间
	 */
	private Date time;

	/**
	 * 文件下載次數
	 */
	private int downLoadCount;

	/**
	 * 查看次数
	 */
	private int lookCount;

	/**
	 * 文件上传者
	 */
	private Long userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getDownLoadCount() {
		return downLoadCount;
	}

	public void setDownLoadCount(int downLoadCount) {
		this.downLoadCount = downLoadCount;
	}

	public int getLookCount() {
		return lookCount;
	}

	public void setLookCount(int lookCount) {
		this.lookCount = lookCount;
	}

}

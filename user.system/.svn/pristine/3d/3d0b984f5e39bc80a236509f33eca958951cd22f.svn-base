package xinrui.cloud.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 具体的文件信息上传对象
 */
@Data
@Entity
@Table(name="project_file_inner")
public class ProjectFileInner extends IdEntity {

    /**
     * 所关联的文件上传对象
     */
    @ManyToOne
    @JoinColumn(name = "projectFile_id")
    private ProjectFile projectFile;
    /**
     * 文件的名
     */
    @Lob
    private String fileName;

    /**
     * 文件的路径
     */
    @Lob
    private String url;
}

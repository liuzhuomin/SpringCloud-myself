package xinrui.cloud.domain;

import com.google.common.collect.Lists;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 项目入驻的文件上传对象
 */
@Data
@Entity
@Table(name="project_file")
public class ProjectFile extends IdEntity {

    /**
     * 此次上传文件的标题
     */
    @Lob
    private String title;
    /**
     * 所关联的项目入驻对象
     */
    @ManyToOne
    @JoinColumn(name = "projectInner_id")
    private ProjectInner projectInner;

    /**
     *创建项目正文
     */
    @Lob
    private String inputProText;

    //具体的文件路径对象
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "projectFile")
    private List<ProjectFileInner> projectFileInerrList= Lists.newArrayList();

}
